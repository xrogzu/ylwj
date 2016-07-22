package com.administrator.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterMyCreditPay;
import com.administrator.bean.Bean_GoodsPayList;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的积分消费fragment
 * Created by xu on 2016/2/28.
 */
@SuppressLint("ValidFragment")
public class MyCreditPay_HistoryFragment extends BaseFragment {

    private ProgressDialog progressDialog;

    private boolean netRequestDone = false;

    private boolean isNeedShowProgressDialog = false;

    private View mView;
    private BaseApplication appContext;
    private ListAdapterMyCreditPay adapter;
    private XListView listView;
    private List<Bean_GoodsPayList.Order> myLists = new ArrayList<>();
    private int currPage = 1;
    private int num = 0;
    private Context context;
    private int which = Constant.WAIT_PAY;

    public MyCreditPay_HistoryFragment(Context context, int which) {
        this.context = context;
        this.which = which;
    }

    public static class MyHandler extends Handler {
        private WeakReference<MyCreditPay_HistoryFragment> mFragment;

        public MyHandler(MyCreditPay_HistoryFragment fragment) {
            mFragment = new WeakReference<MyCreditPay_HistoryFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyCreditPay_HistoryFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.listView.stopLoadMore();
                fragment.listView.stopRefresh();
                fragment.netRequestDone = true;
                if(fragment.progressDialog != null){
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                int which = msg.what;
                String json = (String) msg.obj;
                LogUtils.e("wj", json);
                if (which == Constant.GOODS_LIST) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 0) {//未登录或登陆过期
                            String message = jsonObject.optString("message");
                            ToastUtil.showToast(fragment.context,message);
//                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        } else if (result == 1) {
                            Gson gson = new Gson();
                            Bean_GoodsPayList bean_goodsPayList = gson.fromJson(json, Bean_GoodsPayList.class);
                            List<Bean_GoodsPayList.Order> dataLists = bean_goodsPayList.getData();
                            if (fragment.currPage == 1) {
                                fragment.myLists.clear();
                            }
                            if (dataLists == null) {
                                fragment.currPage--;
                            }
                            if (dataLists != null) {
                                fragment.myLists.addAll(dataLists);
                                fragment.adapter.addData(fragment.myLists, fragment.getActivity());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_pay_recommend, null);
        appContext = (BaseApplication) getActivity().getApplication();

        if(isNeedShowProgressDialog){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
        }
        if (which == Constant.WAIT_PAY) {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditWaitForPay, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
        } else if (which == Constant.PATED) {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayed, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
        } else if (which == Constant.ALL_PAY) {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayAll, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
        }
        initView();
        return mView;
    }

    private void initView() {
        listView = (XListView) mView.findViewById(R.id.listView_my_pay);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                //待付款
                if (which == Constant.WAIT_PAY) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditWaitForPay, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
                }
                //已付款
                if (which == Constant.PATED) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayed, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
                }
                //全部
                if (which == Constant.ALL_PAY) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayAll, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
                }
            }

            @Override
            public void onLoadMore() {
                if (currPage <= 1) {
                    if (myLists.size() < 20) {
                        listView.stopLoadMore();
                        ToastUtil.showToast(getContext(),"没有更多");
                    } else {
                        currPage++;
                        if (which == Constant.WAIT_PAY) {
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditWaitForPay, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                        }
                        if (which == Constant.PATED) {
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayed, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                        }
                        if (which == Constant.ALL_PAY) {
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayAll, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                        }
                    }
                } else if (myLists.size() < 20 * currPage) {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(),"没有更多");
                } else {

                    currPage++;
                    if (which == Constant.WAIT_PAY) {//历届
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditWaitForPay, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                    }
                    if (which == Constant.PATED) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayed, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                    }
                    if (which == Constant.ALL_PAY) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayAll, new String[]{"page", "num"}, new String[]{currPage + "", "20"}, handler, Constant.GOODS_LIST);
                    }

                }

            }
        });
        adapter = new ListAdapterMyCreditPay(getActivity(), appContext, which);
        listView.setAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("xu", "history_fragment_resume");
    }

    public void update() {
        currPage = 1;
        if (appContext != null && handler != null) {
            if (which == Constant.WAIT_PAY) {//
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditWaitForPay, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
            }
            if (which == Constant.PATED) {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayed, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
            }
            if (which == Constant.ALL_PAY) {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.myCreditPayAll, new String[]{"page", "num"}, new String[]{"1", "20"}, handler, Constant.GOODS_LIST);
            }
        }
    }

    public void setNeedShowProgressDialog(boolean isNeedShowProgressDialog){
        this.isNeedShowProgressDialog = isNeedShowProgressDialog;
    }

    public void showProgressDialog(){
        if(!netRequestDone && getActivity() != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
        }
    }
}

