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

import com.administrator.adapter.ListAdapterDraftInvitePeople;
import com.administrator.adapter.ListAdapterInvitePeople;
import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.BigStage_Activity;
import com.administrator.bean.BigstageUser;
import com.administrator.bean.Constant;
import com.administrator.fragment.Launch_activityFragment;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.DraftStartPickPhotoActivity;
import com.king.photo.activity.StartPickPhotoActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageItem;
import com.library.listview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

//草稿箱流程中的邀请人参与互动页面
public class DraftInvitePeopleActivity extends AppCompatActivity implements View.OnClickListener {

    private XListView listView;
    private ListAdapterDraftInvitePeople adapter;
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;
    private BaseApplication appContext;
    private ActivityDetails.DataEntity mData;
    private static final int GET_DATA = 0;
    private static final int SAVE_DATA = 1;

    public String getActivityContent() {
        return mData.getIntroduction();
    }

    public String getActivityID() {
        return mData.getActivity_id();
    }

    public String getActivityTitle() {
        return mData.getTitle();
    }

    public static class MyHandler extends Handler {
        private WeakReference<DraftInvitePeopleActivity> mActivity;

        public MyHandler(DraftInvitePeopleActivity activity) {
            mActivity = new WeakReference<DraftInvitePeopleActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DraftInvitePeopleActivity activity = mActivity.get();
            if (activity != null) {
                activity.listView.stopLoadMore();
                activity.listView.stopRefresh();
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.UPLOAD_ACTIVTY) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            Intent intentBroadCast = new Intent("android.intent.action.DRAFT_UPDATE");
                            intentBroadCast.putExtra("golist",true);
                            activity.sendBroadcast(intentBroadCast);
                            ToastUtil.showToast(activity,"发布成功");
//                            Toast.makeText(activity,"发布成功",Toast.LENGTH_SHORT).show();
                            LogUtils.e("Main", json);
                            Intent intent = new Intent(activity,BigStageNormalDetailsActivity.class);
                            Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                            bean_bigstage_list.setActivity_id(activity.mData.getActivity_id());
                            intent.putExtra("bean", bean_bigstage_list);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                }else if(which == SAVE_DATA){
                    LogUtils.d("xu_save_data",json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if(jsonObject.getInt("result") == 1){
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity.mData.getActivity_id()}, activity.handler, Constant.UPLOAD_ACTIVTY);
                            Bimp.tempSelectBitmap.clear();
                            Bimp.max = 0;
//                            DraftStartPickPhotoActivity.instance.finish();
//                            if (AlbumActivity.instance != null) {
//                                AlbumActivity.instance.finish();
//                            }
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
        mData = (ActivityDetails.DataEntity) intent.getSerializableExtra("data");
        initViews();
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
        adapter = new ListAdapterDraftInvitePeople(this,appContext);
        listView.setAdapter(adapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{mData.getActivity_id(), "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{mData.getActivity_id(), "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(DraftInvitePeopleActivity.this,"没有更多数据");
//                    Toast.makeText(DraftInvitePeopleActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                }
            }
        });

        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type", "page", "pageSize"}, new String[]{mData.getActivity_id(), "2", String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);

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
//                if (AlbumActivity.instance != null) {
//                    AlbumActivity.instance.finish();
//                    AlbumActivity.instance = null;
//                }
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.editActivity,
                        new String[]{"activity_id", "title", "site", "introduction", "apply_start_time", "apply_end_time", "activity_start_time", "activity_end_time", "max_participants_num","activity_type","options"},
                        new String[]{mData.getActivity_id(),mData.getTitle(),mData.getSite(),mData.getIntroduction(),mData.getApply_start_time(),mData.getApply_end_time(),mData.getActivity_start_time(),mData.getActivity_end_time(),mData.getMax_participants_num(),mData.getActivity_type(),""},
                        handler,SAVE_DATA);
                break;
            case R.id.bt_release:
                List<ImageItem> imgLists = Bimp.tempSelectBitmap;
//                AlbumHelper helper=AlbumHelper.getHelper();
//                helper.init(this);
//                helper.getOriginalImagePath();
//                for(int i=0;i<imgLists.size();i++){
//                   imgLists.get(i).getImagePath();
//                }
                boolean isPublish = true;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.editActivity,
                        new String[]{"activity_id", "title", "site", "introduction", "apply_start_time", "apply_end_time", "activity_start_time", "activity_end_time", "max_participants_num", "activity_type", "options"},
                        new String[]{mData.getActivity_id(), mData.getTitle(), mData.getSite(), mData.getIntroduction(), mData.getApply_start_time(), mData.getApply_end_time(), mData.getActivity_start_time(), mData.getActivity_end_time(), mData.getMax_participants_num(), mData.getActivity_type(), ""},
                        handler, SAVE_DATA);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            Bimp.tempSelectBitmap.clear();
        super.onBackPressed();
    }
}
