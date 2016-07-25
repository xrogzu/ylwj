package com.administrator.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.administrator.elwj.R;

/**
 * 目前没有用到，可以删除
 * Created by Administrator on 2016/3/21.
 */
public class ListAdapterMyChangedScores extends BaseAdapter {

    private Context mContext;

    public ListAdapterMyChangedScores(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LinearLayout.inflate(mContext, R.layout.listitem_headline_new,null);
        }else{

        }
        return convertView;
    }
}
