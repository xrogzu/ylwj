package com.administrator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.administrator.adapter.ListAdapterAddress;
import com.administrator.bean.Addresses;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.EditAddressActivity;
import com.administrator.elwj.MyAddressActivity;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;

/**
 * 地址管理fragment
 * Created by xu on 2016/3/2.
 */
public class AddressFragment extends BaseFragment {

    private View mView;
    //列表
    private XListView mListView;
    //listview为空的时候显示的view
    private RelativeLayout noAddressLayout;
    //适配器
    private ListAdapterAddress mAdapter;
    //fragment类型
    private int mType = TYPE_MANAGE_ADDRESS;
    //管理地址
    public static final int TYPE_MANAGE_ADDRESS = 0;
    //选择地址
    public static final int TYPE_SELECT_ADDRESS = 1;

    public static class MyHandler extends Handler{
        private WeakReference<AddressFragment> mFragment;
        public MyHandler(AddressFragment fragment){
            mFragment = new WeakReference<AddressFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            AddressFragment fragment = mFragment.get();
            if(fragment != null) {
                fragment.mListView.stopLoadMore();
                fragment.mListView.stopRefresh();
                if (msg.what == GET_ADDRESS) {
                    String json = (String) msg.obj;
                    fragment.mListView.setEmptyView(fragment.noAddressLayout);
                    fragment.noAddressLayout.setVisibility(View.VISIBLE);
                    if (json != null) {
                        LogUtils.d("xu_test_address", json);
                        Gson gson = new Gson();
                        Addresses addresses = gson.fromJson(json, Addresses.class);
                        if (addresses != null) {
                            fragment.mAdapter.addData(addresses.getData().getAddressList());
                        }else {
                            MyAddressActivity addressActivity = (MyAddressActivity)fragment.getActivity();
                            addressActivity.setRightTopVisible(false);
                        }
                    }
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);
    private static final int GET_ADDRESS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_address, null);
        initViews();
        return mView;
    }

    private void initViews() {
        mListView = (XListView) mView.findViewById(R.id.listview_address);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        noAddressLayout = (RelativeLayout) mView.findViewById(R.id.empty_address_rl);
        noAddressLayout.setVisibility(View.GONE);
        ImageView addAddressView = (ImageView) mView.findViewById(R.id.add_address_iv);
        addAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditAddressActivity.class);
                //此处需使用getActivity()调用startActivityForResult方法，以便直接在当前Activity中接收result
                getActivity().startActivityForResult(intent, Constant.ADD_ADDRESS_REQUEST_CODE);
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt("type");
        } else {
            LogUtils.d("xu_test", "未能获取到address fragment的类型");
        }

        mAdapter = new ListAdapterAddress((BaseApplication) getActivity().getApplication(),getActivity(), mType);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        VolleyUtils.NetUtils(((BaseApplication) getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getRecAddress, null, null, handler, GET_ADDRESS);
    }

    /**
     * 添加地址
     * @param addressListEntity
     */
    public void addAddress(Addresses.DataEntity.AddressListEntity addressListEntity){
        if(mType == TYPE_MANAGE_ADDRESS && mAdapter != null && addressListEntity != null){
            mAdapter.addData(addressListEntity);
        }
    }

    public void updateAddress(Addresses.DataEntity.AddressListEntity addressListEntity){
        if(mType == TYPE_MANAGE_ADDRESS && mAdapter != null && addressListEntity !=  null){
            mAdapter.updateData(addressListEntity);
        }
    }
}
