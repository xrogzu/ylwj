package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.HotProduct;
import com.administrator.bean.NonList;
import com.administrator.elwj.MyWebViewActivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 金融常识adapter
 * Created by Administrator on 2015/12/29.
 */
public class ListAdapterFinancalNon extends BaseAdapter {
    private Context context;
    private List<NonList.DataEntity> list;

    private static ImageLoader imageLoader;
    private DisplayImageOptions options;

    public String title="金融常识";
    public ListAdapterFinancalNon(Context context) {
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
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NonList.DataEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(null == convertView){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_financalnon_list,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_financalnon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_financal);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(list.get(position).getTitle());
        imageLoader.displayImage(list.get(position).getImageurl(), viewHolder.imageView, options);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyWebViewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("type", MyWebViewActivity.TYPE_COMM);
                intent.putExtra("id", String.format("%d", list.get(position).getId()));
                context.startActivity(intent);
            }
        });
        return convertView;

    }

    public void addData(List<NonList.DataEntity> data){
        if(list == null){
            list = new ArrayList<>();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        if(list == null){
            list = new ArrayList<>();
        }else{
            list.clear();
        }
    }


   public class ViewHolder{
        public ImageView imageView;
        public TextView textView;

    }
}
