package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.administrator.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 金融专家详情页面中点击图片进入的图片浏览页面
 * Created by wliping on 2016/4/15.
 */
public class LargeHeadPictureActivity extends AppCompatActivity {

    private String mExpertHeadPictureUrl;
    private List<View> mViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigpicture);
        if(getIntentData()) {
            initViews();
        }else{
            ToastUtil.showToast(this,"没有照片可以显示");
            finish();
        }
    }

    private boolean getIntentData() {
        Intent intent = getIntent();
        if(intent != null) {
            mExpertHeadPictureUrl = intent.getStringExtra("ExpertHeadPicture");
            if(mExpertHeadPictureUrl!=null)
                return true;
            else
                return false;
        }else
            return false;
    }
    private void initViews() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager_bigpicture);

        // 获取图片加载实例
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        mViews = new ArrayList<>();
        View view = LinearLayout.inflate(this, R.layout.viewpager_bigpicture_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_viewpager_bigpicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LargeHeadPictureActivity.this.finish();
            }
        });
        imageLoader.displayImage(mExpertHeadPictureUrl, imageView, options);
        mViews.add(view);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LargeHeadPictureActivity.this.finish();
            }
        });
    }
    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }
}
