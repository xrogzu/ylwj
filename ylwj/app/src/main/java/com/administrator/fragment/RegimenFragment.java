package com.administrator.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterLife;
import com.administrator.bean.Constant;
import com.administrator.bean.FinancialProduct;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.ExquisiteLifeDetailActivity;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 健康养生fragment
 * Created by Administrator on 2016/3/12.
 */
public class RegimenFragment extends BaseFragment {

    private ProgressDialog progressDialog;
    private View mView;

    private XListView listview;
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;
    private ListAdapterLife mAdapter;
    private BaseApplication appContext;
    private int mType = 8;

    private static final int GET_COMMLIST = 0;

    public static class MyHandler extends Handler {
        private WeakReference<RegimenFragment> mFragment;

        public MyHandler(RegimenFragment activity) {
            mFragment = new WeakReference<RegimenFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegimenFragment fragment = mFragment.get();
            if (fragment != null) {
                if (fragment.progressDialog != null) {
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                fragment.listview.stopRefresh();
                fragment.listview.stopLoadMore();
                String json = (String) msg.obj;
                LogUtils.d("xu_test", json);
                Gson gson = new Gson();
                FinancialProduct financialProduct = gson.fromJson(json, FinancialProduct.class);
                if (financialProduct != null && financialProduct.getResult() == 1) {
                    if (fragment.curPage == 1)
                        fragment.mAdapter.clear();
                    List<FinancialProduct.DataEntity> dataEntities = financialProduct.getData();
                    fragment.mAdapter.addData(dataEntities);
                    fragment.hasMoreData = dataEntities.size() >= fragment.pageSize;
                }
            }

        }
    }


    private MyHandler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_exquisitelife, null);
        appContext = (BaseApplication) getActivity().getApplication();
        initViews();
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
        }
//        VolleyUtils.NetUtils(appContext.getRequestQueue(),Constant.baseUrl + Constant.getCommList,new String[]{"page","pagesize"},new String[]{String.format("%d",curPage),String.format("%d",pageSize)},handler,GET_COMMLIST);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
        return mView;
    }


    private void initViews() {
        TextView title = (TextView) mView.findViewById(R.id.regimen_title);
        title.setText("健康养生");
        ImageView ivBack = (ImageView) mView.findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).popLifeFragment();
            }
        });
        listview = (XListView) mView.findViewById(R.id.listview_life);
        mAdapter = new ListAdapterLife(getActivity());
        listview.setAdapter(mAdapter);
        listview.setPullLoadEnable(true);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getProduct, new String[]{"page", "pagesize", "type"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize), String.format("%d", mType)}, handler, mType);
                } else {
                    listview.stopLoadMore();
                    ToastUtil.showToast(getContext(), "没有更多数据");
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FinancialProduct.DataEntity dataEntity = mAdapter.getItem(position - 1);
                LogUtils.d("xu_test", String.format("%d", position));
                Intent intent = new Intent(getContext(), ExquisiteLifeDetailActivity.class);
                intent.putExtra("id", String.format("%d", dataEntity.getId()));
                intent.putExtra("title", "健康养生");
                startActivity(intent);
            }
        });
    }
}
