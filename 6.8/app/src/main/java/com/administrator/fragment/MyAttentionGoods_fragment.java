package com.administrator.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterMyAttentionGoods;
import com.administrator.bean.AttentionGoods;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 前人写的fragment，目前没有用到，可以删除
 * Created by xu on 2016/3/1.
 */
public class MyAttentionGoods_fragment extends BaseFragment {

    private View mView;

    private XListView mListView;
    //数据适配器
    private ListAdapterMyAttentionGoods mAttentionGoodsAdapter;
    private BaseApplication application;

    private static final int GET_MY_ATTENTION = 1;

    private int curPage = 1;
    //是否已经没有数据
    private boolean hasMoreData = true;

    public static class MyHandler extends Handler {
        private WeakReference<MyAttentionGoods_fragment> mFragment;

        public MyHandler(MyAttentionGoods_fragment fragment) {
            mFragment = new WeakReference<MyAttentionGoods_fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyAttentionGoods_fragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case GET_MY_ATTENTION:
                        String json = (String) msg.obj;
                        if (json != null) {
                            Log.d("xu_test_attention", json);
                            Gson gson = new Gson();
                            AttentionGoods attentionGoods = gson.fromJson(json, AttentionGoods.class);
                            if (attentionGoods != null && attentionGoods.getResult() == 1) {
                                List<AttentionGoods.DataEntity> dataEntities = attentionGoods.getData();
                                if (dataEntities == null || dataEntities.size() == 0) {
                                    fragment.hasMoreData = false;
                                    if (fragment.curPage == 1) {
                                        fragment.mAttentionGoodsAdapter.clear();
                                    }
                                } else {
                                    if (fragment.curPage == 1)
                                        fragment.mAttentionGoodsAdapter.clear();
                                    fragment.mAttentionGoodsAdapter.addData(attentionGoods.getData());
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                fragment.mListView.stopRefresh();
            }
        }

    }

    //处理网络返回结果
    private Handler handler = new MyHandler(this);


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_myattention_goods, null);
        initViews();
        getServerData();
        return mView;
    }

    /**
     * 从服务器获取数据
     */
    private void getServerData() {
        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
    }

    private void initViews() {

        mListView = (XListView) mView.findViewById(R.id.listview_myattention_goods);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        application = (BaseApplication) getActivity().getApplication();
        mAttentionGoodsAdapter = new ListAdapterMyAttentionGoods(getActivity(), application);
        mListView.setAdapter(mAttentionGoodsAdapter);


        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.getMyAttentionGoods, new String[]{"page"}, new String[]{String.format("%d", curPage)}, handler, GET_MY_ATTENTION);
                } else {
                    mListView.stopLoadMore();
                    ToastUtil.showToast(getContext(),"没有更多数据");
//                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
