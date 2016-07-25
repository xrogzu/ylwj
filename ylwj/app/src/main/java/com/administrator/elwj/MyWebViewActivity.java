package com.administrator.elwj;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.CommList;
import com.administrator.bean.Constant;
import com.administrator.bean.FinancialProduct;
import com.administrator.bean.NonList;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.ShareUtil2;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 浏览器页面，金融管家里面各项最终进入的详情界面
 * Created by Administrator on 2016/3/6.
 */
public class MyWebViewActivity extends AppCompatActivity {

    //推荐消息
    public static final int TYPE_RECOMMEND = 0;
    //金融产品
    public static final int TYPE_PRODUCT = 1;
    //金融常识
    public static final int TYPE_COMM = 2;

    private TextView tvTitle;

    private TextView tvTitle1;

    private int mType = 0;

    private ImageView iv_webview_share;

    private WebView webView;
    //金融常识
    private NonList.DataEntity mNonListData;
    //金融产品
    private FinancialProduct.DataEntity mFinancialData;
    //推荐消息
    private CommList.DataEntity mCommData;

    private BaseApplication application;

    private static final int GET_DATA = 1;

    public static class MyHandler extends Handler {
        private WeakReference<MyWebViewActivity> mActivity;

        public MyHandler(MyWebViewActivity activity) {
            mActivity = new WeakReference<MyWebViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyWebViewActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                LogUtils.d("xu", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject.getInt("result") == 1) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if (msg.what == GET_DATA) {
                            switch (activity.mType) {
                                case TYPE_RECOMMEND:
                                    activity.mCommData = gson.fromJson(data, CommList.DataEntity.class);
                                    break;
                                case TYPE_COMM:
                                    activity.mNonListData = gson.fromJson(data, NonList.DataEntity.class);
                                    break;
                                case TYPE_PRODUCT:
                                    activity.mFinancialData = gson.fromJson(data, FinancialProduct.DataEntity.class);
                                    break;
                            }
                            activity.showData();
                        }
                    }else{
                        ToastUtil.showToast(activity,jsonObject.getString("message"));
//                        Toast.makeText(activity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywebview);
        application = (BaseApplication) getApplication();
        initViews();
        initWebView();
        getIntentData();
    }

    private void getIntentData() {
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);

        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        if (Build.VERSION.SDK_INT < 19) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        else webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.getSettings().setLoadWithOverviewMode(false);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra("type", 0);
            tvTitle.setText(intent.getStringExtra("title"));
            String mID = intent.getStringExtra("id");
            switch (mType) {
                case TYPE_COMM:
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNousByID, new String[]{"nous_id"}, new String[]{mID}, handler, GET_DATA);
                    break;
                case TYPE_PRODUCT:
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProductByID, new String[]{"product_id"}, new String[]{mID}, handler, GET_DATA);
                    break;
                case TYPE_RECOMMEND:
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getRecommByID, new String[]{"comm_id"}, new String[]{mID}, handler, GET_DATA);
                    break;
            }

//            String content = "";
//            if (Build.VERSION.SDK_INT >= 19) {
//                content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
//                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
//                        "<head>\n" +
//                        "<head>\n" +
//                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
//                        "    <script type=\"text/javascript\">\n" +
//                        "    //JavaScript方法，刷新界面\n" +
//                        "    function myFunction()\n" +
//                        "    {\n" +
//                        "       var imgs = document.getElementsByTagName('img');\n" +
//                        "       for(var i = 0; i<imgs.length; i++)\n" +
//                        "       {\n" +
//                        "           imgs[i].style.width = '100%';\n" +
//                        "           imgs[i].style.height = 'auto';\n" +
//                        "       }\n" +
//                        "    }\n" +
//                        "</script>" +
//                        "</head>\n" +
//                        "<body>\n";
//            }
//            switch (mType) {
//                case TYPE_COMM:
//                    mNonListData = intent.getParcelableExtra("data");
//                    content += mNonListData.getContent_txt();
//                    ;
//                    tvTitle1.setText(mNonListData.getTitle());
//                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNousByID, new String[]{"nous_id"}, new String[]{String.format("%d", mNonListData.getId())}, handler, GET_DATA);
//                    break;
//                case TYPE_PRODUCT:
//                    mFinancialData = intent.getParcelableExtra("data");
//                    content += mFinancialData.getContent();
//                    tvTitle1.setText(mFinancialData.getName());
//                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProductByID, new String[]{"product_id"}, new String[]{String.format("%d", mFinancialData.getId())}, handler, GET_DATA);
//                    break;
//                case TYPE_RECOMMEND:
//                    mCommData = intent.getParcelableExtra("data");
//                    content += mCommData.getContenturl();
//                    tvTitle1.setText(mCommData.getTitle());
//                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getRecommByID, new String[]{"comm_id"}, new String[]{String.format("%d", mCommData.getId())}, handler, GET_DATA);
//                    break;
//            }
//            if (Build.VERSION.SDK_INT >= 19)
//                content += "</body>";
//            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

        }
    }


    /**
     * 图片显示完全
     *
     * @param info
     * @return
     */
    private String formatImageURL(String info) {
        String content = "";
        if (Build.VERSION.SDK_INT >= 19) {
            content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "    //JavaScript方法，刷新界面\n" +
                    "    function myFunction()\n" +
                    "    {\n" +
                    "       var imgs = document.getElementsByTagName('img');\n" +
                    "       for(var i = 0; i<imgs.length; i++)\n" +
                    "       {\n" +
                    "           imgs[i].style.width = '100%';\n" +
                    "           imgs[i].style.height = 'auto';\n" +
                    "       }\n" +
                    "    }\n" +
                    "</script>" +
                    "</head>\n" +
                    "<body>\n" + info + "</body>";
            return content;
        } else {
            return info;
        }

    }


    private void showData() {
        switch (mType) {
            case TYPE_COMM:
                if(mNonListData != null) {
                    webView.loadDataWithBaseURL(null, formatImageURL(mNonListData.getContent_txt()), "text/html", "utf-8", null);
                    tvTitle1.setText(mNonListData.getTitle());
                }
                break;
            case TYPE_PRODUCT:
                if(mFinancialData != null) {
                    webView.loadDataWithBaseURL(null, formatImageURL(mFinancialData.getContent()), "text/html", "utf-8", null);
                    tvTitle1.setText(mFinancialData.getName());
                }
                break;
            case TYPE_RECOMMEND:
                if(mCommData != null) {
                    webView.loadDataWithBaseURL(null, formatImageURL(mCommData.getContenturl()), "text/html", "utf-8", null);
                    tvTitle1.setText(mCommData.getTitle());
                }
                break;
        }

        if(Build.VERSION.SDK_INT >= 19) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:myFunction()");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                    //return super.shouldOverrideUrlLoading(view, url);
                }
            });
        }
    }

    public void initWebView() {
        //支持javascript，设置支持
        webView.getSettings().setJavaScriptEnabled(true);

//        // 设置可以支持缩放
//        webView.getSettings().setSupportZoom(false);
//        // 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(false);
//        //扩大比例的缩放
//        webView.getSettings().setUseWideViewPort(true);
//        //自适应屏幕
//        if(Build.VERSION.SDK_INT >= 19)
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//        else {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        }
//        webView.getSettings().setLoadWithOverviewMode(true);
    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.my_webview);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle1 = (TextView) findViewById(R.id.webView_title);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_webview_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWebViewActivity.this.finish();
            }
        });
        iv_webview_share= (ImageView) findViewById(R.id.iv_webview_share);
        iv_webview_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil2 shareUtil2 = new ShareUtil2(MyWebViewActivity.this);
                switch (mType) {
                    case TYPE_COMM: {//金融常识
                        String link = "http://www.shequchina.cn/javamall/api/mobile/fin!getShareNousById.do?nous_id=";
                        link = link + mNonListData.getId();
                        Log.i("shareLink",link);
                        shareUtil2.openShare(MyWebViewActivity.this.findViewById(R.id.start), MyWebViewActivity.this, mNonListData.getTitle(), mNonListData.getImageurl(), false, null, link);
                    }
                        break;
                    case TYPE_PRODUCT:{//金融产品
                        String link = "http://www.shequchina.cn/javamall/api/mobile/fin!getShareProductById.do?product_id=";
                        link = link + mFinancialData.getId();
                        Log.i("shareLink", link);
                        shareUtil2.openShare(MyWebViewActivity.this.findViewById(R.id.start), MyWebViewActivity.this, mFinancialData.getName(), mFinancialData.getImageurl(), false, null, link);
                     }
                       break;
                    case TYPE_RECOMMEND: {//推荐消息
                        String link = "http:///www.shequchina.cn/javamall/api/mobile/fin!getShareRecommondById.do?comm_id=";
                        link = link + mCommData.getId();
                        Log.i("shareLink", link);
                        shareUtil2.openShare(MyWebViewActivity.this.findViewById(R.id.start), MyWebViewActivity.this, mCommData.getTitle(), mCommData.getImageurl(), false, null, link);
                    }
                        break;
            }
        }
        });
    }
}
