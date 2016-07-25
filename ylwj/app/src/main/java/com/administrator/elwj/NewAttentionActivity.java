package com.administrator.elwj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterNewAttention;
import com.administrator.bean.AttentionPerson;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 最新界面中的新的关注页面
 * Created by Administrator on 2016/4/10.
 */
public class NewAttentionActivity extends AppCompatActivity {

    private XListView listView;

    private static final int GET_DATA = 0;

    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;

    private ListAdapterNewAttention mAdapter;

    private BaseApplication application;

    public static class MyHandler extends Handler{
        private WeakReference<NewAttentionActivity> mActivity;
        public MyHandler(NewAttentionActivity attentionActivity){
            mActivity = new WeakReference<NewAttentionActivity>(attentionActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewAttentionActivity activity = mActivity.get();
            if(activity != null){
                activity.listView.stopLoadMore();
                activity.listView.stopRefresh();
                String json = (String) msg.obj;
                if(msg.what == GET_DATA){
                    LogUtils.d("xu_newattention", json);
                    Gson gson = new Gson();
                    AttentionPerson attentionPerson = gson.fromJson(json, AttentionPerson.class);
                    if(attentionPerson != null){
                        if(activity.curPage == 1){
                            activity.hasMoreData = true;
                            activity.mAdapter.clear();
                        }
                        List<AttentionPerson.DataEntity> dataEntities = attentionPerson.getData();
                        if(dataEntities != null) {
                            if(dataEntities.size() < activity.pageSize)
                                activity.hasMoreData = false;
                            else activity.hasMoreData = true;
                            activity.mAdapter.addData(dataEntities);
                        }
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newattention);
        application = (BaseApplication) getApplication();
        initViews();
    }

    private void initViews() {
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAttentionActivity.this.finish();
            }
        });
        listView = (XListView) findViewById(R.id.listview_newattention);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        mAdapter = new ListAdapterNewAttention(this,application);
        listView.setAdapter(mAdapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage  = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getFansList, new String[]{"page","pageSize"},new String[]{String.format("%d",curPage),String.format("%d",pageSize)},handler,GET_DATA);
            }

            @Override
            public void onLoadMore() {
                if(hasMoreData){
                    curPage ++ ;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getFansList, new String[]{"page","pageSize"},new String[]{String.format("%d",curPage),String.format("%d",pageSize)},handler,GET_DATA);
                }else{
                    listView.stopLoadMore();
                    ToastUtil.showToast(NewAttentionActivity.this,"没有更多");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getFansList, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_DATA);
    }
}
