package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by acer on 2016/1/12.
 * 积分消费与现金消费adapter
 */
public class ListAdapterInSet extends BaseAdapter {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private List<Map<String, Object>> List=new ArrayList<>();
    private String payMent;


    public void addData(List<Map<String, Object>> lists,Context context,String pay){
        this.List=lists;
        notifyDataSetChanged();
        this.payMent=pay;
    }

    public ListAdapterInSet(){
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
//        if(lists.size()%2==0){
//            return lists.size()/2;
//        }else {
//            return lists.size()/2+1;
//        }
        return List.size();

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
        ImageView iv_pic;//主图片
        TextView tv_goodsName;//商品名称
        TextView tv_color;//颜色分类的内容
        TextView tv_price;//单价
        TextView tv_num;//数量

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_listitem, null);
            vh.iv_pic = (ImageView) convertView.findViewById(R.id.iv_shopcar_list_pay);
            vh.tv_goodsName= (TextView) convertView.findViewById(R.id.tv_shopcar_list_name_pay);
            vh.tv_color= (TextView) convertView.findViewById(R.id.tv_shopcar_list_color_pay);
            vh.tv_price= (TextView) convertView.findViewById(R.id.tv_shopcar_list_price_pay);
            vh.tv_num= (TextView) convertView.findViewById(R.id.bt_shopcar_delete_pay);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Map<String, Object> goodsMaplist=List.get(position);
        imageLoader.displayImage(goodsMaplist.get("image").toString(), vh.iv_pic, options);

//          vh.goods_state.setText(bean.getPay_status());

        if(payMent.equals("积分支付")){
            vh.tv_price.setText("单价: "+String.valueOf(goodsMaplist.get("price"))+" 积分");
        }else{
            vh.tv_price.setText("单价：￥"+String.valueOf(goodsMaplist.get("price")));
        }



        vh.tv_num.setText("x" + String.valueOf(goodsMaplist.get("num")));

        vh.tv_goodsName.setText(String.valueOf(goodsMaplist.get("name")));




//        vh.tv_name_right.setText(data_right.getName().trim());
//        vh.tv_interal_right.setText( data_right.getPrice() +"");
//        vh.tv_sold_right.setText("已售："+data_right.getBuy_count()+"件");
//        vh.tv_paraisenum_right.setText(data_right.getView_count());
//        vh.rl_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Interalshop_detailsactivity.class);
//                intent.putExtra("type", 2);
//                intent.putExtra("details_info", data_right);
//                context.startActivity(intent);
//            }
//        });
////        }
//
////        else {
////            vh.rl_right.setVisibility(View.INVISIBLE);
////        }
//
//
//        imageLoader.displayImage(data_left.getOriginal(), vh.iv_left, options);
//        vh.tv_name_left.setText(data_left.getName().trim());
//        vh.tv_interal_left.setText( data_left.getPrice()+"" );
//
//        vh.tv_sold_left.setText("已售：" +data_left.getBuy_count()+"件");
//
//        vh.tv_paraisenum_left.setText(data_left.getView_count());
//
//        vh.rl_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Interalshop_detailsactivity.class);
//                intent.putExtra("details_info",data_left);
//                intent.putExtra("type",2);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

}