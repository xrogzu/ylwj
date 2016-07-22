package com.administrator.elwj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.administrator.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//银联支付页面
public class MainPayActivity extends Activity {

    private String mMode = "01";//设置测试模式:01为测试 00为正式环境  
    private boolean needClose = false;
    private String tn;

    /**
     * 启动支付界面
     */
    public void doStartUnionPayPlugin() {
//        if (tn != null) {
//            Log.d("tn",tn);
//            int ret = UPPayAssistEx.startPay(this, null, null, tn, mMode);
//            if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
//                new AlertDialog.Builder(this).setMessage("要想银联付款，必须安装银联支付服务客户端，是否安装银联客户端？").setPositiveButton("安装", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        installUPPayPlugin(MainPayActivity.this);
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        MainPayActivity.this.finish();
//                    }
//                }).show();
//            } else {
//                finish();
//            }
//        } else {
//            ToastUtil.showToast(this, "获取流水号失败");
//            finish();
//        }

//        if (ret == 0) {
// UPPayAssistEx.startPay(this, null, null,  tn , mode);
//        }  else {
// 正常的有请求返回的
//            UPPayAssistEx. startPayByJAR (activity, PayActivity. class ,  null ,  null , tn, mode);
//        }
// UPPayAssistEx.startPayFromBrowser(arg0, arg1, arg2);
//  int   ret  = UPPayAssistEx.startPay(MyWithDrawUnionpayActivity.this, null, null,  tn , mode);
// if ( ret  != 0)
// {
// // 需要重新安装控件
// AlertDialog.Builder builder = new AlertDialog.Builder(this);
// builder.setTitle("提示");
// builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
//
// builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
// {
// @Override
// public void onClick(DialogInterface dialog,  int  which)
// {
// // 可做安装处理
// dialog.dismiss();
// UPPayAssistEx.installUPPayPlugin(MyWithDrawUnionpayActivity.this);
// }
// });
//
// builder.setPositiveButton("取消", new DialogInterface.OnClickListener()
// {
//
// @Override
// public void onClick(DialogInterface dialog,  int  which)
// {
// dialog.dismiss();
// }
// });
// builder.create().show();
// }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pay);
        initIntent();
        doStartUnionPayPlugin();
    }

    private void initIntent() {
        Intent intent1 = getIntent();
        if (intent1 != null) {
            tn = intent1.getStringExtra("tn");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("xu_test", "onActivityResult");
        if (data == null) {
            return;
        }
        String msg = "";  
        /* 
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消 
         */
        String str = data.getExtras().getString("pay_result");
        Log.v("zftphone", "2 " + data.getExtras().getString("merchantOrderId"));
        if (str != null && str.equalsIgnoreCase("success")) {
            msg = "支付成功";

        } else if (str != null && str.equalsIgnoreCase("fail")) {
            msg = "支付失败";

        } else if (str != null && str.equalsIgnoreCase("cancel")) {
            msg = "取消支付";
        }
        ToastUtil.showToast(this, msg);
        finish();
        //支付完成,处理自己的业务逻辑!  
//        Toast.makeText(getApplicationContext(), msg, 0).show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static boolean installUPPayPlugin(Context var0) {
        boolean var1 = false;

        InputStream var2;
        try {
            var2 = var0.getAssets().open("UPPayPluginExStd.apk");
            FileOutputStream var3 = var0.openFileOutput("UPPayPluginExStd.apk", 1);
            byte[] var4 = new byte[1024];
            boolean var5 = false;

            int var10;
            while ((var10 = var2.read(var4)) > 0) {
                var3.write(var4, 0, var10);
            }

            var3.close();
            var2.close();
            String var7 = var0.getFilesDir().getAbsolutePath();
            String var9 = var7 + File.separator + "UPPayPluginExStd.apk";
            if (var7 != null) {
                Intent var8;
                (var8 = new Intent("android.intent.action.VIEW")).setDataAndType(Uri.parse("file:" + var9), "application/vnd.android.package-archive");
                var0.startActivity(var8);
                var1 = true;
            }
        } catch (IOException var6) {
            var2 = null;
            var6.printStackTrace();
        }

        return var1;
    }

    @Override
    protected void onResume() {
        Log.d("xu_test", "onResume");
        super.onResume();
        if (!needClose) {
            needClose = true;
        } else {
            finish();
        }
    }
}
