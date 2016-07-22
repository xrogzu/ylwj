package com.administrator.elwj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    public static final String LOGIN_URL = Constant.baseUrl + "/api/mobile/member!login.do";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private BaseApplication appContext;
    private boolean isSuccess = false;
    private String email;
    private String pwd;

    public static class MyHandler extends Handler {
        private WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity loginActivity) {
            mActivity = new WeakReference<LoginActivity>(loginActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
                activity.showProgress(false);
                String json = (String) msg.obj;
                LogUtils.e("Main", json);
                JSONObject jsonObject = null;
                JSONObject data = null;
                int result = 0;
                try {
                    jsonObject = new JSONObject(json);
                    result = jsonObject.optInt("result");
                    if (result == 0) {
                        String message = jsonObject.optString("message");
                        ToastUtil.showToast(activity, "登录失败，" + message);
//                        Toast.makeText(activity, "登录失败，" + message, Toast.LENGTH_SHORT).show();
                        activity.isSuccess = false;
                        return;
                    }
                    data = jsonObject.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, new InitMemberIDHandler(), InitMemberIDHandler.GET_MEMBER_ID);
                ToastUtil.showToast(activity, "登录成功");
//                Toast.makeText(activity, "登录成功！", Toast.LENGTH_SHORT).show();
                BaseApplication.isLogin = true;
                SharedPreferences.Editor localEditor = activity.getSharedPreferences("user", 0).edit();
                if (data != null) {
                    localEditor.putString("username", data.optString("username"));
                }
                localEditor.putString("email", activity.email);
                localEditor.putString("password", activity.pwd);
                activity.isSuccess = true;
                if (data != null) {
                    try {
                        localEditor.putString("face", data.optString("face"));
                        localEditor.putString("level", data.optString("level"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                localEditor.commit();
                if (activity.isSuccess) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("logined", true);
                    activity.setResult(BACK_LOGIN, returnIntent);
                    activity.finish();
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    static final int BACK_LOGIN = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appContext = (BaseApplication) getApplication();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        TextView quicklogintextview = (TextView) findViewById(R.id.quicklogintextview);
        quicklogintextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ShortCutLoginActivity.class);
//                startActivity(intent);

            }
        });
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        textViewTitle.setText(R.string.title_activity_login);
        TextView textViewRegedit = (TextView) findViewById(R.id.regedit);
        textViewRegedit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        TextView tvForgetPwd = (TextView) findViewById(R.id.forgetpassword);
        tvForgetPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

//        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (ContextCompat.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        pwd = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(pwd) || isNotPasswordValid(pwd)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
//            Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    showProgress(false);
//                    String json = (String) msg.obj;
//                    LogUtils.e("Main", json);
//                    JSONObject jsonObject = null;
//                    JSONObject data = null;
//                    int result = 0;
//                    try {
//                        jsonObject = new JSONObject(json);
//                        result = jsonObject.optInt("result");
//                        if (result == 0) {
//                            String message = jsonObject.optString("message");
//                            Toast.makeText(LoginActivity.this, "登录失败，" + message, Toast.LENGTH_SHORT).show();
//                            isSuccess = false;
//                            return;
//                        }
//                        data = jsonObject.getJSONObject("data");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, new InitMemberIDHandler(), InitMemberIDHandler.GET_MEMBER_ID);
//                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
//                    BaseApplication.isLogin = true;
//                    SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
//                    localEditor.putString("username", data.optString("username"));
//                    localEditor.putString("email", email);
//                    localEditor.putString("password", password);
//                    isSuccess = true;
//                    try {
//                        localEditor.putString("face", data.optString("face"));
//                        localEditor.putString("level", data.optString("level"));
//                    } catch (Exception ex) {
//                    }
//                    localEditor.commit();
//                    if (isSuccess) {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("logined", true);
//                        setResult(BACK_LOGIN, returnIntent);
//                        finish();
//                    }
//                }
//
//            };


            if (BaiduPushReceiver.ChannelID == null) {
                SharedPreferences localShare = getSharedPreferences("user", 0);
                BaiduPushReceiver.ChannelID = localShare.getString("channelid", null);
                if (BaiduPushReceiver.ChannelID == null) {
                    BaiduPushReceiver.ChannelID = "";
                }
            }
            LogUtils.d("xu_login", BaiduPushReceiver.ChannelID);
            VolleyUtils.NetUtils(appContext.getRequestQueue(), LOGIN_URL, new String[]{"username", "password", "channelid", "devicetype"}, new String[]{email, pwd, BaiduPushReceiver.ChannelID, "3"}, handler, Constant.TRYTO_LOGIN);
            showProgress(true);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                BaseApplication.isLogin = true;
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

