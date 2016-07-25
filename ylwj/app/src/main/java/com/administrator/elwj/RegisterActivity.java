package com.administrator.elwj;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView phone_register;
    private TextView tv_getauthcode;
    private EditText et_authcode;
    //再次发送验证码等待的秒数
    private static final int WAID_SECOND = 120;
    private BaseApplication appContext;


    private Timer timer;


    private int mCount = WAID_SECOND;

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mCount--;
            if (mCount == 0) {
                Message message = handler.obtainMessage(SET_TV_AUTHCODE);
                message.sendToTarget();
                timer.cancel();
                mCount = WAID_SECOND;
            } else {
                Message message = handler.obtainMessage(SET_TV_TEXT);
                message.sendToTarget();
            }
        }
    }

    ;


    private static final int SET_TV_TEXT = 0;
    private static final int AUTHCODE_CHECK = 1;
    private static final int SET_TV_AUTHCODE = 2;

    public static class MyHandler extends Handler {
        private WeakReference<RegisterActivity> mActivity;

        public MyHandler(RegisterActivity activity) {
            mActivity = new WeakReference<RegisterActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.RegisterMember) {
                    LogUtils.e("Main注册结果", json);
                    try {
                        JSONObject object = new JSONObject(json);
                        String message = object.optString("message");
                        ToastUtil.showToast(activity, message);
                        activity.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (which == Constant.GETAUTHCODE) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.getInt("result") != 1) {
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message);
                            activity.tv_getauthcode.setClickable(true);
                        } else {
                            activity.enableAuthCode(false);
                            ToastUtil.showToast(activity, "验证码已经发出");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == SET_TV_TEXT) {
                    activity.tv_getauthcode.setText(activity.mCount + "s");
                } else if (which == AUTHCODE_CHECK) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            Intent intent = new Intent(activity, RegisterInfoActivity.class);
                            intent.putExtra("phone", activity.phone_register.getText().toString().trim());
                            intent.putExtra("authcode", activity.et_authcode.getText().toString().trim());
                            activity.startActivity(intent);
                        } else {
                            ToastUtil.showToast(activity, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == SET_TV_AUTHCODE) {
                    activity.enableAuthCode(true);
                }
            }
        }
    }


    private void enableAuthCode(boolean enable) {
        if (enable) {
            tv_getauthcode.setText("获取验证码");
            tv_getauthcode.setClickable(true);
            if (timer != null)
                timer.cancel();
        } else {
            tv_getauthcode.setText(WAID_SECOND + "s");
            tv_getauthcode.setClickable(false);
            timer = new Timer();
            timer.schedule(new MyTimerTask(), 1000, 1000);
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        appContext = (BaseApplication) getApplication();
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        TextView textViewTitle = (TextView) findViewById(R.id.title);
        textViewTitle.setText(R.string.regedit);
        TextView textViewRegedit = (TextView) findViewById(R.id.regedit);
        textViewRegedit.setText(R.string.title_activity_justlogin);
        textViewRegedit.setOnClickListener(this);
        phone_register = (AutoCompleteTextView) findViewById(R.id.phone_register);//填写手机号码
        populateAutoComplete();
        tv_getauthcode = (TextView) findViewById(R.id.getauthcode);//获取验证码
        et_authcode = (EditText) findViewById(R.id.et_authcode);//填写验证码
        Button bt_nextstep = (Button) findViewById(R.id.nextstep_button);
        ImageButton bt_back = (ImageButton) findViewById(R.id.ib_back);
        tv_getauthcode.setOnClickListener(this);//设置点击监听
        bt_nextstep.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }

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
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(phone_register, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

//    private void initProvinceDialog() {//省市区选择
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_province, null);
//        TextView tv_dialog_name = (TextView) view.findViewById(R.id.tv_dialogprovince_title);
//        tv_dialog_name.setText("请选择所在社区");
//        mViewDistrict = (WheelView) view.findViewById(R.id.id_province);
//        mViewStreet = (WheelView) view.findViewById(R.id.id_city);
//        mViewCommunity = (WheelView) view.findViewById(R.id.id_district);
//        mBtnConfirmPro = (Button) view.findViewById(R.id.btn_confirm);
//        // 添加change事件
//        mViewDistrict.addChangingListener(this);
//        // 添加change事件
//        mViewStreet.addChangingListener(this);
//        // 添加change事件
//        mViewCommunity.addChangingListener(this);
//        // 添加onclick事件
//        mBtnConfirmPro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_district.cancel();
//                tv_address_choose.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
//                //VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, null,null , handler, GET_COMMUNITY);
//                VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, new String[]{"province", "city", "county"}, new String[]{mCurrentProviceName, mCurrentCityName, mCurrentDistrictName}, handler, GET_COMMUNITY);
//            }
//        });
//        initProvinceDatas();
//        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
//        // 设置可见条目数量
//        mViewDistrict.setVisibleItems(7);
//        mViewStreet.setVisibleItems(7);
//        mViewCommunity.setVisibleItems(7);
//        updateCities();
//        updateAreas();
//        dialog_district = new Dialog(this);
//        dialog_district.setContentView(view);
//        dialog_district.show();
//    }
//
//    /**
//     * 解析省市区的XML数据
//     */
//
//    protected void initProvinceDatas() {
//        List<ProvinceModel> provinceList = null;
//        AssetManager asset = this.getAssets();
//        try {
//            InputStream input = asset.open("province_data.xml");
//            // 创建一个解析xml的工厂对象
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            // 解析xml
//            SAXParser parser = spf.newSAXParser();
//            XmlParserHandler handler = new XmlParserHandler();
//            parser.parse(input, handler);
//            input.close();
//            // 获取解析出来的数据
//            provinceList = handler.getDataList();
//            //*/ 初始化默认选中的省、市、区
//            if (provinceList != null && !provinceList.isEmpty()) {
//                mCurrentProviceName = provinceList.get(0).getName();
//                List<CityModel> cityList = provinceList.get(0).getCityList();
//                if (cityList != null && !cityList.isEmpty()) {
//                    mCurrentCityName = cityList.get(0).getName();
//                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
//                    mCurrentDistrictName = districtList.get(0).getName();
//                    mCurrentZipCode = districtList.get(0).getZipcode();
//                }
//            }
//            //*/
//            mProvinceDatas = new String[provinceList.size()];
//            for (int i = 0; i < provinceList.size(); i++) {
//                // 遍历所有省的数据
//                mProvinceDatas[i] = provinceList.get(i).getName();
//                List<CityModel> cityList = provinceList.get(i).getCityList();
//                String[] cityNames = new String[cityList.size()];
//                for (int j = 0; j < cityList.size(); j++) {
//                    // 遍历省下面的所有市的数据
//                    cityNames[j] = cityList.get(j).getName();
//                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
//                    String[] distrinctNameArray = new String[districtList.size()];
//                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
//                    for (int k = 0; k < districtList.size(); k++) {
//                        // 遍历市下面所有区/县的数据
//                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
//                        // 区/县对于的邮编，保存到mZipcodeDatasMap
//                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
//                        distrinctArray[k] = districtModel;
//                        distrinctNameArray[k] = districtModel.getName();
//                    }
//                    // 市-区/县的数据，保存到mDistrictDatasMap
//                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
//                }
//                // 省-市的数据，保存到mCitisDatasMap
//                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }


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
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getAuthcode, new String[]{"mobile"}, new String[]{phone_num}, handler, Constant.GETAUTHCODE);
                    tv_getauthcode.setClickable(false);
                }
                break;
            case R.id.nextstep_button://下一步按钮
                judgement();
                break;
            case R.id.ib_back://返回按钮
                finish();
                break;
            case R.id.regedit://登录
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String validCode = et_authcode.getText().toString().trim();
//            Intent intent=new Intent(this,Register_community.class);
//            startActivity(intent);
            // 跳到下一步
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.validCode, new String[]{"validcode"}, new String[]{validCode}, handler, AUTHCODE_CHECK);
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
                new ArrayAdapter<>(RegisterActivity.this,
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


