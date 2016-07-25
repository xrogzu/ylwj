package com.administrator.elwj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/14.
 * 精致生活 -》精致生活服务类
 */
public class ExquisiteLifeServiceActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ib_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exquisite_life_service);
        initView();
    }

    private void initView() {
        ib_back= (ImageView) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ib_back:
                this.finish();
                break;
        }
    }
}
