package com.administrator.elwj;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;


//前人做的activity，目前没有用到，可以删掉
public class Register_community extends AppCompatActivity implements View.OnClickListener, OnWheelChangedListener {

    private RelativeLayout rl_province;
    private RelativeLayout rl_street;
    private RelativeLayout rl_community;
    private TextView tv_title;
    private TextView tv_login;
    private Button bt_complete;
    private TextView tv_agreement;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirmPro;
    private Dialog dialog_province;
    private TextView tv_showprovince;
    private TextView tv_showstreet;
    private TextView tv_showcommunity;
    private ImageButton ib_back;
    public static Register_community instance = null;
    private WheelView mViewStreet;
    private Dialog dialog_street;
    private WheelView mViewCommunity;
    private Dialog dialog_community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_community);
        instance=this;
        initView();

    }

    private void initView() {
        rl_province = (RelativeLayout) findViewById(R.id.rl_community_provice);//省市区
        rl_street = (RelativeLayout) findViewById(R.id.rl_community_street);//街道
        rl_community = (RelativeLayout) findViewById(R.id.rl_community_community);//社区
        tv_title = (TextView) findViewById(R.id.title);
        tv_login = (TextView) findViewById(R.id.regedit);
        tv_title.setText(R.string.belong_community);
        tv_login.setText(R.string.title_activity_justlogin);
        tv_login.setVisibility(View.GONE);
        tv_login.setOnClickListener(this);
        bt_complete = (Button) findViewById(R.id.community_commplete_button);//完成注册按钮
        tv_agreement = (TextView) findViewById(R.id.tv_agreement);//用户协议
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        rl_province.setOnClickListener(this);
        rl_street.setOnClickListener(this);
        rl_community.setOnClickListener(this);
        bt_complete.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);

        tv_showprovince = (TextView) findViewById(R.id.tv_community1);
        tv_showstreet = (TextView) findViewById(R.id.tv_community2);
        tv_showcommunity = (TextView) findViewById(R.id.tv_community3);
    }


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
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();/**
     * 当前省的名<String, String>();

    称
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

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_community_provice://点击省市区
                initProvinceDialog();
                break;
            case R.id.rl_community_street://点击街道
                initStreetDialog();
                break;
            case R.id.rl_community_community://点击社区
                initCommunityDialog();
                break;
            case R.id.community_commplete_button://点击完成注册
                Intent intent=new Intent(this,Register_complete.class);
                startActivity(intent);
                break;
            case R.id.tv_agreement://点击用户协议
                break;
            case R.id.ib_back://点击回退按钮
                finish();
                break;
            case R.id.regedit://登录
                Intent intent2=new Intent(Register_community.this,LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    private void initCommunityDialog() {//社区选择
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_community, null);
        mViewCommunity = (WheelView) view.findViewById(R.id.id_community);
        Button bt_confirm_community = (Button) view.findViewById(R.id.btn_confirm_community);
        // 添加change事件
        mViewCommunity.addChangingListener(this);
        // 添加onclick事件
        bt_confirm_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_community.cancel();
                tv_showcommunity.setText(mCurrentProviceName);
            }
        });
        initProvinceDatas();
        mViewCommunity.setViewAdapter(new ArrayWheelAdapter<String>(Register_community.this, mProvinceDatas));
        // 设置可见条目数量
        mViewCommunity.setVisibleItems(7);
        dialog_community = new Dialog(this);
        dialog_community.setContentView(view);
        dialog_community.show();
    }

    private void initStreetDialog() {//街道选择
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_street, null);
        mViewStreet = (WheelView) view.findViewById(R.id.id_street);
        Button bt_confirm_street = (Button) view.findViewById(R.id.btn_confirm_street);
        // 添加change事件
        mViewStreet.addChangingListener(this);
        // 添加onclick事件
        bt_confirm_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_street.cancel();
                tv_showstreet.setText(mCurrentProviceName);
            }
        });
        initProvinceDatas();
        mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(Register_community.this, mProvinceDatas));
        // 设置可见条目数量
        mViewStreet.setVisibleItems(7);
        dialog_street = new Dialog(this);
        dialog_street.setContentView(view);
        dialog_street.show();
    }

    private void initProvinceDialog() {//省市区选择
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_province, null);
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
        mBtnConfirmPro = (Button) view.findViewById(R.id.btn_confirm);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirmPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_province.cancel();
                tv_showprovince.setText(mCurrentProviceName+"-"+mCurrentCityName+"-"+mCurrentDistrictName);
            }
        });
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(Register_community.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
        dialog_province = new Dialog(this);
        dialog_province.setContentView(view);
        dialog_province.show();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
}


