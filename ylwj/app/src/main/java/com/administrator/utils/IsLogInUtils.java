package com.administrator.utils;

import android.os.Handler;

import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;

/**
 * 请求服务器判断是否登录
 * Created by acer on 2016/2/3.
 */
public class IsLogInUtils {
    public static void isLogin(BaseApplication appContext,Handler handler){
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
    }
}
