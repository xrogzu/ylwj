package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.bean.LastOrderBean;
import com.administrator.bean.NewCreditInfoBean;
import com.administrator.bean.UserInfoExtra;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 积分查询页面
 * Created by Administrator on 2016/4/8.
 */
public class CreditSearchActivity extends AppCompatActivity implements View.OnClickListener {

    //目前拥有的积分
    private TextView tvOwnCredit;
    //所买的东西名称
    private TextView tvBuyThings;
    //所消费的积分
    private TextView tvUseCredit;
    //新增积分单独的textview
    private TextView tvAddNewCredit;

    private static final int GET_DATA = 0;
    private static final int GET_USERINFO_EXTRA = 1;
    private static final int GET_CREDIT = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_register:
                break;
            case R.id.ll_complete_info:
                break;
            case R.id.ll_trans:
                break;
            case R.id.ll_sign:
                break;
            case R.id.ll_invite:
                break;
            case R.id.ll_organize:
                break;
            case R.id.ll_participate:
                break;
        }
    }

    public static class MyHandler extends Handler {
        private WeakReference<CreditSearchActivity> mActivity;

        public MyHandler(CreditSearchActivity activity) {
            mActivity = new WeakReference<CreditSearchActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CreditSearchActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                if (msg.what == GET_DATA) {
                    Log.d("xu_order", json);
                    Gson gson = new Gson();
                    LastOrderBean bean = gson.fromJson(json, LastOrderBean.class);
                    boolean isSuccess = false;
                    if (bean != null) {
                        if (bean.getResult() == 1) {
                            LastOrderBean.DataEntity dataEntity = bean.getData();
                            activity.tvUseCredit.setText(String.format("%d", (long) dataEntity.getPaymoney()));
                            List<LastOrderBean.DataEntity.ItemListEntity> listEntities = dataEntity.getItemList();
                            if (listEntities != null && listEntities.size() > 0) {
                                isSuccess = true;
                                activity.tvBuyThings.setText("您所购买的" + listEntities.get(0).getName());
                            }
                        } else {
                            Log.d("xu", "获取失败");
                        }
                    }
                    if (!isSuccess) {
                        activity.tvBuyThings.setText("无消费记录");
                    }
                } else if (msg.what == GET_USERINFO_EXTRA) {
                    Gson gson = new Gson();
                    UserInfoExtra userInfoExtra = gson.fromJson(json, UserInfoExtra.class);
                    if (userInfoExtra.getResult() == 1) {
                        UserInfoExtra.DataEntity dataEntity = userInfoExtra.getData();
                        if (dataEntity != null) {
                            String num = dataEntity.getCredit_num();
                            if(num != null && !num.equals(""))
                                activity.tvOwnCredit.setText(dataEntity.getCredit_num());
                            else activity.tvOwnCredit.setText("0");
                        } else {
                            activity.tvOwnCredit.setText("0");
                            //Toast.makeText(activity, "未获取到粉丝数", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        activity.tvOwnCredit.setText("0");
                        //Toast.makeText(activity, "未获取到粉丝数", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == GET_CREDIT) {
                    Log.d("xu_credit", json);
                    Gson gson = new Gson();
                    boolean isSucess = false;
                    NewCreditInfoBean bean = gson.fromJson(json, NewCreditInfoBean.class);
                    if (bean != null && bean.getResult() == 1) {
                        List<NewCreditInfoBean.DataEntity> dataEntities = bean.getData();
                        if (dataEntities != null && dataEntities.size() > 0) {
                            activity.tvAddNewCredit.setText(dataEntities.get(0).getTitle());
                            isSucess = true;
                        }
                    }
                    if (!isSucess) {
                        activity.tvAddNewCredit.setText("无新增积分记录");
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditsearch);
        BaseApplication application = (BaseApplication) getApplication();
        initViews();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getLastOrder, new String[]{}, new String[]{}, handler, GET_DATA);
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, handler, GET_USERINFO_EXTRA);
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getNewAddedCreditInfo, null, null, handler, GET_CREDIT);
    }


    private void initViews() {
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditSearchActivity.this.finish();
            }
        });
        tvAddNewCredit = (TextView) findViewById(R.id.tv_new_add_credit_one);
        tvOwnCredit = (TextView) findViewById(R.id.tv_credit);
        tvBuyThings = (TextView) findViewById(R.id.tv_new_credit_things);
        tvUseCredit = (TextView) findViewById(R.id.tv_new_credit);
        TextView tvMoreConsumption = (TextView) findViewById(R.id.tv_more_consumption);
        tvMoreConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditSearchActivity.this, MyCreditPayActivity.class);
                intent.putExtra("detail",1);
                startActivity(intent);
            }
        });
        TextView tvAddCredit = (TextView) findViewById(R.id.tv_new_add_credit);
        TextView tvMoreCredit = (TextView) findViewById(R.id.tv_more_credit);
        tvMoreCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView tvLastTime = (TextView) findViewById(R.id.tv_new_consumption_time);
        TextView tvAddLastTime = (TextView) findViewById(R.id.tv_new_credit_add_time);

        LinearLayout llRegister = (LinearLayout) findViewById(R.id.ll_register);
        LinearLayout llCompleteInfo = (LinearLayout) findViewById(R.id.ll_complete_info);
        LinearLayout llInvite = (LinearLayout) findViewById(R.id.ll_invite);
        LinearLayout llOrganize = (LinearLayout) findViewById(R.id.ll_organize);
        LinearLayout llParticipate = (LinearLayout) findViewById(R.id.ll_participate);
        LinearLayout llSign = (LinearLayout) findViewById(R.id.ll_sign);
        LinearLayout llTrans = (LinearLayout) findViewById(R.id.ll_trans);

        llRegister.setOnClickListener(this);
        llCompleteInfo.setOnClickListener(this);
        llInvite.setOnClickListener(this);
        llOrganize.setOnClickListener(this);
        llParticipate.setOnClickListener(this);
        llSign.setOnClickListener(this);
        llTrans.setOnClickListener(this);

    }
}

