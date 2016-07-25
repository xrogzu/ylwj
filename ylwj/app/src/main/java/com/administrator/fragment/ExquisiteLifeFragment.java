package com.administrator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.ExquisiteLifeActivity;
import com.administrator.elwj.ExquisiteLifeDetailActivity;
import com.administrator.elwj.ExquisiteLifeListActivity;
import com.administrator.elwj.ExquisiteLifeServiceActivity;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.R;

/**
 * 精致生活引导页 fragment
 * Created by Administrator on 2016/3/12.
 */
public class ExquisiteLifeFragment extends BaseFragment implements View.OnClickListener{

    //fragment的view
    private View mView;

    private BaseApplication appContext;
    private ImageView button1,button2,button3;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_exquisite_life, null);
        appContext = (BaseApplication) getActivity().getApplication();
        initViews();
        return mView;
    }

    //初始化view
    private void initViews() {
        button1= (ImageView) mView.findViewById(R.id.button1);
        button2= (ImageView) mView.findViewById(R.id.button2);
        button3= (ImageView) mView.findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


        //右上角的返回键
        ImageView ivBack = (ImageView) mView.findViewById(R.id.ib_back);
       ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1: {
//                Intent intent = new Intent(getActivity(), ExquisiteLifeActivity.class);
//                intent.putExtra("type", ExquisiteLifeActivity.TYPE_HUAXIA_BANK);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), ExquisiteLifeListActivity.class);
                intent.putExtra("title", "银行信用卡服务");
                startActivity(intent);
            }
                break;
            case R.id.button2:
            {
//                Intent intent = new Intent(getActivity(), ExquisiteLifeDetailActivity.class);
//                intent.putExtra("type", ExquisiteLifeActivity.TYPE_HUAXIA_VIP);
//                startActivity(intent);
                  Intent intent = new Intent(getActivity(), ExquisiteLifeListActivity.class);
                  intent.putExtra("title","银行VIP服务");
                  startActivity(intent);
            }
                break;
            case R.id.button3:
            {
                Intent intent = new Intent(getActivity(), ExquisiteLifeListActivity.class);
                intent.putExtra("title","银行特约商户");
                startActivity(intent);
            }
                break;
            case R.id.ib_back:
                ((HomeActivity) getActivity()).popLifeFragment();
                break;
        }
    }
}
