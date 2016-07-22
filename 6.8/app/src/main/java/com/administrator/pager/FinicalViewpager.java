package com.administrator.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by acer on 2015/12/24.
 */
public class FinicalViewpager extends FragmentPagerAdapter {
    private List<Fragment> mLists;
    public FinicalViewpager(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.mLists=list;
    }

    @Override
    public Fragment getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public int getCount() {
        return mLists == null ? 0 : mLists.size();
    }

}
