package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterFinancialProduct;
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
 * 金融产品
 * Created by Administrator on 2016/3/6.
 * 热门产品ListView
 */
public class FinancialProductActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

//    //存储类产品
//    public static final int TYPE_CHUXU = 1;
//    //理财类产品
//    public static final int TYPE_LICAI = 2;
//    //信用卡类产品
//    public static final int TYPE_XINYONG = 3;
//    //银行卡
//    public static final int TYPE_YINHANG = 4;
//    //贷款类
//    public static final int TYPE_DAIKUAN = 5;
//    //移动银行
//    public static final int TYPE_YIDONG = 6;

    private XListView listView;

    private int curPage = 1;
    private int pageSize = 10;

    private boolean hasMoreData = true;

    private BaseApplication application;

    private ListAdapterFinancialProduct mAdapter;

    public static class MyHandler extends Handler {
        private WeakReference<FinancialProductActivity> mActivity;

        public MyHandler(FinancialProductActivity activity) {
            mActivity = new WeakReference<FinancialProductActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FinancialProductActivity activity = mActivity.get();
            if (activity != null) {
                if(activity.progressDialog != null){
                    activity.progressDialog.dismiss();
                    activity.progressDialog = null;
                }
                activity.listView.stopLoadMore();
                activity.listView.stopRefresh();
                String json = (String) msg.obj;
                LogUtils.d("xu_test", json);
                Gson gson = new Gson();
                FinancialProduct financialProduct = gson.fromJson(json, FinancialProduct.class);
                if (financialProduct != null && financialProduct.getResult() == 1) {
                    if (activity.curPage == 1) {
                        activity.hasMoreData = true;
                        activity.mAdapter.clear();
                    }
                    List<FinancialProduct.DataEntity> dataEntities = financialProduct.getData();
                    activity.mAdapter.addAdd(dataEntities, activity);
                    if (dataEntities.size() < activity.pageSize) {
                        activity.hasMoreData = false;
                        ToastUtil.showToast(activity, "没有更多了");
                    } else {
                        activity.hasMoreData = true;
                    }
                }
            }

        }
    }

    private Handler handler = new MyHandler(this);


    private int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financialproduct);
        application = (BaseApplication) getApplication();
        initIntentData();
        initViews();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waitNetRequest));
        progressDialog.show();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
    }

    private void initViews() {

        TextView tvTitle = (TextView) findViewById(R.id.title);
        ImageButton back = (ImageButton) findViewById(R.id.hot_ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("家庭财富");

        mAdapter = new ListAdapterFinancialProduct(this);
        listView = (XListView) findViewById(R.id.listview_financialproduct);
        listView.setPullLoadEnable(true);
        listView.setAdapter(mAdapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
                } else {
                    listView.stopLoadMore();
                    listView.stopRefresh();
                    ToastUtil.showToast(FinancialProductActivity.this, "没有更多数据了");
                }
            }
        });
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null)
            mType = intent.getIntExtra("type", 0);
    }
}
