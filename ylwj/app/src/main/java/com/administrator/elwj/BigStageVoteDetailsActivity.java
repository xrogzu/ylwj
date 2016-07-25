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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.adapter.ListAdapterVoteDetail;
import com.administrator.adapter.ListAdapterVoteResult;
import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Constant;
import com.administrator.bean.VoteResult;
import com.administrator.utils.LocationUtil;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.library.listview.XListView;
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
import java.util.List;

//社区大舞台投票详情页面
public class BigStageVoteDetailsActivity extends AppCompatActivity implements View.OnClickListener, LocationUtil.MyLocationListener {

    private BaseApplication appContext;
    private ActivityDetails mActivityDetails;
    private Handler handler = new MyHandler(this);
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private static final int GET_ACTIVITY = 1;
    private static final int ACTIVITY_VOTE = 2;
    private static final int GET_VOTE_RESULT = 3;
    private LinearLayout li_bigbottom_startvote;
    private TextView tv_bigbottom_onlyleftNum;
    private CheckBox checkBox;
    private TextView startVote_tv;
    private String choice = "";//投票选项的选择
    private String[] option;
    private boolean initFinish = false;//是否初始化完成
    private LinearLayout contentLayout;
    private VoteResult voteResult;
    private ProgressDialog mGetLocationDialog;
    private double coordinate_x;
    private double coordinate_y;
    private String coordinate;
    private boolean isTransActivity = false;
    private String transComment;
    private static final int SHARE = 6;
    //转发身边新鲜事弹出edit
    private EditText et_pop;
    private static final int CLOSE_PROGRESS_DIALOG = 7;
    private static final int SHOW_TIPS_DIALOG = 8;
    private RelativeLayout mainLayout;

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
                            mActivityDetails.getData().getActivity_id(), coordinate, transComment == null ? "" : transComment, String.format("%f", coordinate_x), String.format("%f", coordinate_y)},
                    handler, SHARE);
        else {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shared2Nearby, new String[]{
                            "activity_id", "coordinate", "message_content", "coordinate_x", "coordinate_y"}, new String[]{
                            mActivityDetails.getData().getActivity_id(), "", transComment == null ? "" : transComment, "", ""},
                    handler, SHARE);
        }
        coordinate = null;
        transComment = null;
        mGetLocationDialog = null;
        isTransActivity = false;
    }

    private void inputDialog() {
        View pop_view = LayoutInflater
                .from(this).inflate(R.layout.popwindow_edittext, null);
        et_pop = (EditText) pop_view.findViewById(R.id.et_input);
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(BigStageVoteDetailsActivity.this);
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
                        mGetLocationDialog = new ProgressDialog(BigStageVoteDetailsActivity.this);
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

    public static class MyHandler extends Handler {

        private WeakReference<BigStageVoteDetailsActivity> mActivity;

        public MyHandler(BigStageVoteDetailsActivity activity) {
            mActivity = new WeakReference<BigStageVoteDetailsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStageVoteDetailsActivity activity = mActivity.get();
            if (activity != null) {
                String json = (String) msg.obj;
                int what = msg.what;
                switch (what) {
                    case Constant.ATTENTION_ACTIVITY:
                        try {
                            JSONObject object = new JSONObject(json);
                            int result = object.optInt("result");
                            String message = object.optString("message");
                            if (result == 1) {
                                activity.checkBox.setChecked(true);
                            } else {
                                activity.checkBox.setChecked(false);
                            }
                            ToastUtil.showToast(activity, message + "");
//                            Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.CANCLE_ATTENTION_ACTIVITY:
                        try {
                            JSONObject object = new JSONObject(json);
                            int result = object.optInt("result");
                            String message = object.optString("message");
                            if (result == 1) {
                                activity.checkBox.setChecked(false);
                            } else {
                                activity.checkBox.setChecked(true);
                            }
                            ToastUtil.showToast(activity, message + "");
//                            Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.JOIN_ACTIVITY:
                        try {
                            JSONObject object = new JSONObject(json);
                            int result = object.optInt("result");
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message + "");
//                            Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ACTIVITY_VOTE:
                        try {
                            JSONObject object = new JSONObject(json);
                            int result = object.optInt("result");
                            if (result == 1) {
                                activity.startVote_tv.setText("已投票");
                                activity.mActivityDetails.getData().setIs_vote("1");
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getVoteResult, new String[]{"activity_id"}, new String[]{activity.mActivityDetails.getData().getActivity_id()}, activity.handler, GET_VOTE_RESULT);
                            }
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message + "");
//                            Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_VOTE_RESULT:
                        try {
                            JSONObject object = new JSONObject(json);
                            int result = object.optInt("result");
                            if (result == 1) {
                                Gson gson = new Gson();
                                activity.voteResult = gson.fromJson(object.optJSONObject("data").toString(), VoteResult.class);
                                activity.showResult();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_ACTIVITY:
                        LogUtils.d("xu_activ_id", json);
                        Gson gson = new Gson();
                        activity.mActivityDetails = gson.fromJson(json, ActivityDetails.class);
                        activity.initViews();
                        activity.initData();
                        break;
                    case SHARE:
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.getInt("result") == 1) {
                                ToastUtil.showToast(activity, "转发成功");
//                                Toast.makeText(activity, "转发成功", Toast.LENGTH_SHORT).show();
                            } else {
                                ToastUtil.showToast(activity,jsonObject.getString("message"));
//                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case CLOSE_PROGRESS_DIALOG:
                        if (activity.mGetLocationDialog != null) {
                            activity.mGetLocationDialog.dismiss();
                            activity.transActivity();
                        }
                        break;
                    case SHOW_TIPS_DIALOG:
                        activity.inputDialog();
                        break;

                    case 1000://代表投票选项的选择
                        if (activity.mActivityDetails != null && activity.mActivityDetails.getData() != null) {
                            activity.option = activity.mActivityDetails.getData().getOptions().split(";");
                            if (activity.options != null) {
                                for (int i = 0; i < activity.option.length; i++) {
                                    if (activity.option[i].equals(json)) {
                                        activity.choice = (i + 1) + "";
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (BaseApplication) getApplication();
        setContentView(R.layout.activity_big_stage_vote_details);
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        String activity_id = getIntent().getStringExtra("activity_id");
        ImageButton backButton = (ImageButton) findViewById(R.id.ib_back);
        backButton.setOnClickListener(this);
        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{activity_id}, handler, GET_ACTIVITY);

    }

    private void initData() {
        if (mActivityDetails != null && mActivityDetails.getData() != null) {
            if ("1".equals(mActivityDetails.getData().getIs_vote())) {
                checkBox.setChecked(true);
                startVote_tv.setText("已投票");
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getVoteResult, new String[]{"activity_id"}, new String[]{mActivityDetails.getData().getActivity_id()}, handler, GET_VOTE_RESULT);
            } else {
                checkBox.setChecked(false);
                startVote_tv.setText("点击投票");
                li_bigbottom_startvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(mActivityDetails.getData().getIs_vote())) {
                            ToastUtil.showToast(getApplicationContext(), "已投票");
//                            Toast.makeText(getApplicationContext(), "已投票", Toast.LENGTH_LONG).show();
                        } else {
                            if ("".equals(choice)) {//未选择投票项
                                ToastUtil.showToast(getApplicationContext(), "请选择投票项");
//                                Toast.makeText(getApplicationContext(), "请选择投票项", Toast.LENGTH_LONG).show();
                            } else {
                                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.activityVote, new String[]{"activity_id", "option"}, new String[]{mActivityDetails.getData().getActivity_id(), choice}, handler, ACTIVITY_VOTE);
                            }
                        }
                    }
                });
            }
            if ("1".equals(mActivityDetails.getData().getIs_attention())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        }
        initFinish = true;
    }

    private void initViews() {
        startVote_tv = (TextView) findViewById(R.id.tv_bigbottom_starvote);
        LinearLayout li_bigbottom_attention = (LinearLayout) findViewById(R.id.li_bigbottom_attention);
        LinearLayout li_bigbottom_share = (LinearLayout) findViewById(R.id.li_bigbottom_share);
        li_bigbottom_startvote = (LinearLayout) findViewById(R.id.li_bigbottom_startvote);
        XListView voteDetailListView = (XListView) findViewById(R.id.bigstage_vote_details_listview);
        voteDetailListView.setPullLoadEnable(false);
        voteDetailListView.setPullRefreshEnable(false);
        mainLayout = (RelativeLayout) findViewById(R.id.vote_detail_main);
        ListAdapterVoteDetail adapterVoteDetail = new ListAdapterVoteDetail(mActivityDetails.getData(), this, imageLoader, appContext, handler);
        voteDetailListView.setAdapter(adapterVoteDetail);
        tv_bigbottom_onlyleftNum = (TextView) findViewById(R.id.tv_bigbottom_onlyleftNum);
        contentLayout = (LinearLayout) findViewById(R.id.bigstage_vote_result_content);
        TextView activityTitle = (TextView) findViewById(R.id.bigstage_vote_details_title);
        activityTitle.setText(mActivityDetails.getData().getTitle());
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (initFinish) {
                    if (isChecked) {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.attentionActivity, new String[]{"activity_id"}, new String[]{mActivityDetails.getData().getActivity_id()}, handler, Constant.ATTENTION_ACTIVITY);
                    } else {
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancleAttentionActivity, new String[]{"activity_id"}, new String[]{mActivityDetails.getData().getActivity_id()}, handler, Constant.CANCLE_ATTENTION_ACTIVITY);
                    }
                }
            }
        });
        li_bigbottom_attention.setOnClickListener(this);
        li_bigbottom_share.setOnClickListener(this);
        li_bigbottom_startvote.setOnClickListener(this);
        TimeCount timeCount = new TimeCount(200000000, 1000);
        timeCount.start();
        //VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivityByID, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, GET_ACTIVITY);
    }

    private void showResult() {
        if (contentLayout != null) {
            contentLayout.removeAllViews();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.activity_big_stage_vote_result, null);
        ImageView userImageView = (ImageView) view.findViewById(R.id.bigstage_vote_details_user);
        TextView username = (TextView) view.findViewById(R.id.bigstage_vote_details_username);
        ListView listView = (ListView) view.findViewById(R.id.bigstage_vote_content_listview);
        ListAdapterVoteResult adapterVoteResult = new ListAdapterVoteResult(this, voteResult);
        listView.setAdapter(adapterVoteResult);
        imageLoader.displayImage(voteResult.getOrganizer_face(), userImageView, options);
        username.setText("发起人：" + voteResult.getOrganizer_name());
        contentLayout.addView(view);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_search:
                break;
            case R.id.ib_add:
                //Intent intent=new Intent(this,Launch_activityFragment.class);
                //startActivity(intent);
                break;
            case R.id.li_bigbottom_attention:
                checkBox.toggle();
                //VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.activity_attention, new String[]{"activity_id"}, new String[]{bean.getActivity_id()}, handler, Constant.ACTIVITY_ATTENTION);
                break;
            case R.id.li_bigbottom_share:
                if(BaseApplication.isLogin) {
                    if (mActivityDetails != null && mActivityDetails.getData() != null) {
                        ShareUtil shareUtil = new ShareUtil(this);
                        List<ActivityDetails.DataEntity.PhotosEntity> photosEntity = mActivityDetails.getData().getPhotos();
                        // Toast.makeText(this,mDataEntity.getTitle(),Toast.LENGTH_SHORT).show();
                        shareUtil.openShare(mainLayout, BigStageVoteDetailsActivity.this, mActivityDetails.getData().getTitle(), (photosEntity != null && photosEntity.size() > 0) ? photosEntity.get(0).getOriginal() : null, true, new SocializeListeners.OnSnsPlatformClickListener() {
                            @Override
                            public void onClick(Context context, SocializeEntity socializeEntity, SocializeListeners.SnsPostListener snsPostListener) {
                                startLocation();
                                Message message = handler.obtainMessage(SHOW_TIPS_DIALOG);
                                message.sendToTarget();
                            }
                        }, ShareUtil.VOTE_ACTIVITY_SHARE, mActivityDetails.getData().getActivity_id());
                    }
                }else{
                    ToastUtil.showToast(this, "请先登录");
//                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public void startLocation() {
        LocationUtil locationUtil = new LocationUtil();
        locationUtil.startLocation(this, this);
    }

    class TimeCount extends CountDownTimer {
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
            int h = (int) (time / 3600);
            int D = (int) (time / (3600 * 24));
            str = D + ":" + h + ":" + m + ":" + s;
            return str;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
