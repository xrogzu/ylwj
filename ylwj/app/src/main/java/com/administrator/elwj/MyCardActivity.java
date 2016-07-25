package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.bean.UserInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * 我的名片界面
 */
public class MyCardActivity extends AppCompatActivity implements View.OnClickListener{
    private UserInfo userInfo;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小
    private Bitmap bm;
    protected int mScreenWidth ;
    private static final int BLACK = 0xff000000;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_card);
        BaseApplication appContext = (BaseApplication) getApplication();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("user_info");

        initViews();
    }

    private void initViews() {
        ImageButton ibShopCar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        ibShopCar.setVisibility(View.GONE);
        TextView address = (TextView) findViewById(R.id.scan_address);
        ImageView mSexView = (ImageView) findViewById(R.id.scan_homepage_sex_iv);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("我的名片");
        title.setTextColor(ContextCompat.getColor(this, R.color.black));
        ImageButton back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(this);

        ImageView head = (ImageView) findViewById(R.id.my_card_head);
        TextView name = (TextView) findViewById(R.id.my_card_name);
        if(userInfo != null){
            imageLoader.displayImage(userInfo.getFace(), head, options);
            name.setText(userInfo.getName());

            if(userInfo.getCity()!=null&&userInfo.getProvince()!=null){
                address.setText(userInfo.getProvince() + " " + userInfo.getCity() + " " + userInfo.getRegion());
            }else{
                address.setText("");

            }

            if (userInfo.getSex() == 1) {
                mSexView.setImageResource(R.mipmap.wd_icon_man);
            } else if (userInfo.getSex() == 2) {
                mSexView.setImageResource(R.mipmap.wd_icon_woman);
            }

            String namea=userInfo.getName();
            String headPic=userInfo.getFace();
            int member_id=userInfo.getMember_id();
            Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.mipmap.logo);

            JSONObject object=new JSONObject();
            String contents = Constant.SHARE_URL + "type=6&id=" + userInfo.getMember_id();
            try {
                //调用方法createCode生成二维码
                bm=createCode(contents, logo, BarcodeFormat.QR_CODE);
//                bm = createCode(contents, mScreenWidth);
            }catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ImageView qr_Code = (ImageView) findViewById(R.id.my_card_qr);
        qr_Code.setImageBitmap(bm);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://
                finish();
                break;
        }
    }

    /**
     　　* 生成二维码
     　　* @param string 二维码中包含的文本信息
     　　* @param mBitmap logo图片
     　　* @param format 编码格式
     　　* @return Bitmap 位图
     　　* @throws WriterException
     　　*/
    public Bitmap createCode(String string,Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hst = new Hashtable();
        hst.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 500, 500, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH); } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


//    /**
//     * 生成一个二维码图像
//     *
//     * @param str
//     *            传入的字符串，通常是一个URL
//     * @param widthAndHeight
//     *           图像的宽高
//     * @return
//     */
//    public static Bitmap createQRCode(String str, int widthAndHeight)
//            throws WriterException {
//        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        BitMatrix matrix = new MultiFormatWriter().encode(str,
//                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//        int[] pixels = new int[width * height];
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = BLACK;
//                }
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }
    public void onDestroy() {
        bm.recycle();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
