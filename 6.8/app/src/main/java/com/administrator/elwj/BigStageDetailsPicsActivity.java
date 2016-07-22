package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Constant;
import com.administrator.utils.LogUtils;
import com.king.photo.activity.DraftStartPickPhotoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * 社区大舞台活动现场gridview图片
 * Created by Administrator on 2016/3/10.
 */
public class BigStageDetailsPicsActivity extends AppCompatActivity {

    private ActivityDetails.DataEntity mData;

    private List<ActivityDetails.DataEntity.PhotosEntity> mPhotos;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigstage__details_gridview);
        getIntentData();
        initImageLoader();
        initViews();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mData = (ActivityDetails.DataEntity) intent.getSerializableExtra("data");
            if (mData != null)
                mPhotos = mData.getPhotos();
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

    public void initViews() {

        TextView add_pic = (TextView) findViewById(R.id.add_pic);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BigStageDetailsPicsActivity.this, DraftStartPickPhotoActivity.class);
                intent.putExtra("data", mData);
                intent.putExtra("new", false);
                intent.putExtra("tag", 1);
                startActivityForResult(intent, Constant.ADD_PIC_NEXT);

            }
        });
        if ((mData.getIs_applay().equals("1")) || mData.getIs_mine().equals("1")) {

            add_pic.setVisibility(View.VISIBLE);
        }else {
            add_pic.setVisibility(View.GONE);
        }
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigStageDetailsPicsActivity.this.finish();
            }
        });
        GridView mGridView = (GridView) findViewById(R.id.gridview_activity_pics);
        mGridView.setAdapter(new MyGridViewAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BigStageDetailsPicsActivity.this, Bigstage_DetailsViewpager.class);
                intent.putExtra("data", mData);
                intent.putExtra("item", position);
                startActivity(intent);
            }
        });
    }

    public class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhotos == null ? 0 : mPhotos.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = LinearLayout.inflate(BigStageDetailsPicsActivity.this, R.layout.gridview_item_bigstage, null);
                imageView = (ImageView) convertView.findViewById(R.id.iv_bigstage_gridview);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            imageLoader.displayImage(mPhotos.get(position).getThumbnail(), imageView, options);
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("Main+resultCode", resultCode + "");
        if (resultCode == Constant.ADD_PIC_NEXT) {
            Intent intent = new Intent();
            setResult(Constant.ADD_PIC, intent);
            finish();
        }


    }
}

