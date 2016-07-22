package com.administrator.elwj;

import android.app.Application;
import android.content.Context;
import android.webkit.CookieSyncManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * 应用基类
 * Created by Administrator on 2015/12/30.
 */
public class BaseApplication extends Application {
    //是否登录标志
    public static boolean isLogin = false;
    //用户id，登录时会写入
    public static String member_id;
    //Volley的请求队列
    private RequestQueue mRequestQueue;
    private static Context context;
//    private RefWatcher refWatcher;
//    public static final boolean isLeakWatchOn = true;

//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }


    @Override
    public void onCreate() {
        super.onCreate();

        //启动百度云推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, getString(R.string.bdapikey));
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), "900027087", false);

        CookieSyncManager.createInstance(this);
        //初始化imageloader
        initImageLoader(getApplicationContext());
        BaseApplication.context = getApplicationContext();

//        if (isLeakWatchOn)
//            refWatcher = LeakCanary.install(this);

    }


    //获取volley请求队列
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    //获取全局context
    public static Context getAppContext() {
        return BaseApplication.context;
    }

    //初始化imageloader
    private void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.denyCacheImageMultipleSizesInMemory();
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.memoryCacheSize(10 * 1024 * 1024);
        builder.diskCacheSize(50 * 1024 * 1024);
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(builder.build());

    }

}
