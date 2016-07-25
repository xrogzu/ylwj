package com.administrator.elwj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.bean.UserInfo;
import com.administrator.bean.UserInfoExtra;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 前人写的activity，目前没有用到，可以删除
 * Created by Administrator on 2016/3/12.
 */
public class AttentionMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseApplication appContext;
    protected int mScreenWidth ;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private String name;
    private String uri;
    private int member_id;
    private ImageView attention;

    public static class MyHandler extends Handler {
        private WeakReference<AttentionMemberActivity> MyAttention;
        public  MyHandler(AttentionMemberActivity activity){
            MyAttention = new WeakReference<AttentionMemberActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            AttentionMemberActivity activity = MyAttention.get();
            if(activity != null) {
                int which = msg.what;
                String Json = (String) msg.obj;
                JSONObject jsonObject = null;
                switch (which) {
                    case Constant.ATTENTION:
                        try {
                            jsonObject = new JSONObject(Json);
                            int result = jsonObject.optInt("result");
                            String message=jsonObject.getString("message");

                            if (result == 0) {//已关注过
                                ToastUtil.showToast(activity, message);
//                                Toast.makeText(AttentionMemberActivity.this, message,Toast.LENGTH_SHORT).show();
                            } else if (result == 1) {
                                ToastUtil.showToast(activity,message);
//                                Toast.makeText(AttentionMemberActivity.this, message,Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                        case Constant.IS_ATTENTION:
                            try {
                                jsonObject = new JSONObject(Json);
                                int result = jsonObject.optInt("result");
                                String message=jsonObject.getString("message");


                                if (Integer.valueOf(message) == 1) {//已关注过
//                                    attention.setBackgroundResource(R.mipmap.guanzhu_icon_03);
                                    activity.attention.setBackgroundResource(R.mipmap.guanzhu_icon_02);
//                                    attention.setFocusable(false);
                                } else if (Integer.valueOf(message) == 0) {
                                    activity.attention.setBackgroundResource(R.mipmap.guanzhu_icon_03);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                }
            }
        }
    }
    private MyHandler attentionHandler = new MyHandler(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_member);
        appContext = (BaseApplication) getApplication();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        Intent intent = getIntent();
        String userInfo=intent.getStringExtra("user_info");
        try {
            JSONObject user_object= new JSONObject(userInfo);
             name=user_object.getString("name");
             uri=user_object.getString("uri");
             member_id=user_object.getInt("member_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getIsAttention, new String[]{"member_id"}, new String[]{String.valueOf(member_id)}, attentionHandler, Constant.IS_ATTENTION);
        initViews();

    }
    private void initViews() {

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(name);
        ImageButton back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(this);
        ImageView head = (ImageView) findViewById(R.id.attention_head);
        attention = (ImageView) findViewById(R.id.attention_go);
        TextView userName = (TextView) findViewById(R.id.attention_name);

        imageLoader.displayImage(uri, head, options);
        userName.setText(name);
        attention.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attention_go://回退按钮
                if(member_id==0){
                    ToastUtil.showToast(AttentionMemberActivity.this, "请扫描正确的二维码");
//                    Toast.makeText(AttentionMemberActivity.this, "请扫描正确的二维码",Toast.LENGTH_SHORT).show();
                }else {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.Attention, new String[]{"member_id"}, new String[]{String.valueOf(member_id)}, attentionHandler, Constant.ATTENTION);
                }
                break;
            case R.id.ib_back://
                finish();
                break;
        }
    }
}
