package com.administrator.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterExpert;
import com.administrator.bean.Constant;
import com.administrator.bean.FinancialExpert;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 金融专家fragment
 * Created by acer on 2016/1/7.
 */
@SuppressLint("ValidFragment")
public class FinancialExpertFragment extends BaseFragment implements XListView.IXListViewListener {
    //网络请求是否完成
    private boolean isNetRequestDone = false;
    //网络请求等待对话框
    private ProgressDialog progressDialog;

    private XListView listView;
    private FinancialExpertFragment mFragment;
    private android.support.v4.app.FragmentManager manager;
    private Activity context;
    private BaseApplication application;
    private ListAdapterExpert mAdapter;

    private static final int EXPERT_HOME = 0;

    //是否还有数据可以请求
    private boolean hasMoreData = true;

    private int curPage = 1;
    private int pageSize = 10;

    public static class MyHandler extends Handler {
        private WeakReference<FinancialExpertFragment> mFragment;

        public MyHandler(FinancialExpertFragment financialExpertFragment) {
            mFragment = new WeakReference<FinancialExpertFragment>(financialExpertFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FinancialExpertFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.isNetRequestDone = true;
                if(fragment.progressDialog != null){
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                fragment.listView.stopLoadMore();
                fragment.listView.stopRefresh();
                String json = (String) msg.obj;
                LogUtils.d("xu", msg.obj.toString());
                switch (msg.what) {
                    case EXPERT_HOME:
                        Gson gson = new Gson();
                        FinancialExpert financialExpert = gson.fromJson(json, FinancialExpert.class);
                        if (financialExpert != null && financialExpert.getResult() == 1) {
                            if (fragment.curPage == 1) {
                                fragment.mAdapter.clear();
                                fragment.hasMoreData = true;
                            }
                            List<FinancialExpert.DataEntity> dataEntities = financialExpert.getData();
                            fragment.mAdapter.addData(dataEntities);
                            if (dataEntities == null || dataEntities.size() < 10) {
                                fragment.hasMoreData = false;
                            }
                        } else {
                            ToastUtil.showToast(fragment.getContext(),"没有更多数据了");
//                            Toast.makeText(fragment.getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    public FinancialExpertFragment() {

    }

    public FinancialExpertFragment(Activity context) {
        this.context = context;
    }

    public FinancialExpertFragment getSingle() {
        if (mFragment == null) {
            mFragment = new FinancialExpertFragment(context);
        }
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_financial_expert, null);
        listView = (XListView) mView.findViewById(R.id.listView_expert);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);//上拉加载更多
        listView.setPullRefreshEnable(true);
        mAdapter = new ListAdapterExpert(context);
        listView.setAdapter(mAdapter);


        application = (BaseApplication) getActivity().getApplication();
        LogUtils.d("xu_", curPage + ":" + pageSize);
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getExpert, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, EXPERT_HOME);

        return mView;
    }

    @Override
    public void onRefresh() {//下拉刷新
        curPage = 1;
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getExpert, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, EXPERT_HOME);

    }

    @Override
    public void onLoadMore() {//上拉加载
        if (hasMoreData) {
            curPage++;
            LogUtils.d("xu_", curPage + ":" + pageSize);
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getExpert, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, EXPERT_HOME);
        } else {
            listView.stopRefresh();
            listView.stopLoadMore();
            ToastUtil.showToast(getContext(), "没有更多数据了");
        }
    }


    public void showProgassDialog(){
        LogUtils.d("xu_show","FinancialExpertFragment");
        if(!isNetRequestDone && getActivity() != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
        }
    }

}
