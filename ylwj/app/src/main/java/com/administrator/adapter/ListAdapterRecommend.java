package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.CommList;
import com.administrator.bean.RecommentList;
import com.administrator.elwj.MyWebViewActivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐消息adapter
 * Created by acer on 2015/12/25.
 */
public class ListAdapterRecommend extends BaseAdapter {
    private List<CommList.DataEntity> mList=new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;


    public ListAdapterRecommend(Context context){
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
    public void addAdd(List<CommList.DataEntity> lists,Context context){
        this.context = context;
        //initImageLoader(context);
        mList.addAll(lists);
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CommList.DataEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
   public class ViewHolder{
        ImageView iv;
        TextView tv_title;
        TextView tv_commend;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_list,null);
            vh.iv= (ImageView) convertView.findViewById(R.id.iv_recommend_item);
            vh.tv_title= (TextView) convertView.findViewById(R.id.tv_recommend_itemtitle);
            vh.tv_commend= (TextView) convertView.findViewById(R.id.tv_recommend_itemcomment);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(mList.get(position).getTitle());
        vh.tv_commend.setText(String.format("%d", mList.get(position).getReadedquantity()));
        imageLoader.displayImage(mList.get(position).getImageurl(), vh.iv, options);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyWebViewActivity.class);
                intent.putExtra("id",String.format("%d",mList.get(position).getId()));
                intent.putExtra("title","理财早知道");
                intent.putExtra("type",MyWebViewActivity.TYPE_RECOMMEND);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
