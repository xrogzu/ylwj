package com.administrator.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.Novelty;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.utils.ShareUtil;
import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 目前没有用到，可以删除
 * Created by acer on 2016/1/19.
 */
public class NearbyViewpagerAdapter extends BaseAdapter {
    private Context context;
    private List<Novelty> lists=new ArrayList<>();
    private ImageLoader imageLoader;
    private Activity contexta;
    private DisplayImageOptions options;
    private BaseApplication appContext;
    private RequestQueue requestQueue;
    private int which;
    private String imgs[]={"http://img1.imgtn.bdimg.com/it/u=2518747066,1401359607&fm=21&gp=0.jpg",
            "http://img1.cache.netease.com/catchpic/E/EB/EB695DEDCAD7784DE56D25147F09DEE8.jpg",
            "http://pic75.nipic.com/file/20150823/2165346_163105204378_2.jpg",
            "http://pic47.nipic.com/20140827/2165346_024840152293_2.jpg",
            "http://upload.hljtv.com/2016/0120/1453258295429.jpg",
            "http://picuser.city8.com/news/image/20140829/6be5cec881f35a04d36f84318e6_p1_mk1_wm35.jpg"};

    public void addData(List<Novelty> lists,Context context,RequestQueue requestQueue){
        //initImageLoader(context);
        this.lists=lists;
        this.context=context;
        this.requestQueue=requestQueue;
        notifyDataSetChanged();
    }
    public NearbyViewpagerAdapter(Context context){
        this.context=context;
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
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder_BigStage {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_introduce;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        TextView tv_location;
        CheckBox checkBox;
        TextView tv_shareNum;
        TextView tv_recommentNum;
        TextView tv_praiseNum;
        LinearLayout linear_share;
        LinearLayout linear_recomment;
        LinearLayout linear_item1_imgs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder_BigStage pcvh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_neaby1, null);
            pcvh = new ViewHolder_BigStage();
            pcvh.iv_head = (ImageView) convertView.findViewById(R.id.iv_nearby_head);
            pcvh.tv_name = (TextView) convertView.findViewById(R.id.tv_nearby_name);
            pcvh.tv_time = (TextView) convertView.findViewById(R.id.tv_nearby_time);
            pcvh.tv_introduce = (TextView) convertView.findViewById(R.id.tv_nearby_introduce);

            pcvh.iv2 = (ImageView) convertView.findViewById(R.id.iv_nearby2);

            pcvh.tv_location = (TextView) convertView.findViewById(R.id.tv_nearby_location);
            pcvh.linear_share = (LinearLayout) convertView.findViewById(R.id.linear_item_share);
            pcvh.linear_recomment = (LinearLayout) convertView.findViewById(R.id.linear_item_comment);
            pcvh.tv_shareNum = (TextView) convertView.findViewById(R.id.tv_shareNum);
            pcvh.tv_recommentNum = (TextView) convertView.findViewById(R.id.tv_recommentNum);
            pcvh.tv_praiseNum = (TextView) convertView.findViewById(R.id.tv_praiseNum);
            pcvh.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);


            convertView.setTag(pcvh);
        } else {
            pcvh = (ViewHolder_BigStage) convertView.getTag();
        }

        final Novelty bean=lists.get(position);
        final int[] num = {Integer.parseInt(pcvh.tv_praiseNum.getText().toString())};
        final ViewHolder_BigStage finalPcvh = pcvh;
        pcvh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int n=num[0];
                    n++;
                    num[0]=n;
                    finalPcvh.tv_praiseNum.setText(n+"");
                } else {
                    int n=num[0];
                    n--;
                    num[0]=n;
                    finalPcvh.tv_praiseNum.setText(n+"");

                }
            }
        });
        pcvh.tv_introduce.setText(bean.getMessage_content());
        String bbbb=bean.getPublisher_name();
        pcvh.tv_name.setText(bean.getPublisher_name());
//            pcvh.linear_item1_imgs.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, Bigstage_DetailsViewpager.class);
//                    context.startActivity(intent);
//                }
//            });
//            pcvh.iv2.setVisibility(View.INVISIBLE);
//            pcvh.iv3.setVisibility(View.INVISIBLE);
//            pcvh.tv_name.setText(bean.getMessage_title());
        final String[] listImages = new String[2];
        //listImages = bean.getPhotoIds();
//            for(int i=0;i<=listImages.length;i++){
//
//            }
        imageLoader.displayImage(listImages[0], pcvh.iv1, options);
        imageLoader.displayImage(listImages[1], pcvh.iv2, options);
        imageLoader.displayImage(listImages[2], pcvh.iv3, options);

//            imageLoader.displayImage("http://img1.hao661.com/uploads/allimg/c141030/1414632KI1V0-5a610.jpg",pcvh.iv_head,options);
        pcvh.linear_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShareUtil.getInstance().openShare(contexta, lists.get(position).getMessage_content(),,false,null);


            }
        });

        return convertView;
    }

}
