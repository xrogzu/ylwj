package com.administrator.elwj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.fragment.BaseFragment;

import com.administrator.fragment.BigStageItemFragment;
import com.administrator.fragment.MyAttentionPerson_fragment;

import java.util.ArrayList;
import java.util.List;

//我的关注页面
public class MyAttentionActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> mList;
    private RadioGroup radioGroup;
    private TextView tv_bottomline;
    private int mTotalWidth;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        mTotalWidth = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        initFragment();
        initViews();
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
        tv_title.setText("我的关注");
        tv_title.setTextSize(17);
        ImageButton ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setVisibility(View.INVISIBLE);

        radioGroup = (RadioGroup) findViewById(R.id.rg_my);
        radioGroup.setOnCheckedChangeListener(this);
        tv_bottomline = (TextView) findViewById(R.id.tv_bottom_line);
        int lineWidth = mTotalWidth / 2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.width = lineWidth;
        tv_bottomline.setLayoutParams(params);

        viewPager = (ViewPager) findViewById(R.id.viewPager_myattention);
        AttentionPagerAdapter pagerAdapter = new AttentionPagerAdapter(getSupportFragmentManager());
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
                if(state == 2){
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            radioGroup.check(R.id.rb_my_attention_person);
                            ((MyAttentionPerson_fragment)mList.get(0)).showProcessDialog();
                            break;
                        case 1:
                            radioGroup.check(R.id.rb_my_attention_activity);
                            ((BigStageItemFragment)mList.get(1)).showProcessDialog();
                            break;
                    }
                }
            }
        });

    }

    private void initFragment() {
        mList = new ArrayList<Fragment>();

        //我关注的人fragment
        MyAttentionPerson_fragment attentionPerson_fragment = new MyAttentionPerson_fragment();
        mList.add(attentionPerson_fragment);

        Bundle bundle = new Bundle();
        bundle.putString("url", Constant.listAttentionActivities);
        //我关注的活动fragment
        BaseFragment fragment2 = new BigStageItemFragment();
        fragment2.setArguments(bundle);
        mList.add(fragment2);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_my_attention_person:
                maginLeft(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_my_attention_activity:
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

    public class AttentionPagerAdapter extends FragmentPagerAdapter {
        public AttentionPagerAdapter(FragmentManager fm) {
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
