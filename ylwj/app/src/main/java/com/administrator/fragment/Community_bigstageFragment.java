package com.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.R;
import com.administrator.minterface.GetServiceWhere;
import com.administrator.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.conn.BasicEofSensorWatcher;

/**
 * Created by acer on 2016/1/15.
 * 社区大舞台主fragment
 */
public class Community_bigstageFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View mView;
    private RadioGroup rg_title;
    private TextView tv_bottomline;
    private ViewPager viewPager;
    private int mTotalWidth;
    private List<Fragment> mList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bigstage, null);
        mTotalWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        initFragment();
        initViews();

        return mView;

    }

    private void initFragment() {
        mList = new ArrayList<Fragment>();

        Bundle arg1 = new Bundle();
        arg1.putString("url", Constant.getPublishActivity);
        arg1.putBoolean("publish", true);
        BigStageItemFragment fragment1 = new BigStageItemFragment();
        fragment1.setArguments(arg1);
        mList.add(fragment1);//全部
        //首次启动也需要显示加载对话框
        fragment1.setNeedShowProcessDialog(true);

        Bundle arg2 = new Bundle();
        BigStageItemFragment fragment2 = new BigStageItemFragment();
        arg2.putString("url", Constant.getCommunityActivity);
        fragment2.setArguments(arg2);
        mList.add(fragment2);//社区

        Bundle arg3 = new Bundle();
        arg3.putString("url",Constant.getRankActivity);
        BigStageItemFragment fragment3 = new BigStageItemFragment();
        fragment3.setArguments(arg3);
        mList.add(fragment3);//排行

        Launch_activityFragment fragment4 = new Launch_activityFragment();
        mList.add(fragment4);

    }

    private void initViews() {
        TextView tv_title = (TextView) mView.findViewById(R.id.title);
        tv_title.setText(R.string.big_stage);
        ImageButton ib_back = (ImageButton) mView.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        ImageButton ib_search = (ImageButton) mView.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        ImageButton ib_add = (ImageButton) mView.findViewById(R.id.ib_add);
        ib_add.setVisibility(View.GONE);
        ib_add.setOnClickListener(this);
        rg_title = (RadioGroup) mView.findViewById(R.id.rg_bigstage);
        tv_bottomline = (TextView) mView.findViewById(R.id.tv_bottom_line);
        viewPager = (ViewPager) mView.findViewById(R.id.viewPager_bigstage);
        viewPager.setOffscreenPageLimit(5);
        rg_title.setOnCheckedChangeListener(this);
        BigStagePagerAdapter adapter = new BigStagePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        int lineWidth = mTotalWidth /4;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.width = lineWidth;
        tv_bottomline.setLayoutParams(params);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    int position = viewPager.getCurrentItem();
                    switch (position) {
                        case 0:
                            rg_title.check(R.id.rb_all);
                            ((BigStageItemFragment) mList.get(0)).showProcessDialog();
                            break;
                        case 1:
                            rg_title.check(R.id.rb_community);
                            if(BaseApplication.isLogin)
                                ((BigStageItemFragment) mList.get(1)).showProcessDialog();
                            break;
                        case 2:
                            rg_title.check(R.id.rb_rate);
                            ((BigStageItemFragment) mList.get(2)).showProcessDialog();
                            break;
                        case 3:
                            rg_title.check(R.id.rb_publish);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                HomeActivity activity = (HomeActivity) getActivity();
                if(activity.getIsFromNewest()){
                    activity.popIsFromNewestFragment();
                }else {
                    GetServiceWhere where = (GetServiceWhere) getActivity();
                    where.getWhere(Constant.OUT_FINANCIAL);
                }
                break;
            case R.id.ib_search:
                break;
            case R.id.ib_add:
                break;
        }
    }

    public void maginLeft(int num) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.leftMargin = (params.width * num);
        tv_bottomline.setLayoutParams(params);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                maginLeft(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_community:
                maginLeft(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_rate:
                maginLeft(2);
                viewPager.setCurrentItem(2);
                break;
            case R.id.rb_publish:
                maginLeft(3);
                viewPager.setCurrentItem(3);
                //IsLogInUtils.isLogin(appContext, handler);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class BigStagePagerAdapter extends FragmentPagerAdapter {

        public BigStagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
