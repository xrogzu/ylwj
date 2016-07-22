package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.BigstageUser;
import com.administrator.bean.Constant;
import com.administrator.bean.NormalPeopleInActivity;
import com.administrator.utils.DateUtils;
import com.administrator.utils.LocationUtil;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.view.MyXListView;
import com.administrator.view.RoundImageView;
import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//社区大舞台一般和公共活动页面
//intent传过来的bean（类型为Bean_Bigstage_List），bean里有效的只是id，然后根据id再像服务器请求活动详细信息，存放在private ActivityDetails.DataEntity mDataEntity，真正的数据存放在mDataEntity中，但要注意使用前需要判断是否为空
//通过startactivity启动此activity时，可以这样启动：
//intent = new Intent(Context, BigStageNormalDetailsActivity.class);
//Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
//bean_bigstage_list.setActivity_id(id);
//intent.putExtra("bean", bean_bigstage_list);
//startActivity(intent);
public class BigStageNormalDetailsActivity extends AppCompatActivity implements View.OnClickListener, LocationUtil.MyLocationListener {

    //活动标题
    private TextView tv_title;
    //开始报名
    private LinearLayout li_bigbottom_startvote;
    //倒计时
    private TextView tv_bigbottom_onlyleftNum;

    //发起人头像
    private ImageView ivHead;
    //发起人名称
    private TextView tvName;
    //转发身边新鲜事弹出edit
    private EditText et_pop;
    //邀请按钮
    private ImageButton ibInvitation;
    //邀请人发起的活动数量
    private TextView tvInvitationCount;
    //活动开始时间
    private TextView tvStartTime;
    private CardView rlRecommendHide;
    private RecommendGridViewAdapter mRecommendGridAdapter;
    //活动地点
    private TextView tvAddress;
    //报名人数
    private TextView tvPeopleCount;
    //共可报名人数
    private TextView tvMaxPeopleCount;
    private ProgressDialog mGetLocationDialog;
    //活动图片gridview
    private GridView gvPics;
    //活动图片
    private List<ActivityDetails.DataEntity.PhotosEntity> photosEntities;
    private TimeCount timeCount;
    //报名人数
    private TextView tvPeopleCount2;
    //报名人listview
    private MyXListView listView;
    //推荐人三个imageview，最多显示三个，多余三个右侧显示展开按钮，少于三个有多少显示多少
    private RoundImageView ivRecommend1;
    private RoundImageView ivRecommend2;
    private RoundImageView ivRecommend3;
    //推荐人右侧展开按钮，当有推荐人时，按钮显示，否则按钮不显示
    private ImageView ivListRecommend;
    //推荐人整个栏目
    private RelativeLayout rlListRecommend;
    //活动内容textview，当文字过多时，右下角显示一个箭头可以展开
    private ExpandableTextView etvContent;
    //分享按钮
    private LinearLayout li_bigbottom_share;
    //报名人列表当前为第几页
    private int curPage = 1;
    //报名人列表中的每一页的个数
    private int pageSize = 5;
    //报名人列表是否还有更多的报名人可以从服务器获取
    private boolean hasMoreData = true;
    //活动相册数量
    private TextView tvPhotoCount;
    private BaseApplication appContext;
    //活动的bean
    private Bean_Bigstage_List bean;

    private ActivityDetails mActivityDetails;
    private ActivityDetails.DataEntity mDataEntity;
    private static final int APPLY_ACTIVITY = 0;
    private static final int CANCEL_ATTENTION = 1;
    private static final int RECOMMEND_PEOPLE = 2;
    private static final int NORMAL_POEPLE = 3;
    private static final int ONEKEY_INVITION = 4;
    private static final int GET_ACTIVITY = 5;
    private static final int SHARE = 6;
    private static final int CLOSE_PROGRESS_DIALOG = 7;
    private static final int SHOW_TIPS_DIALOG = 8;
    private ApplyActivityPeopleAdapter mAdapter;
    private LinearLayout li_bigbottom_attention;
    private double coordinate_x;
    private double coordinate_y;
    private String coordinate;
    private String transComment;
    private boolean isTransActivity = false;


    private TextView tv_bigbottom_starvote;
    private TextView tv_bigbottom_onlyleft;

    private List<BigstageUser.DataEntity> mRecommendPeople;

    @Override
    public void onLoactionChanged(AMapLocation amapLocation) {
        LogUtils.d("xu_location", "onLocationChanged");
        if (mGetLocationDialog != null)
            mGetLocationDialog.dismiss();
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                LogUtils.d("xu_location", "onLocationChanged" + amapLocation.getCity());
                coordinate_x = amapLocation.getLongitude();//获取纬度
                coordinate_y = amapLocation.getLatitude();//获取经度
                coordinate = amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict();
            }
        }
        if (isTransActivity)
            transActivity();
    }

    public void transActivity() {
        if (coordinate != null)
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shared2Nearby, new String[]{
                            "activity_id", "coordinate", "message_content", "coordinate_x", "coordinate_y"}, new String[]{
                            mDataEntity.getActivity_id(), coordinate, transComment == null ? "" : transComment, String.format("%f", coordinate_x), String.format("%f", coordinate_y)},
                    handler, SHARE);
        else {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shared2Nearby, new String[]{
                            "activity_id", "coordinate", "message_content", "coordinate_x", "coordinate_y"}, new String[]{
                            mDataEntity.getActivity_id(), "", transComment == null ? "" : transComment, "", ""},
                    handler, SHARE);
        }
        coordinate = null;
        transComment = null;
        mGetLocationDialog = null;
        isTransActivity = false;
    }


    public static class MyHandler extends Handler {

        private WeakReference<BigStageNormalDetailsActivity> mActivity;

        public MyHandler(BigStageNormalDetailsActivity activity) {
            mActivity = new WeakReference<BigStageNormalDetailsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStageNormalDetailsActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.ATTENTION_ACTIVITY) {
                    try {
                        JSONObject object = new JSONObject(json);
                        String message = object.optString("message");
                        ToastUtil.showToast(activity, message + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (which == APPLY_ACTIVITY) {
                    LogUtils.d("xu", json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            ToastUtil.showToast(activity, "报名成功");
                            activity.curPage = 1;
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getNormalApplayPeople, new String[]{"activity_id", "page", "pageSize"}, new String[]{activity.mDataEntity.getActivity_id(), String.format("%d", activity.curPage), String.format("%d", activity.pageSize)}, activity.handler, NORMAL_POEPLE);
                            VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{activity.bean.getActivity_id()}, activity.handler, GET_ACTIVITY);
                        } else {
                            ToastUtil.showToast(activity, jsonObject.get("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == CANCEL_ATTENTION) {
                    LogUtils.d("xu", json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ((int) jsonObject.get("result") == 1) {
                            ToastUtil.showToast(activity, "取消关注成功");
                        } else {
                            ToastUtil.showToast(activity, jsonObject.get("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == RECOMMEND_PEOPLE) {
                    LogUtils.d("xu + " + which, json);
                    Gson gson = new Gson();
                    BigstageUser user = gson.fromJson(json, BigstageUser.class);
                    if (user.getResult() == 1) {
                        List<BigstageUser.DataEntity> dataEntities = user.getData();
                        if (activity.mRecommendPeople == null)
                            activity.mRecommendPeople = new ArrayList<>();
                        else activity.mRecommendPeople.clear();
                        if (dataEntities.size() > 0) {
                            activity.mRecommendPeople.addAll(dataEntities);
                            activity.mRecommendGridAdapter.update();
                            activity.displayThreeRecommend();
                        }
                    }

                } else if (which == ONEKEY_INVITION) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(json);
                        int result = object.getInt("result");
                        if (result == 1) {
                            ToastUtil.showInviteToast(activity, object.getString("message"));
                        } else {
                            ToastUtil.showToast(activity, object.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == GET_ACTIVITY) {
                    LogUtils.d("xu_activ_id", json);
                    Gson gson = new Gson();
                    activity.mActivityDetails = gson.fromJson(json, ActivityDetails.class);
                    activity.initData();
                } else if (which == NORMAL_POEPLE) {
                    LogUtils.d("xu_normal_people", json);
                    Gson gson = new Gson();
                    activity.listView.stopLoadMore();
                    activity.listView.stopRefresh();
                    NormalPeopleInActivity normalPeopleInActivity = gson.fromJson(json, NormalPeopleInActivity.class);
                    List<NormalPeopleInActivity.DataEntity> dataEntities = normalPeopleInActivity.getData();
                    if (dataEntities != null) {
                        if (activity.curPage == 1) {
                            activity.hasMoreData = true;
                            activity.mAdapter.clear();
                        }
                        activity.hasMoreData = dataEntities.size() >= activity.pageSize;
                        activity.mAdapter.addData(dataEntities);
                        activity.setListViewHeightBasedOnChildren(activity.listView, activity.mAdapter.getCount());
                    }

                } else if (which == SHARE) {
                    LogUtils.d("xu", json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getInt("result") == 1) {
                            ToastUtil.showToast(activity, "转发成功");
                        } else {
                            ToastUtil.showToast(activity, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (which == CLOSE_PROGRESS_DIALOG) {
                    if (activity.mGetLocationDialog != null) {
                        activity.mGetLocationDialog.dismiss();
                        activity.transActivity();
                    }
                } else if (which == SHOW_TIPS_DIALOG) {
                    activity.inputDialog();
                }
            }
        }
    }


    private Handler handler = new MyHandler(this);
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private DisplayImageOptions optionhead;
    //主view
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_stage_normal_details);
        mainView = findViewById(R.id.main);
        Intent intent = getIntent();
        bean = (Bean_Bigstage_List) intent.getSerializableExtra("bean");
        LogUtils.d("xu_test", "id" + bean.getActivity_id());
        appContext = (BaseApplication) getApplication();
        initImageLoader();
        initViews();
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, GET_ACTIVITY);
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    private void initData() {

        tv_bigbottom_starvote.setText("马上报名");
        //关注checkbox
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        if (mActivityDetails != null) {
            mDataEntity = mActivityDetails.getData();
            String isEnd = mDataEntity.getIs_end();
            if (mDataEntity.getIs_attention().equals("1")) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            li_bigbottom_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                }
            });

            //判断活动状态
            if (isEnd != null && !"".equals(isEnd) && Integer.parseInt(mDataEntity.getIs_end()) != 1) {
                if (Integer.parseInt(mDataEntity.getIs_applay()) == 1)
                    tv_bigbottom_starvote.setText("已经报名");
                else {
                    long curTime = System.currentTimeMillis();
                    String applyEndTime = mDataEntity.getApply_end_time();
                    LogUtils.d("xu_test1354", applyEndTime);
                    if (applyEndTime != null && !"".equals(applyEndTime) && curTime > Long.parseLong(applyEndTime))
                        tv_bigbottom_starvote.setText("报名结束");
                }
            } else
                tv_bigbottom_starvote.setText("活动结束");

        }
        tv_bigbottom_starvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isEnd = mDataEntity.getIs_end();
                if (isEnd != null && !"".equals(isEnd)) {
                    if (Integer.parseInt(mDataEntity.getIs_end()) == 0) {
                        long curTime = System.currentTimeMillis();
                        String applyEndTime = mDataEntity.getApply_end_time();
                        if (applyEndTime != null && !"".equals(applyEndTime) && curTime > Long.parseLong(applyEndTime)) {
                            LogUtils.d("xu", "报名结束");
                        } else {
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.applyActivity, new String[]{"activity_id"}, new String[]{mDataEntity.getActivity_id()}, handler, APPLY_ACTIVITY);
                        }
                    }
                }
            }
        });
        //公共活动
        if ("公共".equals(mDataEntity.getActivity_type())) {
            tv_bigbottom_onlyleft.setText("距活动结束仅剩");
            tv_bigbottom_onlyleftNum.setText("00:00:00:00");
            if (mDataEntity != null) {
                final String activityEndTime = mDataEntity.getActivity_end_time();
                if (activityEndTime != null && !"".equals(activityEndTime)) {
                    long curTime = System.currentTimeMillis();
                    long endTime = Long.parseLong(activityEndTime);
                    if (endTime > curTime) {
                        timeCount = new TimeCount(endTime - curTime, 1000);
                        timeCount.start();
                    }
                }
            }
            if (mDataEntity != null && mDataEntity.getActivity_type() != null && mDataEntity.getActivity_type().equals("公共")) {
                RelativeLayout rl_people_count = (RelativeLayout) findViewById(R.id.rl_people_count);
                rl_people_count.setVisibility(View.GONE);
                ibInvitation.setVisibility(View.GONE);
                rlListRecommend.setVisibility(View.GONE);
                LinearLayout ll_list_people = (LinearLayout) findViewById(R.id.ll_list_people);
                ll_list_people.setVisibility(View.GONE);

                tv_bigbottom_starvote.setVisibility(View.GONE);
                li_bigbottom_startvote.setBackgroundColor(getResources().getColor(R.color.orangered));
            }
        }
        //一般活动
        else {
            tv_bigbottom_onlyleft.setText("距报名结束仅剩");
            tv_bigbottom_onlyleftNum.setText("00:00:00:00");
            if (mDataEntity != null) {
                final String applyEndTime = mDataEntity.getApply_end_time();
                if (applyEndTime != null && !"".equals(applyEndTime)) {
                    long curTime = System.currentTimeMillis();
                    long endTime = Long.parseLong(applyEndTime);
                    if (endTime > curTime) {
                        timeCount = new TimeCount(endTime - curTime, 1000);
                        timeCount.start();
                    }
                }
            }
        }
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        li_bigbottom_share.setOnClickListener(this);
        li_bigbottom_startvote.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LogUtils.d("xu_activity_id", mDataEntity.getActivity_id());
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.attentionActivity, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, Constant.ATTENTION_ACTIVITY);
                } else {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancelAttention, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, CANCEL_ATTENTION);
                }
            }
        });


        if (mDataEntity != null) {
            ibInvitation.setVisibility(mDataEntity.getIs_mine().equals("1") ? View.VISIBLE : View.INVISIBLE);
            tv_title.setText(mDataEntity.getTitle());
            String startTime = mDataEntity.getActivity_start_time();
            String endTime = mDataEntity.getActivity_end_time();
            if (startTime != null && !"".equals(startTime) && endTime != null && !"".equals(endTime)) {
                if (startTime.indexOf('.') != -1)
                    startTime = startTime.substring(0, startTime.indexOf('.'));
                if (endTime.indexOf('.') != -1)
                    endTime = endTime.substring(0, endTime.indexOf('.'));
                startTime = DateUtils.format(Long.parseLong(startTime), "MM-dd");
                endTime = DateUtils.format(Long.parseLong(endTime), "MM-dd");
                tvStartTime.setText(startTime + "——" + endTime);
            }
            tvAddress.setText(mDataEntity.getSite());
            String num = mDataEntity.getParticipants_num();
            if (num == null || "".equals(num))
                num = "0";
            tvPeopleCount.setText(num);
            tvPeopleCount2.setText(num);
            tvMaxPeopleCount.setText("/" + mDataEntity.getMax_participants_num());
            etvContent.setText(mDataEntity.getIntroduction());
            photosEntities = mDataEntity.getPhotos();
            if (photosEntities != null) {
                tvPhotoCount.setText(String.format("%d", photosEntities.size()));
            }
            gvPics.setAdapter(new PicAdapter());
            gvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startPhotoActivity();
                }
            });
            imageLoader.displayImage(mDataEntity.getOrganizer_face(), ivHead, options);
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                    intent.putExtra("member_id", mDataEntity.getOrganizer_id());
                    startActivity(intent);
                }
            });
            tvName.setText(mDataEntity.getOrganizer_name());
            String count = mDataEntity.getOrganizer_activity_num();
            if (count == null || count.equals(""))
                count = "0";
            tvInvitationCount.setText(count);
            //获取报名人列表
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNormalApplayPeople, new String[]{"activity_id", "page", "pageSize"}, new String[]{mDataEntity.getActivity_id(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, NORMAL_POEPLE);
            //获取推荐人列表
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityInviteMembers, new String[]{"activity_id", "type"}, new String[]{mDataEntity.getActivity_id(), "5"}, handler, RECOMMEND_PEOPLE);

        }
    }


    /**
     * 最多显示三个推荐人
     */
    private void displayThreeRecommend() {
        if (mRecommendPeople != null && mRecommendPeople.size() > 0) {
            ivListRecommend.setVisibility(View.VISIBLE);
            rlListRecommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlRecommendHide.setVisibility(View.VISIBLE);
                }
            });
            if (mRecommendPeople.size() > 0) {
                imageLoader.displayImage(mRecommendPeople.get(0).getFace(), ivRecommend1, options);
                ivRecommend1.setVisibility(View.VISIBLE);
                ivRecommend1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                        intent.putExtra("member_id", mRecommendPeople.get(0).getMember_id());
                        startActivity(intent);
                    }
                });
            }
            if (mRecommendPeople.size() > 1) {
                imageLoader.displayImage(mRecommendPeople.get(1).getFace(), ivRecommend2, options);
                ivRecommend2.setVisibility(View.VISIBLE);
                ivRecommend2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                        intent.putExtra("member_id", mRecommendPeople.get(1).getMember_id());
                        startActivity(intent);
                    }
                });
            }
            if (mRecommendPeople.size() > 2) {
                imageLoader.displayImage(mRecommendPeople.get(2).getFace(), ivRecommend3, options);
                ivRecommend3.setVisibility(View.VISIBLE);
                ivRecommend3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                        intent.putExtra("member_id", mRecommendPeople.get(2).getMember_id());
                        startActivity(intent);
                    }
                });
            }
        }

    }

    private void initViews() {
        li_bigbottom_share = (LinearLayout) findViewById(R.id.li_bigbottom_share);
        LinearLayout ll_photos = (LinearLayout) findViewById(R.id.ll_photos_parent);
        ll_photos.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.title);
        li_bigbottom_startvote = (LinearLayout) findViewById(R.id.li_bigbottom_startvote);
        ivRecommend1 = (RoundImageView) findViewById(R.id.iv_recommend_people_1);
        ivRecommend2 = (RoundImageView) findViewById(R.id.iv_recommend_people_2);
        ivRecommend3 = (RoundImageView) findViewById(R.id.iv_recommend_people_3);
        ivListRecommend = (ImageView) findViewById(R.id.iv_list_recommend_people);
        rlListRecommend = (RelativeLayout) findViewById(R.id.rl_recommend_people);
        etvContent = (ExpandableTextView) findViewById(R.id.etv_activity_content);
        li_bigbottom_attention = (LinearLayout) findViewById(R.id.li_bigbottom_attention);
        mRecommendGridAdapter = new RecommendGridViewAdapter();
        GridView gvRecommend = (GridView) findViewById(R.id.gridview_recommend);
        gvRecommend.setAdapter(mRecommendGridAdapter);
        rlRecommendHide = (CardView) findViewById(R.id.cardview_recommend_people_hide);
        rlRecommendHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlRecommendHide.setVisibility(View.GONE);
            }
        });

        ivHead = (ImageView) findViewById(R.id.iv_head);
        ibInvitation = (ImageButton) findViewById(R.id.ib_invitation);
        ibInvitation.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvInvitationCount = (TextView) findViewById(R.id.tv_invitation_count);
        tvStartTime = (TextView) findViewById(R.id.tv_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvMaxPeopleCount = (TextView) findViewById(R.id.tv_max_people_count);
        tvPeopleCount = (TextView) findViewById(R.id.tv_people_count);
        TextView tvActivityContent = (TextView) findViewById(R.id.expandable_text);
        tvPhotoCount = (TextView) findViewById(R.id.tv_pic_count);
        tvPeopleCount2 = (TextView) findViewById(R.id.tv_sign_count);
        gvPics = (GridView) findViewById(R.id.gridview_pics);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        ll_photos.setOnClickListener(this);
        listView = (MyXListView) findViewById(R.id.listview_sign_people);
        mAdapter = new ApplyActivityPeopleAdapter();
        listView.setAdapter(mAdapter);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNormalApplayPeople, new String[]{"activity_id", "page", "pageSize"}, new String[]{bean.getActivity_id(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, NORMAL_POEPLE);

            }

            @Override
            public void onLoadMore() {
                if (hasMoreData) {
                    curPage++;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNormalApplayPeople, new String[]{"activity_id", "page", "pageSize"}, new String[]{bean.getActivity_id(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, NORMAL_POEPLE);
                } else {
                    ToastUtil.showToast(BigStageNormalDetailsActivity.this, "没有更多数据");
                    listView.stopLoadMore();
                }
            }
        });
        tv_bigbottom_starvote = (TextView) findViewById(R.id.tv_bigbottom_starvote);
        tv_bigbottom_onlyleft = (TextView) findViewById(R.id.tv_bigbottom_onlyleft);
        tv_bigbottom_onlyleftNum = (TextView) findViewById(R.id.tv_bigbottom_onlyleftNum);

    }

    @Override
    protected void onDestroy() {
        if (timeCount != null)
            timeCount.cancel();
        super.onDestroy();
    }

    public void startLocation() {
        LocationUtil locationUtil = new LocationUtil();
        locationUtil.startLocation(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back://后退
                finish();
                break;
            case R.id.li_bigbottom_share://分享
                if (BaseApplication.isLogin) {
                    ShareUtil shareUtil = new ShareUtil(this);
                    List<ActivityDetails.DataEntity.PhotosEntity> photosEntity = mDataEntity.getPhotos();
                    if (mDataEntity != null) {
                        String type = mDataEntity.getActivity_type().equals("公共") ? ShareUtil.PUBLIC_ACTVITY_SHARE : ShareUtil.NORMAL_ACTIVITY_SHARE;
                        shareUtil.openShare(mainView, BigStageNormalDetailsActivity.this, mDataEntity.getTitle(), (photosEntity != null && photosEntity.size() > 0) ? photosEntity.get(0).getOriginal() : null, true, new SocializeListeners.OnSnsPlatformClickListener() {
                            @Override
                            public void onClick(Context context, SocializeEntity socializeEntity, SocializeListeners.SnsPostListener snsPostListener) {
                                //开启定位
                                startLocation();
                                Message message = handler.obtainMessage(SHOW_TIPS_DIALOG);
                                message.sendToTarget();
                            }
                        }, type, mDataEntity.getActivity_id());
                    }
                } else {
                    ToastUtil.showToast(this, "请先登录");
                }
                break;
            case R.id.li_bigbottom_startvote://开始报名
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.joinActivity, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, Constant.JOIN_ACTIVITY);
                break;
            case R.id.ib_invitation:
                String content = mDataEntity.getIntroduction();
                if (content != null) {
                    if (content.length() > 200)
                        content = content.substring(0, 200);
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.onekeyInvition, new String[]{"activity_id", "title", "description"}, new String[]{mDataEntity.getActivity_id(), mDataEntity.getTitle(), content}, handler, ONEKEY_INVITION);
                } else {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.onekeyInvition, new String[]{"activity_id", "title", "description"}, new String[]{mDataEntity.getActivity_id(), mDataEntity.getTitle(), ""}, handler, ONEKEY_INVITION);
                }
                break;
            case R.id.ll_photos:
            case R.id.ll_photos_parent:
                startPhotoActivity();
                break;
        }
    }

    private void inputDialog() {
        View pop_view = LayoutInflater
                .from(this).inflate(R.layout.popwindow_edittext, null);
        et_pop = (EditText) pop_view.findViewById(R.id.et_input);
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(BigStageNormalDetailsActivity.this);
        //    设置Title的内容
        builder.setTitle("转发活动");
        builder.setView(pop_view);
        //    设置一个PositiveButton
        builder.setPositiveButton("转发", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isTransActivity = true;
                transComment = et_pop.getText().toString();
                if (coordinate == null) {
                    if (mGetLocationDialog == null) {
                        mGetLocationDialog = new ProgressDialog(BigStageNormalDetailsActivity.this);
                        mGetLocationDialog.setMessage("正在获取位置...");
                    }
                    mGetLocationDialog.show();
                    handler.sendEmptyMessageDelayed(CLOSE_PROGRESS_DIALOG, 5000);
                } else {
                    transActivity();
                }
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isTransActivity = false;
                coordinate = null;
                if (mGetLocationDialog != null) {
                    mGetLocationDialog.dismiss();
                    mGetLocationDialog = null;
                }
                transComment = null;
                dialog.dismiss();
            }
        });

        //    显示出该对话框
        builder.show();

    }


    //进入相册详情界面
    private void startPhotoActivity() {
        //如果此活动有相册，进入
        if (photosEntities != null && photosEntities.size() > 0) {
            Intent intent = new Intent(BigStageNormalDetailsActivity.this, BigStageDetailsPicsActivity.class);
            intent.putExtra("data", mDataEntity);
            startActivityForResult(intent, Constant.ADD_PIC);
        }
        //如果此活动没有相册，有权限（参与活动的人、发布活动的人）上传照片的话也进入相册详情
        else {
            if (mDataEntity.getIs_applay().equals("1") || mDataEntity.getIs_mine().equals("1")) {
                Intent intent = new Intent(BigStageNormalDetailsActivity.this, BigStageDetailsPicsActivity.class);
                intent.putExtra("data", mDataEntity);
                startActivityForResult(intent, Constant.ADD_PIC);
            }
        }
    }

    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_bigbottom_onlyleftNum.setText("00:00:00:00");

        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

            tv_bigbottom_onlyleftNum.setText(getTime(millisUntilFinished));
        }

        public String getTime(long time) {
            String str = "";
            time = time / 1000;
            int s = (int) (time % 60);
            int m = (int) (time / 60 % 60);
            int h = (int) (time / 3600 % 24);
            int D = (int) (time / (3600 * 24));
            str = D + ":" + h + ":" + m + ":" + s;
            return str;
        }
    }

    //活动相册的adapter
    public class PicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return photosEntities == null ? 0 : (photosEntities.size() > 3 ? 3 : photosEntities.size());
        }

        @Override
        public Object getItem(int position) {
            return photosEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = LinearLayout.inflate(BigStageNormalDetailsActivity.this, R.layout.gridview_bigstagenormal_item, null);
                imageView = (ImageView) convertView.findViewById(R.id.iv_bigstage_gridview);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            imageLoader.displayImage(photosEntities.get(position).getThumbnail(), imageView, options);
            return convertView;
        }
    }


    //报名参与活动人的adapter
    public class ApplyActivityPeopleAdapter extends BaseAdapter {

        private List<NormalPeopleInActivity.DataEntity> mData = new ArrayList<>();


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
            if (convertView == null) {
                convertView = LinearLayout.inflate(BigStageNormalDetailsActivity.this, R.layout.listview_bigstage_people, null);
                viewHolder = new ViewHolder();
                viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String applyTime = mData.get(position).getApply_time();
            if (applyTime != null && !"".equals(applyTime))
                viewHolder.tvTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(mData.get(position).getApply_time())));
            viewHolder.tvName.setText(mData.get(position).getMember_name());
            imageLoader.displayImage(mData.get(position).getFace(), viewHolder.ivHead, optionhead);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                    intent.putExtra("member_id", mData.get(position).getMember_id());
                    startActivity(intent);
                }
            });
            return convertView;
        }


        public class ViewHolder {
            //头像
            ImageView ivHead;
            //名称
            TextView tvName;
            //报名时间
            TextView tvTime;


        }

        public void clear() {
            if (mData == null) {
                mData = new ArrayList<>();
            } else {
                mData.clear();
                notifyDataSetChanged();
            }
        }

        public void addData(List<NormalPeopleInActivity.DataEntity> dataEntities) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            if (dataEntities != null && dataEntities.size() > 0) {
                mData.addAll(dataEntities);
                notifyDataSetChanged();
            }
        }

    }


    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(XListView listView, int count) {
        if (listView == null)
            return;
        if (mAdapter == null) {
            return;
        }
        if (count <= 0)
            return;
        int singleHeight = 0;
        View listItem = mAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        singleHeight = listItem.getMeasuredHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = singleHeight * count + (count - 1) * 2 + singleHeight / 2;
        listView.setLayoutParams(params);
    }


    //推荐人的adapter
    public class RecommendGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRecommendPeople == null ? 0 : mRecommendPeople.size();
        }

        @Override
        public Object getItem(int position) {
            return mRecommendPeople.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView = null;
            if (convertView == null) {
                convertView = LinearLayout.inflate(BigStageNormalDetailsActivity.this, R.layout.gridview_recommend_item, null);
                imageView = (ImageView) convertView.findViewById(R.id.iv_recommend_item);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            imageLoader.displayImage(mRecommendPeople.get(position).getFace(), imageView, options);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BigStageNormalDetailsActivity.this, HomePageActivity.class);
                    intent.putExtra("member_id", mRecommendPeople.get(position).getMember_id());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        /**
         * 通知更新数据
         */
        public void update() {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("Main+resultCode", resultCode + "");
        if (resultCode == Constant.ADD_PIC) {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, GET_ACTIVITY);

        }

        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }


    }
}
