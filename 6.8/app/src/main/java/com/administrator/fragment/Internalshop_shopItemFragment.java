package com.administrator.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.administrator.adapter.ListAdapterInteral_lbshop;
import com.administrator.adapter.ListAdapterInteral_shop;
import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.bean.GoodType;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu on 2016/2/28.
 * 进口商城子fragment，商品分类fragment，比如（母婴用品页面、美妆护理页面等）
 */
@SuppressLint("ValidFragment")
public class Internalshop_shopItemFragment extends BaseFragment {
    private XListView listView;
    private View mView;
    private List<Bean_GoodsList.DataEntity> myLists = new ArrayList<>();
    private int num;
    private ListAdapterInteral_shop adapter;
    private ListAdapterInteral_lbshop lbadapter;
    private BaseApplication appContext;
    private GoodType mGoodType;
    private boolean firstGetData = true;

    public Internalshop_shopItemFragment(int num, GoodType goodType) {
        this.num = num;
        mGoodType = goodType;
        LogUtils.d("xu_text", "internalshop_shoprecommendfragment");
    }

    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;

    public static class MyHandler extends Handler {
        private WeakReference<Internalshop_shopItemFragment> mFragment;

        public MyHandler(Internalshop_shopItemFragment fragment) {
            mFragment = new WeakReference<Internalshop_shopItemFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Internalshop_shopItemFragment fragment = mFragment.get();
            if (fragment != null) {
                LogUtils.d("xu_text", "handlemessage");
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.GOODS_LIST) {
                    fragment.listView.stopLoadMore();
                    fragment.listView.stopRefresh();
                    Gson gson = new Gson();
                    Bean_GoodsList bean_goodsList = gson.fromJson(json, Bean_GoodsList.class);
                    List<Bean_GoodsList.DataEntity> dataLists = bean_goodsList.getData();
                    if (fragment.curPage == 1) {
                        fragment.myLists.clear();
                        fragment.hasMoreData = true;
                    }
                    if(dataLists!= null && dataLists.size() < fragment.pageSize){
                        fragment.hasMoreData = false;
                        if(fragment.getActivity() != null && !fragment.firstGetData)
                            ToastUtil.showToast(fragment.getContext(),"没有更多");
//                            Toast.makeText(fragment.getActivity(),"没有更多",Toast.LENGTH_SHORT).show();
                    }
                    fragment.firstGetData = false;
                    if(dataLists != null)
                        fragment.myLists.addAll(dataLists);

                    switch (fragment.num) {
                        case 0:
                            fragment.adapter.addData(fragment.myLists, fragment.getActivity());
                            break;

                        case 2:
                            fragment.lbadapter.addData(fragment.myLists, fragment.getActivity());

                            break;

                    }

                }
            }
        }
    }

    private Handler handler = new MyHandler(this);


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_internalshop_shop_recommend, null);
        appContext = (BaseApplication) getActivity().getApplication();
        firstGetData = true;
        initView();
        return mView;
    }

    private void initView() {

        switch (num) {
            case 0:
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "1000", "1"}, handler, Constant.GOODS_LIST);
                break;
            case 1:
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "89", "1"}, handler, Constant.GOODS_LIST);
                break;
            case 2:
                if (mGoodType != null)
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", String.format("%d", mGoodType.getId()), "1"}, handler, Constant.GOODS_LIST);
                break;

        }


        listView = (XListView) mView.findViewById(R.id.listView_integralshop);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                switch (num) {
                    case 0:
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "1000", "1"}, handler, Constant.GOODS_LIST);
                        break;
                    case 1:
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "89", "1"}, handler, Constant.GOODS_LIST);
                        break;
                    case 2:
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", String.format("%d", mGoodType.getId()), "1"}, handler, Constant.GOODS_LIST);
                        break;
                }
            }

            @Override
            public void onLoadMore() {

                if (!hasMoreData) {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(),"没有更多");
                } else {
                    curPage++;
                    switch (num) {
                        case 0:
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "1000", curPage + ""}, handler, Constant.GOODS_LIST);
                            break;
                        case 1:
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page"}, new String[]{pageSize + "", "89", curPage + ""}, handler, Constant.GOODS_LIST);
                            break;
                        case 2:
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsList, new String[]{"num", "catid", "page","pageSize"}, new String[]{pageSize + "", String.format("%d", mGoodType.getId()), curPage + "",pageSize + ""}, handler, Constant.GOODS_LIST);
                            break;
                    }
                }
            }
        });


        switch (num) {
            case 0:
            case 1:
                adapter = new ListAdapterInteral_shop(getActivity());
                listView.setAdapter(adapter);
                break;
            case 2:
                /**以下代码解决礼包商城listview拉不到底BUG，动态适配listView高度，误注释掉**/
                int mTotalHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();//
                int mTotalWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();//
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mTotalHeight - 200);
                listView.setLayoutParams(params);
                /** end **/
                lbadapter = new ListAdapterInteral_lbshop(getActivity());
                listView.setAdapter(lbadapter);
//                setListViewHeightBasedOnChildren(listView);
                break;
        }


    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(XListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int aaa = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
