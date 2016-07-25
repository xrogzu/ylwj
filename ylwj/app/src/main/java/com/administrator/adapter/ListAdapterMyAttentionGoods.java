package com.administrator.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.AttentionGoods;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.Interalshop_detailsactivity;
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
 * 我的关注中的商品适配器
 * Created by xu on 2016/3/1.
 */
public class ListAdapterMyAttentionGoods extends BaseAdapter {

    private List<AttentionGoods.DataEntity> mData;
    private Context mContext;
    private ImageLoader imageLoader;
    private BaseApplication application;

    private String delFavouriteID;

    private static final int DEL_ID = 1;

    public static class MyHandler extends Handler{
        private WeakReference<ListAdapterMyAttentionGoods> mListAdapter;
        public MyHandler(ListAdapterMyAttentionGoods listAdapterMyAttentionGoods){
            mListAdapter = new WeakReference<ListAdapterMyAttentionGoods>(listAdapterMyAttentionGoods);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterMyAttentionGoods listAdapter = mListAdapter.get();
            if(listAdapter != null){
                switch (msg.what) {
                    case DEL_ID:
                        String json = (String) msg.obj;
                        boolean isDelSuccess = false;
                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (1 == Integer.parseInt(String.valueOf(jsonObject.get("result")))) {
                                    isDelSuccess = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (isDelSuccess) {
                            listAdapter.localDelData(msg.what);
                            ToastUtil.showToast(listAdapter.mContext, "删除成功");
                        } else {
                            ToastUtil.showToast(listAdapter.mContext,"删除失败");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


    private MyHandler handler = new MyHandler(this);

    /**
     * 本地删除数据
     * @param what favorite_id
     */
    private void localDelData(int what) {
        for(int i = 0;i < mData.size(); ++i){
            if(delFavouriteID != null && mData.get(i).getFavorite_id().equals(delFavouriteID)){
                mData.remove(i);
                notifyDataSetChanged();
                delFavouriteID = null;
                break;
            }
        }
    }


    public ListAdapterMyAttentionGoods(Context context, BaseApplication application){
        mContext = context;
        mData = new ArrayList<>();
        initImageLoader();
        this.application = application;
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.myattention_goods_item,null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_myattention_goods_item_name);
            viewHolder.tvJifen = (TextView) convertView.findViewById(R.id.tv_myattention_goods_item_jifen);
            viewHolder.tvSelled = (TextView) convertView.findViewById(R.id.tv_myattention_goods_item_selled);
            viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_myattention_goods_item_pic);
            viewHolder.ibDel = (ImageButton) convertView.findViewById(R.id.ib_myattention_goods_item_del);
            viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.ll_myattention_goods_item_click);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSelled.setText("已售" + mData.get(position).getBuy_count() + "件");
        viewHolder.tvJifen.setText("");
        viewHolder.tvName.setText(mData.get(position).getName());

        if(mData.get(position).getFlag().equals("1")){//积分

            viewHolder.tvJifen.setText(String.format("%d",(int)mData.get(position).getPrice())+" 积分");
        }else{//现金
            viewHolder.tvJifen.setText(String.format("%d",(int)mData.get(position).getPrice())+" 元");

        }
//        viewHolder.tvJifen.setText(String.format("%d",(int)mData.get(position).getPrice()));



        viewHolder.ibDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setMessage("确定要删除吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.deleteAttentionGoods, new String[]{"favoriteid"}, new String[]{mData.get(position).getFavorite_id()}, handler, DEL_ID);
                        delFavouriteID = mData.get(position).getFavorite_id();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        viewHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Interalshop_detailsactivity.class);
                intent.putExtra("id",mData.get(position).getGoods_id());
                intent.putExtra("type",Constant.SHOP_TYPE_AUTO);
                mContext.startActivity(intent);
            }
        });
        imageLoader.displayImage(mData.get(position).getThumbnail(),viewHolder.ivPic);
        return convertView;
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<AttentionGoods.DataEntity> data){
        if(data != null)
            mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        if(mData != null && mData.size() > 0){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public class ViewHolder{
        TextView tvName;
        TextView tvJifen;
        TextView tvSelled;
        ImageView ivPic;
        ImageButton ibDel;
        LinearLayout llItem;
    }

}
