package com.administrator.elwj;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Bean_Bigstage_List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

//社区大舞台活动相册详情页面，viewpager左右滚动
public class Bigstage_DetailsViewpager extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_num;
    private List<ActivityDetails.DataEntity.PhotosEntity> mPhotos;
    private List<ImageView> mImageViews;
    private int curPage;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigstage__details_viewpager);
        initImageLoader();
        getIntentData();
        initViews();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent != null){
            curPage = intent.getIntExtra("item", 0);
            ActivityDetails.DataEntity list = (ActivityDetails.DataEntity) intent.getSerializableExtra("data");
            if(list != null ){
                mPhotos = list.getPhotos();
                for (int i= 0; i < mPhotos.size(); ++i) {
                    ImageView imageView = new ImageView(this);
                    if(mImageViews == null)
                        mImageViews = new ArrayList<>();
                    mImageViews.add(imageView);
                }
            }
        }
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    private void initViews() {
        //左上角的返回按钮
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_num = (TextView) findViewById(R.id.tv_viewpagerdetails_num);
        tv_num.setText((curPage+1) + "/" + mPhotos.size());
        ImageButton ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        Button bt_save = (Button) findViewById(R.id.bt_save);
        Button bt_share = (Button) findViewById(R.id.bt_share);
        LinearLayout linear_praise = (LinearLayout) findViewById(R.id.linear_praise);
        ib_back.setOnClickListener(this);
        ib_delete.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_share.setOnClickListener(this);
        linear_praise.setOnClickListener(this);
        MyPagerAdapter adapter=new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_num.setText((position + 1) + "/" + mPhotos.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(curPage);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_delete:
                break;
            case R.id.bt_save:
                break;
            case R.id.bt_share:
                break;
            case R.id.linear_praise:
                break;
        }
    }
    public class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViews == null ? 0 : mImageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            imageLoader.displayImage(mPhotos.get(position).getOriginal(), mImageViews.get(position), options);
            mImageViews.get(position).setClickable(true);
            mImageViews.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bigstage_DetailsViewpager.this.finish();
                }
            });
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

}
