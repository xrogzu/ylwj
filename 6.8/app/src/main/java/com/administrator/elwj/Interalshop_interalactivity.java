package com.administrator.elwj;

import android.app.ProgressDialog;
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
import com.administrator.fragment.Internalshop_internalItemFragment;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分兑换子fragment，比如（5万积分的页面，或者5-10万积分的页面）,放在viewpager中的子项
 */
public class Interalshop_interalactivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private LinearLayout li_drop;
    private RadioGroup radioGroup;
    private LinearLayout li_preparation;
    private TextView tv_title_preparation;
    private PopupWindow popupWindow;
    private BaseApplication appContext;
    private ViewPager mViewPager;
    private RadioButton rb_title_price;
    private RadioButton[] radioButtons;
    private LinearLayout rg_title_preparation;
    private TextView tv_title_comprehensive;
    private TextView tv_title_sold;
    private View li_title_price;
    private TextView tv_title_price;
    private ImageView iv_price;
    private int num_time = 1;
    private TextView tv_bottomline;
    private int mTotalWidth;

    public static class MyHandler extends Handler {
        private WeakReference<Interalshop_interalactivity> mActivity;

        public MyHandler(Interalshop_interalactivity activity) {
            mActivity = new WeakReference<Interalshop_interalactivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Interalshop_interalactivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.ISLOGIN) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 0) {
                            Intent LoginIntent = new Intent(activity, LoginActivity.class);
                            activity.startActivity(LoginIntent);
                        } else if (result == 1) {
                            Intent carIntent = new Intent(activity, ShopCarActivity.class);
                            activity.startActivity(carIntent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);


    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interalshop_interalactivity);
        appContext = (BaseApplication) getApplication();
        mTotalWidth = Interalshop_interalactivity.this.getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        Intent intent = getIntent();
        int num = intent.getIntExtra("num", 1);

        initFragments();
        initViews();
        mViewPager.setCurrentItem(num);
    }

    private void initFragments() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.waitNetRequest));
        progressDialog.show();

        mFragments = new ArrayList<>();

        Internalshop_internalItemFragment fragment1 = new Internalshop_internalItemFragment(Interalshop_interalactivity.this, Constant.LESS_FIVE);
        mFragments.add(fragment1);//小于5

        Internalshop_internalItemFragment fragment2 = new Internalshop_internalItemFragment(Interalshop_interalactivity.this, Constant.FIVE_TEN);
        mFragments.add(fragment2);//5-10

        Internalshop_internalItemFragment fragment3 = new Internalshop_internalItemFragment(Interalshop_interalactivity.this, Constant.TEN_FIF);
        mFragments.add(fragment3);//10-15

        Internalshop_internalItemFragment fragment4 = new Internalshop_internalItemFragment(Interalshop_interalactivity.this, Constant.MORE_FIF);
        mFragments.add(fragment4);//>15


    }

    private void initViews() {


        int lineWidth = mTotalWidth / 4;
        tv_bottomline = (TextView) findViewById(R.id.tv_bottom_line1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
        params.width = lineWidth;
        tv_bottomline.setLayoutParams(params);


        radioButtons = new RadioButton[5];
        radioButtons[0] = (RadioButton) findViewById(R.id.rb_shop_mencloth);
        radioButtons[1] = (RadioButton) findViewById(R.id.rb_shop_womencloth);
        radioButtons[2] = (RadioButton) findViewById(R.id.rb_shop_mom);
        radioButtons[3] = (RadioButton) findViewById(R.id.rb_shop_bag);
        radioButtons[4] = (RadioButton) findViewById(R.id.rb_shop_shoes);

        radioButtons[4].setVisibility(View.GONE);

        radioButtons[0].setText("5万");
        radioButtons[1].setText("5-10万");
        radioButtons[2].setText("10-15万");
        radioButtons[3].setText("15万");


        RadioGroup rg_classify = (RadioGroup) findViewById(R.id.rg_classify);
        rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shop_mencloth://男装
                        maginLeft(0);
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_shop_womencloth://女装
                        maginLeft(1);
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_shop_mom://母婴
                        mViewPager.setCurrentItem(2);
                        maginLeft(2);
                        break;
                    case R.id.rb_shop_bag://箱包
                        mViewPager.setCurrentItem(3);
                        maginLeft(3);
                        break;

                }
            }
        });
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        TextView tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText(R.string.integral_exchange);
//        tv_title_preparation = (TextView) findViewById(R.id.tv_title_preparation);
//        tv_title_preparation.setText(R.string.interal_preparation);


//        li_drop = (LinearLayout) findViewById(R.id.li_title_drop);
        ImageButton ib_shoppingcar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        ib_shoppingcar.setVisibility(View.GONE);
//        tv_title_comprehensive = (TextView) findViewById(R.id.rb_title_comprehensive);//综合
//        tv_title_sold = (TextView) findViewById(R.id.rb_title_salesvolume);//销量
//        li_title_price = findViewById(R.id.rb_title_price);//价格积分
//        tv_title_price = (TextView) findViewById(R.id.tv_price);
//        tv_title_price.setText(R.string.integral);
//        iv_price = (ImageView) findViewById(R.id.iv_price);
//        li_preparation = (LinearLayout) findViewById(R.id.li_title_preparation);
//        li_mcqbox = (LinearLayout) findViewById(R.id.li_title_mcqbox);

        ib_back.setOnClickListener(Interalshop_interalactivity.this);
//        li_drop.setOnClickListener(this);
        ib_shoppingcar.setOnClickListener(this);
//        li_preparation.setOnClickListener(this);

//        tv_title_comprehensive.setOnClickListener(this);
//        tv_title_sold.setOnClickListener(this);
//        li_title_price.setOnClickListener(this);
//        li_mcqbox.setOnClickListener(this);


        mViewPager = (ViewPager) findViewById(R.id.viewpager_internalshop_internal);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new InternalshopAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < radioButtons.length; ++i) {
                    if (position == i)
                        radioButtons[i].setChecked(true);
                    else radioButtons[i].setChecked(false);
                }
                maginLeft(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
//            case R.id.li_title_drop://点击商城下拉
//                showPopWindow();
//                break;
            case R.id.ib_shoppingcar://点击购物车
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
                break;
//            case R.id.li_title_preparation://点击筛选
////                showPopScreen();
//                break;
//            case R.id.rb_title_comprehensive://综合
//                num_time=1;
//                tv_title_comprehensive.setTextColor(getResources().getColor(R.color.colorAccent));
//                tv_title_sold.setTextColor(getResources().getColor(R.color.textgrey));
//                tv_title_price.setTextColor(getResources().getColor(R.color.textgrey));
//                iv_price.setImageResource(R.mipmap.jiantou1);
//                break;
//            case R.id.rb_title_salesvolume://销量
//                num_time=1;
//                tv_title_comprehensive.setTextColor(getResources().getColor(R.color.textgrey));
//                tv_title_sold.setTextColor(getResources().getColor(R.color.colorAccent));
//                tv_title_price.setTextColor(getResources().getColor(R.color.textgrey));
//                iv_price.setImageResource(R.mipmap.jiantou1);
//                break;
//            case R.id.rb_title_price://积分
//                tv_title_comprehensive.setTextColor(getResources().getColor(R.color.textgrey));
//                tv_title_sold.setTextColor(getResources().getColor(R.color.textgrey));
//                tv_title_price.setTextColor(getResources().getColor(R.color.colorAccent));
//                if(num_time%2==1){
//                    iv_price.setImageResource(R.mipmap.jiantou2);
//                }else {
//                    iv_price.setImageResource(R.mipmap.jiantou3);
//                }
//                num_time++;
//                break;
////            case R.id.li_title_mcqbox://点击选框
////                break;
//            case R.id.tv_poppricescreen1:
//                popupWindow.dismiss();
//                break;
//            case R.id.tv_poppricescreen2:
//                popupWindow.dismiss();
//                break;
//            case R.id.tv_poppricescreen3:
//                popupWindow.dismiss();
//                break;
//            case R.id.tv_poppricescreen4:
//                popupWindow.dismiss();
//                break;
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
        tv_popsrceen1.setOnClickListener(Interalshop_interalactivity.this);
        tv_popsrceen2.setOnClickListener(Interalshop_interalactivity.this);
        tv_popsrceen3.setOnClickListener(Interalshop_interalactivity.this);
        tv_popsrceen4.setOnClickListener(Interalshop_interalactivity.this);

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
        popupWindow.showAtLocation(Interalshop_interalactivity.this.findViewById(R.id.linear_shoplist),
                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight + radioGroup.getHeight());
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
                Intent intent0 = new Intent(Interalshop_interalactivity.this, Interalshop_shopactivity.class);
                intent0.putExtra("num", 0);
                startActivity(intent0);
                finish();
                popupWindow.dismiss();
            }
        });
        tv_integral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Interalshop_interalactivity.this, Interalshop_interalactivity.class);
                startActivity(intent1);
                finish();
                popupWindow.dismiss();
            }
        });
        tv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Interalshop_interalactivity.this, Interalshop_shopactivity.class);
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
        popupWindow.showAtLocation(Interalshop_interalactivity.this.findViewById(R.id.linear_shoplist),
                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight);
    }


    public class InternalshopAdapter extends FragmentPagerAdapter {

        public InternalshopAdapter(FragmentManager fm) {
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

    public void dismissProcessDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}