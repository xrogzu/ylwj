package com.administrator.fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterFinancalNon;
import com.administrator.bean.Constant;
import com.administrator.bean.NonList;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.MyWebViewActivity;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.widget.AdDomain;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 金融常识界面
 */
public class FinancalNonFragment extends BaseFragment implements XListView.IXListViewListener {

    private boolean isNetRequestDone = false;
    private ProgressDialog progressDialog;

    private XListView listView;
    /*-----------------------------------*/

    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合

    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;

    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 当前图片的索引号

    private ScheduledExecutorService scheduledExecutorService;

    private ListAdapterFinancalNon mAdapter;

    // 轮播banner的数据
    private List<AdDomain> adList;

    private static final int COMMLIST = 1;//列表数据
    private static final int AD=2;//轮播数据
    private BaseApplication application;

    private boolean hasMoreData = true;
    private int curPage = 1;
    private int pageSize = 10;

    public static class MyHandler extends Handler {
        private WeakReference<FinancalNonFragment> mFragment;

        public MyHandler(FinancalNonFragment financalNonFragment) {
            mFragment = new WeakReference<FinancalNonFragment>(financalNonFragment);
        }

        public void handleMessage(android.os.Message msg) {
            FinancalNonFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case COMMLIST: {
                        fragment.isNetRequestDone = true;
                        if (fragment.progressDialog != null) {
                            fragment.progressDialog.dismiss();
                            fragment.progressDialog = null;
                        }
                        fragment.listView.stopLoadMore();
                        fragment.listView.stopRefresh();
                        String json = (String) msg.obj;
                        Gson gson = new Gson();
                        NonList nonList = gson.fromJson(json, NonList.class);
                        if (nonList != null && nonList.getResult() == 1) {
                            List<NonList.DataEntity> dataEntities = nonList.getData();
                            if (fragment.curPage == 1) {
                                fragment.hasMoreData = true;
                                fragment.mAdapter.clear();
                            }
                            fragment.mAdapter.addData(dataEntities);
                            fragment.hasMoreData = dataEntities.size() >= fragment.pageSize;
                        } else {
                            fragment.hasMoreData = false;

                        }

                        break;
                    }
                    case AD:
                        String json = (String) msg.obj;
                        Gson gson = new Gson();
                        NonList nonList = gson.fromJson(json, NonList.class);
                        if (nonList != null && nonList.getResult() == 1) {
                            List<NonList.DataEntity> dataEntities = nonList.getData();
                            fragment.initAdData(dataEntities);//初始化轮播
                            fragment.startAd();//启动轮播
                        }
                        fragment.adViewPager.setCurrentItem(fragment.currentItem);
                        break;

                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    private View headView;

    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        headView = inflater.inflate(R.layout.banner, null);

        isNetRequestDone = false;
        application = (BaseApplication) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_financialnou, null);
        // 使用ImageLoader之前初始化

        // 获取图片加载实例
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();


        // 初始化下面的金融产品
        listView = (XListView) view.findViewById(R.id.list_financalnon);
        listView.setXListViewListener(this);
        mAdapter = new ListAdapterFinancalNon(getActivity());
        listView.setAdapter(mAdapter);
        listView.setPullRefreshEnable(true);//设置下拉刷新
        listView.setPullLoadEnable(true);//设置上拉加载
        listView.addHeaderView(headView);

        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page","pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),"1"}, handler, COMMLIST);
        //轮播数据
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getAdImg, new String[]{"nous_type"}, new String[]{"1"}, handler, AD);

        return view;
    }

    /**
     * 轮播广播模拟数据
     *
     * @return
     */
    public static List<AdDomain> getBannerAd(List<NonList.DataEntity> entities) {
        List<AdDomain> adList = new ArrayList<AdDomain>();
        for(int i=0;i<entities.size();i++){
            AdDomain adDomain = new AdDomain();
            adDomain.setId("");
            adDomain.setDate("");
            adDomain.setTitle("");
            adDomain.setTopicFrom("");
            adDomain.setTopic("");
            adDomain.setAd(false);
            adList.add(adDomain);
        }

//        AdDomain adDomain = new AdDomain();
//        adDomain.setId("");
//        adDomain.setDate("");
//        adDomain.setTitle("");
//        adDomain.setTopicFrom("");
//        adDomain.setTopic("");
//        adDomain.setAd(false);
//        adList.add(adDomain);
//
//        AdDomain adDomain2 = new AdDomain();
//        adDomain2.setId("");
//        adDomain2.setDate("");
//        adDomain2.setTitle("");
//        adDomain2.setTopicFrom("");
//        adDomain2.setTopic("");
//        adDomain2.setAd(false);
//        adList.add(adDomain2);
//
//        AdDomain adDomain3 = new AdDomain();
//        adDomain3.setId("");
//        adDomain3.setDate("");
//        adDomain3.setTitle("");
//        adDomain3.setTopicFrom("");
//        adDomain3.setTopic("");
//        adDomain3.setAd(false);
//        adList.add(adDomain3);
        return adList;
    }

    private void initAdData( List<NonList.DataEntity> entities) {
        // 广告数据
        adList = getBannerAd(entities);

        imageViews = new ArrayList<ImageView>();

        // 点
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        View dot0 = headView.findViewById(R.id.v_dot0);
        View dot1 = headView.findViewById(R.id.v_dot1);
        View dot2 = headView.findViewById(R.id.v_dot2);
        View dot3 = headView.findViewById(R.id.v_dot3);
        View dot4 = headView.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

//        tv_date = (TextView) headView.findViewById(R.id.tv_date);
//        tv_title = (TextView) headView.findViewById(R.id.tv_title);
//        tv_topic_from = (TextView) headView.findViewById(R.id.tv_topic_from);
//        tv_topic = (TextView) headView.findViewById(R.id.tv_topic);
        addDynamicView(entities);
        adViewPager = (ViewPager) headView.findViewById(R.id.vp);
        adViewPager.setAdapter(new MyAdapter(entities));// 设置填充ViewPager页面的适配器


        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());

    }

    private void addDynamicView(final List<NonList.DataEntity> entities) {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
//        ImageView imageView1 = new ImageView(getActivity());
//        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView1.setBackgroundResource(R.mipmap.banner01);
//
//        ImageView imageView2 = new ImageView(getActivity());
//        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView2.setBackgroundResource(R.mipmap.banner02);
//
//
//        ImageView imageView3 = new ImageView(getActivity());
//        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView3.setBackgroundResource(R.mipmap.banner03);

//
//        imageViews.add(imageView1);
//        imageViews.add(imageView2);
//        imageViews.add(imageView3);

        for(int i=0;i<entities.size();i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageLoader.displayImage(entities.get(i).getImageurl(), imageView, options);
            imageViews.add(imageView);
        }
        for (int i = 0; i < adList.size(); i++) {
            // 异步加载图片
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3,
                TimeUnit.SECONDS);
    }

    /**
     * listView的下拉刷新
     */
    @Override
    public void onRefresh() {
        curPage = 1;
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page","pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),"1"}, handler, COMMLIST);

    }

    /**
     * listView的上拉加载
     */
    @Override
    public void onLoadMore() {
        if (hasMoreData) {
            curPage++;
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page","pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),"1"}, handler, COMMLIST);
        } else {
            listView.stopLoadMore();
            ToastUtil.showToast(getContext(),"没有更多数据了");
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
        // 当Activity不可见的时候停止切换
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
            AdDomain adDomain = adList.get(position);
//            tv_title.setText(adDomain.getTitle()); // 设置标题
//            tv_date.setText(adDomain.getDate());
//            tv_topic_from.setText(adDomain.getTopicFrom());
//            tv_topic.setText(adDomain.getTopic());
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
            //停止后重新计时
            scheduledExecutorService.shutdownNow();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3, TimeUnit.SECONDS);
        }
    }

    private class MyAdapter extends PagerAdapter {
        private  List<NonList.DataEntity> entities;
        public MyAdapter(List<NonList.DataEntity> entities){
            this.entities=entities;
        }

        @Override
        public int getCount() {
            return adList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final AdDomain adDomain = adList.get(position);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                    Intent intent = new Intent(getActivity(), MyWebViewActivity.class);
                    intent.putExtra("title", entities.get(position).getTitle());
                    intent.putExtra("type", MyWebViewActivity.TYPE_COMM);
                    intent.putExtra("id", String.format("%d",entities.get(position).getId()));
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


    public void showProgressDialog(){
        if(!isNetRequestDone && getActivity() != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
        }
    }
}
