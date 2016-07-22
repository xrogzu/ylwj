package com.administrator.elwj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.fragment.BigStageItemFragment;

import java.util.ArrayList;
import java.util.List;

//收到的活动邀请页面
public class MyInviteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private TextView tv_bottomline;
    private int mTotalWidth;
    private ViewPager viewPager;
    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invite);
        mTotalWidth = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        initFragment();
        initViews();
    }

    private void initFragment() {
        mList = new ArrayList<Fragment>();

        Bundle arg1 = new Bundle();
        arg1.putString("url", Constant.getInviteActivity);
        arg1.putBoolean("recommend", false);
        //活动邀请
        BigStageItemFragment fragment1 = new BigStageItemFragment();
        fragment1.setNeedShowProcessDialog(true);
        fragment1.setArguments(arg1);
        mList.add(fragment1);

        Bundle arg2 = new Bundle();
        arg2.putString("url", Constant.getInviteActivity);
        arg2.putBoolean("recommend", true);
        //联名发起人邀请
        BigStageItemFragment fragment2 = new BigStageItemFragment();
        fragment2.setArguments(arg2);
        mList.add(fragment2);


    }

    private void initViews() {
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.tv_viewpagerdetails_num);
        tv_title.setText("收到活动邀请");
        tv_title.setTextSize(17);
        ImageButton ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setVisibility(View.INVISIBLE);
        radioGroup = (RadioGroup) findViewById(R.id.rg_myinvite);
        tv_bottomline = (TextView) findViewById(R.id.tv_bottom_line);
        int lineWidth = mTotalWidth / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.width = lineWidth;
        tv_bottomline.setLayoutParams(params);
        radioGroup.setOnCheckedChangeListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager_myinvite);
        InvitePagerAdapter pagerAdapter = new InvitePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            radioGroup.check(R.id.rb_myinvite_parcipate);
                            ((BigStageItemFragment) mList.get(0)).showProcessDialog();
                            break;
                        case 1:
                            radioGroup.check(R.id.rb_myinvite_recommend);
                            ((BigStageItemFragment) mList.get(1)).showProcessDialog();
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_myinvite_parcipate:
                maginLeft(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_myinvite_recommend:
                maginLeft(1);
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public void maginLeft(int num) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.leftMargin = (params.width * num);
        tv_bottomline.setLayoutParams(params);
    }

    public class InvitePagerAdapter extends FragmentPagerAdapter {
        public InvitePagerAdapter(FragmentManager fm) {
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
