package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.elwj.Interalshop_detailsactivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016/1/12.
 * 积分兑换适配
 */
public class ListAdapterInteral_interal extends BaseAdapter {
    private Context context;
    private List<Bean_GoodsList.DataEntity> lists=new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public void addData(List<Bean_GoodsList.DataEntity> lists,Context context){
        initImageLoader();
        this.lists=lists;
        notifyDataSetChanged();
        this.context=context;
    }

    private void initImageLoader() {
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

    public ListAdapterInteral_interal(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        if(lists.size()%2==0){
            return lists.size()/2;
        }else {
            return lists.size()/2+1;
        }
        //return lists.size()/2;

//        return appList.size()/2
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

   public class ViewHolder {
        RelativeLayout rl_left;
        RelativeLayout rl_right;
        ImageView iv_left;
        ImageView iv_right;
        TextView tv_name_left;
        TextView tv_name_right;
        TextView tv_interal_left;
        TextView tv_interal_right;
        TextView tv_sold_left;
        TextView tv_sold_right;
        TextView tv_paraisenum_left;
        TextView tv_paraisenum_right;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_interalshop_interal, null);
            vh.rl_left = (RelativeLayout) convertView.findViewById(R.id.interalshop_list_left);
            vh.rl_right = (RelativeLayout) convertView.findViewById(R.id.interalshop_list_right);
            vh.iv_left= (ImageView) convertView.findViewById(R.id.iv_interal_left);
            vh.iv_right= (ImageView) convertView.findViewById(R.id.iv_interal_right);
            vh.tv_name_left= (TextView) convertView.findViewById(R.id.tv_interal_nameleft);
            vh.tv_name_right= (TextView) convertView.findViewById(R.id.tv_interal_nameright);
            vh.tv_interal_left= (TextView) convertView.findViewById(R.id.tv_interal_priceleft);
            vh.tv_interal_right= (TextView) convertView.findViewById(R.id.tv_interal_priceright);
            vh.tv_sold_left= (TextView) convertView.findViewById(R.id.tv_interal_havesold_left);
            vh.tv_sold_right= (TextView) convertView.findViewById(R.id.tv_interal_havesold_right);
            vh.tv_paraisenum_left= (TextView) convertView.findViewById(R.id.tv_interal_paraisenum_left);
            vh.tv_paraisenum_right= (TextView) convertView.findViewById(R.id.tv_interal_paraisenum_right);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
       final Bean_GoodsList.DataEntity data_left= lists.get(position*2);

//        if((position+1)*2<lists.size()){
        if(position * 2 + 1 < lists.size()){
            vh.rl_right.setVisibility(View.VISIBLE);
            final Bean_GoodsList.DataEntity data_right = lists.get(position*2+1);
                imageLoader.displayImage(data_right.getSmall(), vh.iv_right, options);

                vh.tv_name_right.setText(data_right.getName().trim());
                vh.tv_interal_right.setText( data_right.getPrice() +"");
                vh.tv_sold_right.setText("已售："+data_right.getBuy_count()+"件");
                vh.tv_paraisenum_right.setText(data_right.getView_count());
                vh.rl_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Interalshop_detailsactivity.class);
                        intent.putExtra("type", Constant.SHOP_TYPE_CREDIT);
                        intent.putExtra("details_info", data_right);
                        context.startActivity(intent);
                    }
                });
        }

        else {
            vh.rl_right.setVisibility(View.INVISIBLE);
        }


        imageLoader.displayImage(data_left.getSmall(), vh.iv_left, options);
        vh.tv_name_left.setText(data_left.getName().trim());
        vh.tv_interal_left.setText( data_left.getPrice()+"" );
        vh.tv_sold_left.setText("已售：" +data_left.getBuy_count()+"件");

        vh.tv_paraisenum_left.setText(data_left.getView_count());

        vh.rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Interalshop_detailsactivity.class);
                intent.putExtra("details_info",data_left);
                intent.putExtra("type",Constant.SHOP_TYPE_CREDIT);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
