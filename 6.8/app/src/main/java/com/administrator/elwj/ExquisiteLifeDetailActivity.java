package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.bean.FinancialProduct;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 精致生活详情页面
 * Created by Administrator on 2016/3/12.
 */
public class ExquisiteLifeDetailActivity extends AppCompatActivity {

    //上方图片底部的文字
    private TextView tvContentTitle;
    //上方图片
    private ImageView ivMain;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private WebView webView;
    //精致生活的数据bean
    private FinancialProduct.DataEntity mData;
    //最上方导航栏中的标题
    private TextView title;

    private BaseApplication application;

    private static final int GET_DATA = 1;

    //静态内部handler，防止内存泄露
    public static class MyHandler extends Handler {
        private WeakReference<ExquisiteLifeDetailActivity> mActivity;

        public MyHandler(ExquisiteLifeDetailActivity activity) {
            mActivity = new WeakReference<ExquisiteLifeDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ExquisiteLifeDetailActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    //result为1表示获取数据成功
                    if (jsonObject.getInt("result") == 1) {
                        String data = jsonObject.getString("data");
                        if (msg.what == GET_DATA) {
                            Gson gson = new Gson();
                            activity.mData = gson.fromJson(data, FinancialProduct.DataEntity.class);
                            activity.showData();
                        }
                    }
                    //获取数据不能够时显示服务器的错误信息
                    else {
                        ToastUtil.showToast(activity, jsonObject.getString("message"));
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
        setContentView(R.layout.activity_exquisitelife_detail);
        application = (BaseApplication) getApplication();
        initImageLoader();
        initViews();
        initIntentData();
    }
    //初始化imageloader
    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }


    //获取intent
    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String title_str = intent.getStringExtra("title");
            String mID = intent.getStringExtra("id");
            title.setText(title_str);
            //支持javascript，设置支持
            webView.getSettings().setJavaScriptEnabled(true);
//            String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
//                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
//                    "<head>\n" +
//                    "<head>\n" +
//                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
//                    "    <script type=\"text/javascript\">\n" +
//                    "    //JavaScript方法，刷新界面\n" +
//                    "    function myFunction()\n" +
//                    "    {\n" +
//                    "       var imgs = document.getElementsByTagName('img');\n" +
//                    "       for(var i = 0; i<imgs.length; i++)\n" +
//                    "       {\n" +
//                    "           imgs[i].style.width = '100%';\n" +
//                    "           imgs[i].style.height = 'auto';\n" +
//                    "       }\n" +
//                    "    }\n" +
//                    "</script>" +
//                    "</head>\n" +
//                    "<body>" +
//                    mData.getContent() +
//                    "</body>";
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            if (mID != null) {
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProductByID, new String[]{"product_id"}, new String[]{mID}, handler, GET_DATA);
            }else{//精致生活华夏VIP服务(暂时写死，以后调整)
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProductByID, new String[]{"product_id"}, new String[]{"113"}, handler, GET_DATA);
         //       VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", 1), String.format("%d", 1), String.format("%d", ExquisiteLifeActivity.TYPE_HUAXIA_VIP)}, handler, GET_DATA);

                title.setText("银行VIP服务");
            }
        }
    }

    //初始化view
    private void initViews() {

        title = (TextView) findViewById(R.id.title_detail);
        webView = (WebView) findViewById(R.id.test);

        //右上角的返回键
        ImageView ivBack = (ImageView) findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExquisiteLifeDetailActivity.this.finish();
            }
        });


        ivMain = (ImageView) findViewById(R.id.iv_exquisitelife_pic);
        tvContentTitle = (TextView) findViewById(R.id.tv_exquisitelife_title);
    }

    //显示数据
    private void showData() {
        if (mData != null) {
            //content字段是html代码，直接加载在webview中
            webView.loadDataWithBaseURL(null, mData.getContent(), "text/html", "utf-8", null);
            tvContentTitle.setText(mData.getName());
            imageLoader.displayImage(mData.getImageurl(), ivMain, options);
        }
    }
}
