package com.administrator.elwj;


import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 忘记密码界面
 */
public class ForgetActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    //输入手机号
    private AutoCompleteTextView phone_register;
    //获取验证码按钮
    private TextView tv_getauthcode;
    //输入验证码edittext
    private EditText et_authcode;
    private BaseApplication appContext;
    //输入新密码第一遍edittext
    private EditText et_pwd_1;
    //输入新密码第二遍edittext
    private EditText et_pwd_2;

    //等待对话框
    private ProgressDialog progressDialog;

    private Timer timer;

    //验证码申请间隔120秒
    private int mCount = 120;

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mCount--;
            if (mCount == 0) {
                Message message = handler.obtainMessage(SET_TV_AUTHCODE);
                message.sendToTarget();
                timer.cancel();
                mCount = 120;
            } else {
                Message message = handler.obtainMessage(SET_TV_TEXT);
                message.sendToTarget();
            }
        }
    };

    private static final int SET_TV_TEXT = 0;
    private static final int SET_TV_AUTHCODE = 2;
    private static final int RESET_PWD = 3;

    public static class MyHandler extends Handler {
        private WeakReference<ForgetActivity> mActivity;

        public MyHandler(ForgetActivity activity) {
            mActivity = new WeakReference<ForgetActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ForgetActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.GETAUTHCODE) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.getInt("result") != 1) {
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message + "");
                        } else {
                            ToastUtil.showToast(activity, "验证码已经发出");
                            activity.enableAuthCode(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (which == SET_TV_TEXT) {
                    activity.tv_getauthcode.setText(activity.mCount + "s");
                }
                else if (which == SET_TV_AUTHCODE) {
                    activity.enableAuthCode(true);
                }else if (msg.what == RESET_PWD) {
                    if(activity.progressDialog != null){
                        activity.progressDialog.dismiss();
                        activity.progressDialog = null;
                    }
                    LogUtils.d("xu_resetpwd", json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            ToastUtil.showToast(activity,"修改密码成功");
                            activity.finish();
                        }else{
                            ToastUtil.showToast(activity,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    //获取验证码按钮的状态设置，true表示可以点击；false表示已经点击了，将处于倒计时状态，倒计时完后才可以点击
    private void enableAuthCode(boolean enable) {
        if (enable) {
            tv_getauthcode.setText("获取验证码");
            tv_getauthcode.setClickable(true);
        } else {
            tv_getauthcode.setText("120s");
            tv_getauthcode.setClickable(false);
            timer = new Timer();
            timer.schedule(mTimerTask, 1000, 1000);
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        appContext = (BaseApplication) getApplication();
        setContentView(R.layout.activity_forget_pwd);
        initView();
    }

    private void initView() {
        et_pwd_1 = (EditText) findViewById(R.id.et_pwd_1);
        et_pwd_2 = (EditText) findViewById(R.id.et_pwd_2);
        //标题
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        textViewTitle.setText("找回密码");
        //注册按钮
        TextView textViewRegedit = (TextView) findViewById(R.id.regedit);
        textViewRegedit.setOnClickListener(this);
        phone_register = (AutoCompleteTextView) findViewById(R.id.phone_register);//填写手机号码
        populateAutoComplete();
        tv_getauthcode = (TextView) findViewById(R.id.getauthcode);//获取验证码
        et_authcode = (EditText) findViewById(R.id.et_authcode);//填写验证码
        Button bt_nextstep = (Button) findViewById(R.id.nextstep_button);
        //返回按钮
        ImageButton bt_back = (ImageButton) findViewById(R.id.ib_back);
        tv_getauthcode.setOnClickListener(this);//设置点击监听
        bt_nextstep.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(phone_register, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getauthcode://获取手机验证码
                String phone_num = phone_register.getText().toString().trim();
                if (TextUtils.isEmpty(phone_num)) {
                    phone_register.setError(getString(R.string.error_field_required));
                    phone_register.requestFocus();
                } else if (!isEmailValid(phone_num)) {
                    phone_register.setError(getString(R.string.error_invalid_email));
                    phone_register.requestFocus();
                } else {

                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.forgetpwd, new String[]{"mobile"}, new String[]{phone_num}, handler, Constant.GETAUTHCODE);
                }
                break;
            case R.id.nextstep_button://下一步按钮
                judgement();
                break;
            case R.id.ib_back://返回按钮
                finish();
                break;
            case R.id.regedit://注册
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void judgement() {
        String phone_num = phone_register.getText().toString().trim();
        String authcode = et_authcode.getText().toString().trim();
//        String password = et_password.getText().toString().trim();
//        String community = tv_community_choose.getText().toString().trim();
//        String nickname = et_NickName.getText().toString().trim();
//        if(mCurrentProviceName == null || mCurrentCommunityName.equals("") ||
//                mCurrentDistrictName == null || mCurrentDistrictName.equals("") ||
//                mCurrentCityName == null || mCurrentCityName.equals("")){
//            Toast.makeText(this,"请选择所在地",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(community.equals("")){
//            Toast.makeText(this,"请选择社区",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(nickname.equals("")){
//            Toast.makeText(this,"请填写昵称",Toast.LENGTH_SHORT).show();
//        }

        boolean cancel = false;
        View focusView = null;
//        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(password) || isNotPasswordValid(password)) {
//            et_password.setError(getString(R.string.error_invalid_password));
//            focusView = et_password;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(authcode)) {
            et_authcode.setError(getString(R.string.error_authcode_required));
            focusView = et_authcode;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone_num)) {
            phone_register.setError(getString(R.string.error_field_required));
            focusView = phone_register;
            cancel = true;
        } else if (!isEmailValid(phone_num)) {
            phone_register.setError(getString(R.string.error_invalid_email));
            focusView = phone_register;
            cancel = true;
        }

        String pwd1 = et_pwd_1.getText().toString().trim();
        String pwd2 = et_pwd_2.getText().toString().trim();
        if(TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)){
            ToastUtil.showToast(this, "请填写完整密码");
//            Toast.makeText(this,"请填写完整密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pwd1.equals(pwd2)){
            ToastUtil.showToast(this, "两次输入的密码不一致");
//            Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isNotPasswordValid(pwd1) || isNotPasswordValid(pwd2)){
            ToastUtil.showToast(this, "输入的密码长度在6至16位");
//            Toast.makeText(this,"输入的密码长度小于8",Toast.LENGTH_SHORT).show();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在修改...");
            progressDialog.show();
            VolleyUtils.NetUtils(((BaseApplication)getApplication()).getRequestQueue(), Constant.baseUrl + Constant.resetpwd, new String[]{"validcode","password","mobile"},new String[]{authcode,pwd1,phone_num},handler,RESET_PWD);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (email.length() == 11);
    }

    private boolean isNotPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() < 6 || password.length() > 16;
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ForgetActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        phone_register.setAdapter(adapter);
    }

//    @Override
//    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//        // TODO Auto-generated method stub
//        if (wheel == mViewDistrict) {
//            updateCities();
//        } else if (wheel == mViewStreet) {
//            updateAreas();
//        } else if (wheel == mViewCommunity) {
//            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
//        }
//    }

//    /**
//     * 根据当前的市，更新区WheelView的信息
//     */
//    private void updateAreas() {
//        int pCurrent = mViewStreet.getCurrentItem();
//        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
//        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//
//        if (areas == null) {
//            areas = new String[]{""};
//        }
//        mViewCommunity.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//        mViewCommunity.setCurrentItem(0);
//        mCurrentDistrictName = areas[0];
//    }
//
//    /**
//     * 根据当前的省，更新市WheelView的信息
//     */
//    private void updateCities() {
//        int pCurrent = mViewDistrict.getCurrentItem();
//        mCurrentProviceName = mProvinceDatas[pCurrent];
//        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//        if (cities == null) {
//            cities = new String[]{""};
//        }
//        mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
//        mViewStreet.setCurrentItem(0);
//        updateAreas();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}


