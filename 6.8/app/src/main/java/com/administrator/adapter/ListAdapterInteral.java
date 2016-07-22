package com.administrator.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.administrator.bean.Constant;
import com.administrator.bean.DisCountBean;
import com.administrator.bean.ShopAdvid;
import com.administrator.elwj.Interalshop_detailsactivity;
import com.administrator.elwj.Interalshop_interalactivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016/1/8.
 * 社区超市首页适配
 * 这个不应该用listview，但是前人不知为啥用了。
 * 当position为0时，表示显示重磅出击 限时优惠这个广告位的内容
 * 当position为1时，表示显示震撼来袭 积分热兑那4个分类
 * 当position为2时，目前没有用到
 */
public class ListAdapterInteral extends BaseAdapter {
    private Activity activity;
    private List<ShopAdvid> lists = new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ShopAdvid.DataEntity bean;
    //社区超市页面中重磅出击下面的那张广告位图片商品bean
    DisCountBean.DataEntity disCountBeanDataEntity;

    public void addData(List<ShopAdvid> lists, Activity activity) {
        this.lists = lists;
        this.activity = activity;
        notifyDataSetChanged();
    }

    public void addData(DisCountBean.DataEntity dataEntity) {
        disCountBeanDataEntity = dataEntity;
        notifyDataSetChanged();
    }

    public ListAdapterInteral(Activity context) {
        this.activity = context;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    @Override
    public int getCount() {
        if (lists != null) {
            return lists.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder_Interal vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_interalshop, null);
            vh = new ViewHolder_Interal();
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_interal_icon);
            vh.fistPic = (ImageView) convertView.findViewById(R.id.iv_interal_first1);
            vh.iv1 = (ImageView) convertView.findViewById(R.id.iv_interal_first);
            vh.iv2 = (ImageView) convertView.findViewById(R.id.iv_interal_second);
            vh.iv3 = (ImageView) convertView.findViewById(R.id.iv_interal_third);
            vh.iv4 = (ImageView) convertView.findViewById(R.id.iv_interal_fourth);
            vh.jifen1 = (LinearLayout) convertView.findViewById(R.id.jifen);
            vh.jifen2 = (LinearLayout) convertView.findViewById(R.id.jifen_22);
            vh.shangpin = (LinearLayout) convertView.findViewById(R.id.shangpin);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder_Interal) convertView.getTag();
        }
        if (position == 0) {
            vh.fistPic.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.fistPic.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            if (activity != null) {
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                params.height = (int) (metrics.widthPixels / Constant.radio);
                vh.fistPic.setLayoutParams(params);
            }

            vh.iv1.setVisibility(View.GONE);
            vh.iv_icon.setImageResource(R.mipmap.pic13);

            if (disCountBeanDataEntity != null)
                imageLoader.displayImage(disCountBeanDataEntity.getSmall(), vh.fistPic, options);
            else
                vh.fistPic.setImageResource(R.mipmap.pic03);
            vh.fistPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disCountBeanDataEntity != null) {
                        Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
                        intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                        intent.putExtra("id", String.format("%d", disCountBeanDataEntity.getGoods_id()));
                        intent.putExtra("imageRemoved", disCountBeanDataEntity.getBig());
                        activity.startActivity(intent);
                    }
                }
            });
            vh.shangpin.setVisibility(View.GONE);
            vh.jifen1.setVisibility(View.GONE);
            vh.jifen2.setVisibility(View.GONE);
        }
        if (position == 1) {
            vh.fistPic.setVisibility(View.GONE);
            vh.iv_icon.setImageResource(R.mipmap.pic14);
            vh.iv1.setVisibility(View.GONE);
            vh.shangpin.setVisibility(View.GONE);
            vh.jifen1.setVisibility(View.VISIBLE);
            vh.jifen2.setVisibility(View.VISIBLE);
            ImageView five = (ImageView) convertView.findViewById(R.id.jifen_01);
            ImageView five_ten = (ImageView) convertView.findViewById(R.id.jifen_02);
            ImageView ten_fif = (ImageView) convertView.findViewById(R.id.jifen_1);
            ImageView fif_more = (ImageView) convertView.findViewById(R.id.jifen_2);
            five.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(activity, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 0);
                    activity.startActivity(intent3);
                }
            });
            five_ten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(activity, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 1);
                    activity.startActivity(intent3);
                }
            });
            ten_fif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(activity, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 2);
                    activity.startActivity(intent3);
                }
            });
            fif_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(activity, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 3);
                    activity.startActivity(intent3);
                }
            });
        }
        if (position == 2) {
            vh.fistPic.setVisibility(View.GONE);
            vh.jifen1.setVisibility(View.GONE);
            vh.jifen2.setVisibility(View.GONE);
            vh.iv1.setVisibility(View.GONE);
            vh.iv_icon.setImageResource(R.mipmap.pic15);
            //vh.shangpin.setVisibility(View.VISIBLE);

            if (lists.get(position - 1).getData().size() > 0) {
//                imageLoader.displayImage(lists.get(position - 1).getData().get(0).getAtturl(), vh.iv1, options);
//                imageLoader.displayImage(lists.get(position - 1).getData().get(1).getAtturl(), vh.iv2, options);
//                imageLoader.displayImage(lists.get(position - 1).getData().get(2).getAtturl(), vh.iv3, options);
//                imageLoader.displayImage(lists.get(position - 1).getData().get(3).getAtturl(), vh.iv4, options);
            }
            vh.iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
                    intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                    //此处lists.get(position).getData().get(2).getUrl()有待修改
                    intent.putExtra("id", lists.get(position - 1).getData().get(0).getUrl());
                    activity.startActivity(intent);
                }
            });
//            vh.iv2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
//                    intent.putExtra("type", 11);
//                    intent.putExtra("id", 351 + "");
//                    activity.startActivity(intent);
//                }
//            });
//            vh.iv3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
//                    intent.putExtra("type", 11);
//                    intent.putExtra("id", 351 + "");
//                    activity.startActivity(intent);
//                }
//            });
//            vh.iv4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(activity, Interalshop_detailsactivity.class);
//                    intent.putExtra("type", 11);
//                    intent.putExtra("id", 351 + "");
//                    activity.startActivity(intent);
//                }
//            });
        }
        return convertView;
    }

    public class ViewHolder_Interal {
        ImageView iv_icon;
        ImageView fistPic;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        ImageView iv4;
        LinearLayout jifen1;
        LinearLayout jifen2;
        LinearLayout shangpin;
    }
}
