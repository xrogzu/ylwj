package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.FinancialProduct;
import com.administrator.elwj.MyWebViewActivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 家庭财富adapter
 * Created by acer on 2015/12/25.
 */
public class ListAdapterFinancialProduct extends BaseAdapter {
    private List<FinancialProduct.DataEntity> mList = new ArrayList<>();
    private static ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public ListAdapterFinancialProduct(Context context) {
        this.context = context;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public void addAdd(List<FinancialProduct.DataEntity> lists, Context context) {
        this.context = context;
        //initImageLoader(context);
        mList.addAll(lists);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FinancialProduct.DataEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        //介绍图片
        ImageView iv;
        //标题
        TextView tv_title;
        //时间
        TextView createTime;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_financialproduct, null);
            vh.iv = (ImageView) convertView.findViewById(R.id.iv_product);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            //vh.tv_commend= (TextView) convertView.findViewById(R.id.tv_product_comment);
            vh.createTime = (TextView) convertView.findViewById(R.id.tv_product_createtime);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(mList.get(position).getName());
        Date date = new Date(mList.get(position).getCreate_time());
        vh.createTime.setText(sdf.format(date));
        imageLoader.displayImage(mList.get(position).getImageurl(), vh.iv, options);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MyWebViewActivity.class);
                intent.putExtra("title","家庭财富");
                intent.putExtra("type",MyWebViewActivity.TYPE_PRODUCT);
                intent.putExtra("id",String.format("%d", mList.get(position).getId()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
