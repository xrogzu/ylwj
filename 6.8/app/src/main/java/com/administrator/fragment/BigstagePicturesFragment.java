package com.administrator.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.adapter.RecyclerViewAdapter_Pictures;
import com.administrator.bean.Constant;
import com.administrator.bean.Photo;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 前人写的fragment，目前没有用到，可以删除
 * Created by acer on 2016/1/23.
 */
public class BigstagePicturesFragment extends BaseFragment {

    private RecyclerViewAdapter_Pictures adapter;
    private List<Photo> photoList=new ArrayList<>();
    public static class MyHandler extends Handler{
        private WeakReference<BigstagePicturesFragment> mFragment;
        public MyHandler(BigstagePicturesFragment fragment){
            mFragment = new WeakReference<BigstagePicturesFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            BigstagePicturesFragment fragment = mFragment.get();
            if (fragment != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                Gson gson = new Gson();
                switch (which) {
                    case Constant.IMG_RECOMMEND:
                        try {
                            JSONArray array = new JSONArray(json);
                            for (int i = 0; i < array.length(); i++) {
                                String j = array.getString(i);
                                Photo photo = gson.fromJson(j, Photo.class);
                                fragment.photoList.add(photo);
                            }
                            fragment.adapter.addData(fragment.getActivity(), fragment.photoList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_bigstagepictures, null);
        BaseApplication appContext = (BaseApplication) getActivity().getApplication();
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.img_recommmend, null, null, handler, Constant.IMG_RECOMMEND);

        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerViewAdapter_Pictures(getActivity());
        recyclerView.setAdapter(adapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
        return mView;
    }

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
