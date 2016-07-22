package com.administrator.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterInteral;
import com.administrator.bean.Constant;
import com.administrator.bean.DisCountBean;
import com.administrator.bean.ShopAdvid;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.Interalshop_detailsactivity;
import com.administrator.elwj.Interalshop_interalactivity;
import com.administrator.elwj.Interalshop_shopactivity;
import com.administrator.elwj.LoginActivity;
import com.administrator.elwj.R;
import com.administrator.elwj.ShopCarActivity;
import com.administrator.minterface.GetServiceWhere;
import com.administrator.utils.LogUtils;
import com.administrator.utils.VolleyUtils;
import com.administrator.widget.AdDomain;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by acer on 2016/1/8.
 * 社区超市首页fragment
 */
public class Integral_ECshopFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton ib_shopcar;
    private View mView;
    private XListView listView;
    private ImageView ivAdTitle;
    //最下面的那一张图片
    private ImageView ivAd;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private ViewPager adViewPager;
    private List<ImageView> imageViews;
    private ScrollView sv;
    private LinearLayout haead_linearLayout;
    private LinearLayout jing_linearLayout;

    private ImageView centerImage;
    int[] location = new int[2];
    int[] location2 = new int[2];

    private Handler handler2 = new MyHandler(this);
    private Handler handler = new AdHandler(this);
    private ListAdapterInteral adapter;
    private List<ShopAdvid.DataEntity> da;
    private View headView;
    private LinearLayout li_dotsList;
    private ArrayList<View> dots;

    private int currentItem = 0; // 当前图片的索引号
    private ScheduledExecutorService scheduledExecutorService;
    private BaseApplication appContext;
    private List<ShopAdvid> list_list = new ArrayList<>();
    private ShopAdvid shopAd;
    //广告图片轮播使用的handler
    private static class AdHandler extends Handler{
        private WeakReference<Integral_ECshopFragment> mFragment;
        public AdHandler(Integral_ECshopFragment fragment){
            mFragment = new WeakReference<Integral_ECshopFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final Integral_ECshopFragment ecShopFragment = mFragment.get();
            ecShopFragment.adViewPager.setCurrentItem(ecShopFragment.currentItem);
        }
    }

    public static class MyHandler extends Handler{
        private WeakReference<Integral_ECshopFragment> mFragment;
        public MyHandler(Integral_ECshopFragment fragment){
            mFragment = new WeakReference<Integral_ECshopFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final Integral_ECshopFragment fragment = mFragment.get();
            if(fragment != null) {
                int which = msg.what;
                String Json = (String) msg.obj;

                switch (which) {
                    case Constant.GOOD_LIST_TAG1://广告
                        Gson gson1 = new Gson();
                        ShopAdvid shopAdvid = gson1.fromJson(Json, ShopAdvid.class);
                        fragment.da = shopAdvid.getData();
                        if (fragment.da != null) {
                            if (fragment.da.size() > 0) {
                                final List<String> imgList = new ArrayList<>();
                                for (int i = 0; i < fragment.da.size(); i++) {
                                    imgList.add(fragment.da.get(i).getAtturl());
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragment.initADdates(imgList);
                                        fragment.startAd();
                                    }
                                },1000);
                            }
                        }
                        break;
                    case Constant.GOOD_LIST_TAG2://重磅出击 限时优惠下面的那个商品的
                        Gson gson2 = new Gson();
                        DisCountBean disCountBean = gson2.fromJson(Json, DisCountBean.class);
                        if(disCountBean.getResult() == 1){
                            List<DisCountBean.DataEntity> dataEntities = disCountBean.getData();
                            if(dataEntities != null && dataEntities.size() > 0) {
                                fragment.adapter.addData(dataEntities.get(0));
                            }
                        }
                        VolleyUtils.NetUtils(fragment.appContext.getRequestQueue(), Constant.baseUrl + Constant.goodListByTag, new String[]{"advid"}, new String[]{"17"}, fragment.handler2, Constant.GOOD_LIST_TAG3);
                        break;
                    case Constant.GOOD_LIST_TAG3://积分兑换
                        Gson gson3 = new Gson();
                        ShopAdvid bean3 = gson3.fromJson(Json, ShopAdvid.class);
                        fragment.list_list.add(bean3);
                        VolleyUtils.NetUtils(fragment.appContext.getRequestQueue(), Constant.baseUrl + Constant.goodListByTag, new String[]{"advid"}, new String[]{"20"}, fragment.handler2, Constant.GOOD_LIST_TAG4);
                        break;
                    case Constant.GOOD_LIST_TAG4://最下面的那个商品，原本是四张图片，后来四张图片合成一张图片，只用第一个商品
                        Gson gson4 = new Gson();
                        ShopAdvid bean4 = gson4.fromJson(Json, ShopAdvid.class);
                        if(bean4 != null){
                            List<ShopAdvid.DataEntity> dataEntities = bean4.getData();
                            if(dataEntities != null && dataEntities.size() > 0){
                                fragment.ivAdTitle.setVisibility(View.VISIBLE);
                                fragment.mImageLoader.displayImage(dataEntities.get(0).getAtturl(),fragment.ivAd,fragment.options);
                            }
                        }
                        fragment.shopAd = bean4;
//                        fragment.list_list.add(bean4);
                        break;
                    case Constant.ISLOGIN:
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(Json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                Intent LoginIntent = new Intent(fragment.getActivity(), LoginActivity.class);
                                fragment.startActivity(LoginIntent);
                            } else if (result == 1) {
                                Intent carIntent = new Intent(fragment.getActivity(), ShopCarActivity.class);//购物车
                                fragment.startActivity(carIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                if (fragment.list_list.size() == 1) {
                    LogUtils.e("Main", "fragment.list_list.size() == 3");
                    fragment.adapter.addData(fragment.list_list, fragment.getActivity());
                    setListViewHeightBasedOnChildren(fragment.listView);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_integral_ecshop, null);
        appContext = (BaseApplication) getActivity().getApplication();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodListByTag, new String[]{"advid"}, new String[]{"1000"}, handler2, Constant.GOOD_LIST_TAG1);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getThisWeekActivity, null,null, handler2, Constant.GOOD_LIST_TAG2);

        // 获取图片加载实例
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        initView();
        return mView;
    }

    private void initADdates(List<String> stringListimgs) {
        imageViews = new ArrayList<ImageView>();
        // 点
        dots = new ArrayList<View>();
        adViewPager = (ViewPager) mView.findViewById(R.id.viewpager_interal_title);
        li_dotsList = (LinearLayout) mView.findViewById(R.id.li_dotslist);
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
        addDynamicView(stringListimgs);
    }

    private void initView() {
        sv = (ScrollView) mView.findViewById(R.id.act_solution_1_sv);
        sv.smoothScrollTo(0, 0);
        haead_linearLayout = (LinearLayout) mView.findViewById(R.id.head_linearlayout);
        jing_linearLayout = (LinearLayout) mView.findViewById(R.id.jing_linearlayout);
        jing_linearLayout.setVisibility(View.GONE);
        centerImage = (ImageView) mView.findViewById(R.id.ecshop_imageview_pic01);
        listView = (XListView) mView.findViewById(R.id.listView_integral);
        ivAd = (ImageView) mView.findViewById(R.id.iv_ad);
        ivAd.setOnClickListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);//getScreenWidth(this)
        ViewGroup.LayoutParams lp = ivAd.getLayoutParams();
        lp.height = (int) (metrics.widthPixels/Constant.radio);
        ivAd.setLayoutParams(lp);
        ivAdTitle = (ImageView) mView.findViewById(R.id.iv_ad_title);
        //延迟加载这两块内容
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.setVisibility(View.VISIBLE);
                centerImage.setVisibility(View.VISIBLE);
                haead_linearLayout.setVisibility(View.VISIBLE);
            }
        },1000);
        sv.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == touchEventId) {

                        if (lastY != sv.getScrollY()) {
                            //scrollview一直在滚动，会触发
                            handler.sendMessageDelayed(
                                    handler.obtainMessage(touchEventId, sv), 5);
                            lastY = sv.getScrollY();
                            haead_linearLayout.getLocationOnScreen(location);
                            jing_linearLayout.getLocationOnScreen(location2);
                            //动的到静的位置时，静的显示。动的实际上还是网上滚动，但我们看到的是静止的那个
                            if (location[1] <= location2[1]) {
                                jing_linearLayout.setVisibility(View.VISIBLE);
                            } else {
                                //静止的隐藏了
                                jing_linearLayout.setVisibility(View.GONE);
//                                jing_linearLayout.setBackgroundResource(R.color.transparent);
                            }
                        }
                    }
                }
            };

            public boolean onTouch(View v, MotionEvent event) {
                //必须两个都搞上，不然会有瑕疵
                //没有这段，手指按住拖动的时候没有效果
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                //没有这段，手指松开scroll继续滚动的时候，没有效果
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                return false;
            }
        });

        TextView jing_favorable = (TextView) mView.findViewById(R.id.jing_favorable);
        TextView jing_integral_exchange = (TextView) mView.findViewById(R.id.jing_integral_exchange);
        TextView jing_shop = (TextView) mView.findViewById(R.id.jing_shop);
        jing_favorable.setOnClickListener(this);
        jing_integral_exchange.setOnClickListener(this);
        jing_shop.setOnClickListener(this);

        ImageButton ib_back = (ImageButton) mView.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        TextView tv_title = (TextView) mView.findViewById(R.id.title);
        tv_title.setText(R.string.integral_shop);//设置标题社区超市
        ib_shopcar = (ImageButton) mView.findViewById(R.id.ib_shoppingcar);
        ib_shopcar.setOnClickListener(this);
        ImageButton ib_more = (ImageButton) mView.findViewById(R.id.ib_more);
        ib_more.setOnClickListener(this);
        TextView tv_integral_exchange = (TextView) mView.findViewById(R.id.tv_integral_exchange);
        tv_integral_exchange.setOnClickListener(this);
        TextView tv_favorable = (TextView) mView.findViewById(R.id.tv_favorable);
        tv_favorable.setOnClickListener(this);
        TextView tv_shop = (TextView) mView.findViewById(R.id.tv_shop);
        tv_shop.setOnClickListener(this);



        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        adapter = new ListAdapterInteral(getActivity());
        listView.setAdapter(adapter);
        listView.setFocusable(false);
//        setListViewHeightBasedOnChildren(listView);
    }

    private void addDynamicView(List<String> stringListimgs) {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        for (int i = 0; i < stringListimgs.size(); i++) {
            if(getActivity() != null){
                ImageView imageView = new ImageView(getActivity());
                // 异步加载图片
                mImageLoader.displayImage(stringListimgs.get(i), imageView,
                        options);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
                imageViews.add(imageView);
                View dotsView=LayoutInflater.from(appContext).inflate(R.layout.dots_view,null);
                View dot = dotsView.findViewById(R.id.dot);
                li_dotsList.addView(dotsView);
                dots.add(dot);//这个地方不能添加dotsView
            }
        }
        if(dots != null && dots.size() > 1){
            //刚进来的时候，显示第一个为focus
            dots.get(0).setBackgroundResource(R.drawable.dot_focused);
        }
        adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://返回按钮
                GetServiceWhere where = (GetServiceWhere) getActivity();
                where.getWhere(Constant.OUT_FINANCIAL);
                break;
            case R.id.ib_shoppingcar://购物车
                ib_shopcar.setEnabled(false);
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler2, Constant.ISLOGIN);
                break;
            case R.id.ib_more://更多按钮
                break;
            case R.id.tv_integral_exchange://积分兑换
                Intent intent2 = new Intent(getActivity(), Interalshop_interalactivity.class);
                intent2.putExtra("num", 0);
                startActivity(intent2);
                break;
            case R.id.tv_favorable://优惠活动
                Intent intent0 = new Intent(getActivity(), Interalshop_shopactivity.class);
                intent0.putExtra("num", 0);
                startActivity(intent0);
                break;
            case R.id.tv_shop://商城
                Intent intent1 = new Intent(getActivity(), Interalshop_shopactivity.class);
                intent1.putExtra("num", 2);
                startActivity(intent1);
                break;
            case R.id.jing_integral_exchange://积分兑换
                Intent intent3 = new Intent(getActivity(), Interalshop_interalactivity.class);
                intent3.putExtra("num", 0);
                startActivity(intent3);
                break;
            case R.id.jing_favorable://优惠活动
                Intent intent4 = new Intent(getActivity(), Interalshop_shopactivity.class);
                intent4.putExtra("num", 0);
                startActivity(intent4);
                break;
            case R.id.jing_shop://进口商城
                Intent intent5 = new Intent(getActivity(), Interalshop_shopactivity.class);
                intent5.putExtra("num", 2);
                startActivity(intent5);
                break;
            case R.id.iv_ad:
                if(shopAd != null) {
                    List<ShopAdvid.DataEntity> dataEntities = shopAd.getData();
                    if(dataEntities != null && dataEntities.size() > 0) {
                        String id = dataEntities.get(0).getUrl();
                        Intent intent = new Intent(getContext(), Interalshop_detailsactivity.class);
                        intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                        intent.putExtra("imageRemoved",dataEntities.get(0).getAtturl());
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private class ScrollTask implements Runnable {
        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(scheduledExecutorService != null)
            scheduledExecutorService.shutdown();
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
            //停止后重新计时
            if(scheduledExecutorService!=null){
                scheduledExecutorService.shutdownNow();
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3, TimeUnit.SECONDS);
            }

        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
//            final AdDomain adDomain = adList.get(position);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                    Intent intent=new Intent(getActivity(), Interalshop_detailsactivity.class);
                    intent.putExtra("type",Constant.SHOP_TYPE_AUTO);
                    intent.putExtra("id",  da.get(position).getUrl());
                    intent.putExtra("imageRemoved",da.get(position).getAtturl());
                    LogUtils.e("Main",da.get(position).getUrl());
                    getActivity().startActivity(intent);
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3,
                TimeUnit.SECONDS);
    }


    /**
     * 轮播广播模拟数据
     *
     * @return
     */
    public static List<AdDomain> getBannerAd() {
        List<AdDomain> adList = new ArrayList<AdDomain>();

        AdDomain adDomain = new AdDomain();
        adDomain.setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=bb99d6add2c8a786be2a4c0f5708c9c7/d50735fae6cd7b8900d74cd40c2442a7d9330e29.jpg");
        adDomain.setAd(false);
        adList.add(adDomain);

        AdDomain adDomain2 = new AdDomain();
        adDomain2
                .setImgUrl("http://g.hiphotos.baidu.com/image/w%3D310/sign=7cbcd7da78f40ad115e4c1e2672e1151/eaf81a4c510fd9f9a1edb58b262dd42a2934a45e.jpg");
        adDomain2.setAd(false);
        adList.add(adDomain2);

        AdDomain adDomain3 = new AdDomain();
        adDomain3
                .setImgUrl("http://e.hiphotos.baidu.com/image/w%3D310/sign=392ce7f779899e51788e3c1572a6d990/8718367adab44aed22a58aeeb11c8701a08bfbd4.jpg");
        adDomain3.setAd(false);
        adList.add(adDomain3);

        AdDomain adDomain4 = new AdDomain();
        adDomain4
                .setImgUrl("http://d.hiphotos.baidu.com/image/w%3D310/sign=54884c82b78f8c54e3d3c32e0a282dee/a686c9177f3e670932e4cf9338c79f3df9dc55f2.jpg");
        adDomain4.setAd(false);
        adList.add(adDomain4);


        return adList;
    }
    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View listItem;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ib_shopcar != null){
            ib_shopcar.setEnabled(true);
        }
    }
}

