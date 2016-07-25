package com.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;

/**
 * fragment的基类
 */
public class BaseFragment extends Fragment {

    private String title;
    private int iconId;
    public TextView textTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null, false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(getTitle());
        return view;
    }

    /**
     * 设置标题
     *
     * @param view  对应的Fragment View
     * @param id    对应的TextView控件R.id.*
     * @param title 标题
     */

    public void setTextTitle(View view, int id, String title) {
        textTitle = (TextView) view.findViewById(id);
        textTitle.setText(title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(BaseApplication.isLeakWatchOn)
//            BaseApplication.getRefWatcher(getContext()).watch(this);
    }
}
