package com.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.R;
import com.administrator.minterface.GetServiceWhere;
import com.administrator.pager.FinicalViewpager;
import com.administrator.pager.MyFinancialViewpager;

import java.util.ArrayList;
import java.util.List;


/**
 * 金融管家fragment
 * Created by Administrator on 2015/12/23.
 */
public class FinancialManagerFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRg_finacial;
    private View mView;
    private int mTotalWidth;
    private TextView mTv_bottomline;
    private MyFinancialViewpager mViewPager_finalexpert;
    private List<Fragment> mList;
    private ServiceMapFragment mapFragment;
    private final int  itemNumber=5;//Fragment个数


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_service, null);
        mRg_finacial = (RadioGroup) mView.findViewById(R.id.rg_finacial);
        mRg_finacial.setOnCheckedChangeListener(this);
        mTotalWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度

        initFragment();
        initView();
        return mView;
    }

    /**
     * 初始化Fragment，以后不同的Fragment来自不同的类
     */
    private void initFragment() {
        mList = new ArrayList<Fragment>();
        RecommendFragment messageFragment = new RecommendFragment(this);
        messageFragment.setTitle("理财早知道");
        mList.add(messageFragment);

        HotProductFragment productFragment = new HotProductFragment();
        productFragment.setTitle("家庭财富");
        mList.add(productFragment);

        FinancialExpertFragment expertFragment = new FinancialExpertFragment(getActivity());
        expertFragment.setTitle("金融专家");
        mList.add(expertFragment);

        mapFragment = new ServiceMapFragment();
        mapFragment.setTitle("服务地图");
        mList.add(mapFragment);

        BaseFragment commonSenseFragment = new FinancalNonFragment();
        commonSenseFragment.setTitle("金融常识");
        mList.add(commonSenseFragment);
    }


    private void initView() {
        ImageButton ib_back = (ImageButton) mView.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity activity = (HomeActivity) getActivity();
                if (activity.getIsFromNewest()) {
                    activity.popIsFromNewestFragment();
                } else {
                    GetServiceWhere where = (GetServiceWhere) getActivity();
                    where.getWhere(Constant.OUT_FINANCIAL);
                }
            }
        });
        TextView tv_title = (TextView) mView.findViewById(R.id.title);
        tv_title.setText(R.string.financial);//设置标题金融管家
        TextView tv_regedit = (TextView) mView.findViewById(R.id.regedit);
        tv_regedit.setVisibility(View.INVISIBLE);//设置不可见

        mTv_bottomline = (TextView) mView.findViewById(R.id.tv_bottom_line);
        mViewPager_finalexpert = (MyFinancialViewpager) mView.findViewById(R.id.viewPager_finicial_expert);
        int lineWidth = mTotalWidth / itemNumber;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTv_bottomline.getLayoutParams();
        params.width = lineWidth;
        mTv_bottomline.setLayoutParams(params);
        FinicalViewpager mAdapter = new FinicalViewpager(getChildFragmentManager(), mList);
        mViewPager_finalexpert.setAdapter(mAdapter);
        mViewPager_finalexpert.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (mViewPager_finalexpert.getCurrentItem()) {
                        case 0:
                            mRg_finacial.check(R.id.rb_recommend);
                            ((RecommendFragment) mList.get(0)).showProgassDialog();
                            break;
                        case 1:
                            mRg_finacial.check(R.id.rb_hot_selling);
                            Log.d("xu", "position=1");
                            break;
                        case 2:
                            mRg_finacial.check(R.id.rb_finial_expert);
                            ((FinancialExpertFragment) mList.get(2)).showProgassDialog();
                            Log.d("xu", "position=2");
                            break;
                        case 3:
                            mRg_finacial.check(R.id.rb_servicemap);
                            ((ServiceMapFragment) mList.get(3)).showProgressDialog();
                            Log.d("xu", "position=3");
                            break;
                        case 4:
                            mRg_finacial.check(R.id.rb_finial_commonsense);
                            ((FinancalNonFragment) mList.get(4)).showProgressDialog();
                            Log.d("xu", "position=4");
                            break;
                    }
                }
            }
        });

    }

    public void maginLeft(int num) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTv_bottomline.getLayoutParams();
        params.leftMargin = (params.width * num);
        mTv_bottomline.setLayoutParams(params);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_recommend:
                maginLeft(0);
                mViewPager_finalexpert.setCurrentItem(0);
                break;
            case R.id.rb_hot_selling:
                maginLeft(1);
                mViewPager_finalexpert.setCurrentItem(1);
                break;
            case R.id.rb_finial_expert:
                maginLeft(2);
                mViewPager_finalexpert.setCurrentItem(2);
                break;
            case R.id.rb_servicemap:
                maginLeft(3);
                mViewPager_finalexpert.setCurrentItem(3);
                break;
            case R.id.rb_finial_commonsense:
                maginLeft(4);
                mViewPager_finalexpert.setCurrentItem(4);
                break;
        }
    }

    public void initMapData(boolean granted) {
        if (mapFragment != null) {
            mapFragment.initData(granted);
        }
    }

    public int getCurrentItem(){
        return mViewPager_finalexpert.getCurrentItem();
    }
}
