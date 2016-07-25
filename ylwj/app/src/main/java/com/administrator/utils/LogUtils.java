package com.administrator.utils;

import android.util.Log;

/**
 * log打印工具类
 * Created by Administrator on 2016/5/9.
 */
public class LogUtils {

    //是否允许打印log
    private static boolean allowLog = true;
    //log等级
    public static int logLevel = Log.VERBOSE;
//    public static int logLevel = Log.ASSERT;

    public static void i(String tag, String msg) {
        if (allowLog && logLevel <= Log.INFO)
            android.util.Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (allowLog && logLevel <= Log.ERROR)
            android.util.Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (allowLog &&logLevel <= Log.DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (allowLog &&logLevel <= Log.VERBOSE)
            android.util.Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (allowLog && logLevel <= Log.WARN)
            android.util.Log.w(tag, msg);
    }

}
