package com.administrator.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.ListAdapterBigstageHome;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.listview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 社区大舞台三个活动页面（全部、社区、排行）的公共fragment，进入社区大舞台可以用viewpager左右滑动，此fragment放在viewpager中
 * Created by acer on 2016/1/19.
 */
public class BigStageItemFragment extends BaseFragment {
    private boolean isNeedShowProcessDialog = false;//是否需要首次启动时显示等待对话框
    private boolean netRequestDone = false;//网络请求是否完成
    private ProgressDialog progressDialog;
    private View mView;
    private XListView listView;
    private List<Bean_Bigstage_List> list_bigstage = new ArrayList<>();
    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;
    //草稿箱编辑活动
    public static final int EDIT_ACTIVITY = 2;
    //普通查看活动
    public static final int READ_ACTIVITY = 1;

    private int mType = READ_ACTIVITY;
    private boolean isAttentionActivity = false;

    private String mUrl;

    private boolean isRecommend;


    public static class MyHandler extends Handler {
        private WeakReference<BigStageItemFragment> mFragment;

        public MyHandler(BigStageItemFragment fragment) {
            mFragment = new WeakReference<BigStageItemFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStageItemFragment fragment = mFragment.get();
            if (fragment != null) {
                if (fragment.progressDialog != null) {
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                fragment.netRequestDone = true;
                fragment.listView.stopLoadMore();
                fragment.listView.stopRefresh();
                fragment.listView.stopRefresh();
                fragment.listView.stopLoadMore();
                int where = msg.what;
                String json = (String) msg.obj;
                LogUtils.e("Main返回的List", json);
                JSONObject object = null;
                try {
                    object = new JSONObject(json);
                    if ((int) object.get("result") == 1) {
                        String array = object.get("data").toString();
                        if (array != null && !"".equals(array)) {
                            Gson gson = new Gson();
                            List<Bean_Bigstage_List> lists = gson.fromJson(array, new TypeToken<List<Bean_Bigstage_List>>() {
                            }.getType());
                            if (lists != null && lists.size() > 0) {
                                if (lists.size() < fragment.pageSize)
                                    fragment.hasMoreData = false;
                                else fragment.hasMoreData = true;
                                if (fragment.curPage == 1) {
                                    fragment.list_bigstage.clear();
                                }
                                fragment.list_bigstage.addAll(lists);
                                fragment.adapter.addData(fragment.list_bigstage, fragment.getActivity(), fragment.appContext.getRequestQueue());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private Handler handler = new MyHandler(this);
    private BaseApplication appContext;
    private ListAdapterBigstageHome adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bigstage_recommend, null);
        // 获取图片加载实例
        appContext = (BaseApplication) getActivity().getApplication();
        if(isNeedShowProcessDialog){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.waitNetRequest));
            progressDialog.show();
        }
        initData();
        initViews();
        return mView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt("type", 1);
            mUrl = bundle.getString("url");
            isRecommend = bundle.getBoolean("recommend");
            isAttentionActivity = (mUrl != null && (mUrl.equals(Constant.listAttentionActivities)));
            if (mUrl != null && !"".equals(mUrl)) {
                if (mUrl.equals(Constant.getInviteActivity)) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"type", "page", "pageSize"}, new String[]{isRecommend ? "2" : "1", String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                } else
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
            }
        }
    }

    private void initViews() {
        listView = (XListView) mView.findViewById(R.id.listView_bigstage);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                if (mUrl.equals(Constant.getInviteActivity)) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"type", "page", "pageSize"}, new String[]{isRecommend ? "2" : "1", String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                } else
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);

            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    if (mUrl.equals(Constant.getInviteActivity)) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"type", "page", "pageSize"}, new String[]{isRecommend ? "2" : "1", String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                    } else
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + mUrl, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(), "没有更多数据");
                }
            }
        });
        adapter = new ListAdapterBigstageHome(getContext(), mType);
        if (isRecommend) {
            adapter.setIsRecommend(true);
        }
        adapter.setIsAttentionActivity(appContext, isAttentionActivity);
        listView.setAdapter(adapter);
    }

    public void showProcessDialog() {
        if (!netRequestDone) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void setNeedShowProcessDialog(boolean isNeedShowProcessDialog){
        this.isNeedShowProcessDialog = isNeedShowProcessDialog;
    }
}
