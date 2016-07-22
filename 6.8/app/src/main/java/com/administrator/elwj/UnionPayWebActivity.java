package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.bean.OrderDetailsBean;
import com.administrator.utils.OrderStatus;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

/**
 * 银联支付网页页面
 * Created by Administrator on 2016/5/12.
 */
public class UnionPayWebActivity extends AppCompatActivity {

    //是否需要显示付款失败，只有当点击完成按钮并且付款失败时才显示提示
    private boolean needShowFailed = false;

    private BaseApplication application;

    //获取订单详情
    private static final int GET_ORDER_DETAIL = 0;

    //订单id
    private String orderID;

    public static class MyHandler extends Handler {
        private WeakReference<UnionPayWebActivity> mActivity;

        public MyHandler(UnionPayWebActivity activity) {
            mActivity = new WeakReference<UnionPayWebActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            UnionPayWebActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                switch (msg.what) {
                    case GET_ORDER_DETAIL:
                        //是否支付成功
                        boolean paySuccess = false;
                        Gson gson = new Gson();
                        OrderDetailsBean orderDetailsBean = gson.fromJson(json, OrderDetailsBean.class);
                        if (orderDetailsBean.getResult() == 1) {
                            OrderDetailsBean.DataEntity dataEntity = orderDetailsBean.getData();
                            if (dataEntity != null) {
                                OrderDetailsBean.DataEntity.OrderEntity orderEntity = dataEntity.getOrder();
                                if(orderEntity != null && orderEntity.getOrderStatus() != null){
                                    if(orderEntity.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM))){
                                        paySuccess = true;
                                    }
                                }
                            }
                            if(paySuccess){
                                activity.setResult(RESULT_OK);
                                activity.finish();
                            }else if(activity.needShowFailed){
                                ToastUtil.showToast(activity, "支付失败");
                            }
                            activity.finish();
                        } else {
                            ToastUtil.showToast(activity, "获取订单支付信息失败");
                            activity.finish();
                        }
                        break;
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unionpay_web);
        application = (BaseApplication) getApplication();
        initViews();
    }

    private void initViews() {
        //返回按钮
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needShowFailed = false;
                exit();
            }
        });
        //完成按钮
        TextView tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needShowFailed = true;
                exit();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webview_main);
        webView.getSettings().setJavaScriptEnabled(true);
        //在页面中跳转的话不允许跳转到系统浏览器，还必须在webview中浏览
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String data = intent.getStringExtra("data");
            if (data != null) {
                webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            }
            orderID = intent.getStringExtra("orderid");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            needShowFailed = false;
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出前需要查看支付状态，必要时返回Result_OK
    private void exit() {
        if (orderID != null && !"".equals(orderID))
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getOrderDetail, new String[]{"orderid"}, new String[]{orderID}, handler, GET_ORDER_DETAIL);
    }

}
