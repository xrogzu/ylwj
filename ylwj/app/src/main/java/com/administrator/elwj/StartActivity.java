package com.administrator.elwj;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.administrator.bean.Constant;
import com.administrator.utils.DownLoadManager;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.UpdataInfoParser;
import com.administrator.utils.UpdateInfo;
import com.administrator.utils.VersionUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/17.
 */
public class StartActivity extends Activity {

    //自动更新信息类
    private UpdateInfo info;

    //强制更新
    private static final int FORCE_UPDATE = -1;
    //由于新版本app
    private static final int UPDATA_CLIENT = 0;
    //更新信息错误
    private static final int GET_UNDATAINFO_ERROR = 1;
    //下载错误
    private static final int DOWN_ERROR = 2;
    //初始化view
    private static final int INIT_VIEWS = 3;
    //开始检测更新
    private static final int START_UPDATE = 4;

    //是否前置更新
    private boolean isForceUpdate = false;

    /*
 * 从服务器获取xml解析并进行比对版本号
 */
    public class CheckVersionTask implements Runnable {

        public void run() {
            try {
                int versionLocal = VersionUtils.getVersionCode(StartActivity.this);
                //从资源文件获取服务器 地址
                //包装成url的对象
                URL url = new URL(Constant.UPDATE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                info = UpdataInfoParser.getUpdataInfo(is);
                String versionCode = info.getVersionCode();
                if (!TextUtils.isEmpty(versionCode)) {
                    int version = Integer.parseInt(versionCode);
                    if (version <= versionLocal) {
                        LogUtils.i("xu", "版本号相同无需升级");
                        Message message = handler.obtainMessage(INIT_VIEWS);
                        message.sendToTarget();
                    } else {
                        LogUtils.i("xu", "版本号不同 ,提示用户升级 ");
                        Message msg = new Message();
                        if (info.getForceUpdate().equals("1"))
                            msg.what = FORCE_UPDATE;
                        else
                            msg.what = UPDATA_CLIENT;
                        handler.sendMessage(msg);
                    }
                } else {
                    Message message = handler.obtainMessage(INIT_VIEWS);
                    message.sendToTarget();
                }

            } catch (Exception e) {
                // 待处理
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    public static class MyHandler extends Handler {
        private WeakReference<StartActivity> mActivity;

        public MyHandler(StartActivity activity) {
            mActivity = new WeakReference<StartActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StartActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    //强制更新
                    case FORCE_UPDATE:
                        activity.showUpdataDialog(true);
                        break;
                    //非强制更新
                    case UPDATA_CLIENT:
                        //对话框通知用户升级程序
                        activity.showUpdataDialog(false);
                        break;
                    //更新信息获取失败
                    case GET_UNDATAINFO_ERROR:
                        //服务器超时
                        ToastUtil.showToast(activity, "获取服务器更新信息失败");
//                        Toast.makeText(activity.getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                        activity.startGuide();
                        break;
                    //下载apk失败
                    case DOWN_ERROR:
                        ToastUtil.showToast(activity.getApplicationContext(), "下载新版本失败");
                        activity.startGuide();
                        break;
                    case INIT_VIEWS:
                        activity.startGuide();
                        break;
                    case START_UPDATE:
                        activity.startUpdate();
                        break;
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     *  1.创建alertDialog的builder.
     *  2.要给builder设置属性, 对话框的内容,样式,按钮
     *  3.通过builder 创建一个对话框
     *  4.对话框show()出来
     */
    protected void showUpdataDialog(final boolean forceUpdate) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LogUtils.i("xu", "下载apk,更新");
                downLoadApk(forceUpdate);
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (forceUpdate) {
                    finish();
                } else {
                    startGuide();
                }
            }
        });
        if (forceUpdate)
            builer.setCancelable(false);
        else builer.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startGuide();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /*
     * 从服务器中下载APK，此函数主要是判断读写空间权限，真正下载的方法在realDownLoad中
     */
    protected void downLoadApk(boolean forceUpdate) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            isForceUpdate = forceUpdate;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.CALL_EXTRNAL_WRITE_REQUIRE);
        } else {
            realDownLoad(forceUpdate);
        }

    }

    //真正下载apk
    private void realDownLoad(boolean forceUpdate) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        if (forceUpdate)
            pd.setCancelable(false);
        else pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startGuide();
            }
        });
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    sleep(1000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，去掉这一行，可以全屏，但还有标题栏存在
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_start);
        handler.sendEmptyMessageDelayed(START_UPDATE, 1500);
    }

    private void startUpdate() {
        new Thread(new CheckVersionTask()).start();
    }

    //兼容Android6.0以上版本的权限系统
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.CALL_EXTRNAL_WRITE_REQUIRE:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    realDownLoad(isForceUpdate);
                } else {
                    if (isForceUpdate) {
                        ToastUtil.showToast(StartActivity.this, "由于没有读写存储卡的权限，无法下载最新的安装包，应用将无法启动");
                        StartActivity.this.finish();
                    } else {
                        startGuide();
                    }
                }
                break;
        }
    }

    public void startGuide() {
        Intent intent1 = new Intent(this, GuideActivity.class);
        startActivity(intent1);
        finish();
    }
}
