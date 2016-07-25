package com.administrator.elwj;


import android.content.Intent;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterDetails;
import com.administrator.adapter.ListAdapterInSet;
import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Bean_GoodsPayList;
import com.administrator.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 订单详情界面
 */
public class MyPayDetails extends AppCompatActivity implements View.OnClickListener {
    private Bean_GoodsPayList.Order beanDetails;

    private java.util.List<Map<String, Object>> List=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pay_details);

        Intent intent = getIntent();
        beanDetails = (Bean_GoodsPayList.Order) intent.getSerializableExtra("bean");
        List=beanDetails.getItemList();

        initViews();
    }

    private void initViews() {
        ImageView btn_cancel = (ImageView) findViewById(R.id.details_cancel_image);
        ImageView btn_pay = (ImageView) findViewById(R.id.details_pay_image);

        Integer payStatus=beanDetails.getPay_status();
        switch (payStatus){
            case 2:
                btn_cancel.setVisibility(View.INVISIBLE);
                btn_pay.setImageResource(R.mipmap.button_hulue);
                break;
        }

        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        TextView tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("订单详情");

        TextView orderNumber = (TextView) findViewById(R.id.details_goods_number);
        orderNumber.setText("订单号：" + beanDetails.getSn());

        TextView orderState = (TextView) findViewById(R.id.order_state);

        Integer Status=beanDetails.getPay_status();
        if (Status==2){

            orderState.setTextColor(this.getResources().getColor(R.color.blue));

        }else{
            orderState.setTextColor(this.getResources().getColor(R.color.red));
        }
        orderState.setText(beanDetails.getPayStatus());

        TextView total_price = (TextView) findViewById(R.id.all_price);

        if(beanDetails.getPayment_name().equals("积分支付")){
            total_price.setText(String.valueOf(beanDetails.getOrder_amount()) + " 积分");
        }else{
            total_price.setText("￥" + String.valueOf(beanDetails.getOrder_amount()));
        }


        TextView receiverName = (TextView) findViewById(R.id.receiver_name);
        receiverName.setText("收件人：" + beanDetails.getShip_name() + "   " + beanDetails.getShip_mobile());

        TextView address = (TextView) findViewById(R.id.address);
        address.setText("收货地址：" + beanDetails.getShip_addr());

        TextView time = (TextView) findViewById(R.id.details_goods_time);
        time.setText(DateUtils.format24Time(beanDetails.getCreate_time() * 1000));


        ListView listView = (ListView) findViewById(R.id.my_pay_details_list);
        ListAdapterDetails adapter = new ListAdapterDetails();
        adapter.addData(List, MyPayDetails.this, beanDetails.getPayment_name());
        listView.setAdapter(adapter);


        ImageButton ib_shoppingcar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        //ib_shoppingcar.setVisibility(View.INVISIBLE);
        ib_back.setOnClickListener(MyPayDetails.this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://回退按钮
                finish();
                break;
        }
    }





    }

