package com.administrator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.administrator.elwj.HotProductTypeActivity;
import com.administrator.elwj.R;

/**
 * 家庭财富fragment
 * Created by Administrator on 2015/12/29.
 */
public class HotProductFragment extends BaseFragment implements View.OnClickListener {

    private ImageView bachelordom,old_age_is_secured,the_couple,happy_life;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.fragment_hotproduct, null);
        initView();
        return mView;
    }

    private void initView() {
        bachelordom= (ImageView) mView.findViewById(R.id.bachelordom);
        old_age_is_secured= (ImageView) mView.findViewById(R.id.old_age_is_secured);
        the_couple= (ImageView) mView.findViewById(R.id.the_couple);
        happy_life= (ImageView) mView.findViewById(R.id.happy_life);
        bachelordom.setOnClickListener(this);
        old_age_is_secured.setOnClickListener(this);
        the_couple.setOnClickListener(this);
        happy_life.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bachelordom: {
                Intent intent = new Intent(getContext(), HotProductTypeActivity.class);
                intent.putExtra("type", HotProductTypeActivity.BACHELORDOM_LIFE);
                startActivity(intent);
            }
                break;
            case R.id.old_age_is_secured: {
                Intent intent = new Intent(getContext(), HotProductTypeActivity.class);
                intent.putExtra("type", HotProductTypeActivity.OLD_AGE_IS_SECURED);
                startActivity(intent);
            }
                break;
            case R.id.the_couple:{
                Intent intent = new Intent(getContext(), HotProductTypeActivity.class);
                intent.putExtra("type", HotProductTypeActivity.THE_COUPLE);
                startActivity(intent);
            }
                break;
            case R.id.happy_life:{
                Intent intent = new Intent(getContext(), HotProductTypeActivity.class);
                intent.putExtra("type", HotProductTypeActivity.HAPPY_LIFT);
                startActivity(intent);
            }
                break;

        }
    }

}
