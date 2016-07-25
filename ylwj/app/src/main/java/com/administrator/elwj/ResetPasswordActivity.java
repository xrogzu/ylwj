package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 前人做的activity，目前没有用到，可以删掉
 * Created by Administrator on 2016/3/28.
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_pwd_1;
    private EditText et_pwd_2;
    private String phone;
    private String validCode;

    private static final int RESET_PWD = 1;

    private static class MyHandler extends Handler{
        private WeakReference<ResetPasswordActivity> mActivity;
        public MyHandler(ResetPasswordActivity activity){
            mActivity = new WeakReference<ResetPasswordActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ResetPasswordActivity activity = mActivity.get();
            if(activity != null) {
                String json = (String) msg.obj;
                if (msg.what == RESET_PWD) {
                    Log.d("xu_resetpwd", json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            ToastUtil.showToast(activity, "修改密码成功");
//                            Toast.makeText(activity,"修改密码成功",Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }else{
                            ToastUtil.showToast(activity,jsonObject.getString("message"));
//                            Toast.makeText(activity,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initIntent();
        intViews();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            phone = intent.getStringExtra("phone");
            validCode = intent.getStringExtra("authcode");
        }
    }

    private void intViews() {

        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.this.finish();
            }
        });
        et_pwd_1 = (EditText) findViewById(R.id.et_pwd_1);
        et_pwd_2 = (EditText) findViewById(R.id.et_pwd_2);
        Button btn_OK = (Button) findViewById(R.id.btn_ok);
        btn_OK.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok){
            String pwd1 = et_pwd_1.getText().toString().trim();
            String pwd2 = et_pwd_2.getText().toString().trim();
            if(TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)){
                ToastUtil.showToast(this, "请填写完整信息");
//                Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!pwd1.equals(pwd2)){
                ToastUtil.showToast(this, "两次输入的密码不一致");
//                Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPasswordValid(pwd1) || !isPasswordValid(pwd2)){
                return;
            }

            VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.resetpwd, new String[]{"validcode", "password", "mobile"}, new String[]{validCode, pwd1, phone}, handler, RESET_PWD);
        }
    }


    private boolean isPasswordValid(String password) {
        if(password.length() > 20){
            et_pwd_1.setError("密码不能超过20个字符");
            return false;
        }else if(password.length() < 8){
            et_pwd_1.setError("密码不能少于8个字符");
            return false;
        }else{
            boolean result = true;
            Pattern pattern = Pattern.compile("[a-zA-Z]+");
            Matcher matcher = pattern.matcher(password);
            if(!matcher.find()){
                result = false;
            }
            pattern = Pattern.compile("[0-9]+");
            matcher = pattern.matcher(password);
            if(!matcher.find()){
                result = false;
            }
            if(result)
                return true;
            else{
                et_pwd_1.setError("密码至少包括一个数字和一个字母");
                return false;
            }
        }
    }
}
