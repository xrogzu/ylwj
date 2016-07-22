package com.administrator.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.AttentionGoods;
import com.administrator.bean.AttentionPerson;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注中的关注的人adapter
 * Created by acer on 2016/1/25.
 */
public class ListAdapter_AttPerson extends BaseAdapter {
    private List<AttentionPerson.DataEntity> mData;
    private Context mContext;
    private BaseApplication application;
    private ImageLoader imageLoader;
    private String delFavouriteID;
    private static final int DEL_ID = 1;
    public static class MyHandler extends Handler {
        private WeakReference<ListAdapter_AttPerson> mFragment;

        public MyHandler(ListAdapter_AttPerson fragment) {
            mFragment = new WeakReference<ListAdapter_AttPerson>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            ListAdapter_AttPerson fragment = mFragment.get();
            switch (msg.what) {
                case DEL_ID:
                    String json = (String) msg.obj;
                    boolean isDelSuccess = false;
                    if (json != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject != null) {
                                if (1 == Integer.parseInt(String.valueOf(jsonObject.get("result")))) {
                                    isDelSuccess = true;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isDelSuccess) {
                        fragment.localDelData(msg.what);
                        ToastUtil.showToast(fragment.mContext,"已取消关注");
//                        Toast.makeText(fragment.mContext, "已取消关注", Toast.LENGTH_SHORT).show();
                    } else {
                        ToastUtil.showToast(fragment.mContext,"取消关注失败");
//                        Toast.makeText(fragment.mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //处理网络返回结果
    private Handler handler = new MyHandler(this);

    public ListAdapter_AttPerson(Context context, BaseApplication application){
        mContext = context;
        mData = new ArrayList<>();
        initImageLoader();
        this.application = application;
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }
    /**
     * 本地删除数据
     * @param what favorite_id
     */
    private void localDelData(int what) {
        for(int i = 0;i < mData.size(); ++i){
            if(delFavouriteID != null && mData.get(i).getMember_id().equals(delFavouriteID)){
                mData.remove(i);
                notifyDataSetChanged();
                delFavouriteID = null;
                break;
            }
        }
    }
    /**
     * 添加数据
     * @param data
     */
    public void addData(List<AttentionPerson.DataEntity> data){
        if(data != null) {
            this.mData = data;
            notifyDataSetChanged();
        }
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

    public class ViewHolder_Invite {
        RelativeLayout rlPeople;
        ImageView iv_title;
        TextView tv_name;
        TextView tv_attention;
        TextView tv_fans;
        TextView tv_hobby;
        ImageView iv_attention;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder_Invite vh;
        if(convertView==null){
            vh=new ViewHolder_Invite();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_attention,null);
            vh.rlPeople = (RelativeLayout) convertView.findViewById(R.id.rl_people);
            vh.iv_title= (ImageView) convertView.findViewById(R.id.iv_title);
            vh.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_attention= (TextView) convertView.findViewById(R.id.tv_attention);
            vh.tv_fans= (TextView) convertView.findViewById(R.id.tv_fans);
            vh.tv_hobby= (TextView) convertView.findViewById(R.id.tv_hobby);
            vh.iv_attention= (ImageView) convertView.findViewById(R.id.iv_attention);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder_Invite) convertView.getTag();
        }
        AttentionPerson.DataEntity bean= mData.get(position);
        imageLoader.displayImage(bean.getFace(), vh.iv_title);
        vh.tv_name.setText(bean.getMember_name());

        if(bean.getNovelty_num().equals("")){
            vh.tv_attention.setText("新鲜事："+0);//新鲜事数量
        }else{
            vh.tv_attention.setText("新鲜事："+bean.getNovelty_num());//新鲜事数量
        }

        vh.tv_fans.setText("粉丝:" + bean.getAttention_num());//粉丝数量


        vh.iv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setMessage("确定要取消关注吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String aaaa = mData.get(position).getMember_id();
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.deleteAttentionPerson, new String[]{"member_id"}, new String[]{mData.get(position).getMember_id()}, handler, DEL_ID);
                        delFavouriteID = mData.get(position).getMember_id();
                    }
                }).setNegativeButton("否", null).show();
            }
        });


        vh.rlPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.putExtra("member_id", mData.get(position).getMember_id());
                mContext.startActivity(intent);
            }
        });





        return convertView;
    }
}
