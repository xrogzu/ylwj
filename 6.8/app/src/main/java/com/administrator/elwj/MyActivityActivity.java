package com.administrator.elwj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.administrator.bean.Constant;
import com.administrator.fragment.BigStageItemFragment;

/**
 * 我的中我的活动页面
 * Created by Administrator on 2016/3/16.
 */
public class MyActivityActivity extends AppCompatActivity {

    private ViewPager viewPager;
    //我参与的活动fragment
    private Fragment applyFragment;
    //我发布的活动fragment
    private Fragment publishFragment;
    private int Direction_Left = 0;
    private int Direction_Right = 1;
    private View leftLine;
    private View rightLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);
        initFragments();
        initViews();
    }

    private void initFragments() {
        Bundle bundle = new Bundle();
        bundle.putString("url", Constant.getApplyActivity);
        applyFragment = new BigStageItemFragment();
        ((BigStageItemFragment)applyFragment).setNeedShowProcessDialog(true);
        applyFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("url", Constant.getMyPublishActivity);
        publishFragment = new BigStageItemFragment();
        publishFragment.setArguments(bundle1);

    }

    private void initViews() {

        ImageButton imageButton = (ImageButton) findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityActivity.this.finish();
            }
        });
        leftLine = findViewById(R.id.left);
        rightLine = findViewById(R.id.right);
        viewPager = (ViewPager) findViewById(R.id.view_pager_container);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.rg_myactivity);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_apply) {
                    viewPager.setCurrentItem(0);
                } else if (checkedId == R.id.rb_publish) {
                    viewPager.setCurrentItem(1);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 2){
                    if(viewPager.getCurrentItem() == 0){
                        moveBottomRedLine(Direction_Left);
                        ((BigStageItemFragment)applyFragment).showProcessDialog();
                    }else if(viewPager.getCurrentItem() == 1){
                        moveBottomRedLine(Direction_Right);
                        ((BigStageItemFragment)publishFragment).showProcessDialog();
                    }
                }
            }
        });
    }

    //移动底部红线
    private void moveBottomRedLine(int direction){
        if(direction == Direction_Left){
            leftLine.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            rightLine.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        }
        else if(direction == Direction_Right){
            leftLine.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            rightLine.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
        }
    }


    private class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return applyFragment;
            else return publishFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
