package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.CommList;
import com.administrator.bean.FinancialProduct;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 精致生活adapter
 * Created by Administrator on 2016/3/12.
 */
public class ListAdapterLife extends BaseAdapter {

    private Context mContext;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private List<FinancialProduct.DataEntity> lists;

    public ListAdapterLife(Context context){
        mContext = context;
        lists = new ArrayList<>();
        initImageloader();
    }

    private void initImageloader() {
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

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public FinancialProduct.DataEntity getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LinearLayout.inflate(mContext, R.layout.listview_item_life,null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_life_item);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_life_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(lists.get(position).getImageurl(),viewHolder.iv,options);
        viewHolder.tv.setText("一  "+lists.get(position).getName()+"  一");
        return convertView;
    }


    public void addData(List<FinancialProduct.DataEntity> datas){
        if(datas != null && datas.size() > 0){
            if(lists == null){
                lists = new ArrayList<>();
            }
            lists.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        if(lists != null){
            lists.clear();
        }else{
            lists = new ArrayList<>();
        }
    }

    public class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
