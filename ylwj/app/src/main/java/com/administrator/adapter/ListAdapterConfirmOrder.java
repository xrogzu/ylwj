package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Bean_GoodsList;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * <p>确认订单页面中的商品信息适配器</p>
 */
public class ListAdapterConfirmOrder extends BaseAdapter {
    private Context mContext;
    private int type;//1现金，2积分
    private List<Bean_GoodsList.DataEntity> dataEntities;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ListAdapterConfirmOrder(List<Bean_GoodsList.DataEntity> dataEntities, Context context, int type, ImageLoader imageLoader) {
        this.dataEntities = dataEntities;
        this.mContext = context;
        this.type = type;
        this.imageLoader = imageLoader;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    @Override
    public int getCount() {
        return dataEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return dataEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (convertView == null) {
            viewHoder = new ViewHoder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.confirm_order_listview_item, parent, false);
            viewHoder.mOrderImage = (ImageView) convertView.findViewById(R.id.order_listitem_iv);
            viewHoder.mOrderTitle = (TextView) convertView.findViewById(R.id.order_title_tv);
            //viewHoder.mOrderParams = (TextView) convertView.findViewById(R.id.order_type_tv);
            viewHoder.mOrderMoney = (TextView) convertView.findViewById(R.id.order_money_tv);
            viewHoder.mOrderCount = (TextView) convertView.findViewById(R.id.order_count_tv);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        if (imageLoader != null) {
            imageLoader.displayImage(dataEntities.get(position).getSmall(), viewHoder.mOrderImage, options);
        }
        viewHoder.mOrderTitle.setText(dataEntities.get(position).getName());
        if (type == 1 || type == 11) {
            viewHoder.mOrderMoney.setText("￥" + String.format("%.2f",dataEntities.get(position).getPrice()) + "");
        } else if (type == 2 || type == 12) {
            viewHoder.mOrderMoney.setText(dataEntities.get(position).getPrice() + " 积分");
        }
        viewHoder.mOrderCount.setText("X" + dataEntities.get(position).getBuy_count());

        return convertView;
    }

    private class ViewHoder {
        private ImageView mOrderImage;
        private TextView mOrderTitle;
        //private TextView mOrderParams;//用户选择的颜色、尺码等参数
        private TextView mOrderMoney;
        private TextView mOrderCount;//单件商品，用户选择数量
    }

}
