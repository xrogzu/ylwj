package com.administrator.elwj;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.CommunityList;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 注册选择信息界面，注册的第二步
 * Created by Administrator on 2016/3/27.
 */
public class RegisterInfoActivity extends AppCompatActivity implements View.OnClickListener, OnWheelChangedListener {

    private EditText et_NickName;
    private EditText et_password;
    private Button bt_nextstep;
    private WheelView mViewDistrict;
    private WheelView mViewStreet;
    private WheelView mViewCommunity;
    private Dialog dialog_district;
    private TextView tv_address_choose;
    private TextView tv_community_choose;
    private List<CommunityList> mCommunitys;
    private String phone_num;
    private String authCode;
    private BaseApplication appContext;
    private CheckBox cbAgree;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**
     * 当前省的名<String, String>();
     * <p/>
     * 称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    private String mCurrentCommunityName;
    private String mCurrentCommunityID;

    private String[] mCommunityStrings;

    private static final int GET_COMMUNITY = 1;

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    public static class MyHandler extends Handler {
        private WeakReference<RegisterInfoActivity> mActivity;

        public MyHandler(RegisterInfoActivity activity) {
            mActivity = new WeakReference<RegisterInfoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterInfoActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == GET_COMMUNITY) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            Gson gson = new Gson();
                            String data = jsonObject.getString("data");
                            List<CommunityList> communityLists = gson.fromJson(data, new TypeToken<List<CommunityList>>() {
                            }.getType());
                            if (communityLists != null && communityLists.size() > 0) {
                                activity.mCommunitys = communityLists;
                            } else {
                                ToastUtil.showToast(activity, "所选区域还没有建立社区");
//                                Toast.makeText(activity, "所选区域还没有建立社区", Toast.LENGTH_SHORT).show();
                                if (activity.mCommunitys != null) {
                                    activity.mCommunitys.clear();
                                }
                                activity.tv_community_choose.setText("");
                            }
                        } else {
                            //Toast.makeText(activity, "获取社区", Toast.LENGTH_SHORT).show();
                            if (activity.mCommunitys != null) {
                                activity.mCommunitys.clear();
                            }
                            if (activity.mCommunityStrings != null) {
                                activity.mCommunityStrings = null;
                            }
                        }
                        if (activity.mCommunitys != null && activity.mCommunitys.size() > 0) {
                            activity.mCommunityStrings = new String[activity.mCommunitys.size()];
                            for (int i = 0; i < activity.mCommunitys.size(); ++i) {
                                activity.mCommunityStrings[i] = activity.mCommunitys.get(i).getCommunity_name();
                            }
                        } else {
                            activity.mCommunityStrings = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == Constant.RegisterMember) {
                    LogUtils.e("Main注册结果", json);
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.getInt("result") == 1) {
                            Intent intent = new Intent(activity, RegisterCompleteActivity.class);
                            intent.putExtra("phone", activity.phone_num);
                            intent.putExtra("pwd", activity.et_password.getText().toString().trim());
                            activity.startActivity(intent);
                        } else {
                            ToastUtil.showToast(activity,object.getString("message"));
//                            Toast.makeText(activity, object.getString("message"), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_register_info);
        appContext = (BaseApplication) getApplication();
        initIntent();
        initViews();
    }

    private void initIntent() {
        Intent intent = getIntent();
        phone_num = intent.getStringExtra("phone");
        authCode = intent.getStringExtra("authcode");
    }

    private void initViews() {
        cbAgree = (CheckBox) findViewById(R.id.cb_agree);
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNextButtonStatus();
            }
        });
        TextView tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterInfoActivity.this, ResponsibilityActivity.class);
                startActivity(intent1);
            }
        });
        et_NickName = (EditText) findViewById(R.id.et_nickname);
        et_password = (EditText) findViewById(R.id.et_password);//填写密码
        bt_nextstep = (Button) findViewById(R.id.nextstep_button);//下一步按钮
        et_NickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setNextButtonStatus();
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setNextButtonStatus();
            }
        });


        bt_nextstep.setOnClickListener(this);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterInfoActivity.this.finish();
            }
        });
        tv_address_choose = (TextView) findViewById(R.id.tv_address_choose);
        tv_community_choose = (TextView) findViewById(R.id.tv_community_choose);
        LinearLayout linear_belong_address = (LinearLayout) findViewById(R.id.linear_belong_address);
        linear_belong_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProvinceDialog();
            }
        });
        LinearLayout linear_belong_community = (LinearLayout) findViewById(R.id.linear_belong_community);
        linear_belong_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommunityStrings == null || mCommunitys.size() == 0) {
                    ToastUtil.showToast(RegisterInfoActivity.this, "此所在地还没有社区创建，请选择其他所在地或联系管理员");
//                    Toast.makeText(RegisterInfoActivity.this, "此所在地还没有社区创建，请选择其他所在地或联系管理员", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(RegisterInfoActivity.this).setItems(mCommunityStrings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentCommunityName = mCommunityStrings[which];
                            tv_community_choose.setText(mCurrentCommunityName);
                            mCurrentCommunityID = mCommunitys.get(which).getCommunity_id();
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        tv_address_choose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setNextButtonStatus();
            }
        });
        tv_community_choose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setNextButtonStatus();
            }
        });
    }

    public void setNextButtonStatus() {
        if (judgementRegButton(false)) {
            bt_nextstep.setEnabled(true);
            bt_nextstep.setBackgroundResource(R.mipmap.complete_reg_on);
        } else {
            bt_nextstep.setEnabled(false);
            bt_nextstep.setBackgroundResource(R.mipmap.complete_reg_off);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextstep_button) {
            judgement();
        }

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewDistrict) {
            updateCities();
        } else if (wheel == mViewStreet) {
            updateAreas();
        } else if (wheel == mViewCommunity) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }


    private void initProvinceDialog() {//省市区选择
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_province, null);
        TextView tv_dialog_name = (TextView) view.findViewById(R.id.tv_dialogprovince_title);
        tv_dialog_name.setText("请选择所在社区");
        mViewDistrict = (WheelView) view.findViewById(R.id.id_province);
        mViewStreet = (WheelView) view.findViewById(R.id.id_city);
        mViewCommunity = (WheelView) view.findViewById(R.id.id_district);
        Button mBtnConfirmPro = (Button) view.findViewById(R.id.btn_confirm);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加change事件
        mViewStreet.addChangingListener(this);
        // 添加change事件
        mViewCommunity.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirmPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_district.cancel();
                tv_address_choose.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
                VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, new String[]{"province", "city", "county"}, new String[]{mCurrentProviceName, mCurrentCityName, mCurrentDistrictName}, handler, GET_COMMUNITY);
            }
        });
        initProvinceDatas();
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        mViewDistrict.setVisibleItems(7);
        mViewStreet.setVisibleItems(7);
        mViewCommunity.setVisibleItems(7);
        updateCities();
        updateAreas();
        mViewDistrict.setCurrentItem(20);
        mViewStreet.setCurrentItem(9);
        mViewCommunity.setCurrentItem(10);
        dialog_district = new Dialog(this);
        dialog_district.setContentView(view);
        dialog_district.show();
    }


    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = this.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public boolean judgementRegButton(boolean needTips) {
        String password = et_password.getText().toString().trim();
        String community = tv_community_choose.getText().toString().trim();
        String nickname = et_NickName.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            if (needTips)
                et_password.setError("请填写信息");
            return false;
        }
        if (TextUtils.isEmpty(community)) {
            if (needTips)
                tv_community_choose.setError("请选择社区");
            return false;
        }
        if (TextUtils.isEmpty(nickname)) {
            if (needTips)
                et_NickName.setError("请填写昵称");
            return false;
        }
        if (!cbAgree.isChecked()) {
            if (needTips)
                cbAgree.setError("请同意协议");
            return false;
        }
        if (TextUtils.isEmpty(mCurrentProviceName) && TextUtils.isEmpty(mCurrentCityName) && TextUtils.isEmpty(mCurrentDistrictName)) {
            if (needTips)
                tv_address_choose.setError("请选择所在地");
            return false;
        }
        return true;
    }

    public void judgement() {
        if (judgementRegButton(true)) {
            String password = et_password.getText().toString().trim();
            String community = tv_community_choose.getText().toString().trim();
            String nickname = et_NickName.getText().toString().trim();
            if (mCurrentProviceName == null || mCurrentProviceName.equals("") ||
                    mCurrentDistrictName == null || mCurrentDistrictName.equals("") ||
                    mCurrentCityName == null || mCurrentCityName.equals("")) {
                ToastUtil.showToast(this, "请选择所在地");
//                Toast.makeText(this, "请选择所在地", Toast.LENGTH_SHORT).show();
                return;
            }
            if (community.equals("")) {
                ToastUtil.showToast(this, "请选择社区");
//                Toast.makeText(this, "请选择社区", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nickname.equals("")) {
                ToastUtil.showToast(this, "请填写昵称");
//                Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nickname.length() > 10) {
                ToastUtil.showToast(this, "昵称长度不能超过10");
//                Toast.makeText(this, "昵称长度不能超过10", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
                return;
            }
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.register, new String[]{"mobile", "password", "validcode", "license", "name", "communityid"}, new String[]{phone_num, password, authCode, "agree", nickname, mCurrentCommunityID}, handler, Constant.RegisterMember);
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() > 16) {
            et_password.setError("密码不能超过16个字符");
            return false;
        } else if (password.length() < 6) {
            et_password.setError("密码不能少于6个字符");
            return false;
        } else {
            boolean result = true;
            Pattern pattern = Pattern.compile("[a-zA-Z]+");
            Matcher matcher = pattern.matcher(password);
            if (!matcher.find()) {
                result = false;
            }
            pattern = Pattern.compile("[0-9]+");
            matcher = pattern.matcher(password);
            if (!matcher.find()) {
                result = false;
            }
            if (result)
                return true;
            else {
                et_password.setError("密码至少包括一个数字和一个字母");
                return false;
            }
        }
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewStreet.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewCommunity.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewCommunity.setCurrentItem(0);
        mCurrentDistrictName = areas[0];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewDistrict.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewStreet.setCurrentItem(0);
        updateAreas();
    }

}
