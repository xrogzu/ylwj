package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.BigStageNormalDetailsActivity;
import com.administrator.elwj.BigStageVoteDetailsActivity;
import com.administrator.elwj.R;
import com.administrator.utils.DateUtils;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 最新页面中的活动邀请adapter
 * Created by xu on 2016/4/9.
 */
public class ListAdapterInvite extends BaseAdapter {

    //数据
    private List<Bean_Bigstage_List> mData = new ArrayList<>();

    private Context mContext;

    private BaseApplication application;

    //当前点击的删除活动的position
    private int curDelPosition = -1;

    private static final int AGREE_ACTIVITY = 0;
    private static final int DEL_PUSH_ACTIVITY = 1;

    public static class MyHandler extends Handler {
        private WeakReference<ListAdapterInvite> mListAdapter;

        public MyHandler(ListAdapterInvite listAdapterInvite) {
            mListAdapter = new WeakReference<ListAdapterInvite>(listAdapterInvite);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterInvite listAdapterInvite = mListAdapter.get();
            if (listAdapterInvite != null) {
                String json = (String) msg.obj;
                //点击接受邀请服务器返回结果
                if (msg.what == AGREE_ACTIVITY) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            ToastUtil.showToast(listAdapterInvite.mContext, "已经接受邀请");
                            if (listAdapterInvite.ibCurApply != null) {
                                listAdapterInvite.ibCurApply.setImageResource(R.mipmap.activity_invite_agreed);
                                listAdapterInvite.ibCurApply.setOnClickListener(null);
                                if(listAdapterInvite.curApplyPosition != -1){
                                    listAdapterInvite.mData.get(listAdapterInvite.curApplyPosition).setIs_recommend("1");
                                }
                            }
                        } else {
                            ToastUtil.showToast(listAdapterInvite.mContext, jsonObject.get("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //点击删除服务器返回结果
                else if (msg.what == DEL_PUSH_ACTIVITY) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            ToastUtil.showToast(listAdapterInvite.mContext, "删除成功");
                            listAdapterInvite.mData.remove(listAdapterInvite.curDelPosition);
                            listAdapterInvite.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(listAdapterInvite.mContext, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ImageButton ibCurApply;//当前同意活动的按钮
    private int curApplyPosition = -1;//当前同意活动的position

    private MyHandler handler = new MyHandler(this);

    public ListAdapterInvite(Context context, BaseApplication application) {
        mContext = context;
        this.application = application;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_invite_activity, null);
            viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.ibAgree = (ImageButton) convertView.findViewById(R.id.ib_agree);
            viewHolder.ibDel = (ImageButton) convertView.findViewById(R.id.ib_del);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_endtime);
            viewHolder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_starttime);
            viewHolder.tvIntroduce = (TextView) convertView.findViewById(R.id.tv_introduce);
            viewHolder.tvInvitePeople = (TextView) convertView.findViewById(R.id.tv_people_invite);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.cardview_item_invite_activity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (mData.get(position).getActivity_type().equals("投票")) {
                    intent = new Intent(mContext, BigStageVoteDetailsActivity.class);
                    intent.putExtra("activity_id", mData.get(position).getActivity_id());
                } else {
                    intent = new Intent(mContext, BigStageNormalDetailsActivity.class);
                    Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                    bean_bigstage_list.setActivity_id(mData.get(position).getActivity_id());
                    intent.putExtra("bean", bean_bigstage_list);
                }
                mContext.startActivity(intent);
            }
        });

        viewHolder.ibDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curDelPosition = position;
                VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.delPushActivity, new String[]{"activity_id"}, new String[]{mData.get(position).getActivity_id()}, handler, DEL_PUSH_ACTIVITY);
            }
        });

        viewHolder.tvAddress.setText(mData.get(position).getSite());
        String endTime = mData.get(position).getActivity_end_time();
        if (endTime != null && !"".equals(endTime))
            viewHolder.tvEndTime.setText(DateUtils.format24Time(Long.parseLong(endTime)));
        String startTime = mData.get(position).getActivity_start_time();
        if (startTime != null && !"".equals(startTime))
            viewHolder.tvStartTime.setText(DateUtils.format24Time(Long.parseLong(startTime)));
        viewHolder.tvInvitePeople.setText(mData.get(position).getOrganizer_name());
        String time = mData.get(position).getCreate_time();
        if (time != null && !"".equals(time))
            viewHolder.tvTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(time)));
        viewHolder.tvTitle.setText(mData.get(position).getTitle());

        //邀请参加活动有两种，一种是邀请推荐人，push_type为“one”表示邀请推荐人成为联名发起人
        if (mData.get(position).getPush_type().equals("one")) {
            viewHolder.ivLogo.setImageResource(R.mipmap.activity_icon_invite_people);
            viewHolder.tvIntroduce.setText("邀请您成为本次活动的联名发起人");
            viewHolder.ibAgree.setVisibility(View.VISIBLE);
            if (mData.get(position).getIs_recommend().equals("1")) {
                viewHolder.ibAgree.setImageResource(R.mipmap.activity_invite_agreed);
                viewHolder.ibAgree.setOnClickListener(null);
            } else {
                viewHolder.ibAgree.setImageResource(R.mipmap.activity_invite_agree);
                final ViewHolder finalViewHolder = viewHolder;
                viewHolder.ibAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ibCurApply = finalViewHolder.ibAgree;
                        curApplyPosition = position;
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.agreeRecommend, new String[]{"activity_id"}, new String[]{mData.get(position).getActivity_id()}, handler, AGREE_ACTIVITY);
                    }
                });
            }
        }
        //另外一种是邀请的普通人，通过活动详情页面里的活动详情一键邀请按钮邀请
        else {
            viewHolder.ivLogo.setImageResource(R.mipmap.activity_icon_invite);
            viewHolder.tvIntroduce.setText("您收到了新的活动邀请");
            viewHolder.ibAgree.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        //左上角的logo
        ImageView ivLogo;
        //右下角的同意按钮
        ImageButton ibAgree;
        //右下角的删除按钮
        ImageButton ibDel;
        //收到的活动分类
        TextView tvIntroduce;
        //活动标题
        TextView tvTitle;
        //tvIntroduce右边的收到时间
        TextView tvTime;
        //发起人
        TextView tvInvitePeople;
        //活动开始时间
        TextView tvStartTime;
        //活动结束时间
        TextView tvEndTime;
        //活动地址
        TextView tvAddress;
        //存放上述view的cardview
        CardView cardView;
    }

    public void addData(List<Bean_Bigstage_List> datas) {
        if (mData != null) {
            mData.addAll(datas);
            notifyDataSetChanged();
        } else {
            mData = datas;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (mData != null)
            mData.clear();
        else mData = new ArrayList<>();
        notifyDataSetChanged();
    }
}
