package com.administrator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.administrator.fragment.BaseFragment;

import java.util.List;

/**
 * 目前没有用到，可以删除
 * Created by acer on 2015/12/24.
 */
public class FinicalViewpagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mLists;
    public FinicalViewpagerAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.mLists=list;
    }

    @Override
    public Fragment getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

}
