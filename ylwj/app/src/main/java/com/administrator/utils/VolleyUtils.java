package com.administrator.utils;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.HomePageActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Volley封装类
 * Created by acer on 2016/1/28.
 */
public class VolleyUtils {
    private static final String TAG = "JSON";

    /**
     * 请求网络
     *
     * @param requestQueue 需要利用BaseApplication中的getRequestQueue方法进行获取
     * @param httpurl      请求网络的地址
     * @param parameter    传参key，必须与value对应
     * @param params       传参value，必须与key对应
     * @param handler      自定的handler，处理网络请求返回
     * @param which        与handler对应的handler
     */
    public static void NetUtils(RequestQueue requestQueue, final String httpurl, final String[] parameter, final String[] params, final Handler handler, final int which) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Main", "response -> " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("result");
                            if (result.equals("1")) {
                                Message message = Message.obtain();
                                message.obj = response;
                                message.what = which;
                                handler.sendMessage(message);
                            } else {
                                String messageIslogin = object.getString("message");
                                if (messageIslogin.equals("用户尚未登录") || messageIslogin.equals("用户未登录，请先登录。") || messageIslogin.equals("服务器连接超时，请刷新页面！")) {
                                    ToastUtil.showToast(BaseApplication.getAppContext(), "请先登录");
                                } else {
                                    Message message = Message.obtain();
                                    message.obj = response;
                                    message.what = which;
                                    handler.sendMessage(message);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Response为空", "response -> " + response);
                            Message message = Message.obtain();
                            message.obj = response;
                            message.what = which;
                            handler.sendMessage(message);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Main", "错误-->" + error.getMessage(), error);
                NetworkResponse response = error.networkResponse;
                ToastUtil.showToast(BaseApplication.getAppContext(), "请检查网络");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                if (parameter != null) {
                    for (int i = 0; i < parameter.length; i++) {
                        map.put(parameter[i], params[i]);
                    }
                }
                return map;
            }

            //获取返回的cookies
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    CookieManager cookieManager = CookieManager.getInstance();
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");//Cookie值
                    cookieManager.setCookie(Constant.baseUrl, rawCookies);
                    CookieSyncManager.getInstance().sync();
                    String dataString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));//返回值
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }

            }

            //设置post的cookies
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                CookieManager cookieManager = CookieManager.getInstance();
                HashMap localHashMap = new HashMap();
                if (cookieManager.hasCookies()) {
//                    Log.e(TAG,"里面还是有cookie的");
                    String cookie = cookieManager.getCookie(httpurl);

                    if (!TextUtils.isEmpty(cookie)) {
//                        Log.e(TAG,"从本地取出的cookier--->"+cookie);

                        localHashMap.put("Cookie", cookie);
                    }
                }
//                Log.e(TAG,"这是空的");
                return localHashMap;
            }
        };
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }
}


