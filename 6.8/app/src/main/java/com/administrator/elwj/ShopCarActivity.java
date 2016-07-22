package com.administrator.elwj;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterShopCar;
import com.administrator.bean.Addresses;
import com.administrator.bean.Bean_Shopcarlist;
import com.administrator.bean.Constant;
import com.administrator.bean.OrderBackBean;
import com.administrator.fragment.AddressFragment;
import com.administrator.minterface.changeAllprice;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车界面
 */

public class ShopCarActivity extends AppCompatActivity implements changeAllprice, View.OnClickListener {

    public ShopCarActivity getInstance() {
        return ShopCarActivity.this;
    }

    private Button bt_pay;
    private TextView tv_price;
    private TextView all_set;
    private List<Bean_Shopcarlist.DataEntity.GoodslistEntity> lists = new ArrayList<>();
    private List<Bean_Shopcarlist.DataEntity.GoodslistEntity> lists_selected = new ArrayList<>();
    private BaseApplication appContext;
    private int checkNum; // 记录选中的条目数量
    private SharedPreferences sharedPreferences;

    private boolean isPaySuccess = false;

    private String orderID;//订单id

    private Addresses.DataEntity.AddressListEntity userAddress;//收货地址

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_shopcar_pay:
                if(isPaySuccess){
                    ToastUtil.showToast(this,"已经支付成功");
                }else {
                    Intent intent1 = new Intent(this,ConfirmOrderActivity.class);
                    intent1.putExtra("from",Constant.FROMSHOPCAR);
                    intent1.putExtra("orderPay",(Serializable)adapter.getData());
                    intent1.putExtra("type",Constant.SHOP_TYPE_MONEY);
                    startActivity(intent1);
//                    Intent intent = new Intent(this, MyAddressActivity.class);
//                    intent.putExtra("type", AddressFragment.TYPE_SELECT_ADDRESS);
//                    startActivityForResult(intent, Constant.SELECT_ADDRESS);
                }
                break;
        }
    }

    private void pay() {
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
                    "remark"
            };
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.create_order_money, parameter, params, handler, Constant.GET_ORDERID);
        }


    }

    public static class MyHandler extends Handler {
        private WeakReference<ShopCarActivity> mActivity;

        public MyHandler(ShopCarActivity activity) {
            mActivity = new WeakReference<ShopCarActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            ShopCarActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;

                switch (which) {
                    case Constant.GET_ALLSHOPCAR:
                        String json = (String) msg.obj;
                        LogUtils.e("SHOPCAR", json);
                        int count = 0;
                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONObject data = obj.getJSONObject("data");
                            count = data.optInt("count");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (count == 0) {
                            ToastUtil.showToast(activity, "购物车是空的哦，快去逛逛吧");
//                            Toast.makeText(activity, "购物车是空的哦，快去逛逛吧", Toast.LENGTH_SHORT).show();
                            activity.bt_pay.setClickable(false);
                            activity.sharedPreferences.edit().clear().commit();
//                        ShopCarActivity.this.finish();
//                        Bean_Shopcarlist bean_shopcarlist=new Bean_Shopcarlist();
//                        adapter.addData(bean_shopcarlist,ShopCarActivity.this,appContext.getRequestQueue());
                            activity.adapter.addData(null, activity, activity.appContext.getRequestQueue());
                            activity.tv_price.setText("￥0");
                        } else {
                            activity.lists.clear();
                            Gson gson = new Gson();
                            Bean_Shopcarlist bean_shopcarlist = gson.fromJson(json, Bean_Shopcarlist.class);
                            activity.adapter.addData(bean_shopcarlist, activity, activity.appContext.getRequestQueue());
                            activity.lists = bean_shopcarlist.getData().getGoodslist();

                        }
                        break;
                    case Constant.ISLOGIN:
                        String logjson = (String) msg.obj;
//                    LogUtils.e("SHOPCAR","登录状态"+logjson);
                        break;
                    case Constant.GoodsPrice:
                        String priceJson = (String) msg.obj;
                        try {
                            JSONObject object = new JSONObject(priceJson);
                            JSONObject data = object.getJSONObject("data");
                            int goodsPrice = data.optInt("goodsPrice");
                            LogUtils.e("wj", goodsPrice + "");
//                            tv_price.setText("￥" + goodsPrice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.CART_CLEAN:
                        String cleanJson = (String) msg.obj;
                        try {
                            JSONObject object = new JSONObject(cleanJson);
                            int result = object.optInt("result");
                            if (result == 1) {
                                ToastUtil.showToast(activity, "已经清空");
//                                Toast.makeText(activity, "已经清空", Toast.LENGTH_SHORT).show();
                                activity.tv_price.setText("￥0");
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getAllShopcar, null, null, activity.handler, Constant.GET_ALLSHOPCAR);
                            } else {
                                ToastUtil.showToast(activity, "清空失败");
//                                Toast.makeText(activity, "清空失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.GET_ORDERID:
                        JSONObject jsonObject;
                        try {
                            json = (String) msg.obj;
                            jsonObject = new JSONObject(json);
                            LogUtils.i("wj", "订单信息" + json);
                            int result = jsonObject.optInt("result");
                            String message = jsonObject.optString("message");
                            if (result == 0) {
                                ToastUtil.showToast(activity, message);
                            } else if (result == 1) {
                                LogUtils.i("wj", "提交订单成功");

                                Gson gson = new Gson();
                                OrderBackBean orderBackBean = gson.fromJson(json, OrderBackBean.class);
                                if (orderBackBean != null) {
                                    if (orderBackBean.getResult() == 1) {
                                        OrderBackBean.OrderEntity orderEntity = orderBackBean.getOrder();
                                        activity.orderID = orderEntity.getOrder_id();
                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.doPayment_point, new String[]{"orderid"}, new String[]{activity.orderID}, activity.handler, Constant.DO_PAYMENT);
                                    } else {
                                        ToastUtil.showToast(activity, "获取订单返回信息失败");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.DO_PAYMENT:
                        activity.payUseUnion((String) msg.obj);
                        break;
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);
    private ListAdapterShopCar adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        appContext = (BaseApplication) getApplication();

        sharedPreferences = getSharedPreferences("shopCar", MODE_PRIVATE);

        initViews();
    }


    private void initViews() {
        CheckBox mAllchecked = (CheckBox) findViewById(R.id.toilet_history_allchecked);
        mAllchecked.setOnCheckedChangeListener(listenera);
        TextView tv_tile = (TextView) findViewById(R.id.title);
        all_set = (TextView) findViewById(R.id.all_set);
        tv_tile.setText("购物车");
        ListView listView = (ListView) findViewById(R.id.listview_shopcar);
        adapter = new ListAdapterShopCar(this, this, sharedPreferences);
        listView.setAdapter(adapter);

        /**
         * @显示菜单点击事件监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
//                pos = position;// 当前点击位置
                ListAdapterShopCar.ShopCarViewHolder holder = (ListAdapterShopCar.ShopCarViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                adapter.getIsSelected().put(position, holder.cb.isChecked());
                // 调整选定条目
                if (holder.cb.isChecked()) {
                    checkNum++;
                } else {
                    checkNum--;
                }
            }
        });

        ImageButton ib_back = (ImageButton) findViewById(R.id.hot_ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button bt_clear = (Button) findViewById(R.id.bt_shopcar_clear);

        bt_pay = (Button) findViewById(R.id.bt_shopcar_pay);
        bt_pay.setOnClickListener(this);

        tv_price = (TextView) findViewById(R.id.tv_shopcar_price);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cartClean, null, null, handler, Constant.CART_CLEAN);
            }
        });
    }

    @Override
    public void change() {
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsPrice, null, null, handler, Constant.GoodsPrice);
    }

    /**
     * 全选按钮监听
     */
    private OnCheckedChangeListener listenera = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // cBox1被选中
            if (buttonView.getId() == R.id.toilet_history_allchecked) {
                if (isChecked) {

                    for (int i = 0; i < lists.size(); i++) {
                        adapter.getIsSelected().put(i, true);
                    }
                    adapter.notifyDataSetChanged();
                    all_set.setText("取消全选");
                } else {
                    for (int i = 0; i < lists.size(); i++) {
                        adapter.getIsSelected().put(i, false);
                    }
                    adapter.notifyDataSetChanged();
                    all_set.setText("全选");
                }
            }

        }
    };

    public void setPrice(String price) {
        tv_price.setText(price);
    }

    public void setBt_payClickable(boolean clickable) {
        bt_pay.setClickable(clickable);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.SELECT_ADDRESS:
                if (resultCode == RESULT_OK && data != null) {
                    userAddress = data.getParcelableExtra("address");
                    if (userAddress != null) {
                        new AlertDialog.Builder(this).setMessage("确定结算？").setPositiveButton("结算", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pay();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    } else {
                        LogUtils.d("xu", "没有获取到收货地址");
                    }
                } else {
                    LogUtils.d("xu", "没有获取到收货地址");
                }
                break;
            case Constant.PAY_RESULT:
                if(resultCode == RESULT_OK){
                    isPaySuccess = true;
                }
                break;
        }
    }

    //银联结算
    private void payUseUnion(String data) {
        if (data != null && orderID != null) {
            Intent intent1 = new Intent(this, UnionPayWebActivity.class);
            intent1.putExtra("data", data);
            intent1.putExtra("orderid",orderID);
            startActivityForResult(intent1, Constant.PAY_RESULT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getAllShopcar, null, null, handler, Constant.GET_ALLSHOPCAR);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsPrice, null, null, handler, Constant.GoodsPrice);
    }
}
