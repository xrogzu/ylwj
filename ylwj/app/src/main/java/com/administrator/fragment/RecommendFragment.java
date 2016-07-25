package com.administrator.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.ListAdapterRecommend;
import com.administrator.bean.CommList;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金融管家中的理财早知道fragment
 * Created by acer on 2015/12/25.
 */
public class RecommendFragment extends BaseFragment implements XListView.IXListViewListener {

    private ProgressDialog progressDialog;
    private boolean isNetRequestDone = false;

    private static final String TAG = "RecommendFragment";
    private XListView mListView;
    private View mView;
    private ListAdapterRecommend mAdapter;
    private RequestQueue requestQueue;
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;
    private BaseApplication application;

    private FinancialManagerFragment financialManagerFragment;
    private static final int GET_COMMLIST = 1;

    @SuppressLint("ValidFragment")
    public RecommendFragment(FinancialManagerFragment financialManagerFragment) {
        super();
        this.financialManagerFragment = financialManagerFragment;
    }


    public static class MyHandler extends Handler {
        private WeakReference<RecommendFragment> mFragment;

        public MyHandler(RecommendFragment fragment) {
            mFragment = new WeakReference<RecommendFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            RecommendFragment fragment = mFragment.get();
            fragment.isNetRequestDone = true;
            if (fragment != null) {
                if(fragment.progressDialog != null){
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                fragment.mListView.stopLoadMore();
                fragment.mListView.stopRefresh();
                String json = (String) msg.obj;
                LogUtils.d("xu", msg.obj.toString());
                switch (msg.what) {
                    case GET_COMMLIST:
                        Gson gson = new Gson();
                        CommList commList = gson.fromJson(json, CommList.class);
                        if (commList != null && commList.getResult() == 1) {
                            List<CommList.DataEntity> dataEntities = commList.getData();
                            if (fragment.curPage == 1)
                                fragment.mAdapter.clear();
                            fragment.mAdapter.addAdd(dataEntities, fragment.getContext());
                            fragment.hasMoreData = dataEntities.size() >= fragment.pageSize;
                        } else {

                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    public RecommendFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recommend, null);
        application = (BaseApplication) getActivity().getApplication();
        requestQueue = application.getRequestQueue();
        if(financialManagerFragment != null && financialManagerFragment.getCurrentItem() == 0) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
            progressDialog.show();
            LogUtils.d("xu_show", "RecommendFragment");
        }
        initListView();
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getCommList, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMLIST);
        return mView;
    }

    public void downLoadJson(final String currpage, final String pageSize) {

        String httpurl = Constant.baseUrl + "/hxb/finance/recommondedinfomation/api/list";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListView.stopLoadMore();
                        mListView.stopRefresh();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("currpage", currpage);
                map.put("pageSize", pageSize);
                return map;
            }
        };
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(TAG);
    }

    private void initListView() {
        downLoadJson("1", "10");
        mListView = (XListView) mView.findViewById(R.id.lv_fragment_recommend);
        mAdapter = new ListAdapterRecommend(getContext());
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getCommList, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMLIST);
    }

    @Override
    public void onLoadMore() {
        if (hasMoreData) {
            curPage++;
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getCommList, new String[]{"page", "pagesize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMLIST);
        } else {
            mListView.stopLoadMore();
            mListView.stopRefresh();
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
