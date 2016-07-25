package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.NoveltyComment;
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 身边新鲜事详细信息中的评论列表adapter
 * Created by xu on 2016/2/29.
 */
public class ListAdapterCommentAdapter extends BaseAdapter {

    private List<NoveltyComment.DataEntity> mData = new ArrayList<>();

    private Context mContext;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ListAdapterCommentAdapter(Context context) {
        mContext = context;
        initImageLoader();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
//                .showStubImage(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = RelativeLayout.inflate(mContext, R.layout.nearby_comment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_nearby_comment_item_commet);
            viewHolder.tv_likecount = (TextView) convertView.findViewById(R.id.tv_nearby_comment_item_likecount);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_nearby_comment_item_nickname);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_nearby_comment_item_time);
            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_nearby_comment_item_head);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_time.setText(utc2Local(mData.get(position).getComment_time()));
        viewHolder.tv_likecount.setText(mData.get(position).getLike_num());
        viewHolder.tv_name.setText(mData.get(position).getComment_user_name());
        viewHolder.tv_comment.setText(mData.get(position).getComment_content());
        imageLoader.displayImage(mData.get(position).getComment_user_face(), viewHolder.iv_head, options);//viewHolder.iv_head
        viewHolder.tv_likecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.putExtra("member_id", mData.get(position).getComment_user_id());
                mContext.startActivity(intent);
            }
        });
        convertView.setTag(viewHolder);
        return convertView;
    }

    private String utc2Local(String utcTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long times = Long.parseLong(utcTime);
        Date date = new Date(times);
        return sdf.format(date);
    }


    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<NoveltyComment.DataEntity> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        if (mData != null && mData.size() > 0) {
            mData.clear();
            notifyDataSetChanged();
        }
    }


    public class ViewHolder {
        public ImageView iv_head;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_comment;
        public TextView tv_likecount;
    }


}
