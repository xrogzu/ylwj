package com.administrator.elwj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.administrator.utils.LogUtils;
import com.baidu.android.pushservice.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 百度云推送接收类，主要接收最新页面中的内容
 * 具体参数详见百度云推送官网sdk
 * Created by Administrator on 2016/3/8.
 */
public class BaiduPushReceiver extends PushMessageReceiver {

    public static String ChannelID;

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        LogUtils.d("xu", "onBind" + i + ":" + s2);
        if (i == 0) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
            ChannelID = sharedPreferences.getString("channelid", null);
            if (ChannelID == null || !ChannelID.equals(s2)) {
                ChannelID = s2;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("channelid", s2);
                editor.apply();
            }
        } else {
            LogUtils.d("xu_baidu_push", getErrorInfo(i));
        }
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        LogUtils.d("xu", "onBind");
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
        LogUtils.d("xu", "onUnbind");
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
        LogUtils.d("xu", "onDelTags");
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
        LogUtils.d("xu", "onListTags");
    }

    //透传消息，不会自动发送notification
    @Override
    public void onMessage(Context context, String s, String s1) {
        LogUtils.d("xu", "onMessage" + s + ":---" + s1);
        try {
            JSONObject jsonObject = new JSONObject(s);
            String data = jsonObject.getString("custom_content");
            if (data != null && !"".equals(data)) {
                Intent intent = new Intent(HomeActivity.UPDATE_NEWEST);
                intent.putExtra("data", data);
                context.sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //当notification被点击时，执行的操作
    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        LogUtils.d("xu", "onNotificationClicked" + s + s1 + ":" + s2);
    }


    //推送消息到达，自动发送notification
    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        LogUtils.d("xu", "onNotificationArrived" + s + s1 + ":" + s2);
    }

    public static String getErrorInfo(int errorCode) {
        String result = "";
        switch (errorCode) {
            case 0:
                result = "百度推送绑定成功";
            case 10001:
                result = "当前网络不可用，请检查网络";
                break;
            case 10002:
                result = "服务不可用，连接server失败";
                break;
            case 10003:
                result = "服务不可用，503错误";
                break;
            case 10101:
                result = "应用集成方式错误，请检查各项声明和权限";
                break;
            case 20001:
                result = "未知错误";
                break;
            case 30600:
                result = "服务内部错误";
                break;
            case 30601:
                result = "非法函数请求，请检查您的请求内容";
                break;
            case 30602:
                result = "请求参数错误，请检查您的参数";
                break;
            case 30603:
                result = "非法构造请求，服务端验证失败";
                break;
            case 30605:
                result = "请求的数据在服务端不存在";
                break;
            case 30608:
                result = "绑定关系不存在或未找到";
                break;
            case 30609:
                result = "一个百度账户绑定设备超出个数限制(多台设备登录同一个百度账户)";
                break;
            case 30612:
                result = "百度账户绑定应用时被禁止，需要白名单授权";
                break;
        }
        return result;
    }

}
