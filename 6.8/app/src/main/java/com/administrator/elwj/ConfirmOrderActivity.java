package com.administrator.elwj;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterConfirmOrder;
import com.administrator.bean.Addresses;
import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Bean_GoodsPayList;
import com.administrator.bean.Bean_Shopcarlist;
import com.administrator.bean.Constant;
import com.administrator.bean.OrderBackBean;
import com.administrator.bean.UserAddress;
import com.administrator.bean.UserInfo;
import com.administrator.fragment.AddressFragment;
import com.administrator.myviews.ListViewForScrollView;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>确认订单</p>
 * <p>created by wujian</p>
 */
public class ConfirmOrderActivity extends AppCompatActivity {

    //银联支付是否成功
    private boolean isPaySuccess = false;
    //提交订单是否成功
    private boolean isCommitSuccess = false;

    private String mMode = "01";//设置测试模式:01为测试 00为正式环境
    private static final String TN_URL_01 = "http://202.101.25.178:8080/sim/gettn";//自己后台需要实现的给予我们app的tn号接口

    private BaseApplication appContext;
    private int type;
    private String num;
    private TextView mOrderUser;
    private TextView mOrderPhone;
    private TextView mOrderAddress;
    //private TextView mMeasureView;//元/积分
    private TextView mBottomMeasure;//元/积分
    private ListViewForScrollView mOrderList;
    //    private TextView mExpressPrice;
//    private TextView mOrderNumTV;
//    private TextView mOrderTotalPrice;
    private TextView mTotalCount;
    private TextView mTotalMoney;
    private TextView mOrderSubmit;
    private PopupWindow mPopupWindow;
    private UserInfo userinfo;
    private UserAddress userAddress;
    private ImageLoader imageLoader;
    private ImageView confirmLocationIV;
    private TextView addAddressTV;
    private RelativeLayout mAddressLayout;
    private DisplayImageOptions optionhead;
    private String people;
    private String address;
    private double total;//总积分，或者总金额
    private double credit;
    private String orderid;
    private boolean isOrderSubmited = false;
    private boolean isGetcredit = false;//判断获取个人积分是否成功
    private int from;//从哪里跳转过来的标示
    //从商品详情页-->立即购买-->跳转过来需要接收的实体对象
    private List<Bean_GoodsList.DataEntity> dataEntities;
    //从我的消费-->待支付-->跳转过来需要接收的实体对象
    private Bean_GoodsPayList.Order waitPayOrder;
    //从购物车跳转过来的实体
    private List<Bean_Shopcarlist.DataEntity.GoodslistEntity> goodslistEntities;
    private static final int BUY_PAY = 0;
    private static final int GET_PAY_TYPE = 1;
    private ProgressDialog mProgressDialog;

    private OrderBackBean mOrderBack;

    public static class MyHandler extends Handler {

        private WeakReference<ConfirmOrderActivity> mActivity;

        public MyHandler(ConfirmOrderActivity activity) {
            mActivity = new WeakReference<ConfirmOrderActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ConfirmOrderActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                JSONObject jsonObject = null;
                switch (msg.what) {
                    case Constant.GET_USERINFO://获取用户信息
                        try {
                            jsonObject = new JSONObject(json);
                            LogUtils.i("wj", json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                //获取失败
                                LogUtils.i("wj", "获取用户信息失败");
                            } else if (result == 1) {
                                String data = jsonObject.optString("data");
                                if (data != null) {
                                    Gson gson = new Gson();
                                    //获取到了用户信息
                                    activity.userinfo = gson.fromJson(data, UserInfo.class);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.GET_ORDERID:
                        try {
                            jsonObject = new JSONObject(json);
                            LogUtils.i("wj", "订单信息" + json);
                            int result = jsonObject.optInt("result");
                            String message = jsonObject.optString("message");
                            if (result == 0) {
                                ToastUtil.showToast(activity, message);
//                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            } else if (result == 1) {
                                LogUtils.i("wj", "提交订单成功");
                                if (activity.type == Constant.SHOP_TYPE_CREDIT) {
                                    String order = jsonObject.optString("order");
                                    jsonObject = new JSONObject(order);
                                    activity.orderid = jsonObject.optString("order_id");
                                    activity.isCommitSuccess = true;
                                    activity.showPopWindow(activity.credit, activity.orderid);
                                } else if (activity.type == Constant.SHOP_TYPE_MONEY) {
                                    Gson gson = new Gson();
                                    activity.mOrderBack = gson.fromJson(json, OrderBackBean.class);
                                    if (activity.mOrderBack != null) {
                                        if (activity.mOrderBack.getResult() == 1) {
                                            OrderBackBean.OrderEntity orderEntity = activity.mOrderBack.getOrder();
                                            activity.orderid = orderEntity.getOrder_id();
                                            activity.showPopWindow(0, activity.orderid);
                                            activity.isCommitSuccess = true;
                                        } else {
                                            ToastUtil.showToast(activity, "获取订单返回信息失败");
                                        }
                                    }

                                }
//                                    else if (activity.isGetcredit) {
//                                        activity.showPopWindow(activity.credit, activity.orderid);
//                                    } else {
//                                        ToastUtil.showToast(activity, "获取用户个人积分失败");
////                                        Toast.makeText(activity, "获取用户个人积分失败", Toast.LENGTH_LONG).show();
//                                    }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.DO_PAYMENT:
                        if (activity.type == Constant.SHOP_TYPE_CREDIT) {
                            try {
                                jsonObject = new JSONObject(json);
                                LogUtils.i("wj", "支付操作" + json);
                                int result = jsonObject.optInt("result");
                                String message = jsonObject.optString("message");
                                if (result == 0) {
                                    ToastUtil.showToast(activity, message);
//                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                } else if (result == 1) {
//                                    ToastUtil.showToast(activity, message);
//                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                    LogUtils.i("wj", "支付成功");
                                    activity.isToMypay();
                                }
                                activity.mPopupWindow.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (activity.type == Constant.SHOP_TYPE_MONEY) {
                            activity.payUseUnion(json);
                        }
                        break;
                    case Constant.GETCREDIT:
                        try {
                            jsonObject = new JSONObject(json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {
                                activity.isGetcredit = true;
                                String message = jsonObject.optString("message");
                                ToastUtil.showToast(activity, message);
//                                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                activity.credit = 0;
                            } else if (result == 1) {
                                String data = jsonObject.optString("data");
                                jsonObject = new JSONObject(data);
                                activity.credit = jsonObject.optDouble("credit");
                                activity.isGetcredit = true;
//                                    Toast.makeText(activity, "获取积分成功"+activity.credit, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.GETDEFULTADDRESS:
                        try {
                            LogUtils.e("wj", "默认地址" + json);
                            jsonObject = new JSONObject(json);
                            int result = jsonObject.optInt("result");
                            if (result == 0) {

                            } else if (result == 1) {
                                String data = jsonObject.optString("data");
                                if (data == null || data.equals("")) {
                                    activity.userAddress = null;
                                    activity.initView();
                                } else {
                                    jsonObject = new JSONObject(data);
                                    Gson gson = new Gson();
                                    activity.userAddress = gson.fromJson(jsonObject.optString("defaultAddress"), UserAddress.class);
                                    activity.initView();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    //以前用银联控件时的代码，现在已经用不到了
                    case BUY_PAY:
                        if (activity.mProgressDialog != null)
                            activity.mProgressDialog.dismiss();
                        String tn = "";
                        if (msg.obj == null || ((String) msg.obj).length() == 0) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                            builder.setTitle("错误提示");
                            builder.setMessage("网络连接失败,请重试!");
                            builder.setNegativeButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().show();
                        } else {
                            tn = (String) msg.obj;
                            activity.doStartUnionPayPlugin(activity, tn, activity.mMode);
                        }
                        break;
                    case GET_PAY_TYPE:
                        LogUtils.d("xu", json);
                        break;
                    case Constant.ADDTO_SHOPCAR:
                        try {
                            JSONObject jsonObject1 = new JSONObject(json);
                            if (jsonObject1.getInt("result") == 1) {
                                activity.createOrder(activity.mOrderPhone.getText().toString().trim(), "");
                            } else {
                                ToastUtil.showToast(activity, jsonObject1.getString("message"));
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

    /**
     * <p>最终的支付操作</p>
     */
    private void payment(String orderid) {
        if (type == Constant.SHOP_TYPE_MONEY && isPaySuccess) {
            ToastUtil.showToast(this, "已经支付成功");
            mOrderSubmit.setEnabled(true);
        } else {
            this.orderid = orderid;
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.doPayment_point,
                    new String[]{"orderid"}, new String[]{orderid}, handler, Constant.DO_PAYMENT);

        }
    }

    private void add2ShopCar() {
        if (dataEntities != null && dataEntities.size() > 0)
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shopcarURL, new String[]{"productid", "num"}, new String[]{String.format("%d", dataEntities.get(0).getGoods_id()), dataEntities.get(0).getBuy_count()}, handler, Constant.ADDTO_SHOPCAR);
    }

    /**
     * <p>提交订单，获取订单id</p>
     */
    private void createOrder(String mobile, String num) {
        if (type == Constant.SHOP_TYPE_CREDIT) {
            String[] parameter = new String[]{
                    "addressId", "addressId", "addressId",
                    "city", "city_id",
                    "paymentId", "paymentId",
                    "province", "province_id",
                    "receipt", "receiptContent", "receiptTitle", "receiptType",
                    "region", "region_id",
                    "shipAddr", "shipDay", "shipDay", "shipMobile",
                    "shipName", "shipTel", "shipZip", "typeId", "typeId",
                    "good_id", "num"};
            String[] params;
            if (userAddress != null) {
                params = new String[]{
                        String.format("%d", userAddress.getAddr_id()), String.format("%d", userAddress.getAddr_id()), String.format("%d", userAddress.getAddr_id()),
                        userAddress.getCity(), String.format("%d", userAddress.getCity_id()),
                        "1000", "1000",
                        userAddress.getProvince(), String.format("%d", userAddress.getProvince_id()),
                        "", "办公用品", "", "1",
                        userAddress.getRegion(), String.format("%d", userAddress.getRegion_id()),
                        userAddress.getAddr(), "任意时间", "任意时间", mobile,
                        userAddress.getName(), mobile, userAddress.getZip() == null ? "266603" : userAddress.getZip(), "1", "1",
                        dataEntities.get(0).getGoods_id() + "", num};
            } else {
                params = new String[]{
                        "1000", "", "1000",
                        "青岛", "33",
                        "1000", "1000",
                        "山东", "1",
                        "", "办公用品", "", "1",
                        "青岛", "451",
                        address, "任意时间", "任意时间", mobile,
                        people, mobile, "266603", "1", "1",
                        dataEntities.get(0).getGoods_id() + "", num};
            }
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant. create_order_credit, parameter, params, handler, Constant.GET_ORDERID);

        } else if (type == Constant.SHOP_TYPE_MONEY) {
            String[] parameter = new String[]{
                    "typeId",
                    "paymentId",
                    "addressId",
                    "shipDay",
                    "shipTime",
                    "remark"};
            String[] params;
            if (userAddress != null) {
                params = new String[]{
                        "1",
                        "1004",
                        String.format("%d", userAddress.getAddr_id()),
                        "任意时间",
                        "任意时间",
                        "remark"};
            } else {
                params = new String[]{
                        "1",
                        "1004",
                        "1000",
                        "任意时间",
                        "任意时间",
                        "青岛", "451",
                        "remark"};
            }
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.create_order_money, parameter, params, handler, Constant.GET_ORDERID);
        }
    }

//    /**
//     * <p>提交订单，获取订单id</p>
//     */
//    private void createOrder(String mobile, String num) {
//        parameter = new String[]{
//                "addressId", "addressId", "addressId",
//                "city", "city_id",
//                "paymentId", "paymentId",
//                "province", "province_id",
//                "receipt", "receiptContent", "receiptTitle", "receiptType",
//                "region", "region_id",
//                "shipAddr", "shipDay", "shipDay", "shipMobile",
//                "shipName", "shipTel", "shipZip", "typeId", "typeId",
//                "good_id", "num"};
//        if (userAddress != null) {
//            params = new String[]{
//                    String.format("%d", userAddress.getAddr_id()), String.format("%d", userAddress.getAddr_id()), String.format("%d", userAddress.getAddr_id()) ,
//                    userAddress.getCity(), String.format("%d",userAddress.getCity_id()),
//                    type == Constant.SHOP_TYPE_MONEY ? "1001" : "1000", type == Constant.SHOP_TYPE_MONEY ? "1001" : "1000",
//                    userAddress.getProvince(), String.format("%d",userAddress.getProvince_id()),
//                    "", "办公用品", "", "1",
//                    userAddress.getRegion(), String.format("%d",userAddress.getRegion_id()),
//                    userAddress.getAddr(), "任意时间", "任意时间", mobile,
//                    userAddress.getName(), mobile, userAddress.getZip() == null ? "266603" : userAddress.getZip(), "1", "1",
//                    dataEntities.get(0).getGoods_id() + "", num};
//        } else {
//            params = new String[]{
//                    "1000","", "1000",
//                    "青岛", "33",
//                    type == Constant.SHOP_TYPE_MONEY ? "1001" : "1000", type == Constant.SHOP_TYPE_MONEY ? "1001" : "1000",
//                    "山东", "1",
//                    "", "办公用品", "", "1",
//                    "青岛", "451",
//                    address, "任意时间", "任意时间", mobile,
//                    people, mobile, "266603", "1", "1",
//                    dataEntities.get(0).getGoods_id() + "", num};
//        }
//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.create_order_credit, parameter, params, handler, Constant.GET_ORDERID);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        appContext = (BaseApplication) getApplication();
        ImageButton mBackButton = (ImageButton) findViewById(R.id.confirm_order_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }


    private void initData() {
        mOrderSubmit = (TextView) findViewById(R.id.confirm_submit_tv);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", Constant.SHOP_TYPE_CREDIT);
        num = intent.getStringExtra("num");
        List<Map<String, Object>> waitOrderDatas;
        if (type == Constant.SHOP_TYPE_CREDIT) {//积分支付
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getCredit, null, null, handler, Constant.GETCREDIT);
//            dataEntities = (List<Bean_GoodsList.DataEntity>) intent.getSerializableExtra("dataEntities");
            from = intent.getIntExtra("from", 0);
            if (from == Constant.FROMDETIAL) {
                dataEntities = (List<Bean_GoodsList.DataEntity>) intent.getSerializableExtra("dataEntities");
            } else if (from == Constant.FROMWAITPAY) {
                mOrderSubmit.setText("支付");
                waitPayOrder = (Bean_GoodsPayList.Order) intent.getSerializableExtra("orderPay");
                //此处转化一下数据格式，方便显示
                dataEntities = new ArrayList<>();
                if (waitPayOrder != null) {
                    waitOrderDatas = waitPayOrder.getItemList();
                    int n = waitOrderDatas.size();
                    for (int i = 0; i < n; i++) {
                        Bean_GoodsList.DataEntity dataEntity = new Bean_GoodsList.DataEntity();
                        dataEntity.setBuy_count(waitOrderDatas.get(i).get("num") + "");
                        dataEntity.setPrice((Double) waitOrderDatas.get(i).get("price"));
                        dataEntity.setSmall(waitOrderDatas.get(i).get("image") + "");
                        dataEntity.setName(waitOrderDatas.get(i).get("name") + "");
//                        dataEntity.setGoods_id(Integer.parseInt(waitOrderDatas.get(i).get("good_id") + ""));
                        dataEntities.add(dataEntity);
                    }
                }
            }
        }
        if (type == Constant.SHOP_TYPE_MONEY) {//现金支付的时候需要判断，从哪个页面跳转过来，因为两个页面使用的数据实体bean不一样
            from = intent.getIntExtra("from", 0);
            if (from == Constant.FROMDETIAL) {
                dataEntities = (List<Bean_GoodsList.DataEntity>) intent.getSerializableExtra("dataEntities");
            } else if (from == Constant.FROMWAITPAY) {
                mOrderSubmit.setText("支付");
                waitPayOrder = (Bean_GoodsPayList.Order) intent.getSerializableExtra("orderPay");
                //此处转化一下数据格式，方便显示
                dataEntities = new ArrayList<>();
                if (waitPayOrder != null) {
                    waitOrderDatas = waitPayOrder.getItemList();
                    int n = waitOrderDatas.size();
                    for (int i = 0; i < n; i++) {
                        Bean_GoodsList.DataEntity dataEntity = new Bean_GoodsList.DataEntity();
                        dataEntity.setBuy_count(waitOrderDatas.get(i).get("num") + "");
                        dataEntity.setPrice((Double) waitOrderDatas.get(i).get("price"));
                        dataEntity.setSmall(waitOrderDatas.get(i).get("image") + "");
                        dataEntity.setName(waitOrderDatas.get(i).get("name") + "");
                        dataEntities.add(dataEntity);
                    }
                }
            } else if (from == Constant.FROMSHOPCAR) {
                mOrderSubmit.setText("提交订单");
                goodslistEntities = (List<Bean_Shopcarlist.DataEntity.GoodslistEntity>) intent.getSerializableExtra("orderPay");
                //此处转化一下数据格式，方便显示
                dataEntities = new ArrayList<>();
                if (goodslistEntities != null) {
                    if (goodslistEntities != null) {
                        int n = goodslistEntities.size();
                        for (int i = 0; i < n; i++) {
                            Bean_GoodsList.DataEntity dataEntity = new Bean_GoodsList.DataEntity();
                            dataEntity.setBuy_count(goodslistEntities.get(i).getNum());
                            dataEntity.setPrice(Double.parseDouble(goodslistEntities.get(i).getPrice()));
                            dataEntity.setSmall(goodslistEntities.get(i).getImage_default());
                            dataEntity.setName(goodslistEntities.get(i).getName());
                            dataEntities.add(dataEntity);
                        }
                    }
                }
            }
        }

        //获取用户信息

        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getUserInfo, null, null, handler, Constant.GET_USERINFO);
        if (from == Constant.FROMWAITPAY)
            initView();
        else
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getDefultAddress, null, null, handler, Constant.GETDEFULTADDRESS);
    }

    //获取地址的时候调用的刷新界面
    private void initView() {
        if (from == Constant.FROMDETIAL || from == Constant.FROMSHOPCAR) {
            addAddressTV = (TextView) findViewById(R.id.add_address_tv);
            confirmLocationIV = (ImageView) findViewById(R.id.confirm_location_iv);
            mBottomMeasure = (TextView) findViewById(R.id.confirm_bottom_measure);
            mOrderUser = (TextView) findViewById(R.id.confirm_username_tv);
            mOrderPhone = (TextView) findViewById(R.id.confirm_userphone_tv);
            mOrderAddress = (TextView) findViewById(R.id.confirm_useraddress_tv);
            mOrderList = (ListViewForScrollView) findViewById(R.id.confirm_order_lv);
            mAddressLayout = (RelativeLayout) findViewById(R.id.confirmorder_userinfo);
            //如果获取到的userAddress为空
            if (userAddress == null) {
                confirmLocationIV.setVisibility(View.GONE);
                mOrderUser.setVisibility(View.GONE);
                mOrderPhone.setVisibility(View.GONE);
                mOrderAddress.setVisibility(View.GONE);
                addAddressTV.setVisibility(View.VISIBLE);
            }
            mAddressLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, MyAddressActivity.class);
                    intent.putExtra("type", AddressFragment.TYPE_SELECT_ADDRESS);
                    if (userAddress == null) {
                        intent.putExtra("isFromOrderEmpty", true);
                        startActivityForResult(intent, Constant.NEW_ADDRESS);
                    } else
                        startActivityForResult(intent, Constant.SELECT_ADDRESS);

                }
            });

            if (from == Constant.FROMWAITPAY) {
                mAddressLayout.setClickable(false);
            } else {
                mAddressLayout.setClickable(true);

            }

//        mMeasureView = (TextView) findViewById(R.id.confirm_measure);
//        mExpressPrice = (TextView) findViewById(R.id.confirm_express_price);
//        mOrderNumTV = (TextView) findViewById(R.id.confirm_ordernum_tv);
//        mOrderTotalPrice = (TextView) findViewById(R.id.confirm_totalprice_tv);
            mTotalCount = (TextView) findViewById(R.id.confirm_ordercount_tv);
            mTotalMoney = (TextView) findViewById(R.id.confirm_totalmoney_tv);

            //点击确认订单之后
            mOrderSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseApplication.isLogin) {
                        if (userAddress == null) {
                            ToastUtil.showToast(ConfirmOrderActivity.this, "当前没有收获地址，请先添加收货地址");
//                        Toast.makeText(ConfirmOrderActivity.this, "当前没有收获地址，请先添加收货地址！", Toast.LENGTH_SHORT).show();
                        } else {
                            if ((type == Constant.SHOP_TYPE_CREDIT && !isOrderSubmited) || (type == Constant.SHOP_TYPE_MONEY && !isOrderSubmited)) {
                                if (type == Constant.SHOP_TYPE_MONEY) {
                                    if (from == Constant.FROMWAITPAY) {
                                        mOrderSubmit.setEnabled(false);
                                        orderid = String.valueOf(waitPayOrder.getOrder_id());
                                        payment(orderid);
                                    } else {
                                        new AlertDialog.Builder(ConfirmOrderActivity.this).setMessage("请确认订单信息，订单提交后不可修改，是否提交?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mAddressLayout.setClickable(false);
                                                isOrderSubmited = true;
                                                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getPayType, null, null, handler, GET_PAY_TYPE);
                                                if (from == Constant.FROMSHOPCAR) {
                                                    createOrder(mOrderPhone.getText().toString().trim(), "");
                                                } else {
                                                    add2ShopCar();
                                                }
//                                            createOrder(mOrderPhone.getText().toString(), num);
                                            }
                                        }).setNegativeButton("否", null).show();
                                    }
                                } else {
                                    if (from == Constant.FROMWAITPAY) {
                                        orderid = String.valueOf(waitPayOrder.getOrder_id());
                                        if (isGetcredit) {
                                            showPopWindow(credit, orderid);
                                        } else {
                                            ToastUtil.showToast(ConfirmOrderActivity.this, "获取用户个人积分失败");
                                        }
                                    } else {
                                        new AlertDialog.Builder(ConfirmOrderActivity.this).setMessage("请确认订单信息，订单提交后不可修改，是否提交?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mAddressLayout.setClickable(false);
                                                isOrderSubmited = true;
                                                createOrder(mOrderPhone.getText().toString(), num);
                                            }
                                        }).setNegativeButton("否", null).show();
                                    }

                                }

                            } else {
                                showOrderCommitAlready();
                            }
                        }
                    } else {
                        ToastUtil.showToast(ConfirmOrderActivity.this, "请先登录");
//                    Toast.makeText(ConfirmOrderActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            if (dataEntities != null && dataEntities.size() > 0) {
                initImageLoaderParams();
                showOrderInfo();
            }
        } else {
            confirmLocationIV = (ImageView) findViewById(R.id.confirm_location_iv);
            mBottomMeasure = (TextView) findViewById(R.id.confirm_bottom_measure);
            mOrderUser = (TextView) findViewById(R.id.confirm_username_tv);
            mOrderPhone = (TextView) findViewById(R.id.confirm_userphone_tv);
            mOrderAddress = (TextView) findViewById(R.id.confirm_useraddress_tv);
            mOrderList = (ListViewForScrollView) findViewById(R.id.confirm_order_lv);
            mAddressLayout = (RelativeLayout) findViewById(R.id.confirmorder_userinfo);
            ImageView ivArrow = (ImageView) findViewById(R.id.iv_arrow);
            ivArrow.setVisibility(View.GONE);

            if (from == Constant.FROMWAITPAY) {
                mAddressLayout.setClickable(false);
            } else {
                mAddressLayout.setClickable(true);
            }

            mTotalCount = (TextView) findViewById(R.id.confirm_ordercount_tv);
            mTotalMoney = (TextView) findViewById(R.id.confirm_totalmoney_tv);

            //点击确认订单之后
            mOrderSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isOrderSubmited && !isCommitSuccess) {
                        if (type == Constant.SHOP_TYPE_MONEY) {
                            if (from == Constant.FROMWAITPAY) {
                                mOrderSubmit.setEnabled(false);
                                orderid = String.valueOf(waitPayOrder.getOrder_id());
                                payment(orderid);
                            }
                        } else if (type == Constant.SHOP_TYPE_CREDIT) {
                            if (from == Constant.FROMWAITPAY) {
                                orderid = String.valueOf(waitPayOrder.getOrder_id());
                                if (isGetcredit) {
                                    showPopWindow(credit, orderid);
                                } else {
                                    ToastUtil.showToast(ConfirmOrderActivity.this, "获取用户个人积分失败");
                                }
                            }

                        }

                    } else {
                        showOrderCommitAlready();
                    }
                }
            });

            if (dataEntities != null && dataEntities.size() > 0) {
                initImageLoaderParams();
                showOrderInfo();
            }
        }
    }

    private void showOrderCommitAlready() {
        if (type == Constant.SHOP_TYPE_MONEY)
            ToastUtil.showToast(ConfirmOrderActivity.this, "订单已提交不能重复提交，请到现金消费中查看");
        else if (type == Constant.SHOP_TYPE_CREDIT)
            ToastUtil.showToast(ConfirmOrderActivity.this, "订单已提交不能重复提交，请到积分消费中查看");
    }


    /**
     * <p>显示出确认订单页面的信息,从商品详情页跳转过来</p>
     */
    private void showOrderInfo() {
        ListAdapterConfirmOrder mOrderAdapter = new ListAdapterConfirmOrder(dataEntities, this, type, imageLoader);
        mOrderList.setAdapter(mOrderAdapter);
        int n = dataEntities.size();
        int count = 0;
        total = 0;
        for (int i = 0; i < n; i++) {
            count += Double.parseDouble(dataEntities.get(i).getBuy_count());
            total += Double.parseDouble(dataEntities.get(i).getBuy_count()) * dataEntities.get(i).getPrice();
        }
//        mOrderNumTV.setText("共"+count+"件商品，合计：");
//        mOrderTotalPrice.setText(""+total);
        mTotalCount.setText(count + "");
        if (type == Constant.SHOP_TYPE_MONEY) {
            mTotalMoney.setText(String.format("%.2f", total) + "元");
            mBottomMeasure.setText("件商品，总金额");
//            mMeasureView.setText("元");
        } else if (type == Constant.SHOP_TYPE_CREDIT) {
            mTotalMoney.setText(String.format("%d", (long) total) + "");
//            mMeasureView.setText("积分");
            mBottomMeasure.setText("件商品，总积分");
        }
        if (from == Constant.FROMWAITPAY) {
            mOrderUser.setText("收货人：" + waitPayOrder.getShip_name());
            mOrderPhone.setText(waitPayOrder.getShip_tel());
            mOrderAddress.setText("收货地址：" + waitPayOrder.getShip_addr());
            people = waitPayOrder.getShip_name();
            address = waitPayOrder.getShip_addr();

        } else {
            if (userAddress != null) {
                confirmLocationIV.setVisibility(View.VISIBLE);
                mOrderUser.setVisibility(View.VISIBLE);
                mOrderPhone.setVisibility(View.VISIBLE);
                mOrderAddress.setVisibility(View.VISIBLE);
                addAddressTV.setVisibility(View.GONE);
                mOrderUser.setText("收货人：" + userAddress.getName());
                mOrderPhone.setText(userAddress.getMobile());
                mOrderAddress.setText("收货地址：" + userAddress.getProvince() + userAddress.getCity() + userAddress.getRegion() + userAddress.getAddr() + " " + userAddress.getZip());
                people = userAddress.getName();
                address = userAddress.getProvince() + userAddress.getCity() + userAddress.getRegion() + userAddress.getAddr() + " " + userAddress.getZip();
            }
        }


    }

    private void initImageLoaderParams() {
        imageLoader = ImageLoader.getInstance();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }


    private void showPopWindow(double credit, final String orderid) {
        View view = LayoutInflater.from(this).inflate(R.layout.confirm_order_popwindow, null);
        TextView shouldpaytv = (TextView) view.findViewById(R.id.confirm_popwindow_shouldpoint_tv);
        TextView shouldpaymeasure = (TextView) view.findViewById(R.id.confirm_popwindow_point_tv);
        TextView leavepointtv = (TextView) view.findViewById(R.id.confirm_popwindow_leavepoint_tv);
        TextView leavepointmeasure = (TextView) view.findViewById(R.id.confirm_popwindow_point_tv1);
        ImageButton dismissButton = (ImageButton) view.findViewById(R.id.confirm_popwindow_dismiss);
        ImageView faceImageView = (ImageView) view.findViewById(R.id.confirm_popwindow_face);
        TextView userNameView = (TextView) view.findViewById(R.id.confirm_popwindow_name);
        TextView totalPointView = (TextView) view.findViewById(R.id.confirm_popwindow_totalpoints);
        TextView ownPointView = (TextView) view.findViewById(R.id.confirm_popwindow_ownpoints);
        TextView leavePointView = (TextView) view.findViewById(R.id.confirm_popwindow_leavepoint);
        TextView confirmButton = (TextView) view.findViewById(R.id.confirm_popwindow_confirmbtn);
        TextView line3 = (TextView) view.findViewById(R.id.confirm_popwindow_line3);
        //初始化
        mPopupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //获取窗口高度
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口高度
        mPopupWindow.setHeight(dm.heightPixels * 2 / 4);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(dw);
        //popwindow出现的时候，半透明
        backgroundAlpha(0.5f);
        // 在参照的View控件下方显示
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        mPopupWindow.showAtLocation(ConfirmOrderActivity.this.findViewById(R.id.start),
                Gravity.BOTTOM, 0, 0);
        // popWindow消失监听方法
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //窗体背景透明度取消
                backgroundAlpha(1f);
                System.out.println("popWindow消失");
            }
        });
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        if (userinfo != null && !userinfo.getFace().equals("")) {
            imageLoader.displayImage(userinfo.getFace(), faceImageView, optionhead);
        }
        if (userAddress != null && userAddress.getName().equals("")) {
            userNameView.setText("闽江路小米");
        } else {
            if (userAddress != null) {
                userNameView.setText(userAddress.getName());
            }
        }
        if (type == Constant.SHOP_TYPE_MONEY) {
            shouldpaytv.setText("应付金额");
            shouldpaymeasure.setText("元");
//            leavepointtv.setText("剩余金额");
            leavepointtv.setVisibility(View.GONE);
//            leavepointmeasure.setText("元");
            leavepointmeasure.setVisibility(View.GONE);
//            totalPointView.setText("账户总金额：" + credit + "元");
            totalPointView.setVisibility(View.GONE);
            leavePointView.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            ownPointView.setText(String.format("%.2f", total));
        } else if (type == Constant.SHOP_TYPE_CREDIT) {
            shouldpaytv.setText("应付积分");
            shouldpaymeasure.setText("积分");
            leavepointtv.setText("剩余积分");
            leavepointmeasure.setText("积分");
            totalPointView.setText("账户总积分：" + String.format("%d", (long) credit) + "积分");
            leavePointView.setText(String.format("%d", (long) (credit - total)) + "");
            ownPointView.setText(String.format("%d", (long) total) + "");
        }
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(orderid);
            }
        });
        if (type == Constant.SHOP_TYPE_CREDIT && credit < total) {
            ToastUtil.showToast(ConfirmOrderActivity.this, "积分不足");
//            Toast.makeText(ConfirmOrderActivity.this, "积分不足", Toast.LENGTH_LONG).show();
            confirmButton.setClickable(false);
        } else {
            confirmButton.setClickable(true);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void payUseUnion(String data) {
        if (data != null && orderid != null) {
            Intent intent1 = new Intent(this, UnionPayWebActivity.class);
            intent1.putExtra("data", data);
            intent1.putExtra("orderid", orderid);
            startActivityForResult(intent1, Constant.PAY_RESULT);
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        } else {
            mOrderSubmit.setEnabled(true);
        }
    }

    //提示是否查看订单详情
    public void isToMypay() {
        new AlertDialog.Builder(ConfirmOrderActivity.this).setMessage("支付成功查看订单详情？").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myPayIntent = null;
                if (type == Constant.SHOP_TYPE_CREDIT) {
                    myPayIntent = new Intent(ConfirmOrderActivity.this, MyCreditPayActivity.class);
                } else if (type == Constant.SHOP_TYPE_MONEY) {
                    myPayIntent = new Intent(ConfirmOrderActivity.this, MyMoneyPayActivity.class);
                }
                if (myPayIntent != null) {
                    myPayIntent.putExtra("detail", 1);
                    startActivity(myPayIntent);
                    finish();
                }
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //选择地址
            case Constant.SELECT_ADDRESS:
                if (resultCode == RESULT_OK && data != null) {
                    Addresses.DataEntity.AddressListEntity addressListEntity = data.getParcelableExtra("address");
                    if (addressListEntity != null) {
                        people = addressListEntity.getName();
                        address = addressListEntity.getProvince() + addressListEntity.getCity() + addressListEntity.getRegion() + addressListEntity.getAddr() + " " + addressListEntity.getZip();
                        mOrderUser.setText("收货人：" + addressListEntity.getName());
                        mOrderPhone.setText(addressListEntity.getMobile());
                        mOrderAddress.setText("收货地址：" + addressListEntity.getProvince() + addressListEntity.getCity() + addressListEntity.getRegion() + addressListEntity.getAddr() + " " + addressListEntity.getZip());
                        userAddress.setAddr_id(addressListEntity.getAddr_id());
                        userAddress.setAddr(addressListEntity.getAddr());
                        userAddress.setCity(addressListEntity.getCity());
                        userAddress.setCity_id(addressListEntity.getCity_id());
                        userAddress.setCountry(addressListEntity.getCountry());
                        userAddress.setDef_addr(addressListEntity.getDef_addr());
                        userAddress.setIsDel(addressListEntity.getIsDel());
                        userAddress.setMember_id(addressListEntity.getMember_id());
                        userAddress.setMobile(addressListEntity.getMobile());
                        userAddress.setName(addressListEntity.getName());
                        userAddress.setProvince(addressListEntity.getProvince());
                        userAddress.setProvince_id(addressListEntity.getProvince_id());
                        userAddress.setRegion(addressListEntity.getRegion());
                        userAddress.setRegion_id(addressListEntity.getRegion_id());
                        userAddress.setRemark(addressListEntity.getRemark());
                        userAddress.setTel(addressListEntity.getTel());
                        userAddress.setZip(addressListEntity.getZip());
                    } else {
//                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getCredit, null, null, handler, Constant.GETCREDIT);
                        initData();
                    }
                }
                break;
            //新增地址
            case Constant.NEW_ADDRESS:
//                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getCredit, null, null, handler, Constant.GETCREDIT);
                initData();
                break;
            case 10:
                if (data == null) {
                    return;
                }
                String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
                String str = data.getExtras().getString("pay_result");
                LogUtils.v("zftphone", "2 " + data.getExtras().getString("merchantOrderId"));
                if (str != null && str.equalsIgnoreCase("success")) {
                    msg = "支付成功！";

                } else if (str != null && str.equalsIgnoreCase("fail")) {
                    msg = "支付失败！";

                } else if (str != null && str.equalsIgnoreCase("cancel")) {
                    msg = "用户取消了支付";
                }
                //支付完成,处理自己的业务逻辑!
//        Toast.makeText(getApplicationContext(), msg, 0).show();
                break;
            //付款结果
            case Constant.PAY_RESULT:
                if (resultCode == RESULT_OK) {
                    isPaySuccess = true;
                }
                mOrderSubmit.setEnabled(true);
                break;
            default:
                break;
        }
    }


    //以前用银联控件支付的时候使用的，现在已经不需要了
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


    //以前使用银联控件使用的，现在不需要了
    public void Buy() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在调用银联...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        new Thread() {
            public void run() {

                String tn = null;
                InputStream is;
                try {

                    String url = TN_URL_01;

                    URL myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    ucon.setConnectTimeout(120000);
                    is = ucon.getInputStream();
                    int i = -1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    tn = baos.toString();
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = handler.obtainMessage(BUY_PAY);
                msg.obj = tn;
                handler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 以前使用银联控件使用的，现在不需要了。
     */
    public void doStartUnionPayPlugin(final Activity activity, String tn, String mode) {
//        int ret = UPPayAssistEx.startPay(activity, null, null, tn, mode);
//        if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
//            new android.app.AlertDialog.Builder(this).setMessage("要想银联付款，必须安装银联支付服务客户端，是否安装银联客户端？").setPositiveButton("安装", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    installUPPayPlugin(activity);
//                }
//            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();
////安装Asset中提供的UPPayPlugin.apk
//// 此处可根据实际情况，添加相应的处理逻辑
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

