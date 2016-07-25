package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
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

/**
 * 扫名片成功后显示的页面
 * Created by Administrator on 2016/4/20.
 */
public class MyScanResultActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvInfo;
    private ImageView ivGender;
    private String member_id;
    private ImageView ivHead;
    private ImageButton ibAttention;
    private BaseApplication application;
    private UserInfoExtra.DataEntity mDataEntity;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private static final int GET_USER_INFO = 0;
    private static final int ATTENTION = 1;
    private static final int CANCEL_ATTENTION = 2;

    public static class MyHandler extends Handler {
        private WeakReference<MyScanResultActivity> mActivity;

        public MyHandler(MyScanResultActivity activity) {
            mActivity = new WeakReference<MyScanResultActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyScanResultActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                if (msg.what == GET_USER_INFO) {
                    Gson gson = new Gson();
                    UserInfoExtra userInfoExtra = gson.fromJson(json, UserInfoExtra.class);
                    if (userInfoExtra.getResult() == 1) {
                        activity.mDataEntity = userInfoExtra.getData();
                        activity.initData();
                    } else {
                        ToastUtil.showToast(activity, "获取失败");
//                        Toast.makeText(activity, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == ATTENTION) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        String message = jsonObject.getString("message");
                        if (result == 0) {//已关注过
                            ToastUtil.showToast(activity, message);
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        } else if (result == 1) {
                            activity.updateAttention(true);
                            ToastUtil.showToast(activity, "关注成功");
//                            Toast.makeText(activity, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == CANCEL_ATTENTION) {
                    boolean isCancelSuccess = false;
                    if (json != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (1 == Integer.parseInt(String.valueOf(jsonObject.get("result")))) {
                                isCancelSuccess = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isCancelSuccess) {
                        //fragment.updateAttentionDisplay(msg.what);
                        activity.updateAttention(false);
                        ToastUtil.showToast(activity, "已取消关注");
//                        Toast.makeText(activity, "已取消关注", Toast.LENGTH_SHORT).show();
                    } else {
                        ToastUtil.showToast(activity,"取消关注失败");
//                        Toast.makeText(activity, "取消关注失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscanresult);
        application = (BaseApplication) getApplication();
        initImageLoader();
        initViews();
        initIntent();
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            member_id = intent.getStringExtra("id");
            if (member_id != null && !"".equals(member_id)) {
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.AttentionMessage, new String[]{"member_id"}, new String[]{member_id}, handler, GET_USER_INFO);
            }
        }
    }

    private void initViews() {
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyScanResultActivity.this.finish();
            }
        });

        tvName = (TextView) findViewById(R.id.tv_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        ivGender = (ImageView) findViewById(R.id.iv_gender);
        ibAttention = (ImageButton) findViewById(R.id.ib_attention);
        ivHead = (ImageView) findViewById(R.id.iv_head);

    }

    private void initData() {
        if (mDataEntity != null) {
            tvName.setText(mDataEntity.getMember_name());
            StringBuilder address = new StringBuilder();
            if (mDataEntity.getProvince() != null)
                address.append(mDataEntity.getProvince());
            if (mDataEntity.getCity() != null)
                address.append(mDataEntity.getCity());
            tvAddress.setText(address.toString());
            setInfoText();
            if (mDataEntity.getSex().equals("1")) {
                ivGender.setImageResource(R.mipmap.wd_icon_man);
            } else {
                ivGender.setImageResource(R.mipmap.wd_icon_woman);
            }
            if (mDataEntity.getIs_attention().equals("1")) {
                ibAttention.setImageResource(R.mipmap.scan_cancel_attention);
            } else {
                ibAttention.setImageResource(R.mipmap.scan_attention);
            }
            ibAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (member_id != null && !"".equals(member_id)) {
                        if (mDataEntity.getIs_attention().equals("1")) {
                            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.ATTENTION, new String[]{"member_id"}, new String[]{member_id}, handler, ATTENTION);
                        } else {
                            VolleyUtils.NetUtils(application.getRequestQueue(),Constant.baseUrl + Constant.deleteAttentionPerson,new String[]{"member_id"},new String[]{member_id},handler,CANCEL_ATTENTION);
                        }
                    }
                }
            });
            imageLoader.displayImage(mDataEntity.getFace(),ivHead,options);
        }
    }

    private void setInfoText(){
        StringBuilder info = new StringBuilder("新鲜事：");
        info.append(mDataEntity.getNovelty_num());
        info.append("       ");
        info.append("粉丝：");
        info.append(mDataEntity.getAttention_num());
        tvInfo.setText(info.toString());
    }

    private void updateAttention(boolean isAttention){
        int fansNum = Integer.parseInt(mDataEntity.getAttention_num());
        if(isAttention){
            mDataEntity.setIs_attention("1");
            ibAttention.setImageResource(R.mipmap.scan_cancel_attention);
            fansNum += 1;
        }else{
            mDataEntity.setIs_attention("0");
            ibAttention.setImageResource(R.mipmap.scan_attention);
            if(fansNum > 0)
                fansNum--;
        }
        mDataEntity.setAttention_num(String.format("%d",fansNum));
        setInfoText();
    }
}
