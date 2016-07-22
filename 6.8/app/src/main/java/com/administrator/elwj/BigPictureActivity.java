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

import com.administrator.bean.Novelty;
import com.administrator.bean.ThumbnailPhoto;
import com.administrator.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 身边新鲜事看相册详情页面，viewpager左右滑动
 * Created by Administrator on 2016/3/5.
 */
public class BigPictureActivity extends AppCompatActivity {

    //新鲜事
    private Novelty mNovelty;
    //图片view list
    private List<View> mViews;
    //进入后显示第几张
    private int mDefaultPosition;
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
            mDefaultPosition = intent.getIntExtra("default", 0);
            mNovelty = (Novelty) intent.getSerializableExtra("novelty");
            return true;
        }else return false;
    }

    private void initViews() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager_bigpicture);
        if(mNovelty != null){
            // 获取图片加载实例
            com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.top_banner_android)
                    .showImageForEmptyUri(R.mipmap.top_banner_android)
                    .showImageOnFail(R.mipmap.top_banner_android)
                    .cacheInMemory(true).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY).build();
            List<ThumbnailPhoto> thumbnailPhotos = mNovelty.getPhotos();
            if(thumbnailPhotos != null && thumbnailPhotos.size() > 0){
                mViews = new ArrayList<>();
                for(int i = 0;i < thumbnailPhotos.size(); ++i){
                    View view = LinearLayout.inflate(this,R.layout.viewpager_bigpicture_item,null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_viewpager_bigpicture);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BigPictureActivity.this.finish();
                        }
                    });
                    imageLoader.displayImage(thumbnailPhotos.get(i).getOriginal(), imageView, options);
                    mViews.add(view);
                }
                mViewPager.setAdapter(new MyPagerAdapter());
                mViewPager.setCurrentItem(mDefaultPosition);
                mViewPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BigPictureActivity.this.finish();
                    }
                });
            }else{
                ToastUtil.showToast(this, "没有照片可以显示");
                finish();
            }

        }else{
            ToastUtil.showToast(this, "没有照片可以显示");
            finish();
        }
    }

    public class MyPagerAdapter extends PagerAdapter{

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
