package com.administrator.elwj;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.administrator.bean.Constant;
import com.administrator.fragment.GuideOneFragment;
import com.administrator.fragment.GuideTwoFragment;
import com.administrator.utils.DownLoadManager;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.UpdataInfoParser;
import com.administrator.utils.UpdateInfo;
import com.administrator.utils.VersionUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面，有两个fragment，左右滑动，最后一个fragment有立即进入按钮
 * Created by Administrator on 2016/3/6.
 */
public class GuideActivity extends AppCompatActivity {

    private List<Fragment> mFragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
    }

    //初始化信息
    private void init() {
        if (check())
            initViews();
    }

    //判断是否是第一次启动，是的话显示引导页，否则直接进入homeactivity
    private boolean check() {
        SharedPreferences sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
        int first = sharedPreferences.getInt("first", 0);
        if (first != 0) {
            start();
            return false;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("first", 1).commit();
            return true;
        }
    }


    private void start() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //初始化引导页
    private void initViews() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        mFragments = new ArrayList<>();
        Fragment fragment = new GuideOneFragment();
        mFragments.add(fragment);
        fragment = new GuideTwoFragment();
        mFragments.add(fragment);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
