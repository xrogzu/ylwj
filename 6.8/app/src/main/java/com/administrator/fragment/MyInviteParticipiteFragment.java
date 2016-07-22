package com.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.ListAdapterBigstageHome;
import com.administrator.bean.Constant;
import com.administrator.elwj.R;
import com.administrator.fragment.BaseFragment;
import com.library.listview.XListView;

/**
 * 前人写的fragment，目前没有用到，可以删除
 * Created by acer on 2016/1/25.
 */
public class MyInviteParticipiteFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.fragment_financial_expert,null);
        XListView listView = (XListView) mView.findViewById(R.id.listView_expert);
        listView.setDivider(null);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        ListAdapterBigstageHome adapter=new ListAdapterBigstageHome(getActivity(),Constant.MY_INVITE);
        listView.setAdapter(adapter);
        return mView;
    }
}
