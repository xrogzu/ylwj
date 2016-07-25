package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.Photo;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;


/**
 * 目前没有用到，可以删除
 * Created by acer on 2016/1/23.
 */
public class RecyclerViewAdapter_Pictures extends RecyclerView.Adapter<RecyclerViewAdapter_Pictures.MasonryView> {
    private Context context;
    private List<Integer> checkPositionlist;
    private List<Photo> photoList = new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public void addData(Context context, List<Photo> photoList) {

        this.context = context;
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter_Pictures(Context context) {
        this.context = context;
        //initImageLoader(context);
        checkPositionlist = new ArrayList<>();

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

    private int imgs[] = {R.mipmap.text_list_interalbig, R.mipmap.text_list_inetralshop, R.mipmap.text_list_beauty};
//    private List<Product> products;
//
//
//    public MasonryAdapter(List<Product> list) {
//        products=list;
//    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_item_pictures, null);

        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(final MasonryView holder, final int position) {
//            holder.iv_title.setImageResource();
        holder.checkbox_praise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int num = Integer.parseInt(holder.tv_num.getText().toString());
                if (isChecked) {
                    if (!checkPositionlist.contains(holder.checkbox_praise.getTag())) {
                        checkPositionlist.add(new Integer(position));
                        num++;
                        holder.tv_num.setText(num + "");
                    }
                } else {
                    if (checkPositionlist.contains(holder.checkbox_praise.getTag())) {
                        checkPositionlist.remove(new Integer(position));
                        num--;
                        holder.tv_num.setText(num + "");
                    }
                }
            }
        });
        holder.checkbox_praise.setTag(new Integer(position));//设置tag 否则划回来时选中消失
        //checkbox  复用问题
        if (checkPositionlist != null) {
            holder.checkbox_praise.setChecked((checkPositionlist.contains(new Integer(position)) ? true : false));
        } else {
            holder.checkbox_praise.setChecked(false);
        }
//        holder.iv_big.setImageResource(imgs[position % 3]);

        imageLoader.displayImage(photoList.get(position).getPath(),holder.iv_big,options);
        holder.tv_introduce.setText(photoList.get(position).getPhoto_name());
        holder.tv_num.setText(photoList.get(position).getLike_num());
//        holder.linear_pictures.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BigStageNormalDetailsActivity.class);
//                context.startActivity(intent);
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return photoList.size();
//        return products.size();
    }

    public static class MasonryView extends RecyclerView.ViewHolder {

        ImageView iv_title;
        TextView tv_name;
        ImageView iv_big;
        TextView tv_introduce;
        TextView tv_num;
        CheckBox checkbox_praise;
        LinearLayout linear_pictures;

        public MasonryView(View itemView) {
            super(itemView);
            iv_title = (ImageView) itemView.findViewById(R.id.iv_recyleview_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_recyleview_name);
            iv_big = (ImageView) itemView.findViewById(R.id.iv_recyleview_big);
            tv_introduce = (TextView) itemView.findViewById(R.id.tv_recyclerview_intruduce);
            tv_num = (TextView) itemView.findViewById(R.id.tv_recyleview_num);
            linear_pictures = (LinearLayout) itemView.findViewById(R.id.linear_pictures);
            checkbox_praise = (CheckBox) itemView.findViewById(R.id.checkbox_praise);
        }

    }
}
