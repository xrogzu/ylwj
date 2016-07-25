package com.administrator.elwj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterMyAttentionGoods;
import com.administrator.bean.AttentionGoods;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

//我的收藏页面
public class MyCollectioinActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private XListView listView;
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;

    private BaseApplication application;

    //数据适配器
    private ListAdapterMyAttentionGoods mAttentionGoodsAdapter;

    private static final int GET_MY_ATTENTION = 1;

    public static class MyHandler extends Handler {
        private WeakReference<MyCollectioinActivity> mActivity;

        public MyHandler(MyCollectioinActivity activity) {
            mActivity = new WeakReference<MyCollectioinActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyCollectioinActivity activity = mActivity.get();
            if (activity != null) {
                activity.listView.stopRefresh();
                activity.listView.stopLoadMore();
                switch (msg.what) {
                    case GET_MY_ATTENTION:
                        if(activity.progressDialog != null){
                            activity.progressDialog.dismiss();
                            activity.progressDialog = null;
                        }
                        String json = (String) msg.obj;
                        if (json != null) {
                            LogUtils.d("xu_test_attention", json);
                            Gson gson = new Gson();
                            AttentionGoods attentionGoods = gson.fromJson(json, AttentionGoods.class);
                            if (attentionGoods != null && attentionGoods.getResult() == 1) {
                                List<AttentionGoods.DataEntity> dataEntities = attentionGoods.getData();
                                if (dataEntities == null || dataEntities.size() == 0) {
                                    activity.hasMoreData = false;
                                    if (activity.curPage == 1) {
                                        activity.mAttentionGoodsAdapter.clear();
                                    }
                                } else {
                                    if (activity.curPage == 1) {
                                        activity.mAttentionGoodsAdapter.clear();
                                        activity.hasMoreData =  true;
                                    }
                                    if(dataEntities.size() < activity.pageSize){
                                        activity.hasMoreData = false;
                                    }
                                    activity.mAttentionGoodsAdapter.addData(attentionGoods.getData());
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        application = (BaseApplication) getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waitNetRequest));
        progressDialog.show();
        initViews();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
    }

    private void initViews() {
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (XListView) findViewById(R.id.listview_my_collection);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        mAttentionGoodsAdapter = new ListAdapterMyAttentionGoods(this, application);
        listView.setAdapter(mAttentionGoodsAdapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(MyCollectioinActivity.this,"没有更多数据");
                }
            }
        });

    }

}
