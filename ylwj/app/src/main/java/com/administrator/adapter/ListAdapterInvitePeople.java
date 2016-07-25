package com.administrator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.BigstageUser;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.InvitePeopleActivity;
import com.administrator.elwj.R;
import com.administrator.utils.LogUtils;
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
 * 发布活动最后一步中邀请人列表的adapter
 * Created by acer on 2016/1/16.
 */
public class ListAdapterInvitePeople extends BaseAdapter {

    private List<BigstageUser.DataEntity> mData;

    private InvitePeopleActivity mActivity;

    private BaseApplication application;

    private TextView tvInvite;

    private static final int INVITE = 1;

    private ImageLoader imageLoader;
    private DisplayImageOptions optionhead;

    public static class MyHandler extends Handler{
        private WeakReference<ListAdapterInvitePeople> mListAdapter;
        public MyHandler(ListAdapterInvitePeople people){
            mListAdapter = new WeakReference<ListAdapterInvitePeople>(people);
        }
        @Override
        public void handleMessage(Message msg) {
            ListAdapterInvitePeople listAdapter = mListAdapter.get();
            if(listAdapter != null){
                String json = (String) msg.obj;
                if(msg.what == INVITE){
                    JSONObject object =  null;
                    try {
                        object = new JSONObject(json);
                        if(object.getInt("result") == 1){
                            ToastUtil.showToast(listAdapter.mActivity, "邀请成功");
//                            Toast.makeText(listAdapter.mActivity,"邀请成功！",Toast.LENGTH_SHORT).show();
                            if(listAdapter.tvInvite != null){
                                listAdapter.tvInvite.setText("已邀请");
                            }
                        }else {
                            ToastUtil.showToast(listAdapter.mActivity,object.get("message").toString());
//                            Toast.makeText(listAdapter.mActivity,object.get("message").toString(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    public ListAdapterInvitePeople(InvitePeopleActivity activity, BaseApplication application) {
        mActivity = activity;
        mData = new ArrayList<>();
        this.application = application;
        initImageLoader();
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
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
        ImageView iv_title;
        TextView tv_name;
        TextView tv_had_attention;
        TextView tv_attention;
        TextView tv_fans;
        TextView tv_hobby;
        LinearLayout linear_invate;
        TextView tv_linear_invate;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder_Invite vh;
        if (convertView == null) {
            vh = new ViewHolder_Invite();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_invitepeople, null);
            vh.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_had_attention = (TextView) convertView.findViewById(R.id.tv_had_attention);
            vh.tv_attention = (TextView) convertView.findViewById(R.id.tv_attention);
            vh.tv_fans = (TextView) convertView.findViewById(R.id.tv_fans);
            vh.tv_hobby = (TextView) convertView.findViewById(R.id.tv_hobby);
            vh.linear_invate = (LinearLayout) convertView.findViewById(R.id.linear_invate);
            vh.tv_linear_invate = (TextView) convertView.findViewById(R.id.tv_linear_invate);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder_Invite) convertView.getTag();
        }
//        if (position == 2) {
//            vh.tv_had_attention.setVisibility(View.VISIBLE);
//        }
        vh.linear_invate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invite = vh.tv_linear_invate.getText().toString();
                if (invite.equals("邀请")) {
                    tvInvite = vh.tv_linear_invate;
                    LogUtils.d("xu_invite", mActivity.getActivityContent() + ":" + mActivity.getActivityID() + ":" + mActivity.getActivityTitle());
                    String content = mActivity.getActivityContent();
                    if(content != null ){
                        if(content.length() > 200)
                            content = content.substring(0,200);
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.invite, new String[]{"member_id", "title", "description", "activity_id"}, new String[]{mData.get(position).getMember_id(), mActivity.getActivityTitle(), content, mActivity.getActivityID()}, handler, INVITE);
                    }else{
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.invite, new String[]{"member_id", "title", "description", "activity_id"}, new String[]{mData.get(position).getMember_id(), mActivity.getActivityTitle(),"", mActivity.getActivityID()}, handler, INVITE);
                    }

                } else {
                    ToastUtil.showToast(mActivity, "已经邀请");
//                    Toast.makeText(mActivity, "已经邀请", Toast.LENGTH_SHORT).show();
                }
            }
        });
        vh.tv_name.setText(mData.get(position).getMember_name());
        vh.tv_attention.setText("关注：" + mData.get(position).getAttention_num());
        vh.tv_fans.setText("粉丝：" + mData.get(position).getAttentionedNum());
        imageLoader.displayImage(mData.get(position).getFace(),vh.iv_title,optionhead);
        return convertView;
    }

    public void addData(List<BigstageUser.DataEntity> dataEntities) {
        if (mData == null)
            mData = new ArrayList<>();
        if (dataEntities != null && dataEntities.size() > 0) {
            mData.addAll(dataEntities);
            notifyDataSetChanged();
        }

    }

    public void clear() {
        if (mData != null && mData.size() > 0) {
            mData.clear();
        }
    }
}
