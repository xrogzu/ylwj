package com.administrator.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.ListAdapterInteral_interal;
import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.Interalshop_interalactivity;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu on 2016/2/28.
 */
@SuppressLint("ValidFragment")
public class Internalshop_internalItemFragment extends BaseFragment {
    private Interalshop_interalactivity activity;

    private View mView;
    private BaseApplication appContext;
    private ListAdapterInteral_interal adapter;
    private XListView listView;
    private List<Bean_GoodsList.DataEntity> myLists = new ArrayList<>();
    private int currPage = 1;
    private int pageSize = 20;
    private static double JIFEN_0 = 0;
    private static double JIFEN_1 = 50000.1;
    private static double JIFEN_2 = 100000.1;
    private static double JIFEN_3 = 150000.1;
    private static double JIFEN_4 = 100000000;
    private int which = Constant.LESS_FIVE;



    public Internalshop_internalItemFragment(Interalshop_interalactivity activity, int which) {
        this.which = which;
        this.activity = activity;
    }

    public static class MyHandler extends Handler{
        private WeakReference<Internalshop_internalItemFragment> mFragment;
        public MyHandler(Internalshop_internalItemFragment fragment){
            mFragment = new WeakReference<Internalshop_internalItemFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            Internalshop_internalItemFragment fragment = mFragment.get();
            if(fragment != null) {
                fragment.listView.stopLoadMore();
                fragment.listView.stopRefresh();
                if(fragment.activity != null){
                    fragment.activity.dismissProcessDialog();
                }

                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.GOODS_LIST) {
                    Gson gson = new Gson();
                    Bean_GoodsList bean_goodsList = gson.fromJson(json, Bean_GoodsList.class);
                    List<Bean_GoodsList.DataEntity> dataLists = bean_goodsList.getData();
                    if (fragment.currPage == 1) {
                        fragment.myLists.clear();
                    }
                    fragment.myLists.addAll(dataLists);
                    fragment.adapter.addData(fragment.myLists, fragment.getActivity());
                }
            }
        }
    }
    private Handler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_internalshop_internal_recommend, null);
        appContext = (BaseApplication) getActivity().getApplication();


        if (which == Constant.LESS_FIVE) {//
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{"1", String.format("%d", pageSize), String.format("%f",JIFEN_0), String.format("%f",JIFEN_1)}, handler, Constant.GOODS_LIST);
        }else if(which==Constant.FIVE_TEN){
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit","maxcredit"}, new String[]{"1", String.format("%d",pageSize), String.format("%f",JIFEN_1), String.format("%f",JIFEN_2)}, handler, Constant.GOODS_LIST);
        }else if(which==Constant.TEN_FIF){
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit","maxcredit"}, new String[]{"1", String.format("%d",pageSize), String.format("%f",JIFEN_2), String.format("%f",JIFEN_3)}, handler, Constant.GOODS_LIST);
        }else{
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit","maxcredit"}, new String[]{"1",String.format("%d",pageSize), String.format("%f",JIFEN_3), String.format("%f",JIFEN_4)}, handler, Constant.GOODS_LIST);
        }


//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{"20", "89", "1"}, handler, Constant.GOODS_LIST);
        initView();
        return mView;
    }


    private void initView(){
        listView = (XListView) mView.findViewById(R.id.listView_integralshop);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                if (which == Constant.LESS_FIVE) {//
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{"1", String.format("%d", pageSize), String.format("%f",JIFEN_0), String.format("%f",JIFEN_1)}, handler, Constant.GOODS_LIST);
                }
                if (which == Constant.FIVE_TEN) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{"1", String.format("%d",pageSize), String.format("%f",JIFEN_1), String.format("%f",JIFEN_2)}, handler, Constant.GOODS_LIST);
                }
                if (which == Constant.TEN_FIF) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{"1", String.format("%d",pageSize), String.format("%f",JIFEN_2), String.format("%f",JIFEN_3)}, handler, Constant.GOODS_LIST);
                } else if(which == Constant.MORE_FIF){
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{"1",String.format("%d",pageSize), String.format("%f",JIFEN_3), String.format("%f",JIFEN_4)}, handler, Constant.GOODS_LIST);
                }
            }

            @Override
            public void onLoadMore() {
                if(myLists != null && myLists.size() < currPage * 20){
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(),"没有更多");
                }else {
                    currPage++;
                    if (which == Constant.LESS_FIVE) {//历届
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{String.format("%d",currPage), String.format("%d", pageSize), String.format("%f",JIFEN_0), String.format("%f",JIFEN_1)}, handler, Constant.GOODS_LIST);
                    }
                    if (which == Constant.FIVE_TEN) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{String.format("%d",currPage), String.format("%d", pageSize), String.format("%f",JIFEN_1), String.format("%f",JIFEN_2)}, handler, Constant.GOODS_LIST);
                    }
                    if (which == Constant.TEN_FIF) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{String.format("%d",currPage), String.format("%d", pageSize), String.format("%f",JIFEN_2), String.format("%f",JIFEN_3)}, handler, Constant.GOODS_LIST);
                    } else if(which == Constant.MORE_FIF){
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsListJiFen, new String[]{"page", "num", "mincredit", "maxcredit"}, new String[]{String.format("%d",currPage), String.format("%d", pageSize), String.format("%f",JIFEN_3), String.format("%f",JIFEN_4)}, handler, Constant.GOODS_LIST);
                    }
                }
            }
        });
        adapter = new ListAdapterInteral_interal(getActivity());
        listView.setAdapter(adapter);
    }
}
