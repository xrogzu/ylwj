package com.administrator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.R;

/**
 * 引导页2
 * Created by Administrator on 2016/3/6.
 */
public class GuideTwoFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_guide_2,null);
        initViews();
        return mView;
    }

    private void initViews() {
        ImageView imageView = (ImageView) mView.findViewById(R.id.guide_button);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        start();
    }

    private void start(){
        Intent intent = new Intent(getContext(),HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
