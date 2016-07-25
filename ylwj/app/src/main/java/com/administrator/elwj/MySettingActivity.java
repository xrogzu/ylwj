package com.administrator.elwj;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.baidu.android.pushservice.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 设置界面，以前的界面，目前没有用到
 */
public class MySettingActivity extends AppCompatActivity implements View.OnClickListener {

    private BaseApplication appContext;
    private boolean isLog=true;

    public static class MyHandler extends Handler{
        private WeakReference<MySettingActivity> mActivity;
        public MyHandler(MySettingActivity activity){
            mActivity = new WeakReference<MySettingActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            MySettingActivity activity = mActivity.get();
            if(activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.LOG_OUT) {
                    try {
                        JSONObject object = new JSONObject(json);
                        String message = object.optString("message");
                        ToastUtil.showToast(activity,message);
//                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        activity.isLog = false;
                        SharedPreferences localShare = activity.getSharedPreferences("user", 0);
                        SharedPreferences.Editor editor = localShare.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.commit();
                        BaseApplication.isLogin = false;
                        Intent intent = new Intent();
                        intent.putExtra("logout",true);
                        activity.setResult(RESULT_OK,intent);
                        PushManager.stopWork(activity.getApplicationContext());
                        activity.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private Handler handler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        appContext = (BaseApplication) getApplication();
        initViews();
    }

    private void initViews() {
        ImageButton ib_delete= (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setVisibility(View.INVISIBLE);
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        TextView tv_viewpagerdetails_num = (TextView) findViewById(R.id.tv_viewpagerdetails_num);
        tv_viewpagerdetails_num.setText("设置");
        RelativeLayout relativity_setting_safe = (RelativeLayout) findViewById(R.id.relativity_setting_safe);
        RelativeLayout relativity_setting_news = (RelativeLayout) findViewById(R.id.relativity_setting_news);
        RelativeLayout relativity_setting_privacy = (RelativeLayout) findViewById(R.id.relativity_setting_privacy);
        RelativeLayout relativity_setting_normal = (RelativeLayout) findViewById(R.id.relativity_setting_normal);
        RelativeLayout relativity_setting_facebook = (RelativeLayout) findViewById(R.id.relativity_setting_facebook);
        RelativeLayout relativity_setting_twitter = (RelativeLayout) findViewById(R.id.relativity_setting_twitter);
        RelativeLayout relativity_setting_help = (RelativeLayout) findViewById(R.id.relativity_setting_help);
        RelativeLayout relativity_setting_about = (RelativeLayout) findViewById(R.id.relativity_setting_about);
        TextView exit_log = (TextView) findViewById(R.id.tv_unlogin);
        ib_back.setOnClickListener(this);
        relativity_setting_safe.setOnClickListener(this);
        relativity_setting_news.setOnClickListener(this);
        relativity_setting_privacy.setOnClickListener(this);
        relativity_setting_normal.setOnClickListener(this);
        relativity_setting_facebook.setOnClickListener(this);
        relativity_setting_twitter.setOnClickListener(this);
        relativity_setting_help.setOnClickListener(this);
        relativity_setting_about.setOnClickListener(this);
        exit_log.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent();
        if(isLog){
            setResult(Constant.MY_backlog,intent);
        }else {
            setResult(Constant.MY_backunlog,intent);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://返回
                Intent intent=new Intent();
                if(isLog){
                    setResult(Constant.MY_backlog,intent);
                }else {
                    setResult(Constant.MY_backunlog,intent);
                }
                finish();
                break;
            case R.id.relativity_setting_safe://账号安全
                break;
            case R.id.relativity_setting_news://新消息通知
                break;
            case R.id.relativity_setting_privacy://隐私
                break;
            case R.id.relativity_setting_normal://通用
                break;
            case R.id.relativity_setting_facebook://facebook关注
                break;
            case R.id.relativity_setting_twitter://twitter关注
                break;
            case R.id.relativity_setting_help://帮助与反馈
                break;
            case R.id.relativity_setting_about://关于
                break;
            case R.id.tv_unlogin://退出登录

                new AlertDialog.Builder(MySettingActivity.this).setMessage("是否退出登录？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(BaiduPushReceiver.ChannelID == null){
                            if(BaiduPushReceiver.ChannelID == null){
                                SharedPreferences localShare = getSharedPreferences("user", 0);
                                BaiduPushReceiver.ChannelID = localShare.getString("channelid",null);
                                if(BaiduPushReceiver.ChannelID == null){
                                    BaiduPushReceiver.ChannelID = "";
                                }
                            }
                        }
                        LogUtils.d("xu_login", BaiduPushReceiver.ChannelID);
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.logOut, new String[]{"channelid"}, new String[]{BaiduPushReceiver.ChannelID},handler, Constant.LOG_OUT);
                    }
                }).setNegativeButton("否",null).show();


                break;
        }
    }
}
