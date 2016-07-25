package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterLife;
import com.administrator.bean.Constant;
import com.administrator.bean.FinancialProduct;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 * 精致生活List
 */
public class ExquisiteLifeActivity extends AppCompatActivity {
    public static final int TYPE_HUAXIA_BANK = 7;//银行特约商户 //正式的type为5
    public static final int TYPE_HUAXIA_VIP = 6;//银行VIP服务
    public static final int TYPE_BANJIA = 9;//搬家
    public static final int TYPE_BAOMU = 10;//保姆月嫂
    public static final int TYPE_WEIXIU = 11;//家具维修
    public static final int TYPE_BAOJIE = 12;//保洁
    public static final int TYPE_PEISONG = 13;//生活配送
    public static final int TYPE_XIANHUA = 14;//鲜花绿植

    private ProgressDialog progressDialog;


    //列表视图
    private XListView listview;
    //当前请求到的页
    private int curPage = 1;
    //一页的数据个数
    private int pageSize = 10;
    //是否还有更多数据
    private boolean hasMoreData = true;
    //列表适配器
    private ListAdapterLife mAdapter;
    private BaseApplication appContext;
    //精致生活请求服务器传的类型值
    private int mType = 7;
    private TextView regimen_title;

    //handler用静态内部类，避免内存泄露
    public static class MyHandler extends Handler {
        private WeakReference<ExquisiteLifeActivity> mActivity;

        public MyHandler(ExquisiteLifeActivity activity) {
            mActivity = new WeakReference<ExquisiteLifeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ExquisiteLifeActivity activity = mActivity.get();
            if (activity != null) {
                if (activity.progressDialog != null) {
                    activity.progressDialog.dismiss();
                    activity.progressDialog = null;
                }
                activity.listview.stopRefresh();
                activity.listview.stopLoadMore();
                String json = (String) msg.obj;
                LogUtils.d("xu_test", json);
                Gson gson = new Gson();
                FinancialProduct financialProduct = gson.fromJson(json, FinancialProduct.class);
                if (financialProduct != null && financialProduct.getResult() == 1) {
                    if (activity.curPage == 1) {
                        activity.mAdapter.clear();
                        activity.hasMoreData = true;
                    }
                    List<FinancialProduct.DataEntity> dataEntities = financialProduct.getData();
                    activity.mAdapter.addData(dataEntities);
                    if (dataEntities.size() < activity.pageSize) {
                        activity.hasMoreData = false;
                    } else {
                        activity.hasMoreData = true;
                    }
                }
            }

        }

    }


    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exquisitelife);
        appContext = (BaseApplication) getApplication();
        initViews();
        initIntentData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waitNetRequest));
        progressDialog.show();

        //请求网络数据
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);

    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra("type", 0);
            if (mType == TYPE_HUAXIA_BANK) {
                regimen_title.setText("银行特约商户");
            }
        }
    }

    //初始化view
    private void initViews() {
        //右上角的返回键
        ImageView ivBack = (ImageView) findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExquisiteLifeActivity.this.finish();
            }
        });
        listview = (XListView) findViewById(R.id.listview_life);
        regimen_title = (TextView) findViewById(R.id.regimen_title);
        mAdapter = new ListAdapterLife(this);
        listview.setAdapter(mAdapter);
        listview.setPullLoadEnable(true);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
                } else {
                    listview.stopLoadMore();
                    ToastUtil.showToast(ExquisiteLifeActivity.this, "没有更多数据");
                }
            }
        });
        //点击listview的某个项，跳转详情
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FinancialProduct.DataEntity dataEntity = mAdapter.getItem(position - 1);
                LogUtils.d("xu_test", String.format("%d", position));
                Intent intent = new Intent(ExquisiteLifeActivity.this, ExquisiteLifeDetailActivity.class);
                intent.putExtra("id", String.format("%d", dataEntity.getId()));
                intent.putExtra("title", "精致生活");
                startActivity(intent);
            }
        });
    }
}
