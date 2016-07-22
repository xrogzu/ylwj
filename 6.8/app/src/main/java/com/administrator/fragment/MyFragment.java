package com.administrator.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.bean.UserInfo;
import com.administrator.bean.UserInfoExtra;
import com.administrator.elwj.BaiduPushReceiver;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.CaptureActivity;
import com.administrator.elwj.CreditSearchActivity;
import com.administrator.elwj.DraftBoxActivity;
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.LoginActivity;
import com.administrator.elwj.MemberBindingActivity;
import com.administrator.elwj.MyActivityActivity;
import com.administrator.elwj.MyAddressActivity;
import com.administrator.elwj.MyAttentionActivity;
import com.administrator.elwj.MyCardActivity;
import com.administrator.elwj.MyCollectioinActivity;
import com.administrator.elwj.MyCreditPayActivity;
import com.administrator.elwj.MyInviteActivity;
import com.administrator.elwj.MyMoneyPayActivity;
import com.administrator.elwj.MyScanResultActivity;
import com.administrator.elwj.NewAttentionActivity;
import com.administrator.elwj.R;
import com.administrator.elwj.RegisterActivity;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static com.tencent.open.utils.Global.getSharedPreferences;


/**
 * “我的”页面
 * Created by Administrator on 2015/12/30.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private BaseApplication appContext;
    private View mView;
    private ImageButton ib_loginout;
    private ImageView iv_my_head;
    private TextView tv_name;
    private TextView tv_introduce;
    private TextView tv_my_news;
    private TextView tv_my_fans;
    private TextView tv_my_integral;
    private TextView tv_member_introduce;
    private RelativeLayout rl_member;
    private ImageView ivLoginBeforeBack;
    private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小
    private Bitmap bm;
    private Bitmap logo;
    static final int GET_USERINFO_EXTRA = 0;
    static final int GOTO_LOGIN = 0x01;
    static final int BACK_LOGIN = 0x02;
    private UserInfo userInfo;
    private LinearLayout layout_login_before;
    private LinearLayout layout_login_after;
    private TextView tvBindCard;
    private LinearLayout layout_login_content_after;
    private ImageLoader imageLoader;
    private DisplayImageOptions optionhead;

    public static class MyHandler extends Handler {
        private WeakReference<MyFragment> mFragment;

        public MyHandler(MyFragment fragment) {
            mFragment = new WeakReference<MyFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyFragment fragment = mFragment.get();
            if (fragment != null) {
                int which = msg.what;
                String Json = (String) msg.obj;
                JSONObject jsonObject = null;
                switch (which) {
                    case Constant.ISLOGIN:
                        try {
                            jsonObject = new JSONObject(Json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                Intent LoginIntent = new Intent(fragment.getActivity(), LoginActivity.class);
                                fragment.startActivity(LoginIntent);
                            } else if (result == 1) {
                                Intent memberIntent = new Intent(fragment.getActivity(), MemberBindingActivity.class);
                                fragment.startActivity(memberIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constant.GET_USERINFO:
                        try {
                            jsonObject = new JSONObject(Json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                Activity activity = fragment.getActivity();
                                if (activity != null)
                                    ToastUtil.showToast(activity, "未获取到用户信息");
//                                    Toast.makeText(activity, "未获取到用户信息", Toast.LENGTH_LONG).show();
                            } else if (result == 1) {
                                VolleyUtils.NetUtils(fragment.appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, fragment.handler2, GET_USERINFO_EXTRA);
                                String data = jsonObject.optString("data");
                                Gson gson = new Gson();
                                fragment.userInfo = gson.fromJson(data, UserInfo.class);
                                fragment.showUserinfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_USERINFO_EXTRA:
                        Gson gson = new Gson();
                        UserInfoExtra userInfoExtra = gson.fromJson(Json, UserInfoExtra.class);
                        if (userInfoExtra.getResult() == 1) {
                            UserInfoExtra.DataEntity dataEntity = userInfoExtra.getData();
                            if (dataEntity != null) {
                                fragment.tv_my_fans.setText(dataEntity.getAttention_num());
                                if (dataEntity.getCredit_num() != null && !"".equals(dataEntity.getCredit_num()))
                                    fragment.tv_my_integral.setText(dataEntity.getCredit_num());
                                else fragment.tv_my_integral.setText("0");
                                fragment.tv_my_news.setText(dataEntity.getNovelty_num());
                                fragment.showBindInfo(dataEntity.getIs_bind());
                            } else {
                                if (fragment.getActivity() != null)
                                    ToastUtil.showToast(fragment.getContext(), "未获取到粉丝数");
//                                    Toast.makeText(fragment.getActivity(), "未获取到粉丝数", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (fragment.getActivity() != null)
                                ToastUtil.showToast(fragment.getContext(), "未获取到粉丝数");
//                                Toast.makeText(fragment.getActivity(), "未获取到粉丝数", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constant.LOG_OUT:
                        try {
                            JSONObject object = new JSONObject(Json);
                            if (fragment.getActivity() != null) {
                                SharedPreferences localShare = fragment.getActivity().getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = localShare.edit();
                                editor.remove("email");
                                editor.remove("password");
                                editor.commit();
                                BaseApplication.isLogin = false;
                                fragment.layout_login_after.setVisibility(View.GONE);
                                fragment.layout_login_before.setVisibility(View.VISIBLE);
                                fragment.layout_login_content_after.setVisibility(View.GONE);
                                fragment.ib_loginout.setVisibility(View.GONE);
                                PushManager.stopWork(fragment.getActivity().getApplicationContext());
                                BaseApplication.member_id = null;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }

    private Handler handler2 = new MyHandler(this);

    public MyFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_home, null);
        appContext = (BaseApplication) getActivity().getApplication();
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        initViews();
        return mView;
    }

    private void initLoginorLogout() {
        if (BaseApplication.isLogin) {
            layout_login_after.setVisibility(View.VISIBLE);
            layout_login_before.setVisibility(View.GONE);
            ivLoginBeforeBack.setVisibility(View.GONE);
            layout_login_content_after.setVisibility(View.VISIBLE);
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, null, null, handler2, Constant.GET_USERINFO);
        } else {
            layout_login_after.setVisibility(View.GONE);
            ivLoginBeforeBack.setVisibility(View.VISIBLE);
            layout_login_before.setVisibility(View.VISIBLE);
            layout_login_content_after.setVisibility(View.GONE);
            ib_loginout.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        RelativeLayout rl_my_collection = (RelativeLayout) mView.findViewById(R.id.relativity_my_collection);
        rl_my_collection.setOnClickListener(this);
        ImageButton ib_back = (ImageButton) mView.findViewById(R.id.ib_back);
        tvBindCard = (TextView) mView.findViewById(R.id.tv_my_member);
        ivLoginBeforeBack = (ImageView) mView.findViewById(R.id.iv_login_before_back);
        ib_back.setVisibility(View.INVISIBLE);
        layout_login_before = (LinearLayout) mView.findViewById(R.id.layout_login_before);
        layout_login_after = (LinearLayout) mView.findViewById(R.id.layout_login_after);

        layout_login_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                if (userInfo != null) {
                    intent.putExtra("member_id", String.valueOf(userInfo.getMember_id()));
                    startActivityForResult(intent, Constant.REFRESH);
                }
            }
        });
        layout_login_content_after = (LinearLayout) mView.findViewById(R.id.layout_login_content_after);

        TextView tv_title = (TextView) mView.findViewById(R.id.tv_viewpagerdetails_num);
        tv_title.setText("我的");
        tv_title.setTextSize(17);
        ib_loginout = (ImageButton) mView.findViewById(R.id.ib_delete);
        ib_loginout.setBackgroundResource(R.mipmap.loginout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ib_loginout.setLayoutParams(layoutParams);
        iv_my_head = (ImageView) mView.findViewById(R.id.iv_my_head);
        tv_name = (TextView) mView.findViewById(R.id.tv_my_name);
        tv_introduce = (TextView) mView.findViewById(R.id.tv_my_introduce);
        LinearLayout linear_my_news = (LinearLayout) mView.findViewById(R.id.linear_my_news);
        LinearLayout linear_my_fans = (LinearLayout) mView.findViewById(R.id.linear_my_fans);
        LinearLayout linear_my_integral = (LinearLayout) mView.findViewById(R.id.linear_my_integral);
        tv_my_news = (TextView) mView.findViewById(R.id.tv_my_news);
        tv_my_fans = (TextView) mView.findViewById(R.id.tv_my_fans);
        tv_my_integral = (TextView) mView.findViewById(R.id.tv_my_integral);//积分
        tv_member_introduce = (TextView) mView.findViewById(R.id.tv_my_member_introduce);
        RelativeLayout rl_scan = (RelativeLayout) mView.findViewById(R.id.relativity_my_scan);
        rl_member = (RelativeLayout) mView.findViewById(R.id.relativity_my_member);
        RelativeLayout rl_my_card = (RelativeLayout) mView.findViewById(R.id.relativity_my_card);
        RelativeLayout rl_my_invite = (RelativeLayout) mView.findViewById(R.id.relativity_my_invite);
        RelativeLayout rl_my_address = (RelativeLayout) mView.findViewById(R.id.relativity_my_address);
        RelativeLayout rl_my_attention = (RelativeLayout) mView.findViewById(R.id.relativity_my_attention);
        RelativeLayout rl_my_activity = (RelativeLayout) mView.findViewById(R.id.relativity_my_activity);
        RelativeLayout rl_my_money_consume = (RelativeLayout) mView.findViewById(R.id.relativity_my_money_consume);
        RelativeLayout rl_my_credit_consume = (RelativeLayout) mView.findViewById(R.id.relativity_my_credit_consume);
        RelativeLayout rl_my_graft = (RelativeLayout) mView.findViewById(R.id.relativity_my_graft);
        Button myself_btn_login = (Button) mView.findViewById(R.id.myself_btn_login);
        Button myself_btn_regist = (Button) mView.findViewById(R.id.myself_btn_regist);
        myself_btn_login.setOnClickListener(this);
        myself_btn_regist.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_loginout.setOnClickListener(this);
        linear_my_news.setOnClickListener(this);
        linear_my_fans.setOnClickListener(this);
        linear_my_integral.setOnClickListener(this);
        rl_member.setOnClickListener(this);
        rl_scan.setOnClickListener(this);
        rl_my_card.setOnClickListener(this);
        rl_my_invite.setOnClickListener(this);
        rl_my_address.setOnClickListener(this);
        rl_my_attention.setOnClickListener(this);
        rl_my_activity.setOnClickListener(this);
        rl_my_money_consume.setOnClickListener(this);
        rl_my_credit_consume.setOnClickListener(this);
        rl_my_graft.setOnClickListener(this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {//显示出来时候
            if (!BaseApplication.isLogin) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myself_btn_login://登录按钮
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(loginIntent, GOTO_LOGIN);
                break;
            case R.id.myself_btn_regist://注册按钮
                Intent registIntent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(registIntent);
                break;
            case R.id.ib_delete://退出登录按钮
                new AlertDialog.Builder(getContext()).setMessage("是否退出登录？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (BaiduPushReceiver.ChannelID == null) {
                            if (BaiduPushReceiver.ChannelID == null) {
                                SharedPreferences localShare = getSharedPreferences("user", 0);
                                BaiduPushReceiver.ChannelID = localShare.getString("channelid", null);
                                if (BaiduPushReceiver.ChannelID == null) {
                                    BaiduPushReceiver.ChannelID = "";
                                }
                            }
                        }
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.logOut, new String[]{"channelid"}, new String[]{BaiduPushReceiver.ChannelID}, handler2, Constant.LOG_OUT);
                    }
                }).setNegativeButton("否", null).show();
                break;
            case R.id.linear_my_news://我的新鲜事
                Intent intent_news = new Intent(getActivity(), HomePageActivity.class);
                intent_news.putExtra("member_id", String.valueOf(userInfo.getMember_id()));
                startActivityForResult(intent_news, Constant.REFRESH);
                break;
            case R.id.linear_my_fans://粉丝
                Intent intent_fans = new Intent(getContext(), NewAttentionActivity.class);
                startActivity(intent_fans);
                break;
            case R.id.linear_my_integral://积分
                Intent intent1 = new Intent(getContext(), CreditSearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.relativity_my_scan://扫一扫
                Intent openCameraIntent = new Intent(getActivity(),
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, Constant.SCAN);

                break;
            case R.id.relativity_my_member://会员绑定
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler2, Constant.ISLOGIN);
                break;
            case R.id.relativity_my_card://我的名片
                Intent intent_my_card = new Intent(getActivity(), MyCardActivity.class);
                intent_my_card.putExtra("user_info", userInfo);
                intent_my_card.putExtra("image", bm);
                startActivity(intent_my_card);
                break;
            case R.id.relativity_my_invite://收到的活动邀请
                Intent intent_invite = new Intent(getActivity(), MyInviteActivity.class);
                startActivity(intent_invite);
                break;
            case R.id.relativity_my_address://地址管理
                Intent intent_address = new Intent(getActivity(), MyAddressActivity.class);
                startActivity(intent_address);
                break;
            case R.id.relativity_my_attention://我的关注
                Intent intent_attention = new Intent(getActivity(), MyAttentionActivity.class);
                startActivity(intent_attention);
                break;
            case R.id.relativity_my_activity://我的活动
                Intent intent_activity = new Intent(getActivity(), MyActivityActivity.class);
                startActivity(intent_activity);
                break;
            case R.id.relativity_my_money_consume://我的金钱消费
                Intent myMoneyPayIntent = new Intent(getActivity(), MyMoneyPayActivity.class);
                startActivity(myMoneyPayIntent);
                break;
            case R.id.relativity_my_credit_consume://我的积分消费
                Intent myCreditPayIntent = new Intent(getActivity(), MyCreditPayActivity.class);
                startActivity(myCreditPayIntent);
                break;
            case R.id.relativity_my_graft://草稿箱
                Intent intent2 = new Intent(getContext(), DraftBoxActivity.class);
                startActivity(intent2);
                break;
            case R.id.relativity_my_collection:
                Intent intent11 = new Intent(getContext(), MyCollectioinActivity.class);
                startActivity(intent11);
                break;
        }
    }

    private void showUserinfo() {
        if (userInfo != null) {
            BaseApplication.member_id = String.valueOf(userInfo.getMember_id());
            imageLoader.displayImage(userInfo.getFace(), iv_my_head, optionhead);
            tv_name.setText(userInfo.getName());
            tv_introduce.setText("简介：" + userInfo.getLvname());
        }
    }

    private void showBindInfo(String isBind) {
        if (isBind == null || "".equals(isBind)) {
            tvBindCard.setText("会员绑定");
            tv_member_introduce.setVisibility(View.VISIBLE);
            rl_member.setOnClickListener(this);
        } else if ("1".equals(isBind)) {
            tvBindCard.setText("已绑定");//绑定待验证
            tv_member_introduce.setVisibility(View.GONE);
            rl_member.setOnClickListener(null);
        } else if ("2".equals(isBind)) {
            tvBindCard.setText("已绑定");
            tv_member_introduce.setVisibility(View.GONE);
            rl_member.setOnClickListener(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("Main+resultCode", resultCode + "");
        if (resultCode == BACK_LOGIN) {
            layout_login_after.setVisibility(View.VISIBLE);
            layout_login_before.setVisibility(View.GONE);
            layout_login_content_after.setVisibility(View.VISIBLE);
            ib_loginout.setVisibility(View.VISIBLE);
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, null, null, handler2, Constant.GET_USERINFO);
        }

        if ((resultCode == Constant.MY_backunlog)) {
            layout_login_after.setVisibility(View.GONE);
            layout_login_before.setVisibility(View.VISIBLE);
            layout_login_content_after.setVisibility(View.GONE);
        }
        if ((resultCode == Constant.MY_backlog)) {

        }
        if ((resultCode == Constant.LOG_OUT)) {
            layout_login_after.setVisibility(View.GONE);
            layout_login_before.setVisibility(View.VISIBLE);
            layout_login_content_after.setVisibility(View.GONE);
            ib_loginout.setVisibility(View.GONE);
        }

        if ((resultCode == Constant.SCAN)) {

            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult != null && !scanResult.equals("")) {
                Intent intentAttention = new Intent(getActivity(), MyScanResultActivity.class);
                intentAttention.putExtra("id", scanResult);
                startActivity(intentAttention);
            }
        }
        if ((resultCode == Constant.REFRESH)) {

            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, null, null, handler2, Constant.GET_USERINFO);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, handler2, GET_USERINFO_EXTRA);
        initLoginorLogout();
    }
}
