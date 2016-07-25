package com.administrator.elwj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 注册完成界面
 * Created by Administrator on 2016/3/27.
 */
public class RegisterCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private String phone;
    private String pwd;
    private static final int LOGIN = 0;
    private static final int BIND = 1;
    private LinearLayout llContent;
    private ProgressBar progressBar;
    private boolean isBindCard = false;
    private BaseApplication application;

    public static class MyHandler extends Handler {
        private WeakReference<RegisterCompleteActivity> mActivity;

        public MyHandler(RegisterCompleteActivity activity) {
            mActivity = new WeakReference<RegisterCompleteActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterCompleteActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                if (msg.what == Constant.TRYTO_LOGIN) {
                    JSONObject jsonObject = null;
                    JSONObject data = null;
                    int result = 0;
                    try {
                        jsonObject = new JSONObject(json);
                        result = jsonObject.optInt("result");
                        if (result == 0) {
                            String message = jsonObject.optString("message");
                            ToastUtil.showToast(activity,"登录失败，" + message);
//                            Toast.makeText(activity, "登录失败，" + message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        data = jsonObject.getJSONObject("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BaseApplication.isLogin = true;
                    if (data != null) {
                        SharedPreferences.Editor localEditor = activity.getSharedPreferences("user", 0).edit();
                        localEditor.putString("username", data.optString("username"));
                        localEditor.putString("email", activity.phone);
                        localEditor.putString("password", activity.pwd);
                        localEditor.putString("face", data.optString("face"));
                        localEditor.putString("level", data.optString("level"));
                        localEditor.apply();
                    }
                    Intent intent = new Intent(activity,HomeActivity.class);
                    intent.putExtra("isFromRegister",true);
                    activity.startActivity(intent);
                    if(activity.isBindCard){
                        intent = new Intent(activity,MemberBindingActivity.class);
                        activity.startActivity(intent);
                    }
                    activity.finish();
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete_new);
        application = (BaseApplication) getApplication();
        initIntent();
        initViews();
    }

    private void initIntent() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        pwd = intent.getStringExtra("pwd");
    }

    private void initViews() {
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterCompleteActivity.this.finish();
            }
        });
        TextView tvLogin = (TextView) findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(this);
        ImageButton ibBind = (ImageButton) findViewById(R.id.ib_bind);
        ibBind.setOnClickListener(this);
        Button btEnter = (Button) findViewById(R.id.enter);
        btEnter.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        llContent = (LinearLayout) findViewById(R.id.ll_regcomplete_content);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ib_bind) {
            isBindCard = true;
            login();
        } else if (v.getId() == R.id.enter) {
            isBindCard = false;
            login();
        }
    }

    private void login() {
        if (BaiduPushReceiver.ChannelID == null) {
            SharedPreferences localShare = getSharedPreferences("user", 0);
            BaiduPushReceiver.ChannelID = localShare.getString("channelid", null);
            if (BaiduPushReceiver.ChannelID == null) {
                BaiduPushReceiver.ChannelID = "";
            }
        }
        LogUtils.d("xu_login", BaiduPushReceiver.ChannelID);
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.login_url, new String[]{"username", "password", "channelid", "devicetype"}, new String[]{phone, pwd, BaiduPushReceiver.ChannelID, "3"}, handler, Constant.TRYTO_LOGIN);
        showProgress(true);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if(show){
            llContent.setVisibility(View.GONE);
        }else{
            llContent.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
