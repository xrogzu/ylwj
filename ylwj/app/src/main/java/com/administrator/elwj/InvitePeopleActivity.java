package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterInvitePeople;
import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.BigStage_Activity;
import com.administrator.bean.BigstageUser;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.StartPickPhotoActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageItem;
import com.library.listview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

//发布活动流程中的最后一步，邀请某人参与互动界面
public class InvitePeopleActivity extends AppCompatActivity implements View.OnClickListener {

    private XListView listView;
    private ListAdapterInvitePeople adapter;
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;
    private BaseApplication appContext;
    private String activity_id;
    private boolean isNew = true;
    private static final int GET_DATA = 0;
    private String ActivityContent;
    private String activityName;//活动名称
    private static final int GET_ACTIVITY = 5;
    private ActivityDetails mActivityDetails;

    public static class MyHandler extends Handler {
        private WeakReference<InvitePeopleActivity> mActivity;

        public MyHandler(InvitePeopleActivity activity) {
            mActivity = new WeakReference<InvitePeopleActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            InvitePeopleActivity activity = mActivity.get();
            if (activity != null) {
                if(activity.listView != null){
                    activity.listView.stopLoadMore();
                    activity.listView.stopRefresh();
                }
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.UPLOAD_ACTIVTY) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            Intent intent = new Intent("android.intent.action.DRAFT_UPDATE");
                            activity.sendBroadcast(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ToastUtil.showToast(activity, "发布成功");
                    LogUtils.e("Main", json);
                    Intent intent = new Intent(activity,BigStageNormalDetailsActivity.class);
                    Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                    bean_bigstage_list.setActivity_id(activity.activity_id);
                    intent.putExtra("bean",bean_bigstage_list);
                    activity.startActivity(intent);
                    activity.finish();
                } else if (which == GET_DATA) {
                    LogUtils.d("xu_invitation", json);
                    Gson gson = new Gson();
                    BigstageUser user = gson.fromJson(json, BigstageUser.class);
                    if (user.getResult() == 1) {
                        List<BigstageUser.DataEntity> dataEntities = user.getData();
                        if (dataEntities.size() < activity.pageSize)
                            activity.hasMoreData = false;
                        else activity.hasMoreData = true;
                        if (activity.curPage == 1)
                            activity.adapter.clear();
                        activity.adapter.addData(dataEntities);
                    }
                }else if (which == GET_ACTIVITY) {
                    LogUtils.d("xu_activ_id", json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        if ( jsonObject.getInt("result")==1) {
                            Gson gson = new Gson();
                            activity.mActivityDetails = gson.fromJson(json, ActivityDetails.class);
                            activity.activityName = activity.mActivityDetails.getData().getTitle();
                            activity.ActivityContent = activity.mActivityDetails.getData().getIntroduction();
                            LogUtils.d("xu_activ_name", activity.activityName);
                            activity.initViews();
                        }else {
                            ToastUtil.showToast(activity.getApplicationContext(),"获取活动失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_people);
        appContext = (BaseApplication) getApplication();
        Intent intent = getIntent();
        activity_id = intent.getStringExtra("activity_id");
        LogUtils.d("xu_activity",activity_id);
        ActivityContent = intent.getStringExtra("content");
        isNew = intent.getBooleanExtra("new", true);
        if(activity_id != null){
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{activity_id}, handler, GET_ACTIVITY);
        }

    }

    private void initViews() {
        TextView tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText(R.string.big_stage);
        ImageButton ib_back = (ImageButton) findViewById(R.id.hot_ib_back);
        Button bt_review = (Button) findViewById(R.id.bt_review);
        Button bt_save = (Button) findViewById(R.id.bt_save);
        Button bt_release = (Button) findViewById(R.id.bt_release);

        ib_back.setOnClickListener(this);
        bt_release.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_review.setOnClickListener(this);

        listView = (XListView) findViewById(R.id.listView);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        adapter = new ListAdapterInvitePeople(this,appContext);
        listView.setAdapter(adapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{activity_id, "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{activity_id, "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(InvitePeopleActivity.this, "没有更多数据");
                }
            }
        });

        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{activity_id, "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_ib_back:
                finish();
                break;
            case R.id.bt_review:
                finish();
                break;
            case R.id.bt_save:
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;

                ToastUtil.showToast(this, "保存成功，请到草稿箱中修改和查看");
                finish();
                break;
            case R.id.bt_release:
                List<ImageItem> imgLists = Bimp.tempSelectBitmap;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity_id}, handler, Constant.UPLOAD_ACTIVTY);
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            Bimp.tempSelectBitmap.clear();
        super.onBackPressed();
    }

    public String getActivityID(){
        return activity_id;
    }

    public String getActivityTitle(){
        return activityName;
    }

    public String getActivityContent(){
        return ActivityContent;
    }
}
