package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.zxing.camera.CameraManager;
import com.administrator.zxing.decoding.CaptureActivityHandler;
import com.administrator.zxing.decoding.InactivityTimer;
import com.administrator.zxing.decoding.RGBLuminanceSource;
import com.administrator.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 扫一扫界面
 *
 * @author zhangguoyu
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Button cancelScanButton;

    int ifOpenLight = 0; // 判断是否开启闪光灯

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        // ViewUtil.addTopView(getApplicationContext(), this,
        // R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		surfaceView.getHolder().setFixedSize(176, 144);
//		surfaceView .getHolder().setKeepScreenOn(true);//使屏幕保持高亮状态
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        // quit the scan view
        cancelScanButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode 获取结果
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
//		playBeepSoundAndVibrate();
        String resultString = result.getText();
        // FIXME
        if (resultString.equals("")) {
            ToastUtil.showToast(CaptureActivity.this,"扫描失败");
//            Toast.makeText(CaptureActivity.this, "扫描失败!", Toast.LENGTH_SHORT)
//                    .show();
        } else {
            LogUtils.d("xu_qr", resultString);
            if (resultString.startsWith(Constant.SHARE_URL+"type=6&id=")) {
                String id = resultString.substring(resultString.lastIndexOf("=") + 1);
                LogUtils.d("xu_id",id + ":" + resultString);
                if (!"".equals(id)) {
                    if (BaseApplication.member_id != null && BaseApplication.member_id.equals(id)) {
                        ToastUtil.showToast(getApplicationContext(), "扫描的是自己账户的二维码");
//                        Toast.makeText(getApplicationContext(), "扫描的是自己账户的二维码", Toast.LENGTH_SHORT).show();
                        handler.restartPreviewAndDecode();
                    } else {

                        // System.out.println("Result:"+resultString);
                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("result", resultString.substring(resultString.lastIndexOf("=") + 1));
                        resultIntent.putExtras(bundle);
                        this.setResult(Constant.SCAN, resultIntent);
                        CaptureActivity.this.finish();
                    }
                } else {
                    handler.restartPreviewAndDecode();
                    ToastUtil.showToast(getApplicationContext(), "您扫描的二维码不正确，请选择正确的二维码扫描");
//                    Toast.makeText(getApplicationContext(), "您扫描的二维码不正确，请选择正确的二维码扫描", Toast.LENGTH_SHORT).show();
                }
            } else {
                handler.restartPreviewAndDecode();
                ToastUtil.showToast(getApplicationContext(), "您扫描的二维码不正确，请选择华夏e生活生成的二维码进行扫描");
//                Toast.makeText(getApplicationContext(), "您扫描的二维码不正确，请选择易联社区生成的二维码进行扫描", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * 获取带二维码的相片进行扫描
     */
    public void pickPictureFromAblum(View v) {
        // 打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        this.startActivityForResult(wrapperIntent, 1);
    }

    String photo_path;
    ProgressDialog mProgress;
    Bitmap scanBitmap;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent) 对相册获取的结果进行分析
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(),
                            null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));

                        LogUtils.i("路径", photo_path);
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = 1;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = 2;
                                m.obj = "Scan failed!";
                                mHandler.sendMessage(m);
                            }

                        }
                    }).start();
                    break;

                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class MyHandler extends Handler {
        private WeakReference<CaptureActivity> mActivity;

        public MyHandler(CaptureActivity activity) {
            mActivity = new WeakReference<CaptureActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CaptureActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        mProgress.dismiss();
                        String resultString = msg.obj.toString();
                        if (resultString.equals("")) {
                            ToastUtil.showToast(CaptureActivity.this,"扫描失败");
//                            Toast.makeText(CaptureActivity.this, "扫描失败!",
//                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // System.out.println("Result:"+resultString);
                            LogUtils.d("xu_scan", resultString);
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("result", resultString);
                            resultIntent.putExtras(bundle);
                            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                        }
                        CaptureActivity.this.finish();
                        break;

                    case 2:
                        mProgress.dismiss();
                        ToastUtil.showToast(CaptureActivity.this,"解析错误");
//                        Toast.makeText(CaptureActivity.this, "解析错误！", Toast.LENGTH_LONG)
//                                .show();

                        break;
                    default:
                        break;
                }

            }
        }
    }

    final MyHandler mHandler = new MyHandler(this);

    /**
     * 扫描二维码图片的方法
     * <p/>
     * 目前识别度不高，有待改进
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 100);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 是否开启闪光灯
    public void IfOpenLight(View v) {
        ifOpenLight++;

        switch (ifOpenLight % 2) {
            case 0:
                // 关闭
                CameraManager.get().closeLight();
                break;

            case 1:
                // 打开
                CameraManager.get().openLight(); // 开闪光灯
                break;
            default:
                break;
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}