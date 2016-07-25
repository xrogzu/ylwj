package com.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.HotProduct;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 热销产品适配器
 * Created by Administrator on 2015/12/29.
 */
public class ListAdapterHotProduct extends ArrayAdapter<HotProduct> {
    private Context context;
    private List<HotProduct> list;


    public ListAdapterHotProduct(Context context, int resource, List<HotProduct> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public HotProduct getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(null == convertView){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_hotproduct_list,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageproduct);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textviewproduct);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(0 == position){
            viewHolder.imageView.setImageResource(R.mipmap.yinhangka);
            viewHolder.textView.setText("银行卡");
        }else if(1 == position){
            viewHolder.imageView.setImageResource(R.mipmap.daikuanlei);
            viewHolder.textView.setText("贷款类");
        }else{
            viewHolder.imageView.setImageResource(R.mipmap.yinhang);
            viewHolder.textView.setText("移动银行");
        }
        return convertView;

    }


  public  class ViewHolder{
        public ImageView imageView;
        public TextView textView;

    }
}
