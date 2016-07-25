package com.administrator.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Community;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.LoginActivity;
import com.administrator.elwj.R;
import com.administrator.utils.IsLogInUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 发布活动界面
 */
public class Launch_activityFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnWheelChangedListener {
    private View mView;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private WheelView mViewDistrict;
    private WheelView mViewStreet;
    private WheelView mViewCommunity;
    private ScrollView mScrollView;
    private Button mLogin;
    private Dialog dialog_district;
    private static final int GET_COMMUNITY = 0;
    private static final int NORMAL_ACTIVITY = 0;
    private static final int PUBLIC_ACTIVITY = 1;
    private static final int VOTE_ACTIVITY = 2;
    private int which;
    private BigStage_normalFragment normalFragment;
    private BigStage_normalFragment publicFragment;
    private BigStage_voteFragment voteFragment;


    public static class MyHandler extends Handler {
        private WeakReference<Launch_activityFragment> mFragment;

        public MyHandler(Launch_activityFragment fragment) {
            mFragment = new WeakReference<Launch_activityFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Launch_activityFragment fragment = mFragment.get();
            if (fragment != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.ISLOGIN) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 1) {
                            fragment.mScrollView.setVisibility(View.VISIBLE);
                            fragment.mLogin.setVisibility(View.GONE);
                            Activity activity = fragment.getActivity();
                            if (activity != null)
                                VolleyUtils.NetUtils(((BaseApplication) fragment.getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, null, null, fragment.handler, GET_COMMUNITY);
                        } else if (result == 0) {
                            fragment.mScrollView.setVisibility(View.GONE);
                            fragment.mLogin.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == GET_COMMUNITY) {//获取当前用户所在的社区
                    Gson gson = new Gson();
                    Community community = gson.fromJson(json, Community.class);
                    int result = community.getResult();
                    if (result == 1) {
                        List<Community.DataEntity> dataEntity = community.getData();
                        if (dataEntity != null && dataEntity.size() > 0) {
                        }
                    } else {
                        ToastUtil.showToast(fragment.getContext(), "获取社区信息失败");
//                        Toast.makeText(fragment.getActivity(),"获取社区信息失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

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

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_launch_activity, null);
        manager = getChildFragmentManager();
        normalFragment = new BigStage_normalFragment(getActivity(), NORMAL_ACTIVITY);
        transaction = manager.beginTransaction();
        transaction.add(R.id.container_framelayout, normalFragment).commit();
        initViews();
        return mView;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        return super.onCreateDialog(savedInstanceState);
//    }

    @Override
    public void onResume() {
        super.onResume();
        IsLogInUtils.isLogin((BaseApplication) getActivity().getApplication(), handler);
    }

    private void initViews() {
        mLogin = (Button) mView.findViewById(R.id.bt_login);
        mLogin.setOnClickListener(this);
        Button nextButton = (Button) mView.findViewById(R.id.bt_next_step);
        nextButton.setOnClickListener(this);
        mScrollView = (ScrollView) mView.findViewById(R.id.scrollview_activity);
        TextView tv_title = (TextView) mView.findViewById(R.id.title);
        ImageButton ib_back = (ImageButton) mView.findViewById(R.id.hot_ib_back);
        RadioGroup rg_activity_type = (RadioGroup) mView.findViewById(R.id.rg_activity_type);
        rg_activity_type.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        transaction = manager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_normal:
                which = NORMAL_ACTIVITY;
                if (normalFragment == null) {
                    normalFragment = new BigStage_normalFragment(getActivity(), NORMAL_ACTIVITY);
                    transaction.show(normalFragment);
                }
                if (publicFragment != null) {
                    transaction.hide(publicFragment);
                }
                if (voteFragment != null) {
                    transaction.hide(voteFragment);
                }
                if (normalFragment != null) {
                    transaction.show(normalFragment);
                }
                transaction.commit();
                break;
            case R.id.rb_vote:
                which = VOTE_ACTIVITY;
                if (voteFragment == null) {
                    voteFragment = new BigStage_voteFragment();
                    transaction.add(R.id.container_framelayout, voteFragment);
                }
                if (publicFragment != null) {
                    transaction.hide(publicFragment);
                }
                if (normalFragment != null) {
                    transaction.hide(normalFragment);
                }
                if (voteFragment != null) {
                    transaction.show(voteFragment);
                }
                transaction.commit();
                break;
            case R.id.rb_public:
                which = PUBLIC_ACTIVITY;
                if (publicFragment == null) {
                    publicFragment = new BigStage_normalFragment(getActivity(), PUBLIC_ACTIVITY);
                    transaction.add(R.id.container_framelayout, publicFragment);
                }
                if (voteFragment != null) {
                    transaction.hide(voteFragment);
                }
                if (normalFragment != null) {
                    transaction.hide(normalFragment);
                }
                if (publicFragment != null) {
                    transaction.show(publicFragment);
                }
                transaction.commit();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_ib_back:
//                finish();
                break;
            case R.id.linear_belong_community:
//                initProvinceDialog();
                break;
            case R.id.bt_login:
                Intent Logintent = new Intent(getActivity(), LoginActivity.class);
                startActivity(Logintent);
                break;
            case R.id.bt_next_step:
                switch (which) {
                    case NORMAL_ACTIVITY:
                        normalFragment.next();
                        break;
                    case PUBLIC_ACTIVITY:
                        publicFragment.next();
                        break;
                    case VOTE_ACTIVITY:
                        voteFragment.next();
                        break;
                }
                break;
        }
    }

    private void initProvinceDialog() {//省市区选择
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_province, null);
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
                //tv_belong_commmunity.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
            }
        });
        initProvinceDatas();
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
        // 设置可见条目数量
        mViewDistrict.setVisibleItems(7);
        mViewStreet.setVisibleItems(7);
        mViewCommunity.setVisibleItems(7);
        updateCities();
        updateAreas();
        dialog_district = new Dialog(getActivity());
        dialog_district.setContentView(view);
        dialog_district.show();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
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
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewDistrict) {
            updateCities();
        } else if (wheel == mViewStreet) {
            updateAreas();
        } else if (wheel == mViewCommunity) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
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
        mViewCommunity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), areas));
        mViewCommunity.setCurrentItem(0);
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
        mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
        mViewStreet.setCurrentItem(0);
        updateAreas();
    }
}
