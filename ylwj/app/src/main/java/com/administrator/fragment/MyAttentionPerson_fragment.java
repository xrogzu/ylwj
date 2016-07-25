package com.administrator.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.ListAdapter_AttPerson;
import com.administrator.bean.AttentionPerson;
import com.administrator.bean.Constant;
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
 * 我的关注页面中“我关注的人”
 * Created by acer on 2016/1/25.
 */
public class MyAttentionPerson_fragment extends BaseFragment {

    private ProgressDialog progressDialog;
    private boolean netRequestDone = false;

    private XListView listView;
    //是否已经没有数据
    private boolean hasMoreData = true;
    private View mView;
    private BaseApplication application;
    private int curPage = 1;
    private ListAdapter_AttPerson adapter;
    private List<AttentionPerson.DataEntity> mData = new ArrayList<>();

    public static class MyHandler extends Handler {
        private WeakReference<MyAttentionPerson_fragment> mFragment;

        public MyHandler(MyAttentionPerson_fragment fragment) {
            mFragment = new WeakReference<MyAttentionPerson_fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyAttentionPerson_fragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case Constant.ATTENTION_PERSON:
                        fragment.netRequestDone = true;
                        if(fragment.progressDialog != null){
                            fragment.progressDialog.dismiss();
                            fragment.progressDialog = null;
                        }
                        String json = (String) msg.obj;
                        if (json != null) {
                            LogUtils.d("xu_test_attention", json);
                            Gson gson = new Gson();
                            AttentionPerson attentionPerson = gson.fromJson(json, AttentionPerson.class);
                            if (attentionPerson != null && attentionPerson.getResult() == 1) {
                                List<AttentionPerson.DataEntity> dataEntities = attentionPerson.getData();

                                if (fragment.curPage == 1) {
                                    fragment.mData.clear();
                                }
                                fragment.mData.addAll(dataEntities);
                                if (fragment.mData.size() < 20 * fragment.curPage)
                                    fragment.hasMoreData = false;
                                fragment.adapter.addData(fragment.mData);
                            } else {
                                fragment.hasMoreData = false;
                                fragment.listView.stopLoadMore();
                                ToastUtil.showToast(fragment.getContext(), "没有更多数据");
                            }

                        }
                        break;
                    default:
                        break;
                }
                fragment.listView.stopRefresh();
            }
        }

    }

    //处理网络返回结果
    private Handler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_financial_expert, null);
        application = (BaseApplication) getActivity().getApplication();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
        progressDialog.show();
        getServerData();
        initViews();

        return mView;

    }

    /**
     * 从服务器获取数据
     */
    private void getServerData() {
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.MyAttentionPerson, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, Constant.ATTENTION_PERSON);
    }

    private void initViews() {

        listView = (XListView) mView.findViewById(R.id.listView_expert);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        adapter = new ListAdapter_AttPerson(getActivity(), application);
        listView.setAdapter(adapter);

        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.MyAttentionPerson, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, Constant.ATTENTION_PERSON);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.MyAttentionPerson, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, Constant.ATTENTION_PERSON);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(), "没有更多数据");
                }
            }
        });
    }

    public void showProcessDialog() {
        if (!netRequestDone) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.show();
        }
    }
}
