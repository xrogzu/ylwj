package com.administrator.elwj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterInvite;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.listview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 最新页面中的活动邀请界面
 * Created by xu on 2016/4/9.
 */
public class ActivityInviteActivity extends AppCompatActivity {

    private static final int GET_DATA = 0;
    private XListView listView;
    private ListAdapterInvite mListAdapter;
    private BaseApplication application;
    //当前是第几页
    private int curPage = 1;
    //一页数据的个数
    private int pageSize = 10;
    //是否有更多数据
    private boolean hasMoreData = true;


    public static class MyHandler extends Handler {
        private WeakReference<ActivityInviteActivity> mActivity;

        public MyHandler(ActivityInviteActivity activityInviteActivity) {
            mActivity = new WeakReference<ActivityInviteActivity>(activityInviteActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityInviteActivity activity = mActivity.get();
            if (activity != null) {
                activity.listView.stopRefresh();
                activity.listView.stopLoadMore();
                String json = (String) msg.obj;
                if (msg.what == GET_DATA) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            String data = jsonObject.getString("data");
                            if (data != null && !"".equals(data)) {
                                Gson gson = new Gson();
                                List<Bean_Bigstage_List> datas = gson.fromJson(data, new TypeToken<List<Bean_Bigstage_List>>() {
                                }.getType());
                                if (datas != null) {
                                    if (activity.curPage == 1) {
                                        activity.hasMoreData = true;
                                        activity.mListAdapter.clear();
                                    }
                                    activity.hasMoreData = datas.size() >= activity.pageSize;
                                    activity.mListAdapter.addData(datas);
                                }
                            }
                        } else {
                            ToastUtil.showToast(activity,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteactivity);
        application = (BaseApplication) getApplication();
        initViews();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getPushActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
    }

    private void initViews() {
        //左上角返回键
        ImageButton imageButton = (ImageButton) findViewById(R.id.ib_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityInviteActivity.this.finish();
            }
        });
        listView = (XListView) findViewById(R.id.listview_activity_invite);
        mListAdapter = new ListAdapterInvite(this,application);
        listView.setAdapter(mListAdapter);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getPushActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getPushActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(ActivityInviteActivity.this, "没有更多");
                }
            }
        });
    }
}
