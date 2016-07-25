package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterFinancalNon;
import com.administrator.bean.Constant;
import com.administrator.bean.NonList;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.widget.AdDomain;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
 * Created by Administrator on 2016/7/4.
 * 精致生活List
 */
public class ExquisiteLifeListActivity extends AppCompatActivity implements XListView.IXListViewListener ,View.OnClickListener{
    private boolean isNetRequestDone = false;
    private ProgressDialog progressDialog;
    private ImageView ib_back;
    private XListView listView;
    /*-----------------------------------*/

    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 滑动的图片集合

    private List<View> dots; // 图片标题正文的那些点
    private List<View> dotList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 当前图片的索引号

    private ScheduledExecutorService scheduledExecutorService;

    private ListAdapterFinancalNon mAdapter;

    // 轮播banner的数据
    private List<AdDomain> adList;

    private static final int COMMLIST = 1;
    private static final int AD=2;//轮播数据

    private BaseApplication application;

    private boolean hasMoreData = true;
    private int curPage = 1;
    private int pageSize = 10;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int nous_type=1;

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ExquisiteLifeList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.administrator.elwj/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                    ExquisiteLifeListActivity.this.finish();
                break;
        }
    }

    public static class MyHandler extends Handler {
        private WeakReference<ExquisiteLifeListActivity> mFragment;

        public MyHandler(ExquisiteLifeListActivity financalNonFragment) {
            mFragment = new WeakReference<ExquisiteLifeListActivity>(financalNonFragment);
        }

        public void handleMessage(Message msg) {
            ExquisiteLifeListActivity fragment = mFragment.get();
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exquisite_life_list);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = inflater.inflate(R.layout.layout_exquisite_life_list_head, null);

        isNetRequestDone = false;
        application = (BaseApplication) getApplication();

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
        initTitle();
        // 初始化下面的金融产品
        listView = (XListView) findViewById(R.id.list_financalnon);
        listView.setXListViewListener(this);
        mAdapter = new ListAdapterFinancalNon(this);
        listView.setAdapter(mAdapter);
        listView.setPullRefreshEnable(true);//设置下拉刷新
        listView.setPullLoadEnable(true);//设置上拉加载
        listView.addHeaderView(headView);


        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page","pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),String.format("%d", nous_type)}, handler, COMMLIST);
        //轮播数据
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getAdImg, new String[]{"nous_type"}, new String[]{String.format("%d", nous_type)}, handler, AD);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mAdapter.title=title;
    }

    private void initTitle(){
        //设置标题
        Intent intent= getIntent();
        title= intent.getStringExtra("title");
        hot_product_title.setText(title);
        switch (title){
            case "银行信用卡服务" :
                nous_type=2;
                break;
            case "银行VIP服务":
                nous_type=3;
                break;
            case  "银行特约商户":
                nous_type=4;
                break;
        }
    }
    private TextView hot_product_title;
    private void initView(){
        ib_back = (ImageView) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        hot_product_title= (TextView) findViewById(R.id.hot_product_title);
    }
    private String title;

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
    private void initAdData(List<NonList.DataEntity> entities) {
        adList = getBannerAd(entities); // 广告数据

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

    private void addDynamicView(List<NonList.DataEntity> entities) {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
//        ImageView imageView1 = new ImageView(this);
//        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView1.setBackgroundResource(R.mipmap.banner01);
//
//        ImageView imageView2 = new ImageView(this);
//        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView2.setBackgroundResource(R.mipmap.banner02);
//
//
//        ImageView imageView3 = new ImageView(this);
//        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView3.setBackgroundResource(R.mipmap.banner03);
//
//        imageViews.add(imageView1);
//        imageViews.add(imageView2);
//        imageViews.add(imageView3);
        for(int i=0;i<entities.size();i++){
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundResource(R.mipmap.banner01);
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
   //     VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, COMMLIST);
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page", "pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),String.format("%d", nous_type)}, handler, COMMLIST);


    }

    /**
     * listView的上拉加载
     */
    @Override
    public void onLoadMore() {
        if (hasMoreData) {
            curPage++;
        //    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, COMMLIST);
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNonList, new String[]{"page", "pagesize","nous_type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize),String.format("%d", nous_type)}, handler, COMMLIST);

        } else {
            listView.stopLoadMore();
            ToastUtil.showToast(ExquisiteLifeListActivity.this, "没有更多数据了");
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ExquisiteLifeList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.administrator.elwj/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
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
        public Object instantiateItem(ViewGroup container,final int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final AdDomain adDomain = adList.get(position);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                    Intent intent = new Intent(ExquisiteLifeListActivity.this, MyWebViewActivity.class);
                    intent.putExtra("title", entities.get(position).getTitle());
                    intent.putExtra("type", MyWebViewActivity.TYPE_COMM);
                    intent.putExtra("id", String.format("%d",entities.get(position).getId()));
                    ExquisiteLifeListActivity.this.startActivity(intent);
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



    public void showProgressDialog() {
        if (!isNetRequestDone && this != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(this.getString(R.string.waitNetRequest));
            progressDialog.show();
        }
    }
}
