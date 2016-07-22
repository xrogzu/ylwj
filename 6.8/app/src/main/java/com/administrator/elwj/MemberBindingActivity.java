package com.administrator.elwj;


import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

//会员绑定页面
public class MemberBindingActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView phone_register;
    private TextView tv_getauthcode;
    private EditText et_authcode;
    private EditText et_password;
    private BaseApplication appContext;

    public static class MyHandler extends Handler {
        private WeakReference<MemberBindingActivity> mActivity;

        public MyHandler(MemberBindingActivity activity) {
            mActivity = new WeakReference<MemberBindingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MemberBindingActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.MemberBinding) {
                    LogUtils.e("Main注册结果", json);
                    try {
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        String message = object.optString("message");
                        if (result == 1) {
                            ToastUtil.showToast(activity, message);
                            activity.finish();
                        } else {
                            ToastUtil.showToast(activity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (which == Constant.GETAUTHCODE) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.getInt("result") != 1) {
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message);
                        } else {
                            ToastUtil.showToast(activity, "验证码已经发出");
                            activity.enableAuthCode(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (which == SET_TV_TEXT) {
                    activity.tv_getauthcode.setText("(" + activity.mCount + "秒)");
                } else if (which == SET_TV_AUTHCODE) {
                    activity.enableAuthCode(true);
                }
            }
        }
    }

    private Timer timer;
    private int mCount = 120;
    private static final int SET_TV_AUTHCODE = 2;
    private static final int SET_TV_TEXT = 0;
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

    private void enableAuthCode(boolean enable) {
        if (enable) {
            tv_getauthcode.setText("获取验证码");
            tv_getauthcode.setClickable(true);
            tv_getauthcode.setTextColor(ContextCompat.getColor(MemberBindingActivity.this, R.color.orangered));
        } else {
            tv_getauthcode.setClickable(false);
            tv_getauthcode.setTextColor(ContextCompat.getColor(MemberBindingActivity.this, R.color.gray));
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
        setContentView(R.layout.member_binding);
        initView();

    }

    private void initView() {
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        textViewTitle.setText("我的");
        TextView textViewRegedit = (TextView) findViewById(R.id.regedit);
        textViewRegedit.setVisibility(View.INVISIBLE);


        phone_register = (AutoCompleteTextView) findViewById(R.id.phone_member);//填写手机号码
        populateAutoComplete();
        tv_getauthcode = (TextView) findViewById(R.id.getmember);//获取验证码
        et_authcode = (EditText) findViewById(R.id.et_authcode_member);//填写验证码
        et_password = (EditText) findViewById(R.id.et_password_member);//开户名
        Button bt_nextstep = (Button) findViewById(R.id.nextstep_button_member);
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
            case R.id.getmember://获取手机验证码
                String phone_num = phone_register.getText().toString().trim();
                if (TextUtils.isEmpty(phone_num)) {
                    phone_register.setError(getString(R.string.error_field_required));
                    phone_register.requestFocus();
                } else if (!isEmailValid(phone_num)) {
                    phone_register.setError(getString(R.string.error_invalid_email));
                    phone_register.requestFocus();
                } else {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getBindAuthCode, new String[]{"mobile"}, new String[]{phone_num}, handler, Constant.GETAUTHCODE);
                }
                break;
            case R.id.nextstep_button_member://下一步按钮
                judgement();
                break;
            case R.id.ib_back://返回按钮
                finish();
                break;

        }
    }

    private void judgement() {
        String phone_num = phone_register.getText().toString().trim();
        String authcode = et_authcode.getText().toString().trim();
        String name = et_password.getText().toString().trim();
        if (authcode.equals("")) {
            ToastUtil.showToast(this, "请输入开户名");
//            Toast.makeText(this,"请输入开户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 10) {
            ToastUtil.showToast(this, "输入开户名过长");
//            Toast.makeText(this,"输入开户名过长",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(name) || isNotPasswordValid(name)) {
//            et_password.setError(getString(R.string.error_invalid_password));
//            focusView = et_password;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(authcode)) {
//            et_authcode.setError(getString(R.string.error_authcode_required));
//            focusView = et_authcode;
//            cancel = true;
//        }

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
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
//            Intent intent=new Intent(this,Register_community.class);
//            startActivity(intent);
            // 跳到下一步
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.menberBinding, new String[]{"mobile", "name", "code"}, new String[]{phone_num, name, authcode}, handler, Constant.MemberBinding);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (email.length() == 11);
    }

    private boolean isNotPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() < 8;
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
                new ArrayAdapter<>(MemberBindingActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        phone_register.setAdapter(adapter);
    }
}


