package com.administrator.elwj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.fragment.MyMoneyPay_HistoryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的现金消费
 */
public class MyMoneyPayActivity extends AppCompatActivity implements View.OnClickListener {
    private PopupWindow popupWindow;
    private ViewPager mViewPager;
    private RadioButton [] radioButtons;
    private TextView tv_bottomline;
    private int mTotalWidth;
    private int num;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pay);
        mTotalWidth = MyMoneyPayActivity.this.getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        Intent intent = getIntent();
        num=intent.getIntExtra("detail",0);

        initFragments();
        initViews();
    }

    private void initFragments() {

        mFragments = new ArrayList<>();

        MyMoneyPay_HistoryFragment fragment1 = new MyMoneyPay_HistoryFragment(MyMoneyPayActivity.this,Constant.WAIT_PAY);
        fragment1.setNeedShowProgressDialog(true);
        mFragments.add(fragment1);//待付款

        MyMoneyPay_HistoryFragment fragment2 = new MyMoneyPay_HistoryFragment(MyMoneyPayActivity.this,Constant.PATED);
        mFragments.add(fragment2);//已付款

        MyMoneyPay_HistoryFragment fragment3 = new MyMoneyPay_HistoryFragment(MyMoneyPayActivity.this,Constant.ALL_PAY);
        mFragments.add(fragment3);//全部消费




    }

    private void initViews() {

        ImageButton ibShopCar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        ibShopCar.setVisibility(View.GONE);
        int lineWidth = mTotalWidth / 3;
        tv_bottomline = (TextView) findViewById(R.id.tv_bottom_line1_pay);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.width = lineWidth;
        tv_bottomline.setLayoutParams(params);


        radioButtons = new RadioButton[3];
        radioButtons[0] = (RadioButton) findViewById(R.id.rb_shop_mencloth_pay);
        radioButtons[1] = (RadioButton) findViewById(R.id.rb_shop_womencloth_pay);
        radioButtons[2] = (RadioButton) findViewById(R.id.rb_shop_mom_pay);


        RadioGroup rg_classify = (RadioGroup) findViewById(R.id.rg_classify_pay);
        rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shop_mencloth_pay://代付款
                        maginLeft(0);
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_shop_womencloth_pay://待评价
                        maginLeft(1);
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_shop_mom_pay://全部消费
                        mViewPager.setCurrentItem(2);
                        maginLeft(2);
                        break;

                }
            }
        });
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        TextView tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("我的现金消费");
        tv_title.setTextColor(ContextCompat.getColor(this, R.color.black));

        ib_back.setOnClickListener(MyMoneyPayActivity.this);


        mViewPager = (ViewPager) findViewById(R.id.viewpager_internalshop_internal_pay);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new InternalMyPayAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 2){
                    int position = mViewPager.getCurrentItem();
                    for (int i = 0; i < radioButtons.length; ++i) {
                        if (position == i) {
                            radioButtons[i].setChecked(true);
                        }
                        else radioButtons[i].setChecked(false);
                    }
                    ((MyMoneyPay_HistoryFragment)mFragments.get(position)).showProgressDialog();
                    ((MyMoneyPay_HistoryFragment)mFragments.get(position)).update();
                    maginLeft(position);
                }
            }
        });
        mViewPager.setCurrentItem(num);

    }

    public void maginLeft(int num) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.leftMargin = (params.width * num);
        tv_bottomline.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://回退按钮
                finish();
                break;
        }
    }

    private void showPopScreen() {//筛选弹窗
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_pricescreen, null);
        TextView tv_popsrceen1 = (TextView) view.findViewById(R.id.tv_poppricescreen1);
        TextView tv_popsrceen2 = (TextView) view.findViewById(R.id.tv_poppricescreen2);
        TextView tv_popsrceen3 = (TextView) view.findViewById(R.id.tv_poppricescreen3);
        TextView tv_popsrceen4 = (TextView) view.findViewById(R.id.tv_poppricescreen4);
        tv_popsrceen1.setOnClickListener(MyMoneyPayActivity.this);
        tv_popsrceen2.setOnClickListener(MyMoneyPayActivity.this);
        tv_popsrceen3.setOnClickListener(MyMoneyPayActivity.this);
        tv_popsrceen4.setOnClickListener(MyMoneyPayActivity.this);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopshoplist_anim_style);
        //显示位置
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
//        popupWindow.showAtLocation(MyMoneyPayActivity.this.findViewById(R.id.linear_shoplist),
//                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight + radioGroup.getHeight());
    }

    private void showPopWindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_shoplist, null);
        TextView tv_favoral = (TextView) view.findViewById(R.id.tv_popshoplist_favorable);
        TextView tv_integral = (TextView) view.findViewById(R.id.tv_popshoplist_integral);
        TextView tv_shop = (TextView) view.findViewById(R.id.tv_popshoplist_shop);
        tv_favoral.setOnClickListener(new View.OnClickListener() {//优惠活动
            @Override
            public void onClick(View v) {
                Intent intent0 = new Intent(MyMoneyPayActivity.this, Interalshop_shopactivity.class);
                intent0.putExtra("num", 0);
                startActivity(intent0);
                finish();
                popupWindow.dismiss();
            }
        });
        tv_integral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyMoneyPayActivity.this, Interalshop_interalactivity.class);
                startActivity(intent1);
                finish();
                popupWindow.dismiss();
            }
        });
        tv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyMoneyPayActivity.this, Interalshop_shopactivity.class);
                intent1.putExtra("num", 2);
                startActivity(intent1);
                finish();
                popupWindow.dismiss();
            }
        });
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopshoplist_anim_style);
        //显示位置
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
//        popupWindow.showAtLocation(MyMoneyPayActivity.this.findViewById(R.id.linear_shoplist),
//                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight);
    }


    public class InternalMyPayAdapter extends FragmentPagerAdapter {

        public InternalMyPayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }



}