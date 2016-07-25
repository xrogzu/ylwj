package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/11.
 * 金融管家-》家庭财富 点击进入
 */
public class HotProductTypeActivity extends AppCompatActivity implements  View.OnClickListener {

    public static final int BACHELORDOM_LIFE = 1;//单身生活
    public static final int THE_COUPLE = 2;//两口之家
    public static final int HAPPY_LIFT=3;//幸福家庭
    public static final int OLD_AGE_IS_SECURED=4;//晚年无忧
    private int mType = 0;
    private ImageView product_bg, prpduct_enter,ib_back;
    private TextView hot_product_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_product_type);
        initView();
        initIntentData();
        setBackground();
    }

    private void initView() {
        product_bg= (ImageView) findViewById(R.id.product_bg);
        prpduct_enter= (ImageView) findViewById(R.id.prpduct_enter);
        ib_back= (ImageView) findViewById(R.id.ib_back);
        hot_product_title= (TextView) findViewById(R.id.hot_product_title);
        ib_back.setOnClickListener(this);
        prpduct_enter.setOnClickListener(this);

    }


    private void setBackground() {
        switch (mType){
            case BACHELORDOM_LIFE:
                product_bg.setImageResource(R.mipmap.bachelordom_bg);
                prpduct_enter.setImageResource(R.mipmap.enter1);
                hot_product_title.setText("单身生活");
                break;
            case THE_COUPLE:
                product_bg.setImageResource(R.mipmap.the_couple_bg);
                prpduct_enter.setImageResource(R.mipmap.enter2);
                hot_product_title.setText("两口之家");
                break;
            case HAPPY_LIFT:
                product_bg.setImageResource(R.mipmap.happy_life_bg);
                prpduct_enter.setImageResource(R.mipmap.enter3);
                hot_product_title.setText("幸福家庭");
                break;
            case OLD_AGE_IS_SECURED:
                product_bg.setImageResource(R.mipmap.old_age_is_secured_bg);
                prpduct_enter.setImageResource(R.mipmap.enter4);
                hot_product_title.setText("晚年无忧");

                break;
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null)
            mType = intent.getIntExtra("type", 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prpduct_enter:
                Intent intent = new Intent(this, FinancialProductActivity.class);
                intent.putExtra("type", mType);
                startActivity(intent);
                break;
            case R.id.ib_back:
                this.finish();
                break;
        }
    }
}
