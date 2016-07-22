package com.king.photo.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.InvitePeopleActivity;
import com.administrator.elwj.R;
import com.administrator.utils.DateUtils;
import com.administrator.utils.LocationUtil;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.amap.api.location.AMapLocation;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageItem;
import com.king.photo.util.Res;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import cz.msebera.android.httpclient.Header;


/**
 * 首页面activity
 *
 * @author king
 * @version 2014年10月18日  下午11:48:34
 *          上传图片
 * @QQ:595163260
 */
public class StartPickPhotoActivity extends AppCompatActivity implements LocationUtil.MyLocationListener {


    private LocalBroadcastManager mLocalBroadcastManager;
    public static final String MESSAGE_ACTION = "org.feng.message_ACTION";

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private ImageButton ib_back;
    private Button bt_review;
    private TextView tv_title;
    private Button bt_next;
    private TextView fabu;
    private EditText content;
    private LinearLayout next;
//    public static StartPickPhotoActivity instance = null;
    private List<String> imgList = new ArrayList<>();
    private String activity_id;
    private BaseApplication appContext;
    private String contentText = "";
    //type为0表示一般活动，type为1表示公共活动，type为2表示身边发布新鲜事
    private String type;
    private TextView id1;
    private int mImageUploadCount = 0;
    private ProgressDialog mProgressDialog;
    private int ImageCountSuccess;
    private int ImageCountFailed;
    private double coordinate_x;
    private double coordinate_y;
    private String coordinate;
    private boolean isNew = true;
    private int result;
    private File f;
    private int photo = 0;
    private String fileName;
    private int failedNum;

    private String ActivityContent;

    @Override
    public void onLoactionChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                coordinate_x = amapLocation.getLongitude();//获取纬度
                coordinate_y = amapLocation.getLatitude();//获取经度
                coordinate = amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict();
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                Log.d("xu_location", coordinate);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
    //	private BigStage_Activity bean;

    public static class MyHandler extends Handler {
        private WeakReference<StartPickPhotoActivity> mActivity;

        public MyHandler(StartPickPhotoActivity activity) {
            mActivity = new WeakReference<StartPickPhotoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final StartPickPhotoActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.ADD_IMGS) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int result = object.optInt("result");
                    if (result == 0) {
                        activity.ImageCountFailed++;
                        Log.e("最后一张", "333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
                        if ((activity.type.equals("0") || activity.type.equals("1")) && ((activity.mImageUploadCount + activity.ImageCountFailed) == activity.ImageCountSuccess)) {
                            if (activity.ImageCountFailed != 0) {
                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                        Toast.LENGTH_SHORT).show();
                            }
                            if (activity.type.equals("0")) {
                                Intent intent = new Intent(activity, InvitePeopleActivity.class);
                                if (activity.ActivityContent != null && !"".equals(activity.ActivityContent))
                                    intent.putExtra("content", activity.ActivityContent);
                                intent.putExtra("new", activity.isNew);
                                intent.putExtra("activity_id", activity.activity_id);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            if (activity.type.equals("1")) {
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity.activity_id}, activity.handler, Constant.UPLOAD_ACTIVTY);
                            }
                        } else if (activity.mImageUploadCount + activity.ImageCountFailed == activity.ImageCountSuccess) {
                            if (activity.ImageCountFailed != 0) {
                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
                            }
                            activity.finishThis();
                        }
                    } else {
                        activity.mImageUploadCount++;
                        Log.e("成功上传", "44444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
                        if ((activity.type.equals("0") || activity.type.equals("1")) && ((activity.mImageUploadCount + activity.ImageCountFailed) == activity.ImageCountSuccess)) {
                            if (activity.ImageCountFailed != 0) {
                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
                            }
                            if (activity.type.equals("0")) {
                                Intent intent = new Intent(activity, InvitePeopleActivity.class);
                                if (activity.ActivityContent != null && !"".equals(activity.ActivityContent))
                                    intent.putExtra("content", activity.ActivityContent);
                                intent.putExtra("new", activity.isNew);
                                intent.putExtra("activity_id", activity.activity_id);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            if (activity.type.equals("1")) {
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity.activity_id}, activity.handler, Constant.UPLOAD_ACTIVTY);
                            }
                        } else if (activity.mImageUploadCount + activity.ImageCountFailed == activity.ImageCountSuccess) {
                            if (activity.ImageCountFailed != 0) {
                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                        Toast.LENGTH_SHORT).show();
                            }
                            activity.finishThis();
                        }
                        Log.e("Main添加照片", json);
                    }
                }
                if (which == Constant.UPLOAD_ACTIVTY) {
                    Log.e("Mainr", json);
                    activity.finishThis();
                    activity.finish();
                }
                if (which == Constant.GET_ACTIVITY_ID) {
//                            Log.e("NORMAL",json);
                    JSONObject object = null;
                    try {
                        object = new JSONObject(json);
                        activity.result = object.optInt("result");
                        if (activity.result == 1) {
                            activity.activity_id = object.getString("message");
                            ArrayList<ImageItem> list = activity.isTooBig(Bimp.tempSelectBitmap);
                            activity.ImageCountSuccess = list.size();
                            activity.ImageCountFailed = 0;
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    final File file = new File(list.get(i).getImagePath());
                                    String path = list.get(i).getImagePath();
                                    RequestParams params = new RequestParams();
                                    params.put("imageFileName", "tom.jpg");
                                    params.put("subFolder", "mobile");
                                    params.put("isthumb", "1");
                                    params.put("subFolder", "mobile");
                                    try {

                                        params.put("image", new File(list.get(i).getImagePath())); // Upload a File
                                        params.put("width", Bimp.getWidth(list.get(i).getImagePath()));
                                        params.put("height", Bimp.getHight(list.get(i).getImagePath()));


                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.setTimeout(60000);
//                                        list.remove(i);
                                        //http://192.168.1.114:8088/api/base/upload-image.do
                                        client.post(Constant.baseUrl + Constant.uploadImgs, params, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                                JSONArray array = null;
                                                try {
                                                    String result = response.getString("result");
                                                    if (result.equals("0")) {
                                                        Log.e("最后一张", "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//                                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.newsAddImgs, new String[]{"photo_name", "path", "add_time", "novelty_id"}, new String[]{"美如画", "", DateUtils.format24Time(DateUtils.getCurrentTime()), activity.activity_id}, activity.handler, Constant.ADD_IMGS);
                                                        activity.ImageCountFailed++;
                                                        if (activity.type.equals("0") && ((activity.mImageUploadCount + activity.ImageCountFailed) == activity.ImageCountSuccess)) {
                                                            if (activity.ImageCountFailed != 0) {
                                                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                                                Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                            Intent intent = new Intent(activity, InvitePeopleActivity.class);
                                                            if (activity.ActivityContent != null && !"".equals(activity.ActivityContent))
                                                                intent.putExtra("content", activity.ActivityContent);
                                                            intent.putExtra("new", activity.isNew);
                                                            intent.putExtra("activity_id", activity.activity_id);
                                                            activity.startActivity(intent);
                                                            activity.finish();
                                                        } else if (activity.mImageUploadCount + activity.ImageCountFailed == activity.ImageCountSuccess) {
                                                            if (activity.ImageCountFailed != 0) {
                                                                ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                                                Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                            activity.finishThis();
                                                        }
                                                    } else {
                                                        array = response.getJSONArray("data");
                                                        JSONObject img = (JSONObject) array.get(0);
                                                        JSONObject fsimg = (JSONObject) array.get(1);
                                                        String imgPath = img.getString("img");//实际
                                                        String fsimgPath = fsimg.getString("img");
                                                        Log.d("xu", fsimgPath);
                                                        Log.d("xu", imgPath);

                                                        String path = fsimgPath + "," + imgPath;
                                                        Log.e("Main", activity.activity_id);

                                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.newsAddImgs, new String[]{"photo_name", "path", "add_time", "novelty_id"}, new String[]{"美如画", path, DateUtils.format24Time(DateUtils.getCurrentTime()), activity.activity_id}, activity.handler, Constant.ADD_IMGS);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                                activity.ImageCountFailed++;
                                                Log.e("最后一张", "2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
                                                if (activity.type.equals("0") && ((activity.mImageUploadCount + activity.ImageCountFailed) == activity.ImageCountSuccess)) {
                                                    if (activity.ImageCountFailed != 0) {
                                                        ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    Intent intent = new Intent(activity, InvitePeopleActivity.class);
                                                    if (activity.ActivityContent != null && !"".equals(activity.ActivityContent))
                                                        intent.putExtra("content", activity.ActivityContent);
                                                    intent.putExtra("new", activity.isNew);
                                                    intent.putExtra("activity_id", activity.activity_id);
                                                    activity.startActivity(intent);
                                                    activity.finish();
                                                } else if (activity.mImageUploadCount + activity.ImageCountFailed == activity.ImageCountSuccess) {
                                                    if (activity.ImageCountFailed != 0) {
                                                        ToastUtil.showToast(activity,"有" + activity.ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(activity, "有" + activity.ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    activity.finishThis();
                                                }

                                            }
                                        });
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                                Bimp.tempSelectBitmap.clear();
                                Bimp.max = 0;
                            } else {
                                activity.finishThis();
                            }
                        } else {
                            activity.finishThis();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private Handler handler = new MyHandler(this);


    private void finishThis() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        if (type.equals("1")) {
            if (result == 0) {
                ToastUtil.showToast(StartPickPhotoActivity.this,"发布成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtil.showToast(StartPickPhotoActivity.this,"发布失败");

            }
        } else {
            if (result == 1) {
                ToastUtil.showToast(StartPickPhotoActivity.this,"发布成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtil.showToast(StartPickPhotoActivity.this,"发布失败");


            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (BaseApplication) getApplication();
        Res.init(getApplicationContext());
        if(Bimp.tempSelectBitmap != null)
            Bimp.tempSelectBitmap.clear();
        Intent intent = getIntent();
        activity_id = intent.getStringExtra("activity_id");
        type = intent.getStringExtra("type");
        ActivityContent = intent.getStringExtra("content");
        isNew = intent.getBooleanExtra("new", true);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
        setContentView(parentView);
        mImageUploadCount = 0;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Init();
        initViews();
        LocationUtil locationUtil = new LocationUtil();
        locationUtil.startLocation(getApplicationContext(), this);
    }

    private void initViews() {

        id1 = (TextView) parentView.findViewById(R.id.id_1);
        ib_back = (ImageButton) parentView.findViewById(R.id.hot_ib_back);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bimp.tempSelectBitmap.size() != 0) {
                    Bimp.tempSelectBitmap.clear();
                    Bimp.max = 0;
                }
                finish();
            }
        });

        content = (EditText) parentView.findViewById(R.id.id_content);
        fabu = (TextView) parentView.findViewById(R.id.id_go);
        next = (LinearLayout) parentView.findViewById(R.id.id_next);

        tv_title = (TextView) parentView.findViewById(R.id.title);
        bt_review = (Button) parentView.findViewById(R.id.bt_review);
        bt_next = (Button) parentView.findViewById(R.id.bt_nextstep);


        if (type.equals("0")) {//一般
            content.setVisibility(View.GONE);
            fabu.setVisibility(View.GONE);
            id1.setVisibility(View.GONE);
            tv_title.setText(R.string.big_stage);
            bt_review.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            bt_next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ImageItem> list = null;
                    try {
                        list = isTooBig(Bimp.tempSelectBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list != null && list.size() > 0) {
                        ImageCountSuccess = list.size();
                        ImageCountFailed = 0;
                        StartPickPhotoActivity.this.publish();
                        AsyncHttpClient client = new AsyncHttpClient();
                        for (int i = 0; i < list.size(); i++) {

                            RequestParams params = new RequestParams();
                            params.put("imageFileName", "tom.jpg");
                            params.put("subFolder", "mobile");
                            params.put("isthumb", "1");
                            params.put("subFolder", "mobile");
                            try {

                                params.put("image", new File(list.get(i).getImagePath())); // Upload a File
                                params.put("width", Bimp.getWidth(list.get(i).getImagePath()));
                                params.put("height", Bimp.getHight(list.get(i).getImagePath()));

                                client.setTimeout(60000);
                                //http://192.168.1.114:8088/api/base/upload-image.do
                                client.post(Constant.baseUrl + Constant.uploadImgs, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

//
                                        JSONArray array = null;
                                        try {
                                            String result = response.getString("result");
                                            if (result.equals("0")) {
//                                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.newsAddImgs, new String[]{"photo_name", "path", "add_time", "novelty_id"}, new String[]{"美如画", "", DateUtils.format24Time(DateUtils.getCurrentTime()), activity.activity_id}, activity.handler, Constant.ADD_IMGS);
                                                ImageCountFailed++;
                                                Log.e(result, "55555555555555555555555555555555555555555555555555555555555555555555555555555");
                                                if (type.equals("0") && ((mImageUploadCount + ImageCountFailed) == ImageCountSuccess)) {
                                                    if (ImageCountFailed != 0) {
                                                        ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    Intent intent = new Intent(StartPickPhotoActivity.this, InvitePeopleActivity.class);
                                                    if (ActivityContent != null && !"".equals(ActivityContent))
                                                        intent.putExtra("content", ActivityContent);
                                                    intent.putExtra("new", isNew);
                                                    intent.putExtra("activity_id", activity_id);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (mImageUploadCount + ImageCountFailed == ImageCountSuccess) {
                                                    if (ImageCountFailed != 0) {
                                                        ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    finishThis();
                                                }
                                            } else {
                                                array = response.getJSONArray("data");
                                                JSONObject img = (JSONObject) array.get(0);
                                                JSONObject fsimg = (JSONObject) array.get(1);
                                                String imgPath = img.getString("img");//实际
                                                String fsimgPath = fsimg.getString("img");
                                                Log.d("xu", fsimgPath);
                                                Log.d("xu", imgPath);
                                                LogUtils.d("xu_activity", "result!=0");

                                                String path = fsimgPath + "," + imgPath;
                                                Log.e("Main", activity_id);
                                                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.newStageAddImgs, new String[]{"photo_name", "intro", "path", "activity_id"}, new String[]{"ceshi", "美如画", path, activity_id}, handler, Constant.ADD_IMGS);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                        ImageCountFailed++;
                                        Log.e("社区大舞台超时", "66666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
                                        if (type.equals("0") && ((mImageUploadCount + ImageCountFailed) == ImageCountSuccess)) {
                                            if (ImageCountFailed != 0) {
                                                ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            Intent intent = new Intent(StartPickPhotoActivity.this, InvitePeopleActivity.class);
                                            if (ActivityContent != null && !"".equals(ActivityContent))
                                                intent.putExtra("content", ActivityContent);
                                            intent.putExtra("new", isNew);
                                            intent.putExtra("activity_id", activity_id);
                                            startActivity(intent);
                                            finish();
                                        } else if (mImageUploadCount + ImageCountFailed == ImageCountSuccess) {
                                            if (ImageCountFailed != 0) {
                                                ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            finishThis();
                                        }
                                    }
                                });
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Bimp.tempSelectBitmap.clear();
                        Bimp.max = 0;
                    } else {
                        Intent intent = new Intent(StartPickPhotoActivity.this, InvitePeopleActivity.class);
                        if (ActivityContent != null && !"".equals(ActivityContent))
                            intent.putExtra("content", ActivityContent);
                        intent.putExtra("new", isNew);
                        intent.putExtra("activity_id", activity_id);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        } else if (type.equals("1")) {//公共
            content.setVisibility(View.GONE);
            fabu.setVisibility(View.GONE);
            id1.setVisibility(View.GONE);
            tv_title.setText(R.string.big_stage);
            bt_review.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            bt_next.setText("发布");
            bt_next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ImageItem> list = null;
                    try {
                        list = isTooBig(Bimp.tempSelectBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list.size() > 0) {
                        ImageCountSuccess = list.size();
                        ImageCountFailed = 0;
                        StartPickPhotoActivity.this.publish();
                        for (int i = 0; i < list.size(); i++) {

                            RequestParams params = new RequestParams();
                            params.put("imageFileName", "tom.jpg");
                            params.put("subFolder", "mobile");
                            params.put("isthumb", "1");
                            params.put("subFolder", "mobile");
                            try {

                                params.put("image", new File(list.get(i).getImagePath())); // Upload a File
                                params.put("width", Bimp.getWidth(list.get(i).getImagePath()));
                                params.put("height", Bimp.getHight(list.get(i).getImagePath()));

                                AsyncHttpClient client = new AsyncHttpClient();
                                client.setTimeout(60000);
                                //http://192.168.1.114:8088/api/base/upload-image.do
                                client.post(Constant.baseUrl + Constant.uploadImgs, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        JSONArray array = null;
                                        try {
                                            String result = response.getString("result");
                                            if (result.equals("0")) {
                                                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity_id}, handler, Constant.UPLOAD_ACTIVTY);
//                                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.newsAddImgs, new String[]{"photo_name", "path", "add_time", "novelty_id"}, new String[]{"美如画", "", DateUtils.format24Time(DateUtils.getCurrentTime()), activity.activity_id}, activity.handler, Constant.ADD_IMGS);
                                                ImageCountFailed++;
                                                Log.e(result, "55555555555555555555555555555555555555555555555555555555555555555555555555555");
                                                if (type.equals("1") && ((mImageUploadCount + ImageCountFailed) == ImageCountSuccess)) {
                                                    if (ImageCountFailed != 0) {
                                                        ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }

                                                } else if (mImageUploadCount + ImageCountFailed == ImageCountSuccess) {
                                                    if (ImageCountFailed != 0) {
                                                        ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                        Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                array = response.getJSONArray("data");
                                                JSONObject img = (JSONObject) array.get(0);
                                                JSONObject fsimg = (JSONObject) array.get(1);
                                                String imgPath = img.getString("img");//实际
                                                String fsimgPath = fsimg.getString("img");
                                                Log.d("xu", fsimgPath);
                                                Log.d("xu", imgPath);
                                                String path = fsimgPath + "," + imgPath;
                                                Log.e("Main", activity_id);
                                                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.newStageAddImgs, new String[]{"photo_name", "intro", "path", "activity_id"}, new String[]{"ceshi", "美如画", path, activity_id}, handler, Constant.ADD_IMGS);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                        ImageCountFailed++;
                                        Log.e("社区大舞台超时", "66666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
                                        if (type.equals("0") && ((mImageUploadCount + ImageCountFailed) == ImageCountSuccess)) {
                                            if (ImageCountFailed != 0) {
                                                ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity_id}, handler, Constant.UPLOAD_ACTIVTY);
                                            finish();
                                        } else if (mImageUploadCount + ImageCountFailed == ImageCountSuccess) {
                                            if (ImageCountFailed != 0) {
                                                ToastUtil.showToast(StartPickPhotoActivity.this,"有" + ImageCountFailed + "张照片上传失败");
//                                                Toast.makeText(StartPickPhotoActivity.this, "有" + ImageCountFailed + "张照片上传失败",
//                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            finishThis();
                                        }
                                    }
                                });
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Bimp.tempSelectBitmap.clear();
                        Bimp.max = 0;
                    } else {
                        //没有图片发布
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.uploadActivity, new String[]{"activity_id"}, new String[]{activity_id}, handler, Constant.UPLOAD_ACTIVTY);
                        finish();
                    }
                }
            });
        } else {
            tv_title.setText("新鲜事儿");
            id1.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            content.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    contentText = s.toString();
                    if (s.length() == 140) {
                        ToastUtil.showToast(StartPickPhotoActivity.this,"最多输入140字");
//                        Toast.makeText(StartPickPhotoActivity.this, "最多输入140字",
//                                Toast.LENGTH_SHORT).show();
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });


            fabu.setVisibility(View.VISIBLE);
            fabu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                    if (contentText.equals("")) {
                        ToastUtil.showToast(StartPickPhotoActivity.this,"请输入内容");
//                        Toast.makeText(StartPickPhotoActivity.this, "请输入内容",
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (coordinate == null) {
                                new AlertDialog.Builder(StartPickPhotoActivity.this).setMessage("还未获取到当前位置，确定要发布？").setPositiveButton("发布", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        publish();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            } else {
                                StartPickPhotoActivity.this.publish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


            next.setVisibility(View.GONE);


        }
    }

    private void publish() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        mProgressDialog = new ProgressDialog(StartPickPhotoActivity.this);
        if (type.equals("0"))
            mProgressDialog.setMessage("正在上传图片...");
        else mProgressDialog.setMessage("正在发布...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        if (coordinate == null) {
            coordinate = "青岛市市南区";
            coordinate_x = 0;
            coordinate_y = 0;
        }
        if (!(type.equals("0") || type.equals("1")))
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.newGetActivity_id, new String[]{"message_content", "message_title", "message_status", "coordinate", "coordinate_x", "coordinate_y", "like_num"}, new String[]{contentText, "", "", coordinate, String.format("%f", coordinate_x), String.format("%f", coordinate_y), "0"}, handler, Constant.GET_ACTIVITY_ID);

    }

    public void Init() {

        pop = new PopupWindow(StartPickPhotoActivity.this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        CardView cardview_1 = (CardView) view.findViewById(R.id.cardview_1);
        CardView cardview_2 = (CardView) view.findViewById(R.id.cardview_2);
        CardView cardview_3 = (CardView) view.findViewById(R.id.item_popupwindows_camera);//拍照
        cardview_3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(StartPickPhotoActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StartPickPhotoActivity.this, new String[]{android.Manifest.permission.CAMERA}, Constant.CALL_CAMERA_REQUIRE);
                } else {
                    takePhoto();
                }

            }
        });


        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.btn_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
//        bt1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                photo();
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
        cardview_1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                StartPickPhotoActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/DICM")));
                Intent intent = new Intent(StartPickPhotoActivity.this,
                        AlbumActivity.class);
                intent.putExtra("maxNum", "6");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);

                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        cardview_2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(StartPickPhotoActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(StartPickPhotoActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }

    private void takePhoto() {
        photo();
        pop.dismiss();
        ll_popup.clearAnimation();
    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 6) {
                return 6;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = String.valueOf(System.currentTimeMillis());
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg")));
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {
                    Bitmap bm = null;
                    try {
                        bm = Bimp.revitionImageSize(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    compressImage(bm, fileName);
//                    FileUtils.saveBitmap(bm, fileName);
                    bm = rotateBitmapByDegree(bm, getBitmapDegree(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg"));
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    takePhoto.setImagePath(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg");
                    Bimp.tempSelectBitmap.add(takePhoto);
                    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg")));

                }
                break;
        }
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
//
//    private Bitmap compressImage(Bitmap bitmap, String fileName) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        saveBitmapFile(bitmap2, fileName);
//        return bitmap2;
//    }
//
//    private void saveBitmapFile(Bitmap bitmap, String fileName) {
//        File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".JPEG");//将要保存图片的路径
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private ArrayList<ImageItem> isTooBig(ArrayList<ImageItem> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            FileInputStream fis = new FileInputStream(list.get(i).getImagePath());
            fis.available();
            double sizeFile = FormetFileSize(fis.available(), 3);
            Log.i("图片大小", String.valueOf(sizeFile) + "M************************************************************************");
            if (sizeFile > 1) {
                Bitmap bitmap2 = list.get(i).getBitmap();
                save(bitmap2, list, i);
            }
        }
        return list;
    }

    private void save(Bitmap bitmap, ArrayList<ImageItem> list, int postion) {
        String path = list.get(postion).getImagePath();
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

    }

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case 2:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case 3:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Bimp.tempSelectBitmap.size() != 0) {
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
            }
            finish();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.CALL_CAMERA_REQUIRE:
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto();
                }
                break;
        }
    }
}

