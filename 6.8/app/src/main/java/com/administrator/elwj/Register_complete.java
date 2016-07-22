package com.administrator.elwj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

//前人写的activity，目前没有用到，可以删掉
public class Register_complete extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);
        initView();
    }

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.title);
        TextView tv_regedit = (TextView) findViewById(R.id.regedit);
        tv_title.setText(R.string.regedit_complete);
        tv_regedit.setText(R.string.title_activity_justlogin);
        tv_regedit.setVisibility(View.GONE);
        tv_regedit.setOnClickListener(this);
        Button bt_binding = (Button) findViewById(R.id.bt_binding);
        Button bt_perfect = (Button) findViewById(R.id.bt_perfect);
        Button bt_survey = (Button) findViewById(R.id.bt_survey);
        Button bt_access = (Button) findViewById(R.id.bt_access);
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);

        ib_back.setOnClickListener(this);
        bt_binding.setOnClickListener(this);
        bt_perfect.setOnClickListener(this);
        bt_survey.setOnClickListener(this);
        bt_access.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_binding://立即绑定
                break;
            case R.id.bt_perfect://去完善
                break;
            case R.id.bt_survey://参加调查
                break;
            case R.id.bt_access://立即进入
                finish();
                break;
            case R.id.ib_back://回退按钮
                finish();
                break;
            case R.id.regedit://登录
                Intent intent2=new Intent(Register_complete.this,LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
