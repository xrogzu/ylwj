package com.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.bean.Bean_DetailsImgs;
import com.administrator.bean.Constant;
import com.administrator.bean.HeadLineBean;
import com.administrator.elwj.ActivityInviteActivity;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.CreditSearchActivity;
import com.administrator.elwj.ExquisiteLifeDetailActivity;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.Interalshop_detailsactivity;
import com.administrator.elwj.Interalshop_interalactivity;
import com.administrator.elwj.Interalshop_shopactivity;
import com.administrator.elwj.MyWebViewActivity;
import com.administrator.elwj.NewAttentionActivity;
import com.administrator.elwj.R;
import com.administrator.fragment.NewestFragment;
import com.administrator.utils.LogUtils;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 最新页面adapter
 * Created by acer on 2016/1/29.
 */
public class ListAdapterHeadline extends BaseAdapter {
    private List<HeadLineBean.DataEntity> myDataLists = new ArrayList<>();
    private Context context;
    private BaseApplication application;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private NewestFragment fragment;
    private List<Bean_DetailsImgs.DataEntity> mImgs = new ArrayList<>();

    private static final int READ_HEAD = 0;
    private static final int GET_CREDIT_IMG = 1;

    private static final int TYPE_SERVICE = 0;//金融管家
    private static final int TYPE_GIFT = 1;//今日礼包
    private static final int TYPE_SHOP_CREDIT = 2;//积分活动
    private static final int TYPE_ATTENTION = 3;//我的关注
    private static final int TYPE_ACTIVITY = 4;//活动邀请
    private static final int TYPE_LIFE = 5;//精致生活
    private static final int TYPE_HEALTH = 6;//健康养生
    private static final int TYPE_CREDIT_CHAGNE = 7;//积分变动


    private static final int TYPE_LAYOUT_SERVICE = 0;
    private static final int TYPE_LAYOUT_OTHER = 1;
    private static final int TYPE_LAYOUT_GIFT = 2;
    private static final int TYPE_LAYOUT_CREDIT = 3;
    private static final int TYPE_LAYOUT_COUNT = 4;


    public ListAdapterHeadline(Context context, BaseApplication application, NewestFragment fragment) {
        this.context = context;
        initImageLoader();
        this.application = application;
        this.fragment = fragment;
    }

    public static class MyHandler extends Handler {
        private WeakReference<ListAdapterHeadline> mListAdapter;

        public MyHandler(ListAdapterHeadline listAdapterHeadline) {
            mListAdapter = new WeakReference<ListAdapterHeadline>(listAdapterHeadline);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterHeadline listAdapterHeadline = mListAdapter.get();
            if (listAdapterHeadline != null) {
                String json = (String) msg.obj;
                if (msg.what == READ_HEAD) {
                    LogUtils.d("xu", json);
                } else if (msg.what == Constant.DETAILS_IMGS) {
                    Gson gson = new Gson();
                    Bean_DetailsImgs imgs_bean = gson.fromJson(json, Bean_DetailsImgs.class);
                    if (imgs_bean.getResult() == 1) {
                        List<Bean_DetailsImgs.DataEntity> dataEntities = imgs_bean.getData();
                        if (dataEntities != null && dataEntities.size() > 0) {
                            listAdapterHeadline.mImgs.addAll(dataEntities);
                            listAdapterHeadline.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }


    private MyHandler handler = new MyHandler(this);

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public void addData(List<HeadLineBean.DataEntity> myDataLists) {
        if (this.myDataLists != null) {
            this.myDataLists.addAll(myDataLists);
        } else
            this.myDataLists = myDataLists;
        for (int i = 0; i < myDataLists.size(); ++i) {
            getImg(myDataLists.get(i));
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (myDataLists != null)
            this.myDataLists.clear();
        else myDataLists = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myDataLists.size();
    }

    @Override
    public Object getItem(int position) {
        return myDataLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        String type = myDataLists.get(position).getDescription();
        if (type.equals("nous") || type.equals("recommend") || type.startsWith("product")) {
            return TYPE_LAYOUT_SERVICE;
        } else if (type.equals("gift")) {
            return TYPE_LAYOUT_GIFT;
        } else if (type.equals("point")) {
            return TYPE_LAYOUT_CREDIT;
        } else {
            return TYPE_LAYOUT_OTHER;
        }

    }

    @Override
    public int getViewTypeCount() {
        return TYPE_LAYOUT_COUNT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final String typeS = myDataLists.get(position).getDescription();
        int type = -1;
        if (typeS != null) {
            if (typeS.equals("nous")) {
                type = TYPE_SERVICE;
            } else if (typeS.equals("recommend")) {
                type = TYPE_SERVICE;
            } else if (typeS.startsWith("product")) {
                int typeI = Integer.parseInt(typeS.substring(typeS.length() - 1));
                if (typeI == 7)
                    type = TYPE_LIFE;
                else if (typeI == 8)
                    type = TYPE_HEALTH;
                else
                    type = TYPE_SERVICE;
            } else if (typeS.equals("gift")) {
                type = TYPE_GIFT;
            } else if (typeS.equals("point")) {
                type = TYPE_SHOP_CREDIT;
            } else if (typeS.equals("activity")) {
                type = TYPE_ACTIVITY;
            } else if (typeS.equals("attention")) {
                type = TYPE_ATTENTION;
            } else if (typeS.equals("credit")) {
                type = TYPE_CREDIT_CHAGNE;
            } else return convertView;
        }

        int currentType = getItemViewType(position);
        //推送时间
        String time = myDataLists.get(position).getCreate_time();
        if (currentType == TYPE_LAYOUT_SERVICE) {
            ServiceViewHolder serviceViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_item_service, null);
                serviceViewHolder = new ServiceViewHolder();
                serviceViewHolder.tvServiceTime = (TextView) convertView.findViewById(R.id.tv_headline_item_time);
                serviceViewHolder.tvServiceTitle = (TextView) convertView.findViewById(R.id.tv_headline_item_title);
                serviceViewHolder.cardViewService = (CardView) convertView.findViewById(R.id.cardview_service);
                serviceViewHolder.ivService = (ImageView) convertView.findViewById(R.id.iv_headline_item_service);
                serviceViewHolder.tvFrom = (TextView) convertView.findViewById(R.id.tv_from);
                serviceViewHolder.ivRedCircle = (ImageView) convertView.findViewById(R.id.iv_red_circle);
                convertView.setTag(serviceViewHolder);
            } else {
                serviceViewHolder = (ServiceViewHolder) convertView.getTag();
            }
            if (time != null && !"".equals(time)) {
                serviceViewHolder.tvServiceTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(time)));
            }
            if (myDataLists.get(position).getIs_read().equals("0")) {
                serviceViewHolder.ivRedCircle.setVisibility(View.VISIBLE);
            } else {
                serviceViewHolder.ivRedCircle.setVisibility(View.GONE);
            }
            serviceViewHolder.tvServiceTitle.setText(myDataLists.get(position).getTitle());
            String image = myDataLists.get(position).getBusiness_icon();
            if (image != null && !"".equals(image)) {
                imageLoader.displayImage(image, serviceViewHolder.ivService, options);
            }
            if (type == TYPE_SERVICE) {
                serviceViewHolder.tvFrom.setText("\"金融管家\"");
                serviceViewHolder.tvFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.JUMP);
                        intent.putExtra("jump", HomeActivity.JUMP_FIN);
                        context.sendBroadcast(intent);
                    }
                });
            } else if (type == TYPE_HEALTH) {
                serviceViewHolder.tvFrom.setText("\"健康养生\"");
                serviceViewHolder.tvFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.JUMP);
                        intent.putExtra("jump", HomeActivity.JUMP_HEALTH);
                        context.sendBroadcast(intent);
                    }
                });
            } else {
                serviceViewHolder.tvFrom.setText("\"精致生活\"");
                serviceViewHolder.tvFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.JUMP);
                        intent.putExtra("jump", HomeActivity.JUMP_LIFE);
                        context.sendBroadcast(intent);
                    }
                });
            }
            final ServiceViewHolder finalServiceViewHolder = serviceViewHolder;
            serviceViewHolder.cardViewService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (typeS != null) {
                        if (typeS.equals("nous")) {
                            Intent intent = new Intent(context, MyWebViewActivity.class);
                            intent.putExtra("id", myDataLists.get(position).getActivity_id());
                            intent.putExtra("title", "金融常识");
                            intent.putExtra("type", MyWebViewActivity.TYPE_COMM);
                            context.startActivity(intent);
                            sendReadInfo(myDataLists.get(position), finalServiceViewHolder.ivRedCircle, false);
                        } else if (typeS.equals("recommend")) {
                            Intent intent = new Intent(context, MyWebViewActivity.class);
                            intent.putExtra("id", myDataLists.get(position).getActivity_id());
                            intent.putExtra("title", "推荐消息");
                            intent.putExtra("type", MyWebViewActivity.TYPE_RECOMMEND);
                            context.startActivity(intent);
                            sendReadInfo(myDataLists.get(position), finalServiceViewHolder.ivRedCircle, false);
                        } else if (typeS.startsWith("product")) {
                            int typeI = Integer.parseInt(typeS.substring(typeS.length() - 1));
                            if (typeI < 7) {
                                Intent intent = new Intent(context, MyWebViewActivity.class);
                                intent.putExtra("id", myDataLists.get(position).getActivity_id());
                                intent.putExtra("title", "金融管家");
                                intent.putExtra("type", MyWebViewActivity.TYPE_PRODUCT);
                                context.startActivity(intent);
                                sendReadInfo(myDataLists.get(position), finalServiceViewHolder.ivRedCircle, false);
                            } else if (typeI == 7) {
                                Intent intent = new Intent(context, ExquisiteLifeDetailActivity.class);
                                intent.putExtra("id", myDataLists.get(position).getActivity_id());
                                intent.putExtra("title", "精致生活");
                                context.startActivity(intent);
                                sendReadInfo(myDataLists.get(position), finalServiceViewHolder.ivRedCircle, false);
                            } else if (typeI == 8) {
                                Intent intent = new Intent(context, ExquisiteLifeDetailActivity.class);
                                intent.putExtra("id", myDataLists.get(position).getActivity_id());
                                intent.putExtra("title", "健康养生");
                                context.startActivity(intent);
                                sendReadInfo(myDataLists.get(position), finalServiceViewHolder.ivRedCircle, false);
                            }
                        }
                    }
                }
            });

        } else if (currentType == TYPE_LAYOUT_CREDIT) {
            CreditViewHolder creditViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_item_credit, null);
                creditViewHolder = new CreditViewHolder();
                creditViewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_credit_time);
                creditViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_credit_title);
                creditViewHolder.iv1 = (ImageView) convertView.findViewById(R.id.iv_credit1);
                creditViewHolder.iv2 = (ImageView) convertView.findViewById(R.id.iv_credit2);
                creditViewHolder.iv3 = (ImageView) convertView.findViewById(R.id.iv_credit3);
                creditViewHolder.cardViewCredit = (CardView) convertView.findViewById(R.id.cardview_credit);
                creditViewHolder.tvFrom = (TextView) convertView.findViewById(R.id.tv_credit_from);
                convertView.setTag(creditViewHolder);
            } else {
                creditViewHolder = (CreditViewHolder) convertView.getTag();
            }
            if (time != null && !"".equals(time))
                creditViewHolder.tvTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(time)));
            creditViewHolder.tvTitle.setText(myDataLists.get(position).getTitle());
            creditViewHolder.cardViewCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Interalshop_detailsactivity.class);
                    intent.putExtra("id", myDataLists.get(position).getActivity_id());
                    intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                    context.startActivity(intent);
                }
            });
            creditViewHolder.tvFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(context, Interalshop_interalactivity.class);
                    intent3.putExtra("num", 0);
                    context.startActivity(intent3);
                }
            });

            int begin = mImgs.size() - 1;
            int imgCount = 0;
            for (; begin >= 0 && imgCount < 3; --begin) {
                if (mImgs.get(begin).getGoods_id() == Integer.parseInt(myDataLists.get(position).getActivity_id())) {
                    if (imgCount == 0) {
                        imageLoader.displayImage(mImgs.get(begin).getThumbnail(), creditViewHolder.iv1, options);
                        creditViewHolder.iv1.setVisibility(View.VISIBLE);
                        imgCount++;
                    } else if (imgCount == 1) {
                        creditViewHolder.iv2.setVisibility(View.VISIBLE);
                        imageLoader.displayImage(mImgs.get(begin).getThumbnail(), creditViewHolder.iv2, options);
                        imgCount++;
                    } else if (imgCount == 2) {
                        creditViewHolder.iv3.setVisibility(View.VISIBLE);
                        imageLoader.displayImage(mImgs.get(begin).getThumbnail(), creditViewHolder.iv3, options);
                        imgCount++;
                    }
                }

            }
//            for (; imgCount < 3; --imgCount) {
//
//            }

        } else if (currentType == TYPE_LAYOUT_GIFT) {
            GiftViewHolder giftViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_item_gift, null);
                giftViewHolder = new GiftViewHolder();
                giftViewHolder.cardViewGift = (CardView) convertView.findViewById(R.id.cardview_gift);
                giftViewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_gift_time);
                giftViewHolder.ivGift = (ImageView) convertView.findViewById(R.id.iv_gift_main);
                giftViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_gift_title);
                giftViewHolder.tvFrom = (TextView) convertView.findViewById(R.id.tv_gift_from);
                convertView.setTag(giftViewHolder);
            } else {
                giftViewHolder = (GiftViewHolder) convertView.getTag();
            }
            giftViewHolder.tvFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent5 = new Intent(context, Interalshop_shopactivity.class);
                    intent5.putExtra("num", 2);
                    context.startActivity(intent5);
                }
            });
            if (time != null && !"".equals(time))
                giftViewHolder.tvTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(time)));
            giftViewHolder.tvTitle.setText(myDataLists.get(position).getTitle());
            imageLoader.displayImage(myDataLists.get(position).getBusiness_icon(), giftViewHolder.ivGift, options);
            giftViewHolder.cardViewGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Interalshop_detailsactivity.class);
                    intent.putExtra("id", myDataLists.get(position).getActivity_id());
                    intent.putExtra("imageRemoved",myDataLists.get(position).getBusiness_icon());
                    intent.putExtra("type", Constant.SHOP_TYPE_AUTO);
                    context.startActivity(intent);
                }
            });
        } else {
            OthersViewHolder othersViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_item_others, null);
                othersViewHolder = new OthersViewHolder();
                othersViewHolder.cardViewCustom = (CardView) convertView.findViewById(R.id.cardview_custom);
                othersViewHolder.tvCustomTime = (TextView) convertView.findViewById(R.id.tv_headline_item_time);
                othersViewHolder.tvCustomTitle = (TextView) convertView.findViewById(R.id.tv_headline_item_title);
                othersViewHolder.ivCustom = (ImageView) convertView.findViewById(R.id.iv_headline_item);
                othersViewHolder.tvHeadLine = (TextView) convertView.findViewById(R.id.tv_headline_item_tips);
                othersViewHolder.ivRedCircle = (ImageView) convertView.findViewById(R.id.iv_red_circle);
                convertView.setTag(othersViewHolder);
            } else {
                othersViewHolder = (OthersViewHolder) convertView.getTag();
            }
            if (time != null && !"".equals(time)) {
                othersViewHolder.tvCustomTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(time)));
            }
            if (myDataLists.get(position).getIs_read().equals("0")) {
                othersViewHolder.ivRedCircle.setVisibility(View.VISIBLE);
            } else {
                othersViewHolder.ivRedCircle.setVisibility(View.GONE);
            }
            if (type == TYPE_ATTENTION) {
                final OthersViewHolder finalOthersViewHolder = othersViewHolder;
                othersViewHolder.cardViewCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewAttentionActivity.class);
                        context.startActivity(intent);
                        sendReadInfo(myDataLists.get(position), finalOthersViewHolder.ivRedCircle, true);
                    }
                });
                othersViewHolder.tvHeadLine.setText("新的关注");
                othersViewHolder.ivCustom.setImageResource(R.mipmap.headline_attention);
            } else if (type == TYPE_ACTIVITY) {
                final OthersViewHolder finalOthersViewHolder1 = othersViewHolder;
                othersViewHolder.cardViewCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityInviteActivity.class);
                        context.startActivity(intent);
                        sendReadInfo(myDataLists.get(position), finalOthersViewHolder1.ivRedCircle, true);
                    }
                });
                othersViewHolder.tvHeadLine.setText("活动邀请");
                othersViewHolder.ivCustom.setImageResource(R.mipmap.headline_activity);
            } else {
                final OthersViewHolder finalOthersViewHolder2 = othersViewHolder;
                othersViewHolder.cardViewCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendReadInfo(myDataLists.get(position), finalOthersViewHolder2.ivRedCircle, true);
                        Intent intent = new Intent(context, CreditSearchActivity.class);
//                        intent.putExtra("add_credit", myDataLists.get(position).getTitle());
                        context.startActivity(intent);
                    }
                });
                othersViewHolder.tvHeadLine.setText("积分查询");
                othersViewHolder.ivCustom.setImageResource(R.mipmap.headline_credit_icon);
            }
            othersViewHolder.tvCustomTitle.setText(myDataLists.get(position).getTitle());
        }


        return convertView;
    }


    private void sendReadInfo(HeadLineBean.DataEntity dataEntity, ImageView imageView, boolean needSend) {
        if (needSend) {
            dataEntity.setIs_read("1");
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.readHeadLine, new String[]{"push_id"}, new String[]{dataEntity.getPush_id()}, handler, READ_HEAD);
        } else {
            fragment.setReadFlag(dataEntity);
        }
        if (imageView != null)
            imageView.setVisibility(View.GONE);

    }


    public class OthersViewHolder {
        CardView cardViewCustom;
        ImageView ivCustom;
        TextView tvCustomTitle;
        TextView tvCustomTime;
        TextView tvHeadLine;
        ImageView ivRedCircle;
    }

    public class ServiceViewHolder {
        CardView cardViewService;
        ImageView ivService;
        TextView tvServiceTitle;
        TextView tvServiceTime;
        TextView tvFrom;
        ImageView ivRedCircle;
    }

    public class GiftViewHolder {
        CardView cardViewGift;
        TextView tvTitle;
        TextView tvTime;
        ImageView ivGift;
        TextView tvFrom;
    }

    public class CreditViewHolder {
        CardView cardViewCredit;
        TextView tvTitle;
        TextView tvTime;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        TextView tvFrom;
    }

    public void update(String data) {
        LogUtils.d("xu", "ListAdpterHeadLine_update");
        if (data != null && !"".equals(data)) {
            Gson gson = new Gson();
            HeadLineBean.DataEntity dataEntity = gson.fromJson(data, HeadLineBean.DataEntity.class);
            if (dataEntity != null) {
                boolean added = false;
                if (dataEntity.getDescription().equals("activity")) {
                    for (int i = 0; i < myDataLists.size(); ++i) {
                        if (myDataLists.get(i).getDescription().equals("activity")) {
                            updateDataEntity(i, dataEntity);
                            added = true;
                            break;
                        }
                    }
                } else if (dataEntity.getDescription().equals("credit")) {
                    for (int i = 0; i < myDataLists.size(); ++i) {
                        if (myDataLists.get(i).getDescription().equals("credit")) {
                            updateDataEntity(i, dataEntity);
                            added = true;
                            break;
                        }
                    }
                } else if (dataEntity.getDescription().equals("attention")) {
                    for (int i = 0; i < myDataLists.size(); ++i) {
                        if (myDataLists.get(i).getDescription().equals("attention")) {
                            updateDataEntity(i, dataEntity);
                            added = true;
                            break;
                        }
                    }
                } else {
                    add2List(dataEntity);
                    added = true;
                }
                if (!added) {
                    add2List(dataEntity);
                }
            }
        }
    }

    /**
     * 往列表中添加新的数据
     *
     * @param dataEntity
     */
    public void add2List(HeadLineBean.DataEntity dataEntity) {
        myDataLists.add(0, dataEntity);
        fragment.save2DB(dataEntity);
        getImg(dataEntity);
        notifyDataSetChanged();
    }

    private void getImgs() {

    }

    /**
     * 更新列表
     *
     * @param position
     * @param newEntity
     */
    public void updateDataEntity(int position, HeadLineBean.DataEntity newEntity) {
        HeadLineBean.DataEntity oldEntity = myDataLists.get(position);
        oldEntity.setIs_read("0");
        oldEntity.setTitle(newEntity.getTitle());
        oldEntity.setActivity_id(newEntity.getActivity_id());
        oldEntity.setBusiness_icon(newEntity.getBusiness_icon());
        oldEntity.setCreate_time(newEntity.getCreate_time());
        oldEntity.setDescription(newEntity.getDescription());
        oldEntity.setPush_id(newEntity.getPush_id());
        myDataLists.remove(position);
        myDataLists.add(0, oldEntity);
        getImg(newEntity);
        fragment.save2DB(newEntity);
        notifyDataSetChanged();
    }

    /**
     * 获取积分活动的三张图片
     *
     * @param dataEntity
     */
    public void getImg(HeadLineBean.DataEntity dataEntity) {
        if (dataEntity.getDescription().equals("point")) {
            VolleyUtils.NetUtils(application.getRequestQueue(), Constant.baseUrl + Constant.detailsImg, new String[]{"id"}, new String[]{dataEntity.getActivity_id()}, handler, Constant.DETAILS_IMGS);
        }
    }
}
