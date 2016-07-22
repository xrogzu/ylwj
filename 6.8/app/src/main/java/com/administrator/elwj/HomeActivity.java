package com.administrator.elwj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.fragment.Community_bigstageFragment;
import com.administrator.fragment.ExquisiteLifeFragment;
import com.administrator.fragment.FinancialManagerFragment;
import com.administrator.fragment.Integral_ECshopFragment;
import com.administrator.fragment.MyFragment;
import com.administrator.fragment.NearByFragment;
import com.administrator.fragment.NewestFragment;
import com.administrator.fragment.RegimenFragment;
import com.administrator.fragment.ServiceFragment;
import com.administrator.minterface.GetServiceWhere;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


/**
 * 主页页面，下面存放最新、身边、服务、我的四个按钮。
 */
public class HomeActivity extends AppCompatActivity implements GetServiceWhere {


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    public static class MyHandler1 extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    }

    //当前所在的fragment
    private int curFragment = CUR_FRAGMENT_NEWEST;

    //当前为最新
    private static final int CUR_FRAGMENT_NEWEST = 1;
    private String tagNewest = "tagNewest";
    //当前为身边
    private static final int CUR_FRAGMENT_NEARBY = 2;
    private String tagNearby = "tagNearby";
    //当前为服务
    private static final int CUR_FRAGMENT_SERVICE = 3;
    private String tagService = "tagService";
    //当前为我的
    private static final int CUR_FRAGMENT_MY = 4;
    private String tagMy = "tagMy";
    //当前为社区超市
    private static final int CUR_FRAGMENT_SHOP = 5;
    private String tagShop = "tagShop";
    //当前为社会大舞台
    private static final int CUR_FRAGMENT_BIGSTAGE = 6;
    private String tagBigStage = "tagBigStage";
    //当前为金融管家
    private static final int CUR_FRAGMENT_FIN = 7;
    private String tagFin = "tagFin";
    //当前为精致生活
    private static final int CUR_FRAGMENT_LIFE = 8;
    private String tagLife = "tagLife";
    //当前为健康养生
    private static final int CUR_FRAGMENT_HEALTH = 9;
    private String tagHealth = "tagHealth";

    Handler mHandler = new MyHandler1();
    //下面四个按钮group（最新、身边、服务、我的）
    private RadioGroup rg_bottom;

    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;
    //最新更新broadcast receiver
    private NewestUpdateReceiver mUpdateReceiver;
    //最新中上方的跳转
    private JumpReceiver mJumpNearbyReceiver;
    //是否是经由注册完成后的立即进入启动的homeactivity
    private boolean isNewRegisterStart = false;
    //启动活动的id
    private String ActivityID;

    private BaseApplication appContext;

    //更新最新标签
    public static final String UPDATE_NEWEST = "android.intent.action.UPDATE_NEWEST";
    //跳转金融管家
    public static final String JUMP_FIN = "android.intent.action.JUMP_FIN";
    //跳转精致生活
    public static final String JUMP_LIFE = "android.intent.action.JUMP_LIFE";
    //跳转社区大舞台
    public static final String JUMP_BIGSTAGE = "android.intent.action.JUMP_BIGSTAGE";
    //跳转进口商城
    public static final String JUMP_SHOP = "android.intent.action.JUMP_SHOP";
    //跳转积分商城
    public static final String JUMP_CREDIT = "android.intent.action.JUMP_CREDIT";
    //跳转健康养生
    public static final String JUMP_HEALTH = "android.intent.action.JUMP_HEALTH";
    //最新页面中上方的六个跳转action
    public static final String JUMP = "android.intent.action.JUMP";
    //是否是从最新页面跳转过来的，因为跳转到社区大舞台后，返回上一页面需要返回到最新页面
    private boolean isFromNewest = false;

    public static class MyHandler extends Handler {
        private WeakReference<HomeActivity> mActivity;

        public MyHandler(HomeActivity activity) {
            mActivity = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HomeActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                //查询是否已经登录
                if (which == Constant.ISLOGIN) {
                    String json = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        if (result == 1) {
                            BaseApplication.isLogin = true;

                            if (activity.ActivityID != null) {
                                Intent intent1 = new Intent(activity, BigStageNormalDetailsActivity.class);
                                Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                                bean_bigstage_list.setActivity_id(activity.ActivityID);
                                intent1.putExtra("bean", bean_bigstage_list);
                                activity.startActivity(intent1);
                                activity.ActivityID = null;
                            }
                            //获取用户id
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, new InitMemberIDHandler(), InitMemberIDHandler.GET_MEMBER_ID);
                        } else {
                            //获取记录在sharedpreferences中的用户名和密码
                            SharedPreferences localShare = activity.getSharedPreferences("user", 0);
                            String email = localShare.getString("email", "18866624108");
                            String password = localShare.getString("password", 0 + "");
                            if (BaiduPushReceiver.ChannelID == null) {
                                BaiduPushReceiver.ChannelID = localShare.getString("channelid", null);
                                if (BaiduPushReceiver.ChannelID == null) {
                                    BaiduPushReceiver.ChannelID = "";
                                }
                            }
                            LogUtils.d("xu_login", BaiduPushReceiver.ChannelID);
                            //用户名密码登录
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.login_url, new String[]{"username", "password", "channelid", "devicetype"}, new String[]{email, password, BaiduPushReceiver.ChannelID, "3"}, activity.handler, Constant.TRYTO_LOGIN);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //用户名密码登录
                if (which == Constant.TRYTO_LOGIN) {
                    String json = (String) msg.obj;
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 1) {
                            //获取用户id
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, new InitMemberIDHandler(), InitMemberIDHandler.GET_MEMBER_ID);
                            BaseApplication.isLogin = true;
                            if (activity.ActivityID != null) {
                                Intent intent1 = new Intent(activity, BigStageNormalDetailsActivity.class);
                                Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                                bean_bigstage_list.setActivity_id(activity.ActivityID);
                                intent1.putExtra("bean", bean_bigstage_list);
                                activity.startActivity(intent1);
                                activity.ActivityID = null;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initIntent(null);
        appContext = (BaseApplication) getApplication();
        //获取是否已经登录
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
        mFragmentManager = getSupportFragmentManager();
        //初始化view
        initView();
        //注册最新页面更新数据broadcast receiver
        mUpdateReceiver = new NewestUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_NEWEST);
        registerReceiver(mUpdateReceiver, intentFilter);
        //注册最新页面跳转broadcast receiver
        mJumpNearbyReceiver = new JumpReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(JUMP);
        registerReceiver(mJumpNearbyReceiver, intentFilter1);

        //设置默认进入的页面为最新页面
        rg_bottom.check(R.id.rb_bottom_newest);

    }

    //初始化通过分享跳转进来的intent，跳转到分享的不同的页面
    void initIntent(Intent intent) {
        if (intent == null)
            intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                LogUtils.d("xu_scheme", uri.getScheme());
                String type = uri.getQueryParameter("type");
                LogUtils.d("xu_scheme", type);
                String id = uri.getQueryParameter("id");
                switch (type) {
                    case ShareUtil.NOVELTY_SHARE: {
                        LogUtils.d("xu_scheme", "novelty_share");
                        Intent intent1 = new Intent(this, CommentActivity.class);
                        intent1.putExtra("id", id);
                        startActivity(intent1);
                        break;
                    }
                    case ShareUtil.GOODS: {
                        Intent intent1 = new Intent(this, Interalshop_detailsactivity.class);
                        intent1.putExtra("id", id);
                        intent1.putExtra("type", Constant.SHOP_TYPE_AUTO);
                        startActivity(intent1);
                        break;
                    }
                    case ShareUtil.VOTE_ACTIVITY_SHARE: {
                        Intent intent1 = new Intent(this, BigStageVoteDetailsActivity.class);
                        intent1.putExtra("activity_id", id);
                        startActivity(intent1);
                        break;
                    }
                    case ShareUtil.NORMAL_ACTIVITY_SHARE:
                    case ShareUtil.PUBLIC_ACTVITY_SHARE: {
                        Intent intent1 = new Intent(this, BigStageNormalDetailsActivity.class);
                        Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                        bean_bigstage_list.setActivity_id(id);
                        intent1.putExtra("bean", bean_bigstage_list);
                        startActivity(intent1);
                        break;
                    }
                    case ShareUtil.SCAN_QR_SHARE:
                        break;
                }

            }
        }
    }

    private void initNewIntent(Intent intent) {
        if (intent != null) {
            ActivityID = intent.getStringExtra("activity_id");
            isNewRegisterStart = intent.getBooleanExtra("isFromRegister", false);
        }
    }


    //如果app在后台运行，这时通过scheme分享跳转进来时不走oncreate，会运行onnewintent，所以在这里进行判断跳转不同的页面
    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.d("xu_homeactivity", "onNewIntent");
        super.onNewIntent(intent);
        initIntent(intent);
        initNewIntent(intent);
        if (ActivityID != null) {
            Intent intent1 = new Intent(this, BigStageNormalDetailsActivity.class);
            Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
            bean_bigstage_list.setActivity_id(ActivityID);
            intent1.putExtra("bean", bean_bigstage_list);
            startActivity(intent1);
            ActivityID = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //当页面为金融管家、积分商城、进口商城、社区大雾天、精致生活、健康养生时，点击返回键，需要popBackStack，否则就是需要退出
            if (curFragment == CUR_FRAGMENT_FIN) {
                mFragmentManager.popBackStack();
                if (!isFromNewest) {
                    getWhere(Constant.OUT_FINANCIAL);
                } else {
                    isFromNewest = false;
                }
            } else if (curFragment == CUR_FRAGMENT_SHOP) {
                mFragmentManager.popBackStack();
                getWhere(Constant.OUT_FINANCIAL);
            } else if (curFragment == CUR_FRAGMENT_BIGSTAGE) {
                mFragmentManager.popBackStack();
                if (!isFromNewest) {
                    getWhere(Constant.OUT_FINANCIAL);
                } else
                    isFromNewest = false;
            } else if (curFragment == CUR_FRAGMENT_LIFE) {
                mFragmentManager.popBackStack();
            } else if (curFragment == CUR_FRAGMENT_HEALTH) {
                mFragmentManager.popBackStack();
            } else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.showToast(getApplicationContext(), "再按一次退出程序");
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    //退出精致生活fragment
    public void popLifeFragment() {
        if (mFragmentManager != null)
            mFragmentManager.popBackStack();
    }

    //退出社区大舞台、积分商城、金融管家、进口商城、精致生活、健康养生页面
    private void popFragment() {
        if (curFragment == CUR_FRAGMENT_BIGSTAGE || curFragment == CUR_FRAGMENT_SHOP || curFragment == CUR_FRAGMENT_FIN || curFragment == CUR_FRAGMENT_LIFE || curFragment == CUR_FRAGMENT_HEALTH) {
            mFragmentManager.popBackStack();
        }
    }

    private void initView() {
        rg_bottom = (RadioGroup) findViewById(R.id.rg_bottom);
        rg_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bottom_newest://头条
                        popFragment();
                        NewestFragment newestFragment = new NewestFragment();
                        newestFragment.setTitle("最新");
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.container, newestFragment, tagNewest).commit();
                        curFragment = CUR_FRAGMENT_NEWEST;
                        break;
                    case R.id.rb_bottom_nb://身边
                        popFragment();
                        NearByFragment nearFragment = new NearByFragment();
                        nearFragment.setTitle("身边");
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.container, nearFragment, tagNearby).commit();
                        curFragment = CUR_FRAGMENT_NEARBY;
                        break;
                    case R.id.rb_bottom_service://服务
                        popFragment();
                        ServiceFragment serviceFragment = new ServiceFragment();
                        serviceFragment.setTitle("服务");
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.container, serviceFragment, tagService).commit();
                        curFragment = CUR_FRAGMENT_SERVICE;
                        break;

                    case R.id.rb_bottom_user://我的
                        popFragment();
                        MyFragment myFragment = new MyFragment();
                        myFragment.setTitle("我的");
                        transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.container, myFragment, tagMy).commit();
                        curFragment = CUR_FRAGMENT_MY;
                        break;
                }
            }
        });
        RadioButton rg_bottom_service = (RadioButton) findViewById(R.id.rb_bottom_service);
        //在精致生活、社区大舞台、积分商城、进口商城、健康养生、金融管家页面中，点击下方的服务直接返回到服务fragment页面
        rg_bottom_service.setOnClickListener(new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curFragment != CUR_FRAGMENT_SERVICE) {
                    if (mFragmentManager.findFragmentByTag(tagFin) != null
                            || mFragmentManager.findFragmentByTag(tagShop) != null
                            || mFragmentManager.findFragmentByTag(tagBigStage) != null || mFragmentManager.findFragmentByTag(tagLife) != null || mFragmentManager.findFragmentByTag(tagHealth) != null) {
                        mFragmentManager.popBackStack();
                        getWhere(Constant.OUT_FINANCIAL);
                    }
                }
            }
        });

        initNewIntent(null);

    }


    @Override
    public void getWhere(int where) {
        if (where == Constant.IN_FINANCIAL_EXPERT) {//进入金融管家
            FinancialManagerFragment financialManagerFragment = new FinancialManagerFragment();
            transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.container, financialManagerFragment, tagFin).addToBackStack(null).commit();
            curFragment = CUR_FRAGMENT_FIN;
        } else if (where == Constant.OUT_FINANCIAL) {//返回到服务fragment
            popFragment();
            if (curFragment != CUR_FRAGMENT_SERVICE) {
                ServiceFragment serviceFragment = new ServiceFragment();
                serviceFragment.setTitle("服务");
                transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.container, serviceFragment, tagService).commit();
                curFragment = CUR_FRAGMENT_SERVICE;
            }
        } else if (where == Constant.IN_INTEGRAL_SHOP) {//进入社区超市
            Integral_ECshopFragment integral_eCshopFragment = new Integral_ECshopFragment();
            transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.container, integral_eCshopFragment, tagShop).addToBackStack(null).commit();
            curFragment = CUR_FRAGMENT_SHOP;

        } else if (where == Constant.IN_COMMUNITY_STAGE) {//进入社区大舞台
            Community_bigstageFragment community_bigstageFragment = new Community_bigstageFragment();
            transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.container, community_bigstageFragment, tagBigStage).addToBackStack(null).commit();
            curFragment = CUR_FRAGMENT_BIGSTAGE;

        } else if (where == Constant.IN_DELICATE_LIFE) {//进入精致生活
            ExquisiteLifeFragment exquisiteLifeFragment = new ExquisiteLifeFragment();
            transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.container, exquisiteLifeFragment, tagLife).addToBackStack(null).commit();
            curFragment = CUR_FRAGMENT_LIFE;

        } else if (where == Constant.IN_REGIMEN) {//进入健康养生
            RegimenFragment regimenFragment = new RegimenFragment();
            transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.container, regimenFragment, tagHealth).addToBackStack(null).commit();
            curFragment = CUR_FRAGMENT_HEALTH;

        }

    }

    @Override
    protected void onResume() {
        LogUtils.d("xu_homeactivity", "onResume");
        super.onResume();
        if (isNewRegisterStart) {
            if (curFragment == CUR_FRAGMENT_MY) {
                popFragment();
//                        switchContent(mContent,recordFragment);
                MyFragment myFragment = new MyFragment();
                myFragment.setTitle("我的");
                transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.container, myFragment, tagMy).commit();
            }
        }
    }

    //兼容Android 6.0以上版本的权限系统
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //位置请求权限
            case Constant.PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (curFragment == CUR_FRAGMENT_FIN) {
                        FinancialManagerFragment financialManagerFragment = (FinancialManagerFragment) mFragmentManager.findFragmentByTag(tagFin);
                        if (financialManagerFragment != null)
                            financialManagerFragment.initMapData(true);
                    }
                } else {
                    FinancialManagerFragment financialManagerFragment = (FinancialManagerFragment) mFragmentManager.findFragmentByTag(tagFin);
                    if (financialManagerFragment != null)
                        financialManagerFragment.initMapData(false);
                }
                break;
        }
    }


    //更新最新fragment页面中的内容
    public void updateNewestFragment(Intent intent) {
        LogUtils.d("xu_update", "updateNewestFragment");
        if (curFragment == CUR_FRAGMENT_NEWEST) {
            if (intent != null) {
                String data = intent.getStringExtra("data");
                //百度云推送推送来的更新数据
                if (data != null) {
                    NewestFragment newestFragment = (NewestFragment) mFragmentManager.findFragmentByTag(tagNewest);
                    if (newestFragment != null)
                        newestFragment.update(data);
                }
                //登录后，更新一下最新页面中的小红点，标明已读未读
                else {
                    NewestFragment newestFragment = (NewestFragment) mFragmentManager.findFragmentByTag(tagNewest);
                    if (newestFragment != null)
                        newestFragment.initSP();
                }
            }
        }
    }


    public class NewestUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d("xu_update", "onReceive_update");
            updateNewestFragment(intent);
        }
    }

    public class JumpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String where = intent.getStringExtra("jump");
            //进入金融管家页面
            switch (where) {
                //进入金融管家页面
                case JUMP_FIN:
                    isFromNewest = true;
                    //rg_bottom_service.setChecked(true);
                    getWhere(Constant.IN_FINANCIAL_EXPERT);
                    break;
                //进入健康养生页面
                case JUMP_HEALTH:
                    //rg_bottom_service.setChecked(true);
                    getWhere(Constant.IN_REGIMEN);
                    break;
                //进入精致生活页面
                case JUMP_LIFE:
                    //rg_bottom_service.setChecked(true);
                    getWhere(Constant.IN_DELICATE_LIFE);
                    break;
                //进入社区大舞台页面
                case JUMP_BIGSTAGE:
                    isFromNewest = true;
                    getWhere(Constant.IN_COMMUNITY_STAGE);
                    break;
                //进入积分商城页面
                case JUMP_CREDIT:
                    Intent intent3 = new Intent(HomeActivity.this, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 0);
                    startActivity(intent3);
                    break;
                //进入进口商城页面
                case JUMP_SHOP:
                    Intent intent1 = new Intent(HomeActivity.this, Interalshop_shopactivity.class);
                    intent1.putExtra("num", 2);
                    startActivity(intent1);
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mUpdateReceiver);
        unregisterReceiver(mJumpNearbyReceiver);
        super.onDestroy();
    }

    //获取是否是从最新页面跳转过来的
    public boolean getIsFromNewest() {
        return isFromNewest;
    }


    //从最新页面跳转出去的，返回的时候还是要显示最新页面
    public void popIsFromNewestFragment() {
        if (mFragmentManager != null) {
            mFragmentManager.popBackStack();
            isFromNewest = false;
        }
    }

    //微博分享需要设置的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
