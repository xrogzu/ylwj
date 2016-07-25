package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.bean.DisCountBean;
import com.administrator.bean.GoodType;
import com.administrator.fragment.Internalshop_shopItemFragment;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠活动和进口商场
 * intent中的num为0表示优惠活动
 * intent中的num为2表示进口商城
 */
public class Interalshop_shopactivity extends AppCompatActivity implements View.OnClickListener {
    private int num;
    private ProgressDialog progressDialog;
    private PopupWindow popupWindow;
    private ImageView ben_activity_image;
    private ImageView next_activity_image;
    private RelativeLayout rl_benzhou;
    private RelativeLayout rl_xiazhou;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private XListView listviewHistoryActivity;
    private HistoryActivityAdapter mHistoryActivityAdapter;
    private List<DisCountBean.DataEntity> mHistoryData;

    private List<Fragment> mFragments;
    private ViewPager mViewPager;

    private List<GoodType> mGoodTypes;



    private BaseApplication appContext;
    private int num_time = 1;
    private List<Bean_GoodsList.DataEntity> myLists = new ArrayList<>();

    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;


    private static final int GET_TYPE_LIST = 1;
    private static final int GET_GOOD_LIST = 2;
    private static final int GET_TWO_WEEK_LIST = 3;
    private static final int GET_HISTORY_LIST = 5;
    private Internalshop_shopItemFragment fragment;


    public static class MyHandler extends Handler {
        private WeakReference<Interalshop_shopactivity> mActivity;

        public MyHandler(Interalshop_shopactivity interalshop_shopactivity) {
            mActivity = new WeakReference<Interalshop_shopactivity>(interalshop_shopactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Interalshop_shopactivity activity = mActivity.get();
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


                } else if (which == GET_TYPE_LIST) {
                    if(activity.progressDialog != null){
                        activity.progressDialog.dismiss();
                        activity.progressDialog = null;
                    }
                    Gson gson = new Gson();
                    activity.mGoodTypes = gson.fromJson(json, new TypeToken<List<GoodType>>() {
                    }.getType());
                    if (activity.mGoodTypes != null) {
                        activity.initShopFragments();
                    }
                } else if (which == GET_GOOD_LIST) {
                    LogUtils.d("xu", json);

                } else if (which == GET_TWO_WEEK_LIST) {
                    LogUtils.d("xu", json);
                    Gson gson = new Gson();
                    DisCountBean disCountBean = gson.fromJson(json, DisCountBean.class);
                    if (disCountBean.getResult() == 1) {
                        final List<DisCountBean.DataEntity> dataEntities = disCountBean.getData();
                        if (dataEntities != null && dataEntities.size() > 0) {
                            for (int i = 0; i < dataEntities.size(); ++i) {
                                final DisCountBean.DataEntity dataEntity = dataEntities.get(i);
                                if (dataEntity.getType().equals("1")) {
                                    activity.rl_benzhou.setVisibility(View.VISIBLE);
                                    activity.tvbenzhou.setText(ToSBC(dataEntity.getName()));
                                    activity.adjustBound(activity.ben_activity_image);
                                    activity.imageLoader.displayImage(dataEntity.getSmall(), activity.ben_activity_image, activity.options);
                                    activity.ben_activity_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
                                            intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                                            intent.putExtra("id", String.format("%d", dataEntity.getGoods_id()));
                                            intent.putExtra("imageRemoved",dataEntity.getBig());
                                            activity.startActivity(intent);
                                        }
                                    });
                                } else if (dataEntity.getType().equals("2")) {
                                    activity.rl_xiazhou.setVisibility(View.VISIBLE);
                                    activity.tvxiazhou.setText(ToSBC(dataEntity.getName()));
                                    activity.adjustBound(activity.next_activity_image);
                                    activity.imageLoader.displayImage(dataEntity.getSmall(), activity.next_activity_image, activity.options);
                                    activity.next_activity_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
                                            intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                                            intent.putExtra("id", String.format("%d", dataEntity.getGoods_id()));
                                            intent.putExtra("imageRemoved",dataEntity.getBig());
                                            activity.startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        LogUtils.d("xu", "获取广告位失败");
                    }
                } else if (which == GET_HISTORY_LIST) {
                    activity.listviewHistoryActivity.stopLoadMore();
                    activity.listviewHistoryActivity.stopRefresh();
                    LogUtils.d("xu", json);
                    Gson gson = new Gson();
                    DisCountBean disCountBean = gson.fromJson(json, DisCountBean.class);
                    if (disCountBean.getResult() == 1) {
                        List<DisCountBean.DataEntity> dataEntities = disCountBean.getData();
                        if (activity.curPage == 1) {
                            activity.hasMoreData = true;
                            if (activity.mHistoryData != null)
                                activity.mHistoryData.clear();
                            else activity.mHistoryData = new ArrayList<>();
                            activity.mHistoryActivityAdapter.notifyDataSetChanged();
                        }
                        if (dataEntities != null && dataEntities.size() > 0) {
                            if (activity.mHistoryData != null)
                                activity.mHistoryData.addAll(dataEntities);
                            else activity.mHistoryData = dataEntities;
                            activity.ivLijie.setVisibility(View.VISIBLE);
                            activity.mHistoryActivityAdapter.notifyDataSetChanged();
                            if (dataEntities.size() < activity.curPage) {
                                activity.hasMoreData = false;
                                ToastUtil.showToast(activity, "没有更多");
                            } else {
                                activity.hasMoreData = true;
                            }
                        } else {
                            ToastUtil.showToast(activity, "没有更多");
                            activity.hasMoreData = false;
                        }
                    } else {
                        LogUtils.d("xu", json);
                    }
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    private void initShopFragments() {
        if (mGoodTypes != null) {
            Internalshop_shopItemFragment fragment1 = new Internalshop_shopItemFragment(num, mGoodTypes.get(0));
            mFragments.add(fragment1);
            Internalshop_shopItemFragment fragment2 = new Internalshop_shopItemFragment(num, mGoodTypes.get(1));
            mFragments.add(fragment2);
            Internalshop_shopItemFragment fragment3 = new Internalshop_shopItemFragment(num, mGoodTypes.get(2));
            mFragments.add(fragment3);
            Internalshop_shopItemFragment fragment4 = new Internalshop_shopItemFragment(num, mGoodTypes.get(3));
            mFragments.add(fragment4);
            Internalshop_shopItemFragment fragment5 = new Internalshop_shopItemFragment(num, mGoodTypes.get(4));
            mFragments.add(fragment5);
//            Internalshop_shopItemFragment fragment6 = new Internalshop_shopItemFragment(num, mGoodTypes.get(5));
//            mFragments.add(fragment6);

            for (int i = 0; i < 5; ++i) {
                radioButtons[i].setText(mGoodTypes.get(i).getText());
            }


            mViewPager = (ViewPager) findViewById(R.id.viewpager_internalshop_shop);
            mViewPager.setOffscreenPageLimit(5);
            mViewPager.setAdapter(new Internalshop_shopRecommendAdapter(getSupportFragmentManager()));

            RadioGroup rg_classify = (RadioGroup) findViewById(R.id.lbrg_classify);
            rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.lbrb_shop_mencloth://居家
                            mViewPager.setCurrentItem(0);
                            horizontalScrollView.smoothScrollTo(0, 0);
                            break;
                        case R.id.lbrb_shop_womencloth://卫浴
                            mViewPager.setCurrentItem(1);
                            horizontalScrollView.smoothScrollTo(0, 0);
                            break;
                        case R.id.lbrb_shop_mom://厨房
                            mViewPager.setCurrentItem(2);
                            horizontalScrollView.smoothScrollTo(0, 0);
                            break;
                        case R.id.lbrb_shop_bag://母婴
                            mViewPager.setCurrentItem(3);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
                            horizontalScrollView.smoothScrollTo(params.width * 6, 0);
                            break;
                        case R.id.lbrb_shop_bag1://母婴
                            mViewPager.setCurrentItem(4);
                            params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
                            horizontalScrollView.smoothScrollTo(params.width * 6, 0);
                            break;
                        case R.id.lbrb_shop_bag2://母婴
                            mViewPager.setCurrentItem(5);
                            params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
                            horizontalScrollView.smoothScrollTo(params.width * 6, 0);
                            break;

                    }
                }
            });

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

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 3500);
            mViewPager.setLayoutParams(params);
        }
    }

    private RadioButton[] radioButtons;
    private int mTotalWidth;
    private TextView tv_bottomline;
    private TextView tvbenzhou;
    private TextView tvxiazhou;
    private HorizontalScrollView horizontalScrollView;

    private ImageView ivLijie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interalshop_shopactivity);
        appContext = (BaseApplication) getApplication();
        mTotalWidth = Interalshop_shopactivity.this.getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
        initImageLoader();
        Intent intent = getIntent();
        num = intent.getIntExtra("num", 0);
        initFragments();
        initViews();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();

        switch (num) {
            case 0:
            case 1:
                Internalshop_shopItemFragment fragment =  new Internalshop_shopItemFragment(num, null);
                mFragments.add(fragment);
              //  VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getTwoWeekActivty, null, null, handler, GET_TWO_WEEK_LIST);
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHistoryActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_HISTORY_LIST);
                break;
            case 2:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.waitNetRequest));
                progressDialog.show();
                VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getShopTypeList, new String[]{"parentid"}, new String[]{"1001"}, handler, GET_TYPE_LIST);
                break;
            default:
                break;
        }
    }


    private void initViews() {

        View headView = LayoutInflater.from(this).inflate(R.layout.interalshop_shopactivity_head, null);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_1);
        LinearLayout ll_cur_week_activity = (LinearLayout) headView.findViewById(R.id.ll_cur_week_activity);

        tvbenzhou = (TextView) headView.findViewById(R.id.tv_benzhou);
        tvxiazhou = (TextView) headView.findViewById(R.id.tv_xiazhou);
        TextView tvbenzhou_hot_icon = (TextView) headView.findViewById(R.id.tv_benzhou_hot);
        TextView tvxiazhou_hot_icon = (TextView) headView.findViewById(R.id.tv_xiazhou_hot);

        radioButtons = new RadioButton[6];
        radioButtons[0] = (RadioButton) findViewById(R.id.lbrb_shop_mencloth);
        radioButtons[1] = (RadioButton) findViewById(R.id.lbrb_shop_womencloth);
        radioButtons[2] = (RadioButton) findViewById(R.id.lbrb_shop_mom);
        radioButtons[3] = (RadioButton) findViewById(R.id.lbrb_shop_bag);
        radioButtons[4] = (RadioButton) findViewById(R.id.lbrb_shop_bag1);
        radioButtons[5] = (RadioButton) findViewById(R.id.lbrb_shop_bag2);


        listviewHistoryActivity = (XListView) findViewById(R.id.listview_history_activity);
        listviewHistoryActivity.addHeaderView(headView);
        mHistoryActivityAdapter = new HistoryActivityAdapter();
        listviewHistoryActivity.setAdapter(mHistoryActivityAdapter);
        ivLijie = (ImageView) headView.findViewById(R.id.lijie);
        listviewHistoryActivity.setPullRefreshEnable(true);
        listviewHistoryActivity.setPullLoadEnable(true);
        listviewHistoryActivity.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
  //              VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getTwoWeekActivty, null, null, handler, GET_TWO_WEEK_LIST);
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHistoryActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_HISTORY_LIST);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHistoryActivity, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_HISTORY_LIST);
                } else {
                    listviewHistoryActivity.stopLoadMore();
                    ToastUtil.showToast(Interalshop_shopactivity.this, "没有更多");
                }
            }
        });


        tv_bottomline = (TextView) findViewById(R.id.lbtv_bottom_line1);
        tv_bottomline.setVisibility(View.GONE);
        if (num == 2) {
            LinearLayout lbHead = (LinearLayout) findViewById(R.id.lb_head_linearlayout);
            lbHead.setVisibility(View.VISIBLE);
            listviewHistoryActivity.setVisibility(View.GONE);
            ll_cur_week_activity.setVisibility(View.GONE);
            int lineWidth = mTotalWidth / 5;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_bottomline.getLayoutParams();
            params.width = lineWidth;
            tv_bottomline.setLayoutParams(params);


        } else {
            listviewHistoryActivity.setVisibility(View.VISIBLE);
        //    listviewHistoryActivity.setPullLoadEnable(false);
            LinearLayout lbHead = (LinearLayout) findViewById(R.id.lb_head_linearlayout);
            lbHead.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
            tv_bottomline.setVisibility(View.GONE);
            ben_activity_image = (ImageView) headView.findViewById(R.id.benzhou_pic1);
            rl_benzhou = (RelativeLayout) headView.findViewById(R.id.rl_benzhou);
            next_activity_image = (ImageView) headView.findViewById(R.id.xiazhou_pic2);
            rl_xiazhou = (RelativeLayout) headView.findViewById(R.id.rl_xiazhou);
        }
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        TextView tv_title = (TextView) findViewById(R.id.title);
        switch (num) {
            case 0:

                tv_title.setText(R.string.favorable);
                break;
            case 1:
                tv_title.setText(R.string.favorable);
                break;
            case 2:
                headView.setVisibility(View.GONE);
                tv_title.setText(R.string.shop);
                break;
        }

        ImageButton ib_shoppingcar = (ImageButton) findViewById(R.id.ib_shoppingcar);

        if (num != 2) {
            ib_shoppingcar.setVisibility(View.GONE);
        }
        ib_shoppingcar.setOnClickListener(this);
        ib_back.setOnClickListener(this);
    }

    private void adjustBound(ImageView iv) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        layoutParams.height = (int) (metrics.widthPixels / Constant.radio);
        iv.setLayoutParams(layoutParams);

    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(XListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
            case R.id.ib_shoppingcar://点击购物车
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
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
        tv_popsrceen1.setOnClickListener(this);
        tv_popsrceen2.setOnClickListener(this);
        tv_popsrceen3.setOnClickListener(this);
        tv_popsrceen4.setOnClickListener(this);

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
//        popupWindow.showAtLocation(Interalshop_shopactivity.this.findViewById(R.id.linear_shoplist),
//                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight + rg_title_preparation.getHeight());
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
                Intent intent0 = new Intent(Interalshop_shopactivity.this, Interalshop_shopactivity.class);
                intent0.putExtra("num", 0);
                startActivity(intent0);
                finish();
                popupWindow.dismiss();
            }
        });
        tv_integral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Interalshop_shopactivity.this, Interalshop_interalactivity.class);
                startActivity(intent1);
                finish();
            }
        });
        tv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Interalshop_shopactivity.this, Interalshop_shopactivity.class);
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
//        popupWindow.showAtLocation(Interalshop_shopactivity.this.findViewById(R.id.linear_shoplist),
//                Gravity.TOP, 0, li_drop.getHeight() + statusBarHeight);
    }

    public class Internalshop_shopRecommendAdapter extends FragmentPagerAdapter {

        public Internalshop_shopRecommendAdapter(FragmentManager fm) {
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

    public class HistoryActivityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mHistoryData == null ? 0 : mHistoryData.size();
        }

        @Override
        public Object getItem(int position) {
            return mHistoryData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(Interalshop_shopactivity.this).inflate(R.layout.gridview_item_history_activity, null);
                imageView = (ImageView) convertView.findViewById(R.id.iv_history_activity_left);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            adjustBound(imageView);
            imageLoader.displayImage(mHistoryData.get(position).getSmall(), imageView, options);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Interalshop_shopactivity.this, Interalshop_detailsactivity.class);
                    intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                    intent.putExtra("id", String.format("%d", mHistoryData.get(position).getGoods_id()));
                    intent.putExtra("imageRemoved",mHistoryData.get(position).getBig());
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }



    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
