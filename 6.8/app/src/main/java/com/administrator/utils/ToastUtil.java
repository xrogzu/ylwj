package com.administrator.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.elwj.R;

/**
 * 土司工具类
 * Created by Administrator on 2016/3/9.
 */
public class ToastUtil {
    /**
     * 展示普通的土司
     * @param context
     * @param info
     */
    public static void showToast(Context context,String info){
        TextView textView = (TextView) LinearLayout.inflate(context, R.layout.my_toast,null);
        textView.setText(info);
        Toast toast = Toast.makeText(context,info,Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    //展示一键邀请成功后的土司
    public static void showInviteToast(Context context, String info){
        View view = LinearLayout.inflate(context, R.layout.my_invite_toast,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_content);
        textView.setText(info);
        Toast toast = Toast.makeText(context,info,Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    //获取土司
    public static Toast getToast(Context context, String info){
        TextView textView = (TextView) LinearLayout.inflate(context, R.layout.my_toast,null);
        textView.setText(info);
        Toast toast = Toast.makeText(context,info,Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.setGravity(Gravity.CENTER,0,0);
        return toast;
    }

}
