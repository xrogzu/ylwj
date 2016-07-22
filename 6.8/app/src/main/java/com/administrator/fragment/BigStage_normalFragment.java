package com.administrator.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Constant;
import com.administrator.bean.UserInfoExtra;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.R;
import com.administrator.minterface.TimeChooseListener;
import com.administrator.utils.DateTimePickDialogUtil;
import com.administrator.utils.DateUtils;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.king.photo.activity.StartPickPhotoActivity;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 社区大舞台发布一般活动的fragment
 * Created by acer on 2016/1/15.
 */
@SuppressLint("ValidFragment")
public class BigStage_normalFragment extends BaseFragment implements View.OnClickListener, TimeChooseListener {

    private View mView;
    private EditText activityName, startYear, startMonth, startDay, endYear, endMonth, endDay,
            signEndTimeYear, signEndTimeMonth, signEndTimeDay, activityContent;
    private TextView tv_belong_commmunity;
    private TextView tv_belong_place;
    private int people_num = 20;
    private EditText et_people_num;
    private Context context;
    private String initDateTime;
    private EditText et_pop;
    private static final int FLAG_POSITION = 1;
    private static final int FLAG_DESCRIBE = 2;
    private BaseApplication appContext;
    private ActivityDetails.DataEntity mData;
    private static final int EDIT = 0;
    private static final int START_ACTIVITY_TIME = 3;//设置活动开始时间标示
    private static final int END_ACTIVITY_TIME = 4;//设置活动结束时间标示
    private static final int END_APPLY_TIME = 5;//设置报名结束时间标示
    private static final int GET_COMMUNITY = 6;//获取所在社区
    private int which = 0;//设置哪一部分时间的标志
    private DateTimePickDialogUtil dateTimePicKDialog;
    private int activityType;//0一般活动，或者1公共活动

    public static class MyHandler extends Handler {
        private WeakReference<BigStage_normalFragment> mFragment;

        public MyHandler(BigStage_normalFragment fragment) {
            mFragment = new WeakReference<BigStage_normalFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStage_normalFragment fragment = mFragment.get();
            if (fragment != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.GET_ACTIVITY_ID) {
                    Intent intent = new Intent(fragment.getActivity(), StartPickPhotoActivity.class);
                    intent.putExtra("activity_id", json);
                    intent.putExtra("content", fragment.activityContent.getText().toString());
                    intent.putExtra("type", fragment.activityType + "");//社区大舞台标示，用在选取照片界面和身边作为区分
                    intent.putExtra("new", true);
                    fragment.startActivity(intent);
                } else if (which == GET_COMMUNITY) {
                    Gson gson = new Gson();
                    UserInfoExtra userInfoExtra = gson.fromJson(json, UserInfoExtra.class);
                    if (userInfoExtra.getResult() == 1) {
                        UserInfoExtra.DataEntity dataEntity = userInfoExtra.getData();
                        if (dataEntity != null) {
                            fragment.tv_belong_commmunity.setText(dataEntity.getCommunity_name());

                        } else {
                            LogUtils.d("xu", "获取所在社区失败");
                        }
                    } else {
                        LogUtils.d("xu", "获取所在社区失败");
                    }
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    public BigStage_normalFragment() {
    }

    public BigStage_normalFragment(Context context, int type) {
        this.context = context;
        this.activityType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bigstage_normal, null);
        appContext = (BaseApplication) getActivity().getApplication();
        initViews();
        VolleyUtils.NetUtils(((BaseApplication) getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, handler, GET_COMMUNITY);
        return mView;
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        initDateTime = calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE);
        setStartTime(calendar);
        setEndTime(calendar);
        setSignTime(calendar);
    }

    private void setStartTime(Calendar calendar) {
        startYear.setText(calendar.get(Calendar.YEAR) + "");
        startMonth.setText((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1) + "");
        startDay.setText(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "");
    }

    private void setEndTime(Calendar calendar) {
        endYear.setText(calendar.get(Calendar.YEAR) + "");
        endMonth.setText((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1) + "");
        endDay.setText(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "");
    }

    private void setSignTime(Calendar calendar) {
        signEndTimeYear.setText(calendar.get(Calendar.YEAR) + "");
        signEndTimeMonth.setText((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1) + "");
        signEndTimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "");
    }

    private void initViews() {
        activityName = (EditText) mView.findViewById(R.id.bigstage_activity_name);
        LinearLayout linear_belong_community = (LinearLayout) mView.findViewById(R.id.bigstage_activity_community);
        tv_belong_commmunity = (TextView) mView.findViewById(R.id.tv_community_choose);
        startYear = (EditText) mView.findViewById(R.id.bigstage_activity_start_year);
        startMonth = (EditText) mView.findViewById(R.id.bigstage_activity_start_month);
        startDay = (EditText) mView.findViewById(R.id.bigstage_activity_start_day);
        endYear = (EditText) mView.findViewById(R.id.bigstage_activity_end_year);
        endMonth = (EditText) mView.findViewById(R.id.bigstage_activity_end_month);
        endDay = (EditText) mView.findViewById(R.id.bigstage_activity_end_day);
        LinearLayout linear_belong_place = (LinearLayout) mView.findViewById(R.id.linear_belong_place);
        linear_belong_place.setOnClickListener(this);
        tv_belong_place = (TextView) mView.findViewById(R.id.bigstage_activity_place);
        TextView tv_people_reduce = (TextView) mView.findViewById(R.id.bigstage_activity_people_reduce);
        TextView tv_people_add = (TextView) mView.findViewById(R.id.bigstage_activity_people_add);
        et_people_num = (EditText) mView.findViewById(R.id.bigstage_activity_people);
        et_people_num.setText(20 + "");
        signEndTimeYear = (EditText) mView.findViewById(R.id.activity_sign_end_year);
        signEndTimeMonth = (EditText) mView.findViewById(R.id.activity_sign_end_month);
        signEndTimeDay = (EditText) mView.findViewById(R.id.activity_sign_end_day);
        activityContent = (EditText) mView.findViewById(R.id.bigstage_activity_content);
        View people_layout = mView.findViewById(R.id.bigstage_activity_people_layout);
        View signtime_layout = mView.findViewById(R.id.bigstage_activity_signtime_layout);
        View people_line = mView.findViewById(R.id.line_people);
        View signtime_line = mView.findViewById(R.id.line_signtime);
        if (activityType == 1) {
            people_layout.setVisibility(View.GONE);
            signtime_layout.setVisibility(View.GONE);
            people_line.setVisibility(View.GONE);
            signtime_line.setVisibility(View.GONE);
        }

        if (mData != null) {
            tv_belong_place.setText(mData.getSite());
            if (!"".equals(mData.getMax_participants_num())) {
                people_num = Integer.parseInt(mData.getMax_participants_num());
            } else people_num = 0;
            et_people_num.setText(mData.getMax_participants_num());
            activityContent.setText(mData.getIntroduction());
            String endApplyTime = mData.getApply_end_time();
            if (endApplyTime != null && !"".equals(endApplyTime)) {
                Calendar calendar = DateUtils.getCalendar(endApplyTime);
                setSignTime(calendar);
            }
        }

        //bt_next_step.setOnClickListener(this);
        linear_belong_community.setOnClickListener(this);
        tv_people_reduce.setOnClickListener(this);
        tv_people_add.setOnClickListener(this);
        initStartTimeSet();
        initFinishTimeSet();
        initApplyTimeSet();
        initTime();
    }

    /**
     * 活动开始时间的设置初始化
     */
    private void initStartTimeSet() {
        startYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = START_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        startMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = START_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        startDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = START_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        startYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = START_ACTIVITY_TIME;
                chooseTime();
            }
        });
        startMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = START_ACTIVITY_TIME;
                chooseTime();
            }
        });
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = START_ACTIVITY_TIME;
                chooseTime();
            }
        });
    }

    /**
     * 活动结束时间的设置初始化
     */
    private void initFinishTimeSet() {
        endYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        endMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        endDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_ACTIVITY_TIME;
                    chooseTime();
                }
            }
        });
        endMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_ACTIVITY_TIME;
                chooseTime();
            }
        });
        endYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_ACTIVITY_TIME;
                chooseTime();
            }
        });
        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_ACTIVITY_TIME;
                chooseTime();
            }
        });
    }

    /**
     * 活动报名结束时间的设置初始化
     */
    private void initApplyTimeSet() {
        signEndTimeYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_APPLY_TIME;
                    chooseTime();
                }
            }
        });
        signEndTimeMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_APPLY_TIME;
                    chooseTime();
                }
            }
        });
        signEndTimeDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    which = END_APPLY_TIME;
                    chooseTime();
                }
            }
        });
        signEndTimeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_APPLY_TIME;
                chooseTime();
            }
        });
        signEndTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_APPLY_TIME;
                chooseTime();
            }
        });
        signEndTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = END_APPLY_TIME;
                chooseTime();
            }
        });
    }

    /**
     * 选择时间
     */
    private void chooseTime() {
        if (dateTimePicKDialog == null) {
            dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), initDateTime);
        }
        dateTimePicKDialog.dateTimePick(BigStage_normalFragment.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_belong_place://活动地点
                inputDialog("活动地点", FLAG_POSITION);
                break;
            case R.id.bigstage_activity_people_add://增加人数
                people_num++;
                et_people_num.setText(people_num + "");
                break;
            case R.id.bigstage_activity_people_reduce://减少人数
                if (people_num > 1) {
                    people_num--;
                    et_people_num.setText(people_num + "");
                } else {
                    ToastUtil.showToast(getContext(), "活动人数无法再减少");
                }
                break;
        }
    }

    /**
     * 下一步
     */
    public void next() {
        Handler handler = new MyHandler(this);
        String activity_type = "一般";
        if (activityType == 1) {
            activity_type = "公共";
        }
        String actvity_name;
        String community_name;
        String activity_start_time;
        String activity_end_time;

        actvity_name = activityName.getText().toString().trim();//活动标题
        community_name = tv_belong_commmunity.getText().toString();//所属社区
        activity_start_time = startYear.getText().toString() + startMonth.getText().toString()
                + startDay.getText().toString();//活动开始时间
        activity_end_time = endYear.getText().toString() + endMonth.getText().toString()
                + endDay.getText().toString();//活动结束时间
        Calendar calendar = Calendar.getInstance();
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND, 0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        long curTime = calendar.getTimeInMillis();
        LogUtils.d("xu_time", String.format("%d", curTime));
        long endTime = 0;
        long startTime = 0;
        if (activity_end_time != null && !"".equals(activity_end_time) && activity_start_time != null && !"".equals(activity_start_time)) {
            endTime = Long.parseLong(DateUtils.changeDate(activity_end_time));
            startTime = Long.parseLong(DateUtils.changeDate(activity_start_time));
            LogUtils.d("xu_endtime", String.format("%d", endTime));
            LogUtils.d("xu_startTime", String.format("%d", startTime));
            if (startTime > endTime) {
                ToastUtil.showToast(getContext(), "活动开始时间不能晚于活动结束时间");
                return;
            }
            if (startTime < curTime) {
                ToastUtil.showToast(getContext(), "活动开始时间不能比当前时间早");
                return;
            }
        } else {
            ToastUtil.showToast(getContext(), "请填写完整活动时间");
            return;
        }
        activity_start_time = startTime + "";
        activity_end_time = endTime + "";
        String site = tv_belong_place.getText().toString();//活动地点
        if(site.length() > 40){
            ToastUtil.showToast(getContext(),"活动地点文字不能超过40");
            return;
        }

        String apply_end_time = "";
        String participants_num = "";
        if (activityType == 0) {
            participants_num = et_people_num.getText().toString();//参与人数
            if (participants_num.trim() == null || participants_num.trim().equals("")) {
                ToastUtil.showToast(getContext(), "请输入活动参与人数");
                return;
            } else {
                long num = Long.parseLong(participants_num.trim());
                if (num <= 0) {
                    ToastUtil.showToast(getContext(), "活动参与人数必须大于0");
                    return;
                }else if(num > 10000){
                    ToastUtil.showToast(getContext(),"活动参与人数不能大于10000");
                }
            }
            String signEndTime = signEndTimeYear.getText().toString() + signEndTimeMonth.getText().toString() + signEndTimeDay.getText().toString();//报名截止时间
            apply_end_time = DateUtils.changeDate(signEndTime);
            long apply_time = Long.parseLong(apply_end_time);

            if (apply_time > startTime) {
                ToastUtil.showToast(getContext(), "活动报名结束时间不能晚于活动开始时间");
                return;
            }
            if (apply_time < curTime) {
                ToastUtil.showToast(getContext(), "活动报名结束时间不能早于当前时间");
                return;
            }
            apply_end_time = apply_time + "";
        }

        String introduction = activityContent.getText().toString();//活动描述
        if(introduction.length() > 2500){
            ToastUtil.showToast(getContext(),"活动内容不能超过2500字");
            return ;
        }
        if (actvity_name.equals("") || introduction.equals("")) {
            ToastUtil.showToast(getContext(), "请填写活动名称以及描述");
        }else if(actvity_name.length() > 40){
            ToastUtil.showToast(getContext(),"活动主题不能超过40个子");
        } else if (mData == null) {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_id,
                    new String[]{"title", "site", "introduction", "tag", "apply_start_time", "apply_end_time", "activity_start_time", "activity_end_time", "max_participants_num", "activity_type", "options", "is_released"},
                    new String[]{actvity_name, site, introduction, "", DateUtils.getCurrentTime() + "", apply_end_time, activity_start_time, activity_end_time, participants_num, activity_type, "", "0"}, handler, Constant.GET_ACTIVITY_ID);
        } else {
            mData.setTitle(actvity_name);
            mData.setSite(site);
            mData.setIntroduction(introduction);
            mData.setApply_end_time(apply_end_time);
            mData.setActivity_start_time(activity_start_time);
            mData.setActivity_end_time(activity_end_time);
            mData.setMax_participants_num(participants_num);
            mData.setActivity_type(activity_type);
            mData.setCommunity_name(community_name);
            Intent intent = new Intent(getActivity(), StartPickPhotoActivity.class);
            intent.putExtra("activity_id", mData.getActivity_id());
            intent.putExtra("type", activityType + "");//社区大舞台标示，用在选取照片界面和身边作为区分
            intent.putExtra("new", false);
            startActivity(intent);
        }
    }

    /**
     * 输入活动相关信息的对话框
     *
     * @param str
     * @param flag
     */
    private void inputDialog(String str, final int flag) {
        View pop_view = LayoutInflater
                .from(getActivity()).inflate(R.layout.popwindow_edittext, null);
        et_pop = (EditText) pop_view.findViewById(R.id.et_input);
        if (flag == FLAG_DESCRIBE) {
            String text = activityContent.getText().toString().trim();
            if (text.equals("点击填写"))
                text = "";
            if (!"".equals(text)) {
                et_pop.setText(text);
            }
        } else {
            String text = tv_belong_place.getText().toString().trim();
            if (!"".equals(text)) {
                et_pop.setText(text);
            }
        }
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的内容
        builder.setTitle(str);
        builder.setView(pop_view);
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String position = et_pop.getText().toString();
                if (flag == FLAG_POSITION) {
                    tv_belong_place.setText(position);
                } else {
                    activityContent.setText(position);
                }
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //    显示出该对话框
        builder.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(0,
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }

    /**
     * 时间设置监听
     *
     * @param calendar
     */
    @Override
    public void changeCalendar(Calendar calendar) {
        if (calendar != null) {
            switch (which) {
                case START_ACTIVITY_TIME:
                    setStartTime(calendar);
                    break;
                case END_ACTIVITY_TIME:
                    setEndTime(calendar);
                    break;
                case END_APPLY_TIME:
                    setSignTime(calendar);
                    break;
            }
        }
    }

    public void setData(ActivityDetails.DataEntity data) {
        mData = data;
    }
}
