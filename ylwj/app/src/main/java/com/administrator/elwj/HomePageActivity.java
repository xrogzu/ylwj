package com.administrator.elwj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterHomePage;
import com.administrator.adapter.ListAdapterNearbyHome;
import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.bean.UserInfo;
import com.administrator.bean.UserInfoExtra;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人主页页面
 */
public class HomePageActivity extends AppCompatActivity {
    private XListView mContentListView;


    private ImageView mFaceImageView;//头像
    private TextView mNickNameView;//昵称
    private TextView mFollowsView;//关注
    private ImageView mSexView;//性别图标
    private TextView mFansView;//粉丝
    private TextView mBriefView;//简介
    static final int GET_USERINFO_EXTRA = 0;
    private UserInfo userInfo;
    private int curPage = 1;//当前页
    private int size = 10;
    private List<Novelty> novelties = new ArrayList<>();//存放新鲜事
    private String member_id;
    private BaseApplication appContext;
    private ListAdapterNearbyHome adapterNearByHome;
    private ImageLoader imageLoader;
    private DisplayImageOptions optionhead;

    private ImageView attention;
    private static final int DEL_ID = 1;

    public static class MyHandler extends Handler {
        private WeakReference<HomePageActivity> mActivity;

        public MyHandler(HomePageActivity activity) {
            mActivity = new WeakReference<HomePageActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HomePageActivity activity = mActivity.get();
            if (activity != null) {

                String json = (String) msg.obj;
                JSONObject jsonObject = null;

                LogUtils.i("wj", "我的主页" + json);
                switch (msg.what) {
                    case Constant.GET_USERINFO:
                        try {
                            jsonObject = new JSONObject(json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                String message = jsonObject.optString("message");
                                ToastUtil.showToast(activity, message);
//                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                LogUtils.e("wj", message);
                            } else if (result == 1) {
                                String data = jsonObject.optString("data");
                                Gson gson = new Gson();
                                activity.userInfo = gson.fromJson(data, UserInfo.class);

                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.AttentionMessage, new String[]{"member_id"}, new String[]{activity.member_id,}, activity.handler, GET_USERINFO_EXTRA);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.My_NOVELTIES:
                        LogUtils.d("xu_Home", "my_novelty");
                        if (activity.mContentListView != null) {
                            activity.mContentListView.stopRefresh();
                            activity.mContentListView.stopLoadMore();
                        }
                        try {
                            jsonObject = new JSONObject(json);
                            JSONArray array = null;
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                String message = jsonObject.optString("message");
                                ToastUtil.showToast(activity, message);
                                LogUtils.e("wj", message);
                            } else if (result == 1) {
                                Gson gson = new Gson();
                                array = jsonObject.optJSONArray("data");
                                if (array != null) {
                                    if (activity.curPage == 1) {
                                        activity.novelties.clear();
                                        activity.adapterNearByHome.clear();
                                    }
                                    int n = array.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; i++) {
                                            String object = array.getString(i);
                                            Novelty novelty = gson.fromJson(object, Novelty.class);
                                            activity.novelties.add(novelty);
                                        }
                                        if (activity.adapterNearByHome != null) {
                                            activity.adapterNearByHome.addData(activity.novelties, activity);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_USERINFO_EXTRA:
                        Gson gson = new Gson();
                        UserInfoExtra userInfoExtra = gson.fromJson(json, UserInfoExtra.class);
                        if (userInfoExtra != null && userInfoExtra.getResult() == 1) {
                            UserInfoExtra.DataEntity dataEntity = userInfoExtra.getData();
                            if (dataEntity != null) {
                                String fans = dataEntity.getAttention_num();//粉丝
                                String attend = dataEntity.getAttentionedNum();//关注
                                activity.showUserInfo(attend, fans);

                            } else {
                                ToastUtil.showToast(activity, "获取失败");
                            }
                        } else {
                            ToastUtil.showToast(activity, "获取失败");
                        }
                        break;
                    case Constant.IS_ATTENTION:
                        try {
                            jsonObject = new JSONObject(json);
                            int result = jsonObject.optInt("result");
                            String message = jsonObject.getString("message");
                            if (!message.equals("用户未登录")) {
                                if (Integer.valueOf(message) == 1) {//已关注过
                                    activity.attention.setBackgroundResource(R.mipmap.cancel_attention);
                                } else if (Integer.valueOf(message) == 0) {
                                    activity.attention.setBackgroundResource(R.mipmap.attention);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constant.ATTENTION:
                        try {
                            jsonObject = new JSONObject(json);
                            int result = jsonObject.optInt("result");
                            String message = jsonObject.getString("message");

                            if (result == 0) {//已关注过
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.deleteAttentionPerson, new String[]{"member_id"}, new String[]{activity.member_id}, activity.handler, DEL_ID);
                            } else if (result == 1) {
                                activity.attention.setBackgroundResource(R.mipmap.cancel_attention);
                                ToastUtil.showToast(activity, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case DEL_ID:
                        boolean isDelSuccess = false;
                        if (json != null) {
                            try {
                                jsonObject = new JSONObject(json);
                                if (1 == Integer.parseInt(String.valueOf(jsonObject.get("result")))) {
                                    isDelSuccess = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (isDelSuccess) {
                            activity.attention.setBackgroundResource(R.mipmap.attention);
                            ToastUtil.showToast(activity, "已取消关注");
                        } else {
                            ToastUtil.showToast(activity,"取消关注失败");
                        }
                        break;
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    /**
     * <p>用户显示用户的基本信息</p>
     */
    private void showUserInfo(String attent, String fans) {
        imageLoader.displayImage(userInfo.getFace(), mFaceImageView, optionhead);
        mNickNameView.setText(userInfo.getName());
        mFollowsView.setText("关注：" + attent);
        mFansView.setText("粉丝：" + fans);
        if (userInfo.getSex() == 1) {
            mSexView.setImageResource(R.mipmap.wd_icon_man);
        } else if (userInfo.getSex() == 0) {
            mSexView.setImageResource(R.mipmap.wd_icon_woman);
        }
        if (userInfo.getRemark().equals("")) {
            mBriefView.setVisibility(View.GONE);
        } else {
            mBriefView.setText("简介" + userInfo.getRemark());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        member_id = intent.getStringExtra("member_id");
        initData();
        initView();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, new String[]{"member_id"}, new String[]{member_id}, handler, Constant.GET_USERINFO);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_SomeOne, new String[]{"page", "pageSize", "memberId"}, new String[]{curPage + "", size + "", member_id}, handler, Constant.My_NOVELTIES);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        appContext = (BaseApplication) getApplication();
        initImageLoaderParams();

    }

    private void initView() {

        ImageButton mBackButton = (ImageButton) findViewById(R.id.homepage_back_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                setResult(Constant.REFRESH, resultIntent);
                finish();
            }
        });
        ImageButton mEditButton = (ImageButton) findViewById(R.id.homepage_edit_btn);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, EditUserInfoActivity.class);
                if (userInfo != null) {
                    intent.putExtra("userinfo", userInfo);
                }
                startActivityForResult(intent, Constant.EDIT_MY_INFO);
            }
        });


        mContentListView = (XListView) findViewById(R.id.homepage_listview);
        mContentListView.setPullLoadEnable(true);
        mContentListView.setPullRefreshEnable(true);
        mContentListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_SomeOne, new String[]{"page", "pageSize", "memberId"}, new String[]{curPage + "", size + "", member_id}, handler, Constant.My_NOVELTIES);

            }

            @Override
            public void onLoadMore() {
                if (novelties != null && novelties.size() < curPage * size) {
                    mContentListView.stopLoadMore();
                    ToastUtil.showToast(HomePageActivity.this,"没有更多");
                } else {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_SomeOne, new String[]{"page", "pageSize", "memberId"}, new String[]{curPage + "", size + "", member_id}, handler, Constant.My_NOVELTIES);
                }
            }
        });
        mFaceImageView = (ImageView) findViewById(R.id.homepage_faceimage);
        mNickNameView = (TextView) findViewById(R.id.homepage_nickname);
        mFollowsView = (TextView) findViewById(R.id.homepage_follow_tv);
        mSexView = (ImageView) findViewById(R.id.homepage_sex_iv);
        mFansView = (TextView) findViewById(R.id.homepage_fans);
        mBriefView = (TextView) findViewById(R.id.homepage_brief_tv);
        attention = (ImageView) findViewById(R.id.attention_go);
        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.Attention, new String[]{"member_id"}, new String[]{String.valueOf(member_id)}, handler, Constant.ATTENTION);
            }
        });
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getIsAttention, new String[]{"member_id"}, new String[]{String.valueOf(member_id)}, handler, Constant.IS_ATTENTION);
        if (BaseApplication.member_id != null && !BaseApplication.member_id.equals(member_id)) {
            mEditButton.setVisibility(View.INVISIBLE);
        } else {
            attention.setVisibility(View.GONE);
        }

        adapterNearByHome = new ListAdapterNearbyHome(this, appContext, this);
        mContentListView.setAdapter(adapterNearByHome);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case Constant.EDIT_MY_INFO:
                if (resultCode == RESULT_OK) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, new String[]{"member_id"}, new String[]{member_id}, handler, Constant.GET_USERINFO);
                    curPage = 1;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_SomeOne, new String[]{"page", "pageSize", "memberId"}, new String[]{curPage + "", size + "", member_id}, handler, Constant.My_NOVELTIES);
                    LogUtils.d("xu_Home", "onActivityResult");
                }
                break;
        }
    }

    private void initImageLoaderParams() {
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }


    /*
需要更新新鲜事
 */
    public void updateNovelty() {
        curPage = 1;
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_SomeOne, new String[]{"page", "pageSize", "memberId"}, new String[]{curPage + "", size + "", member_id}, handler, Constant.My_NOVELTIES);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent resultIntent = new Intent();
            setResult(Constant.REFRESH, resultIntent);
            finish();

        }

        return true;
    }

    public View getMainView() {
        return findViewById(R.id.main);
    }


}
