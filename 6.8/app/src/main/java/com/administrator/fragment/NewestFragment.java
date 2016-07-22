package com.administrator.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterHeadline;
import com.administrator.bean.Constant;
import com.administrator.bean.HeadLineBean;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.CreditSearchActivity;
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
 * "最新"页面
 * Created by acer on 2016/1/29.
 */
public class NewestFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private XListView listView;

    private View headView;

    private ProgressDialog progressDialog = null;

    int num = 10;
    private ListAdapterHeadline adapter;
    private BaseApplication appContext;

    private int curPage = 1;
    private int pageSize = 10;
    private boolean hasMoreData = true;

    private SharedPreferences sharedPreferences;

    private static final int GET_INFO = 1;

    public static class MyHandler extends Handler {
        private WeakReference<NewestFragment> mFragment;

        public MyHandler(NewestFragment fragment) {
            mFragment = new WeakReference<NewestFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            NewestFragment fragment = mFragment.get();
            if (fragment != null) {
                if (fragment.progressDialog != null) {
                    fragment.progressDialog.dismiss();
                    fragment.progressDialog = null;
                }
                fragment.listView.stopLoadMore();
                fragment.listView.stopRefresh();
                String json = (String) msg.obj;
                if (msg.what == GET_INFO) {
                    Gson gson = new Gson();
                    HeadLineBean bean = gson.fromJson(json, HeadLineBean.class);
                    if (bean != null && bean.getResult() == 1) {
                        if (fragment.curPage == 1) {
                            fragment.hasMoreData = true;
                            fragment.adapter.clear();
                        }
                        List<HeadLineBean.DataEntity> dataEntities = bean.getData();
                        if (dataEntities != null && dataEntities.size() > 0) {
                            if (dataEntities.size() < fragment.pageSize)
                                fragment.hasMoreData = false;
                            else fragment.hasMoreData = true;
                            fragment.dealIsRead(dataEntities);
                            fragment.adapter.addData(dataEntities);
                        } else if (dataEntities != null) {
                            fragment.hasMoreData = false;
                        }
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);


    //处理是否已读
    private void dealIsRead(List<HeadLineBean.DataEntity> dataEntities) {
        if (dataEntities != null) {
            for (int i = 0; i < dataEntities.size(); ++i) {
                //活动推送、积分推送、关注推送的已读标志已服务器为准，这三个推送是精确推送个单个人的；其他的推送都是推送全部的，其他的推送以本地的记录为准。
                if (!dataEntities.get(i).getDescription().equals("credit") && !dataEntities.get(i).getDescription().equals("activity") && !dataEntities.get(i).getDescription().equals("attention")) {
                    String description = sharedPreferences.getString(dataEntities.get(i).getPush_id(), null);
                    if (description != null && description.equals(dataEntities.get(i).getDescription())) {
                        dataEntities.get(i).setIs_read("1");
                    } else {
                        dataEntities.get(i).setIs_read("0");
                    }
                }
            }
        }
    }

    /**
     * 删除sharedpreferences
     *
     * @param key   关键字
     * @param value 值，null表示不论之前存储的值，都删除。
     */
    private void delSP(String key, String value) {
        String description = sharedPreferences.getString(key, null);
        if (description != null && (value == null || description.equals(value))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key).apply();
        }
    }

    /**
     * 存放到sharedpreferences中
     *
     * @param key   关键字
     * @param value 值
     */
    private void putSP(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public void setReadFlag(HeadLineBean.DataEntity dataEntity) {
        //活动推送、积分推送、关注推送的已读标志已服务器为准，其他的以本地为准
        if (dataEntity != null && !dataEntity.getDescription().equals("credit") && !dataEntity.getDescription().equals("activity") && !dataEntity.getDescription().equals("attention")) {
            putSP(dataEntity.getPush_id(), dataEntity.getDescription());
        }
    }


    public void save2DB(final HeadLineBean.DataEntity dataEntity) {
        if (dataEntity != null) {
            putSP(dataEntity.getPush_id(), dataEntity.getDescription());
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ib_life:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_LIFE);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.ib_health:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_HEALTH);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.ib_bigstage:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_BIGSTAGE);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.ib_shop:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_SHOP);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.ib_credit:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_CREDIT);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.ib_fin:
                intent = new Intent(HomeActivity.JUMP);
                intent.putExtra("jump", HomeActivity.JUMP_FIN);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.cardview_credit:
                intent = new Intent(getActivity(), CreditSearchActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_headline, null);
        headView = inflater.inflate(R.layout.headline_head, null);
        appContext = (BaseApplication) getActivity().getApplication();
        initSharedPreferences();

//        if(getActivity() != null) {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
//            progressDialog.show();
//        }
        initViews();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHeadLineInfo, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_INFO);
        return mView;
    }

    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences(Constant.headDB + (BaseApplication.member_id == null ? "" : BaseApplication.member_id), 0);
    }


    private void initViews() {
        ImageButton ibBigstage = (ImageButton) headView.findViewById(R.id.ib_bigstage);
        ImageButton ibLife = (ImageButton) headView.findViewById(R.id.ib_life);
        ImageButton ibCredit = (ImageButton) headView.findViewById(R.id.ib_credit);
        ImageButton ibFin = (ImageButton) headView.findViewById(R.id.ib_fin);
        ImageButton ibHealth = (ImageButton) headView.findViewById(R.id.ib_health);
        ImageButton ibShop = (ImageButton) headView.findViewById(R.id.ib_shop);

        CardView cvCreditItem = (CardView) headView.findViewById(R.id.cardview_credit);
        TextView tvCreditItemTime = (TextView) headView.findViewById(R.id.tv_headline_item_time);
        TextView tvCreditItemTitle = (TextView) headView.findViewById(R.id.tv_headline_item_title);
        cvCreditItem.setOnClickListener(this);

        ibBigstage.setOnClickListener(this);
        ibLife.setOnClickListener(this);
        ibCredit.setOnClickListener(this);
        ibFin.setOnClickListener(this);
        ibHealth.setOnClickListener(this);
        ibShop.setOnClickListener(this);

        ImageButton back = (ImageButton) mView.findViewById(R.id.hot_ib_back);
        back.setVisibility(View.INVISIBLE);
        TextView tv_headline = (TextView) mView.findViewById(R.id.title);
        tv_headline.setText(R.string.headline);
//        niv_commend3 = (NumImageView) attentionView.findViewById(R.id.iv_headline_item13);
//        niv_commend3.setNum(3);
//        TextView tv_commend_name3 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_name3);
//        tv_commend_name3.setText("新的评论");
//        TextView tv_commend_introduce3 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_introduce3);
//        tv_commend_introduce3.setText("猜猜我是谁评论了你的说说");
//        TextView tv_commend_time3 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_time3);
//
//        niv_invite4 = (NumImageView) attentionView.findViewById(R.id.iv_headline_item14);
//        niv_invite4.setNum(99);
//        TextView tv_invite_name4 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_name4);
//        tv_invite_name4.setText("活动邀请");
//        TextView tv_invite_introduce4 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_introduce4);
//        tv_invite_introduce4.setText("你收到了活动的邀请，快去参加吧");
//        TextView tv_invite_time4 = (TextView) attentionView.findViewById(R.id.tv_headline_item1_time4);

        listView = (XListView) mView.findViewById(R.id.listView_headline);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        adapter = new ListAdapterHeadline(getActivity(), appContext, this);
        listView.setAdapter(adapter);
        listView.addHeaderView(headView);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHeadLineInfo, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_INFO);
            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHeadLineInfo, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_INFO);
                } else {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(), "没有更多");
//                    Toast.makeText(getContext(), "没有更多", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void update(String data) {
        LogUtils.d("xu_update", "headlinefragment_update");
        if (adapter != null) {
            adapter.update(data);
        }
    }

    public void initSP() {
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getHeadLineInfo, new String[]{"page", "pageSize"}, new String[]{String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_INFO);
        initSharedPreferences();
    }
}
