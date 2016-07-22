package com.administrator.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Bean_GoodsPayList;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.ConfirmOrderActivity;
import com.administrator.elwj.MyPayDetails;
import com.administrator.elwj.R;
import com.administrator.utils.DateUtils;
import com.administrator.utils.LogUtils;
import com.administrator.utils.OrderStatus;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by acer on 2016/1/12.
 * 现金消费adapter
 */
public class ListAdapterMyMoneyPay extends BaseAdapter {
    private Context context;
    private List<Bean_GoodsPayList.Order> lists = new ArrayList<>();
    private static final int CANCEL = 1;
    private static final int CONFIRM_REC = 2;
    private BaseApplication application;
    private int mType;
    private String curDelOrderSN = null;
    private ImageView ivConfirmOrder = null;
    private int curRecPosition = -1;

    public static class MyHandler extends Handler {
        private WeakReference<ListAdapterMyMoneyPay> mMyPay;

        public MyHandler(ListAdapterMyMoneyPay myPay) {
            mMyPay = new WeakReference<ListAdapterMyMoneyPay>(myPay);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterMyMoneyPay myPay = mMyPay.get();
            if (myPay != null) {
                String json = (String) msg.obj;
                if (msg.what == CANCEL) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            if (myPay.curDelOrderSN != null) {
                                for (int i = 0; i < myPay.lists.size(); ++i) {
                                    if (myPay.lists.get(i).getSn().equals(myPay.curDelOrderSN)) {
                                        myPay.lists.remove(i);
                                        myPay.notifyDataSetChanged();
                                        ToastUtil.showToast(myPay.context, "取消订单成功");
                                        break;
                                    }
                                }
                            }
                            if (myPay.curRecPosition != -1) {
                                myPay.lists.get(myPay.curRecPosition).setOrderStatus("已收货");
                            }
                        } else {
                            ToastUtil.showToast(myPay.context, jsonObject.get("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == CONFIRM_REC) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            if (myPay.ivConfirmOrder != null) {
                                myPay.ivConfirmOrder.setImageResource(R.mipmap.confirmed_rec);
                                myPay.ivConfirmOrder = null;
                            }
                            ToastUtil.showToast(myPay.application, "确认收货成功");
                        } else {
                            ToastUtil.showToast(myPay.context,jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    public void addData(List<Bean_GoodsPayList.Order> lists, Context context) {
        //initImageLoader(context);
        this.lists = lists;
        notifyDataSetChanged();
        this.context = context;
    }

    public ListAdapterMyMoneyPay(Context context, BaseApplication appContext, int type) {
        this.context = context;
        application = appContext;
        mType = type;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {

        RelativeLayout rl;

        ImageView iv_cancel;//取消
        ImageView iv_pay;//支付
        ImageView more;//详情
        TextView tv_number;//订单号
        TextView tv_time;//时间
        TextView tv_num_text;//共几件
        TextView tv_allPrice;//总价
        TextView goods_state;
        ListView listview;
        TextView tv_danwei;
        TextView youfei;
        TextView youfei_num;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_pay_listitem, null);
            vh.rl = (RelativeLayout) convertView.findViewById(R.id.rl_my_pay_listitem);
            vh.iv_cancel = (ImageView) convertView.findViewById(R.id.my_cancel_image);
            vh.iv_pay = (ImageView) convertView.findViewById(R.id.my_pay_image);
            vh.tv_number = (TextView) convertView.findViewById(R.id.goods_number);
            vh.tv_time = (TextView) convertView.findViewById(R.id.goods_time);
            vh.tv_num_text = (TextView) convertView.findViewById(R.id.goods_text_1);
            vh.tv_allPrice = (TextView) convertView.findViewById(R.id.goods_text_2);
            vh.goods_state = (TextView) convertView.findViewById(R.id.goods_state);
            vh.listview = (ListView) convertView.findViewById(R.id.lounge_history_listView);
            vh.tv_danwei = (TextView) convertView.findViewById(R.id.goods_text_3);
            vh.more = (ImageView) convertView.findViewById(R.id.my_image);
            vh.youfei = (TextView) convertView.findViewById(R.id.youfei);
            vh.youfei_num = (TextView) convertView.findViewById(R.id.youfei_num);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Bean_GoodsPayList.Order bean = lists.get(position);
        java.util.List<Map<String, Object>> list = bean.getItemList();
        vh.goods_state.setText(bean.getPayStatus());
        LogUtils.e("List", list.toString());
        vh.tv_number.setText("订单号：" + bean.getSn());
        vh.tv_time.setText(DateUtils.format24Time(bean.getCreate_time() * 1000));

        vh.tv_allPrice.setText(String.valueOf(bean.getOrder_amount()));//总价

        vh.tv_num_text.setText("共" + String.valueOf(list.size()) + "件商品，合计：");
        if (bean.getPayment_name().equals("积分支付")) {
            vh.tv_danwei.setText("积分");
            vh.youfei.setText("积分");
        } else {
            vh.tv_danwei.setText("元");
            vh.youfei.setText("元");
        }
        Integer payStatus = bean.getPay_status();
        if (mType == Constant.WAIT_PAY) {
            vh.iv_pay.setVisibility(View.VISIBLE);
            //支付功能
            vh.iv_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConfirmOrderActivity.class);
                    intent.putExtra("type", Constant.SHOP_TYPE_MONEY);
                    intent.putExtra("from", Constant.FROMWAITPAY);
                    intent.putExtra("orderPay", lists.get(position));
                    intent.putExtra("num", String.valueOf(lists.get(position).getItemList().get(0).get("num")));
                    context.startActivity(intent);
                }

            });
            vh.iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == Constant.WAIT_PAY) {
                        new AlertDialog.Builder(context).setMessage("确定取消订单吗？").setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                curDelOrderSN = lists.get(position).getSn();
                                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.cancelPayOrder, new String[]{"sn"}, new String[]{lists.get(position).getSn()}, handler, CANCEL);
                            }
                        }).setNegativeButton("保留订单", null).show();
                    }
                }
            });
            vh.goods_state.setTextColor(context.getResources().getColor(R.color.red));
        } else if (mType == Constant.PATED) {
            vh.iv_cancel.setVisibility(View.GONE);
            if (bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP)) || bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM))) {
                vh.iv_pay.setImageResource(R.mipmap.confirm_rec);
                vh.iv_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context).setMessage("确认收货吗？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ivConfirmOrder = vh.iv_pay;
                                curRecPosition = position;
                                VolleyUtils.NetUtils(application.getRequestQueue(),Constant.baseUrl + Constant.confirmRec,new String[]{"orderid"},new String[]{String.format("%d", lists.get(position).getOrder_id())},handler,CONFIRM_REC);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                });
            } else if (bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG)) || bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE))) {
                vh.iv_pay.setImageResource(R.mipmap.confirmed_rec);
                vh.iv_pay.setOnClickListener(null);
            }
            vh.goods_state.setTextColor(context.getResources().getColor(R.color.blue));

        } else if (mType == Constant.ALL_PAY) {
            if (bean.getPay_status() == 1 || bean.getPay_status() == 0) {
                vh.iv_pay.setVisibility(View.VISIBLE);
                vh.iv_cancel.setVisibility(View.VISIBLE);
                vh.iv_pay.setImageResource(R.mipmap.payorder);
                //支付功能
                vh.iv_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ConfirmOrderActivity.class);
                        intent.putExtra("type", Constant.SHOP_TYPE_MONEY);
                        intent.putExtra("from", Constant.FROMWAITPAY);
                        intent.putExtra("orderPay", lists.get(position));
                        intent.putExtra("num", String.valueOf(lists.get(position).getItemList().get(0).get("num")));
                        context.startActivity(intent);
                    }

                });
                vh.iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context).setMessage("确定取消订单吗？").setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                curDelOrderSN = lists.get(position).getSn();
                                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.cancelPayOrder, new String[]{"sn"}, new String[]{lists.get(position).getSn()}, handler, CANCEL);
                            }
                        }).setNegativeButton("保留订单", null).show();

                    }
                });
                vh.goods_state.setTextColor(context.getResources().getColor(R.color.red));
            } else if (bean.getPay_status() == 2) {
                vh.iv_cancel.setVisibility(View.GONE);
                vh.iv_pay.setVisibility(View.VISIBLE);
                vh.iv_pay.setImageResource(R.mipmap.confirm_rec);
                if (bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP)) || bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM))) {
                    vh.iv_pay.setImageResource(R.mipmap.confirm_rec);
                    vh.iv_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivConfirmOrder = vh.iv_pay;
                            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.confirmRec, new String[]{"orderid"}, new String[]{String.format("%d", lists.get(position).getOrder_id())}, handler, CONFIRM_REC);
                        }
                    });
                } else if (bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG)) || bean.getOrderStatus().equals(OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE))) {
                    vh.iv_pay.setImageResource(R.mipmap.confirmed_rec);
                    vh.iv_pay.setOnClickListener(null);
                }
                vh.goods_state.setTextColor(context.getResources().getColor(R.color.blue));
            }
        }


        vh.youfei_num.setText(String.valueOf(bean.getShipping_amount()));
        ListAdapterInSet adapter = new ListAdapterInSet();
        adapter.addData(list, context, bean.getPayment_name());
        vh.listview.setAdapter(adapter);
        setListViewHeightBasedOnChildren(vh.listview);
        vh.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyPayDetails.class);
                intent.putExtra("bean", bean);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
