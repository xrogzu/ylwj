package com.administrator.elwj;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.administrator.bean.UserInfoExtra;
import com.google.gson.Gson;

/**
 * 登录时需要记录用户id，此handler将获取到的用户id记录在BaseApplication中
 * Created by Administrator on 2016/4/11.
 */
public class InitMemberIDHandler extends Handler {

    public static final int GET_MEMBER_ID = 0;


    @Override
    public void handleMessage(Message msg) {
        if (msg.what == GET_MEMBER_ID) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            UserInfoExtra userInfoExtra = gson.fromJson(json, UserInfoExtra.class);
            if (userInfoExtra.getResult() == 1) {
                UserInfoExtra.DataEntity dataEntity = userInfoExtra.getData();
                if (dataEntity != null) {
                    BaseApplication.member_id = dataEntity.getMember_id();
//                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(BaseApplication.getAppContext());
                    Intent intent = new Intent(HomeActivity.UPDATE_NEWEST);
                    BaseApplication.getAppContext().sendBroadcast(intent);
                }
            }
        }
    }
}
