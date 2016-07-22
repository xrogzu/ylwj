package com.administrator.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Community;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

/**
 * 草稿箱活动界面
 */

public class DraftLaunch_activityFragment extends BaseFragment implements View.OnClickListener {

    private RadioButton rbNormal;
    private RadioButton rbVote;
    private RadioButton rbPublic;
    //类型
    private int mType = -1;

    private ActivityDetails.DataEntity mData;
    private FragmentManager manager;
    private View mView;

    private static final int GET_COMMUNITY = 0;
    private static final int GET_ACTIVITY = 1;
    private static final int NORMAL_ACTIVITY = 0;
    private static final int PUBLIC_ACTIVITY = 1;
    private DraftBigStage_normalFragment normalFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_launch_activity, null);
        VolleyUtils.NetUtils(((BaseApplication) getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, null, null, handler, GET_COMMUNITY);
        initViews();
        manager = getChildFragmentManager();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String activity_id = bundle.getString("activity_id", "");
            mType = bundle.getInt("type");
            if (activity_id != null && !"".equals(activity_id)) {
                VolleyUtils.NetUtils(((BaseApplication) getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{activity_id}, handler, GET_ACTIVITY);
            }
        }
        return mView;
    }


    public static class MyHandler extends Handler {

        private WeakReference<DraftLaunch_activityFragment> mFragment;

        public MyHandler(DraftLaunch_activityFragment fragment) {
            mFragment = new WeakReference<DraftLaunch_activityFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            DraftLaunch_activityFragment fragment = mFragment.get();
            if (fragment != null) {
                String json = (String) msg.obj;
                LogUtils.d("xu", json);
                if (msg.what == GET_COMMUNITY) {
                    Gson gson = new Gson();
                    Community community = gson.fromJson(json, Community.class);
                    int result = community.getResult();
                    if (result == 1) {
                        List<Community.DataEntity> dataEntity = community.getData();
                        if (dataEntity != null && dataEntity.size() > 0) {
                        }
                    } else {
                        ToastUtil.showToast(fragment.getContext(), "获取社区信息失败");
                    }
                } else if (msg.what == GET_ACTIVITY) {
                    Gson gson = new Gson();
                    ActivityDetails activityDetails  = gson.fromJson(json, ActivityDetails.class);
                    if(activityDetails.getResult() == 1){
                        fragment.mData = activityDetails.getData();
                        fragment.initData();
                    }else {
                        ToastUtil.showToast(fragment.getContext(),"未能获取到活动详细信息");
                    }

                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /**
     * 解析省市区的XML数据
     */


    private void initData() {
        if (mData != null) {
            switch (mType) {
                case Constant.DRAFT_TYPE_NORMAL:
                    rbNormal.setChecked(true);
                    normalFragment = new DraftBigStage_normalFragment(getActivity());
                    normalFragment.setData(mData);
                    normalFragment.setDraftParentFragment(this);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container_framelayout, normalFragment).commit();
                    break;
                case Constant.DRAFT_TYPE_PUBLIC:
                    rbPublic.setChecked(true);
                    BigStage_normalFragment normalFragment1 = new BigStage_normalFragment(getActivity(),PUBLIC_ACTIVITY);
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.container_framelayout, normalFragment1).commit();
                    break;
                case Constant.DRAFT_TYPE_VOTE:
                    rbVote.setChecked(true);
                    BigStage_voteFragment voteFragment = new BigStage_voteFragment();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.container_framelayout, voteFragment).commit();
                    break;
            }

        }
    }

    private void initViews() {
        RadioGroup rg_activity_type = (RadioGroup) mView.findViewById(R.id.rg_activity_type);
        rbNormal = (RadioButton) mView.findViewById(R.id.rb_normal);
        rbVote = (RadioButton) mView.findViewById(R.id.rb_vote);
        rbPublic = (RadioButton) mView.findViewById(R.id.rb_public);
        rbVote.setVisibility(View.GONE);
        rbPublic.setVisibility(View.GONE);
        Button bt_next_step = (Button) mView.findViewById(R.id.bt_next_step);
        bt_next_step.setOnClickListener(this);
        rg_activity_type.setEnabled(false);
        rbNormal.setEnabled(false);
        rbPublic.setEnabled(false);
        rbVote.setEnabled(false);
        initTimerDialog();
    }

    private void initTimerDialog() {
        Calendar calendar = Calendar.getInstance();
        String initDateTime = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "  "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_ib_back:
                break;
            case R.id.linear_belong_community:
                break;
            case R.id.bt_next_step:
                normalFragment.next();
                break;
        }
    }

}
