package com.administrator.elwj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.CommunityList;
import com.administrator.bean.Constant;
import com.administrator.bean.UserInfo;
import com.administrator.utils.FileStoreUtil;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageItem;
import com.king.photo.util.Res;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cz.msebera.android.httpclient.Header;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 编辑个人信息页面
 * wujian
 */
public class EditUserInfoActivity extends AppCompatActivity implements OnWheelChangedListener {
    private TextView mLoginName;
    private EditText mNickName;
    private ImageView head;
    private TextView mSex;
    private TextView shequ;
    private TextView mRegion;
    private EditText mBrief;
    private TextView mBirthday;
    private EditText mEmail;
    private TextView mRegisterTime;
    private TextView mSaveButton;
    private UserInfo userInfo;
    private Calendar calendar;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private CharSequence[] sexItems = new CharSequence[]{"男", "女"};
    private String[] regionItems;
    private String[] regionItems_id;
    private int mYear;
    private int mMonth;
    private int mDay;
    private File file;
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    private ArrayList<ImageItem> list;
    private double sizeFileaFinal;
    private Bitmap bmap;
    private WheelView mViewDistrict;
    private WheelView mViewStreet;
    private WheelView mViewCommunity;
    private Dialog dialog_district;
    private static TextView tv_belong_commmunity;
    protected String mCurrentProviceName;
    private static final int GET_COMMUNITY = 1;
    private static final int GET_ADDRESS = 2;
    private String id;
    private int pos;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    private boolean shouldUpdateHomePageActivity = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EditUserInfo Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.administrator.elwj/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EditUserInfo Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.administrator.elwj/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    public static class MyHandler extends Handler {

        private WeakReference<EditUserInfoActivity> mActivity;

        public MyHandler(EditUserInfoActivity activity) {
            mActivity = new WeakReference<EditUserInfoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final EditUserInfoActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                JSONObject jsonObject = null;
                LogUtils.i("wj", "编辑信息" + json);
                switch (msg.what) {
                    case Constant.SAVEUSERINFO:
                        //保存用户信息的反馈
                        RequestParams params = new RequestParams();
                        params.put("address", activity.userInfo.getAddress());
                        params.put("city", activity.userInfo.getCity());
                        params.put("city_id", activity.userInfo.getCity_id());
                        params.put("email", activity.userInfo.getEmail());
                        if (activity.file == null) {
                            params.put("file", ""); // Upload a File

                        } else {
                            try {
                                params.put("file", activity.file); // Upload a File
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        params.put("mobile", activity.userInfo.getMobile());
                        params.put("mybirthday", activity.mBirthday.getText().toString().trim());
                        params.put("province", activity.userInfo.getProvince());
                        params.put("province_id", activity.userInfo.getProvince_id() + "");

                        params.put("region", activity.userInfo.getRegion());
                        params.put("region_id", activity.userInfo.getRegion_id() + "");
                        params.put("sex", activity.userInfo.getSex() + "");
                        params.put("tel", activity.userInfo.getTel());
                        params.put("truename", activity.userInfo.getName());
                        params.put("zip", activity.userInfo.getZip());
                        params.put("communityid", activity.userInfo.getCommunity_id());


                        AsyncHttpClient client = new AsyncHttpClient();
//                        client.setTimeout(10000);
                        //http://192.168.1.114:8088/api/base/upload-image.do
//                        CookieManager cookieManager = CookieManager.getInstance();
//                        String cookie = cookieManager.getCookie(Constant.baseUrl + Constant.saveUserInfo);


//                        BasicClientCookie newCookie = new BasicClientCookie("Cookie", cookie);
////                        newCookie.setVersionCode(1);
////                        newCookie.setDomain("mydomain.com");
////                        newCookie.setPath("/");
//                        myCookieStore.addCookie(newCookie);
                        CookieManager cookieManager = CookieManager.getInstance();
                        String cookie = cookieManager.getCookie(Constant.baseUrl + Constant.saveUserInfo);
//                       PersistentCookieStore myCookieStore = new PersistentCookieStore(activity);
//                        client.setCookieStore(myCookieStore);
                        client.addHeader("Cookie", cookie);
                        client.post(Constant.baseUrl + Constant.saveUserInfo, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                                int result = response.optInt("result");
                                String message = response.optString("message");
                                ToastUtil.showToast(activity, message);
//                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                Bimp.tempSelectBitmap.clear();
                                Bimp.max = 0;
                                if (result == 1) {
                                    activity.mSaveButton.setVisibility(View.GONE);
                                    activity.shouldUpdateHomePageActivity = true;
//                                    activity.finish();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });

                        break;
                    case GET_ADDRESS:
                        //获取地区的反馈
                        try {
                            jsonObject = new JSONObject(json);

                            if (jsonObject.getInt("result") == 1) {
                                Gson gson = new Gson();
                                String data = jsonObject.getString("data");
                                CommunityList communityLists = gson.fromJson(data, CommunityList.class);
                                activity.mCurrentProviceName = communityLists.getProvince();
                                activity.mCurrentCityName = communityLists.getCity();
                                activity.mCurrentDistrictName = communityLists.getCounty();
                                activity.mRegion.setText(activity.mCurrentProviceName + "-" + activity.mCurrentCityName + "-" + activity.mCurrentDistrictName);
                                VolleyUtils.NetUtils(((BaseApplication) activity.getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, new String[]{"province", "city", "county"}, new String[]{activity.mCurrentProviceName, activity.mCurrentCityName, activity.mCurrentDistrictName}, activity.handler, GET_COMMUNITY);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constant.GET_USERINFO:
                        LogUtils.d("xu_info", json);
                        break;
                    case GET_COMMUNITY:
                        try {
                            jsonObject = new JSONObject(json);
                            if (jsonObject.getInt("result") == 1) {
                                Gson gson = new Gson();
                                String data = jsonObject.getString("data");
                                List<CommunityList> communityLists = gson.fromJson(data, new TypeToken<List<CommunityList>>() {
                                }.getType());
                                if (communityLists != null && communityLists.size() > 0) {
                                    activity.regionItems_id = new String[communityLists.size()];
                                    activity.regionItems = new String[communityLists.size()];
                                    activity.shequ.setText(communityLists.get(0).getCommunity_name());
                                    for (int i = 0; i < communityLists.size(); i++) {
                                        activity.regionItems[i] = communityLists.get(i).getCommunity_name();
                                        activity.regionItems_id[i] = communityLists.get(i).getCommunity_id();
                                    }
                                    activity.setCommunity();
                                } else {
                                    activity.regionItems = null;
                                    activity.regionItems_id = null;
                                    activity.shequ.setText("");
                                }
                            } else {
                                ToastUtil.showToast(activity, jsonObject.getString("message"));
//                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        Res.init(getApplication());


        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        initData();
        initView();
//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.saveUserInfo, parameter, params, handler, 0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        calendar = Calendar.getInstance();

        RelativeLayout head_RelativeLayout = (RelativeLayout) findViewById(R.id.user_head_relativeLayout);
        head_RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                Intent intent = new Intent(EditUserInfoActivity.this,
                        AlbumActivity.class);
                intent.putExtra("maxNum", "1");
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });
        head = (ImageView) findViewById(R.id.user_head);


        ImageButton mBackButton = (ImageButton) findViewById(R.id.edit_userinfo_back);
        mLoginName = (TextView) findViewById(R.id.edit_userinfo_loginname);
        mNickName = (EditText) findViewById(R.id.edit_userinfo_nickname);
        mSex = (TextView) findViewById(R.id.edit_userinfo_sex);
        mRegion = (TextView) findViewById(R.id.edit_userinfo_location);
        mBrief = (EditText) findViewById(R.id.edit_userinfo_brief);
        TextView mComanyInfo = (TextView) findViewById(R.id.edit_userinfo_company);
        TextView mSchoolInfo = (TextView) findViewById(R.id.edit_userinfo_school);
        mBirthday = (TextView) findViewById(R.id.edit_userinfo_birsday);
        mEmail = (EditText) findViewById(R.id.edit_userinfo_email);
        mRegisterTime = (TextView) findViewById(R.id.edit_userinfo_register);
        mSaveButton = (TextView) findViewById(R.id.edit_userinfo_save);

        shequ = (TextView) findViewById(R.id.edit_userinfo);
        shequ.setOnClickListener(new View.OnClickListener() {//社区
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditUserInfoActivity.this).setTitle("选择社区").setItems(regionItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        pos = position;
                        shequ.setText(regionItems[position]);
                        dialog.dismiss();
                        mSaveButton.setVisibility(View.VISIBLE);
                    }
                }).show();

            }
        });

        mRegion.setOnClickListener(new View.OnClickListener() {//所在地
            @Override
            public void onClick(View v) {
                initProvinceDialog();
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldUpdateHomePageActivity) {
                    EditUserInfoActivity.this.setResult(RESULT_OK);
                }

                finish();
            }
        });

        mLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });
        mNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);
                    ToastUtil.showToast(EditUserInfoActivity.this, "昵称最多输入10个字");
//                    Toast.makeText(EditUserInfoActivity.this, "昵称最多输入10个字",
//                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });
        mSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditUserInfoActivity.this).setTitle("选择性别").setItems(sexItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        mSex.setText(sexItems[position]);
                        userInfo.setSex(position + 1);//更改用户信息实体
                        dialog.dismiss();
                        mSaveButton.setVisibility(View.VISIBLE);
                    }
                }).show();
            }
        });

        mBrief.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });
        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                new DatePickerDialog(EditUserInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        //更新EditText控件日期 小于10加0
                        mBirthday.setText(new StringBuilder().append(mYear).append("-")
                                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                .append("-")
                                .append((mDay < 10) ? "0" + mDay : mDay));
                        mSaveButton.setVisibility(View.VISIBLE);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSaveButton.setVisibility(View.VISIBLE);
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传保存数据
                try {
                    if (shequ.getText().equals("")) {
                        ToastUtil.showToast(EditUserInfoActivity.this, "请选择社区");
//                        Toast.makeText(EditUserInfoActivity.this, "请选择社区",
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        prepareParams();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        if (userInfo != null) {
            showInfo();


            VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getAddress, new String[]{"community_id"}, new String[]{userInfo.getCommunity_id()}, handler, GET_ADDRESS);
        }
    }

    private void initData() {
        BaseApplication appContext = (BaseApplication) getApplication();
        userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.saveUserInfo, parameter, params, handler, 0);
//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getMyRegion,new String[]{"regionid"},new String[]{"1000"},handler,Constant.MYREGION);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (shouldUpdateHomePageActivity)
                EditUserInfoActivity.this.setResult(RESULT_OK);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 用于显示用户的基本信息
     */
    private void showInfo() {
        mCurrentProviceName = userInfo.getProvince();
        mCurrentCityName = userInfo.getCity();
        mCurrentDistrictName = userInfo.getRegion();
        if (mCurrentProviceName.equals("") || mCurrentCityName.equals("") || mCurrentDistrictName.equals("")) {

            VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getAddress, new String[]{"community_id"}, new String[]{userInfo.getCommunity_id()}, handler, GET_ADDRESS);

        } else {
            mRegion.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
        }

        imageLoader.displayImage(userInfo.getFace(), head, options);
        mLoginName.setText(userInfo.getUname());
        mNickName.setText(userInfo.getName());
        mSex.setText(userInfo.getSex() == 1 ? "男" : "女");
        mBrief.setText(userInfo.getRemark());
        Date date = new Date(Long.parseLong(userInfo.getBirthday() + "000"));
        String birthdayTime = sdFormatter.format(date);
        mBirthday.setText(birthdayTime);
        mEmail.setText(userInfo.getEmail());

        date = new Date(Long.parseLong(userInfo.getRegtime() + ""));
        String registerTime = sdFormatter.format(date);
        mRegisterTime.setText(registerTime);
        mSaveButton.setVisibility(View.GONE);
    }

    /**
     * <p>准备上传保存的参数</p>
     */
    private void prepareParams() throws IOException {
        userInfo.setName(mNickName.getText().toString().trim());
        userInfo.setEmail(mEmail.getText().toString().trim());
        userInfo.setSex(mSex.getText().toString().equals("男") ? 1 : 0);
        userInfo.setInfo_full(mBrief.getText().toString());
        userInfo.setProvince(mCurrentProviceName);
        userInfo.setCity(mCurrentCityName);
        userInfo.setRegion(mCurrentDistrictName);
        String bbb = get_id();
        userInfo.setCommunity_id(get_id());

        LogUtils.e("wj", userInfo.getEmail());
        userInfo.setNickname(mNickName.getText().toString().trim());
        Message message = Message.obtain();
        message.obj = "";
        message.what = Constant.SAVEUSERINFO;
        handler.sendMessage(message);

    }


    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    private Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        saveBitmapFile(bitmap2);
        return bitmap2;
    }

    public String get_id() {

        for (int i = 0; i < regionItems.length; i++) {
            if (shequ.getText().equals(regionItems[i])) {
                id = regionItems_id[i];
            }
        }
        return id;
    }

    public void setCommunity() {

        for (int i = 0; i < regionItems_id.length; i++) {
            if (userInfo.getCommunity_id().equals(regionItems_id[i])) {
                shequ.setText(regionItems[i]);
            }
        }
    }

    public void saveBitmapFile(Bitmap bitmap) {
        File newDirectory = FileStoreUtil.getStorageDirectory(this, "head_pic");
        if (newDirectory != null) {
            File newPic = new File(newDirectory, "new_head.jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(newPic);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                list.get(0).setImagePath(newPic.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void initProvinceDialog() {//省市区选择
        View view = LayoutInflater.from(EditUserInfoActivity.this).inflate(R.layout.dialog_province, null);
        TextView tv_dialog_name = (TextView) view.findViewById(R.id.tv_dialogprovince_title);
        tv_dialog_name.setText("请选择所在社区");
        mViewDistrict = (WheelView) view.findViewById(R.id.id_province);
        mViewStreet = (WheelView) view.findViewById(R.id.id_city);
        mViewCommunity = (WheelView) view.findViewById(R.id.id_district);
        Button mBtnConfirmPro = (Button) view.findViewById(R.id.btn_confirm);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加change事件
        mViewStreet.addChangingListener(this);
        // 添加change事件
        mViewCommunity.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirmPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_district.cancel();
                mRegion.setText(mCurrentProviceName + "-" + mCurrentCityName + "-" + mCurrentDistrictName);
//                VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, null,null, handler, GET_COMMUNITY);
                VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.searchCommunity, new String[]{"province", "city", "county"}, new String[]{mCurrentProviceName, mCurrentCityName, mCurrentDistrictName}, handler, GET_COMMUNITY);
            }
        });
        initProvinceDatas();
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(EditUserInfoActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewDistrict.setVisibleItems(7);
        mViewStreet.setVisibleItems(7);
        mViewCommunity.setVisibleItems(7);
        updateCities();
        updateAreas();
        dialog_district = new Dialog(EditUserInfoActivity.this);
        dialog_district.setContentView(view);
        dialog_district.show();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewStreet.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewCommunity.setViewAdapter(new ArrayWheelAdapter<String>(EditUserInfoActivity.this, areas));
        mViewCommunity.setCurrentItem(0);
        mCurrentDistrictName = areas[0];


    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewDistrict.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(EditUserInfoActivity.this, cities));
        mViewStreet.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = EditUserInfoActivity.this.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewDistrict) {
            updateCities();
        } else if (wheel == mViewStreet) {
            updateAreas();
        } else if (wheel == mViewCommunity) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    if (Bimp.tempSelectBitmap.size() >= 1) {
                        list = Bimp.tempSelectBitmap;
                        if (list.size() != 0) {
                            File file = new File(list.get(0).getImagePath());
                            Intent intent = new Intent();
                            intent.setAction("com.android.camera.action.CROP");
                            intent.setDataAndType(Uri.fromFile(file), "image/*");// mUri是已经选择的图片Uri
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);// 裁剪框比例
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 150);// 输出图片大小
                            intent.putExtra("outputY", 150);
                            intent.putExtra("return-data", true);
                            EditUserInfoActivity.this.startActivityForResult(intent, 200);
                        }
                    }
                }
//                if(AlbumActivity.instance != null){
//                    AlbumActivity.instance.finish();
//                    AlbumActivity.instance =  null;
//                }
                break;
            case 200:
                if (data != null) {
                    bmap = data.getParcelableExtra("data");
                    if (bmap == null) {
                    } else {
                        saveBitmapFile(bmap);
                        try {
                            FileInputStream fis = new FileInputStream(list.get(0).getImagePath());
                            fis.available();
                            double sizeFile = FormetFileSize(fis.available(), SIZETYPE_KB);
                            if (sizeFile > 200) {
                                new AlertDialog.Builder(EditUserInfoActivity.this).setMessage("头像文件应小于200KB，是否将原图压缩？").setPositiveButton("压缩", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        compressImage(bmap);
                                        FileInputStream fisa = null;
                                        try {
                                            fisa = new FileInputStream(list.get(0).getImagePath());
                                            fisa.available();
                                            sizeFileaFinal = FormetFileSize(fisa.available(), SIZETYPE_KB);
                                            if (sizeFileaFinal < 200) {
                                                ToastUtil.showToast(EditUserInfoActivity.this, "压缩成功可以保存头像");
//                                                Toast.makeText(EditUserInfoActivity.this, "压缩成功可以保存头像" + sizeFileaFinal + "KB", Toast.LENGTH_LONG).show();
                                                file = new File(list.get(0).getImagePath());
                                                head.setImageBitmap(bmap);
                                            } else {
                                                ToastUtil.showToast(EditUserInfoActivity.this, "压缩失败，请选择小于200KB的图片");
//                                                Toast.makeText(EditUserInfoActivity.this, "压缩失败，请选择小于200KB的图片", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).setNegativeButton("取消", null).show();

                            } else {
                                head.setImageBitmap(bmap);
                                file = new File(list.get(0).getImagePath());
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
        }

    }

    public void onDestroy() {
        if (bmap != null && !bmap.isRecycled()) {
            // 回收并且置为null
            bmap.recycle();
            bmap = null;
        }
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mainMapView. onResume ()，实现地图生命周期管理

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mainMapView. onPause ()，实现地图生命周期管理

    }


}
