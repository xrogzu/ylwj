package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.R;
import com.administrator.fragment.BigStageItemFragment;
import com.administrator.utils.DateUtils;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 社区大舞台活动adapter
 * Created by acer on 2016/1/19.
 */
public class ListAdapterBigstageHome extends BaseAdapter {
    private Context context;
    private List<Bean_Bigstage_List> lists = new ArrayList<>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private DisplayImageOptions optionhead;
    private int mType;
    private BaseApplication application;
    private boolean isAttentionActivity = false;
    private static final int AGREE_ACTIVITY = 0;//接收邀请
    private static final int DELETE_ACTIVITY = 1;
    private int DeletePosition = -1;
    private ImageButton ibCurApply;//当前同意邀请的按钮
    private int curApplyPosition = -1;//当前点击的同意按钮的position
    private boolean isRecommend = false;//是否是收到活动邀请中的联名发起人邀请界面

    public void addData(List<Bean_Bigstage_List> lists, Context context, RequestQueue requestQueue) {
        this.lists = lists;
        this.context = context;
        notifyDataSetChanged();
    }

    public class PicHolder {
        private ImageView iv;
        private String url;

        public ImageView getIv() {
            return iv;
        }

        public void setIv(ImageView iv) {
            this.iv = iv;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public static class MyHandler extends Handler {
        private WeakReference<ListAdapterBigstageHome> mListAdapter;

        public MyHandler(ListAdapterBigstageHome listAdapterBigstageHome) {
            mListAdapter = new WeakReference<ListAdapterBigstageHome>(listAdapterBigstageHome);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterBigstageHome listAdapterBigstageHome = mListAdapter.get();
            if (listAdapterBigstageHome != null) {
                String json = (String) msg.obj;
                switch (msg.what) {
                    case DELETE_ACTIVITY:
                        if (listAdapterBigstageHome.DeletePosition != -1) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getInt("result") == 1) {
                                    ToastUtil.showToast(listAdapterBigstageHome.context, "删除成功");
//                                    Toast.makeText(listAdapterBigstageHome.context, "删除成功", Toast.LENGTH_SHORT).show();
                                    listAdapterBigstageHome.lists.remove(listAdapterBigstageHome.DeletePosition);
                                    listAdapterBigstageHome.notifyDataSetChanged();
                                } else {
                                    ToastUtil.showToast(listAdapterBigstageHome.context, jsonObject.getString("message"));
//                                    Toast.makeText(listAdapterBigstageHome.context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case AGREE_ACTIVITY:
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if ((int) jsonObject.get("result") == 1) {
                                ToastUtil.showToast(listAdapterBigstageHome.context, "已经接受邀请");
                                if (listAdapterBigstageHome.ibCurApply != null) {
                                    listAdapterBigstageHome.ibCurApply.setImageResource(R.mipmap.activity_invite_agreed);
                                    listAdapterBigstageHome.ibCurApply.setOnClickListener(null);
                                    if(listAdapterBigstageHome.curApplyPosition != -1)
                                        listAdapterBigstageHome.lists.get(listAdapterBigstageHome.curApplyPosition).setIs_recommend("1");
                                }
                            } else {
                                ToastUtil.showToast(listAdapterBigstageHome.context, jsonObject.get("message").toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    public ListAdapterBigstageHome(Context context, int type) {
        this.context = context;
        this.mType = type;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public void setIsAttentionActivity(BaseApplication application, boolean isAttentionActivity) {
        this.isAttentionActivity = isAttentionActivity;
        this.application = application;
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
        TextView tv_Title;
        TextView tv_name;
        ImageView iv_head;
        TextView tv_startTime;
        TextView tv_picCount;
        TextView tv_state;
        TextView tv_readCount;
        ImageView iv_Pic;
        ImageView iv_DeleteActivity;
        ImageButton ibAgree;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder_BigStage vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_bigstage, null);
            vh = new ViewHolder_BigStage();
            vh.tv_Title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            vh.tv_picCount = (TextView) convertView.findViewById(R.id.tv_pic_count);
            vh.tv_readCount = (TextView) convertView.findViewById(R.id.tv_read_count);
            vh.tv_startTime = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            vh.iv_Pic = (ImageView) convertView.findViewById(R.id.iv_bigstage_pic);
            vh.iv_DeleteActivity = (ImageView) convertView.findViewById(R.id.iv_delete_attention);
            vh.ibAgree = (ImageButton) convertView.findViewById(R.id.ib_agree);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder_BigStage) convertView.getTag();
        }

        //“我的”中“收到活动邀请”中的联名发起人邀请界面，显示同意按钮
        if(isRecommend) {
            vh.ibAgree.setVisibility(View.VISIBLE);
            if (lists.get(position).getIs_recommend().equals("1")) {
                vh.ibAgree.setImageResource(R.mipmap.activity_invite_agreed);
                vh.ibAgree.setOnClickListener(null);
            } else {
                vh.ibAgree.setImageResource(R.mipmap.activity_invite_agree);
                final ViewHolder_BigStage finalViewHolder = vh;
                finalViewHolder.ibAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ibCurApply = finalViewHolder.ibAgree;
                        curApplyPosition = position;
                        VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.agreeRecommend, new String[]{"activity_id"}, new String[]{lists.get(position).getActivity_id()}, handler, AGREE_ACTIVITY);
                    }
                });
            }
        }else{
            vh.ibAgree.setVisibility(View.GONE);
        }
        final Bean_Bigstage_List bean = lists.get(position);
        //vh.tv_name.setText(bean.getTitle());
        long curTime = System.currentTimeMillis();
        String time = lists.get(position).getApply_start_time();
        long applyStartTime = 0;
        long applyEndTime = 0;
        long activityStartTime = 0;
        long activityEndTime = 0;
        vh.tv_Title.setText(bean.getTitle());
        time = DateUtils.delDot(time);
        if (time != null && !"".equals(time))
            applyStartTime = Long.parseLong(time);
        time = lists.get(position).getApply_end_time();
        time = DateUtils.delDot(time);
        if (time != null && !"".equals(time))
            applyEndTime = Long.parseLong(time);
        time = lists.get(position).getActivity_start_time();
        time = DateUtils.delDot(time);
        if (time != null && !"".equals(time))
            activityStartTime = Long.parseLong(time);
        time = lists.get(position).getActivity_end_time();
        LogUtils.e("活动结束时间", time);
        time = DateUtils.delDot(time);
        if (time != null && !"".equals(time))
            activityEndTime = Long.parseLong(time);
        if (bean.getIs_end().equals("1"))
            vh.tv_state.setText("活动结束");
        else if (curTime < applyEndTime)
            vh.tv_state.setText("报名中");
        else
            vh.tv_state.setText("报名结束");
        if ("公共".equals(bean.getActivity_type())) {
            vh.tv_state.setText("公共活动");
        }
        if ("投票".equals(bean.getActivity_type())) {
            vh.tv_state.setText("投票活动");
            if ("1".equals(bean.getIs_vote())) {
                vh.tv_state.setText("已投票");
            }
            LogUtils.e("投票哈哈", bean.getTitle() + "vote?  " + bean.getIs_vote());
//            if(curTime < activityEndTime){
//                vh.tv_state.setText("投票中");
//            }else
//                vh.tv_state.setText("投票结束");
        }
        final List<Bean_Bigstage_List.PhotosEntity> photosEntities = lists.get(position).getPhotos();
        if (photosEntities != null)
            vh.tv_picCount.setText(String.format("%d", photosEntities.size()));
        if (bean.getActivity_start_time() != null) {
            String sstime = bean.getActivity_start_time().trim();
            LogUtils.e("Main时间", sstime);
            if (!"".equals(sstime)) {
                sstime = DateUtils.delDot(sstime);
                Date date = new Date(Long.parseLong(sstime));
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
                vh.tv_startTime.setText(sdf.format(date) + " 开始");
            }
        }

        if (isAttentionActivity) {
            vh.iv_DeleteActivity.setVisibility(View.VISIBLE);
            vh.iv_DeleteActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeletePosition = position;
                    VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.cancelAttention, new String[]{"activity_id"}, new String[]{lists.get(position).getActivity_id()}, handler, DELETE_ACTIVITY);
                }
            });
        } else {
            vh.iv_DeleteActivity.setVisibility(View.GONE);
        }

        vh.tv_readCount.setText(bean.getRead_num());
        if (photosEntities != null && photosEntities.size() > 0) {
            vh.iv_Pic.measure(0,0);
            final double [] ratio = {0};
            imageLoader.loadImage(photosEntities.get(0).getThumbnail(), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    //ToastUtil.showToast(listAdapter.context,"图片加载失败");
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (bitmap != null && photosEntities.get(0) != null && photosEntities.get(0).getThumbnail() != null) {
                        if (bitmap.getHeight() != 0 && vh.iv_Pic.getMeasuredHeight() != 0) {
                            double ratio1 = bitmap.getWidth() * 1.0 / bitmap.getHeight();
                            double ratio2 = vh.iv_Pic.getMeasuredWidth() * 1.0 / vh.iv_Pic.getMeasuredHeight();
                            if (ratio1 < ratio2) {
                                ratio[0] = ratio2;
                            }
                        }
                        Bitmap displayBitmap;
                        if (ratio[0] != 0) {
                            displayBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int) (bitmap.getWidth() / ratio[0]));
                        } else {
                            displayBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                        }
                        if (vh.iv_Pic != null) {
                            vh.iv_Pic.setImageBitmap(displayBitmap);
                        }
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
//            imageLoader.displayImage(photosEntities.get(0).getThumbnail(), vh.iv_Pic, options);
//            vh.iv_Pic.post(new Runnable() {
//                @Override
//                public void run() {
//                    PicHolder picHolder = new PicHolder();
//                    picHolder.setIv(vh.iv_Pic);
//                    picHolder.setUrl(photosEntities.get(0).getThumbnail());
//                    Message message = handlerPic.obtainMessage();
//                    message.obj = picHolder;
//                    message.sendToTarget();
//                }
//            });
        } else
            imageLoader.displayImage("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1457937501&di=eb5f10ece9feec97ed179f2dc45fb4d5&src=http://preview.quanjing.com/ing036/ing_42323_03890.jpg", vh.iv_Pic, options);
        imageLoader.displayImage(bean.getOrganizer_face(), vh.iv_head, optionhead);
        vh.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAdapterBigstageHome.this.context, HomePageActivity.class);
                intent.putExtra("member_id", bean.getOrganizer_id());
                context.startActivity(intent);
            }
        });
        vh.tv_name.setText(bean.getOrganizer_name());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mType == BigStageItemFragment.EDIT_ACTIVITY) {
                    Intent intent = new Intent("android.intent.action.EDIT_ACTIVITY");
                    intent.putExtra("golist", false);
                    intent.putExtra("data", lists.get(position));
                    context.sendBroadcast(intent);
                } else {
                    if (bean != null && "投票".equals(bean.getActivity_type())) {
                        Intent intent = new Intent(context, BigStageVoteDetailsActivity.class);
                        intent.putExtra("activity_id", bean.getActivity_id());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, BigStageNormalDetailsActivity.class);
                        intent.putExtra("bean", bean);
                        context.startActivity(intent);
                    }
                }


//               String type= bean.getActivity_type();
//                if(mType==Constant.CHOOSE_BIG){//评选详情
//                    Intent intent=new Intent(context, BigStage_Choose_Activity.class);
//                    intent.putExtra("bean",bean);
//                    context.startActivity(intent);
//                }else {
//                    if(type != null && type.equals("一般")){
//                        Intent intent=new Intent(context, BigStageNormalDetailsActivity.class);
//                        intent.putExtra("bean",bean);
//                        context.startActivity(intent);
//                    }else if(type != null && type.equals("投票")){
//                        Intent intent=new Intent(context, BigStageVoteDetailsActivity.class);
//                        intent.putExtra("bean",bean);
//                        context.startActivity(intent);
//                    }
//                }

            }
        });

        return convertView;
    }

    public void clear() {
        if (lists != null) {
            if (lists.size() > 0) {
                lists.clear();
            }
        } else {
            lists = new ArrayList<>();
        }
    }

    public void setIsRecommend(boolean isRecommend){
        this.isRecommend = isRecommend;
    }
}
