package com.administrator.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterNearbyHome;
import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.LoginActivity;
import com.administrator.elwj.R;
import com.administrator.utils.IsLogInUtils;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.king.photo.activity.StartPickPhotoActivity;
import com.library.listview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 身边fragment
 * Created by acer on 2016/1/27.
 */
public class NearByFragment extends BaseFragment {

    private ProgressDialog progressDialog = null;
    private View mView;
    private XListView listView;
    private BaseApplication appContext;
    int currPage = 1;
    private int pageSize = 6;
    private Novelty mNovelty;
    private boolean hasMoreData = true;
    private boolean firstGetData = true;

    private ListAdapterNearbyHome adapter;
    private List<Novelty> list_bigstage = new ArrayList<>();

    public NearByFragment() {
    }


    public static class MyHandler extends Handler {
        private WeakReference<NearByFragment> mFragment;

        public MyHandler(NearByFragment fragment) {
            mFragment = new WeakReference<NearByFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            NearByFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.listView.stopRefresh();
                fragment.listView.stopLoadMore();
                int which = msg.what;
                if (which == Constant.GET_ACTIVITYLIST) {
                    if(fragment.progressDialog != null){
                        fragment.progressDialog.dismiss();
                        fragment.progressDialog = null;
                    }
                    String json = (String) msg.obj;
                    LogUtils.e("Main返回的List", json);
                    JSONObject object = null;
                    JSONArray array = null;
                    try {
                        object = new JSONObject(json);
                        array = object.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (array != null) {
                        Gson gson = new Gson();
                        if (fragment.currPage == 1) {
                            fragment.hasMoreData = true;
                            fragment.list_bigstage.clear();
                            fragment.adapter.clear();
                        }

                        if (array.length() > 0) {
                            int aaa = array.length();
                            if(aaa < fragment.pageSize) {
                                fragment.hasMoreData = false;
                                if(fragment.getActivity() != null && !fragment.firstGetData)
                                    ToastUtil.showToast(fragment.getContext(),"没有更多");
//                                    Toast.makeText(fragment.getContext(),"没有更多",Toast.LENGTH_SHORT).show();
                            }else{
                                fragment.hasMoreData = true;
                            }
                            fragment.firstGetData = false;
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    String j = array.getString(i);
                                    Novelty novelty = gson.fromJson(j, Novelty.class);
                                    fragment.list_bigstage.add(novelty);
//                                LogUtils.e("json",j);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            fragment.adapter.addData(fragment.list_bigstage, fragment.getActivity());
                        }else{
                            ToastUtil.showToast(fragment.getContext(), "没有更多");
                        }
                    }
                }
                if (which == Constant.ISLOGIN) {
                    String json = (String) msg.obj;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 1) {

                            Intent intent = new Intent(fragment.getActivity(), StartPickPhotoActivity.class);
                            intent.putExtra("activity_id", json);
                            intent.putExtra("type", "2");//社区大舞台标示，用在选取照片界面和身边作为区分
                            fragment.startActivityForResult(intent, Constant.ADD_NOVELTY_SUCCESS_REQUEST_CODE);
                        } else if (result == 0) {
                            Intent Logintent = new Intent(fragment.getActivity(), LoginActivity.class);
                            fragment.startActivity(Logintent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == 0) {
                    LogUtils.d("xu_likenum", msg.obj.toString());
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_nearby_home, null);
        appContext = (BaseApplication) getActivity().getApplication();
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage(getActivity().getString(R.string.waitNetRequest));
//        progressDialog.show();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_listAll, new String[]{"page", "pageSize"}, new String[]{"1", String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);

        ImageButton add = (ImageButton) mView.findViewById(R.id.near_back);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsLogInUtils.isLogin(appContext, handler);
            }
        });


        ImageButton back = (ImageButton) mView.findViewById(R.id.hot_ib_back);
        back.setVisibility(View.INVISIBLE);
        TextView tv_head = (TextView) mView.findViewById(R.id.title);
        tv_head.setText(R.string.nearby);
        listView = (XListView) mView.findViewById(R.id.listView_nearby);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_listAll, new String[]{"page", "pageSize"}, new String[]{"1", String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
            }

            @Override
            public void onLoadMore() {
                if (!hasMoreData) {
                    listView.stopLoadMore();
                    ToastUtil.showToast(getContext(),"没有更多");
                } else {
                    currPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_listAll, new String[]{"page", "pageSize"}, new String[]{currPage + "", String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                }
            }
        });

        adapter = new ListAdapterNearbyHome(getActivity(), appContext, this);
        adapter.setFragment(this);
        listView.setAdapter(adapter);
        return mView;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d("xu_onactivity", String.format("%d", requestCode));
        switch (requestCode) {
            case Constant.ADD_NOVELTY_SUCCESS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    currPage = 1;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_listAll, new String[]{"page", "pageSize"}, new String[]{"1", String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
                }
                break;
            case Constant.LIKE_IN_COMMENT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    int likecount = data.getIntExtra("likecount", -1);
                    if (likecount != -1 && mNovelty != null) {
                        mNovelty.setLike_num(String.format("%d", likecount));
                        if(mNovelty.getIs_like().equals("1")){
                            mNovelty.setIs_like("0");
                        }else{
                            mNovelty.setIs_like("1");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                mNovelty = null;
                break;
            case Constant.ADD_COMMENT_SUCCESS_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    String commentNum = mNovelty.getComment_num();
                    int num = 0;
                    if(commentNum != null && !"".equals(commentNum)){
                        num = Integer.parseInt(commentNum);
                    }
                    num ++;
                    mNovelty.setComment_num(String.format("%d",num));
                    adapter.notifyDataSetChanged();
                }
                mNovelty = null;
                break;
            default:
                break;
        }
    }



    /*
    需要更新新鲜事
     */
    public void updateNovelty() {
        currPage = 1;
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_listAll, new String[]{"page", "pageSize"}, new String[]{"1", String.format("%d", pageSize)}, handler, Constant.GET_ACTIVITYLIST);
    }


    public void setNovelty(Novelty novelty){
        mNovelty = novelty;
    }

    public View getMainView(){
        return mView;
    }
}
