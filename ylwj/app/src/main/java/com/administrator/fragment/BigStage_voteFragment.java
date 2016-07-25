package com.administrator.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Bean_Bigstage_List;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 发布活动流程中，发布投票活动的fragment
 * Created by acer on 2016/1/15.
 */
public class BigStage_voteFragment extends BaseFragment implements TimeChooseListener, RadioGroup.OnCheckedChangeListener {
    private int type_num = 0;
    private EditText activityName;
    private EditText voteEndTimeYear;
    private EditText voteEndTimeMonth;
    private EditText voteEndTimeDay;
    private EditText voteContent;
    private TextView tv_belong_commmunity;
    private View mView;
    private EditText et_pop;
    private String initDateTime;
    private LinearLayout linear_vote_add;
    private BaseApplication appContext;
    private DateTimePickDialogUtil dateTimePicKDialog;
    private static final int GET_COMMUNITY = 1;
    private int checkWhich = 0;//0代表选择单选，1代表多选，2代表无限制

    public static class MyHandler extends Handler {
        private WeakReference<BigStage_voteFragment> mFragment;

        public MyHandler(BigStage_voteFragment fragment) {
            mFragment = new WeakReference<BigStage_voteFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStage_voteFragment fragment = mFragment.get();
            if (fragment != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.GET_ACTIVITY_ID) {
//                            LogUtils.e("NORMAL",json);
                    Intent intent = new Intent(fragment.getActivity(), StartPickPhotoActivity.class);
                    intent.putExtra("activity_id", json);
                    intent.putExtra("type", 1+"");//代表公共
                    fragment.startActivity(intent);
                }else if(which == GET_COMMUNITY){
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

    private Handler handler = new MyHandler(this);
    private List<EditText> editTextList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bigstage_vote, null);
        appContext = (BaseApplication) getActivity().getApplication();
        initViews();
        initTypeItemView();
        VolleyUtils.NetUtils(((BaseApplication) getActivity().getApplication()).getRequestQueue(), Constant.baseUrl + Constant.getMemberLoginMsg, null, null, handler, GET_COMMUNITY);
        return mView;
    }

    private void initTimerDialog() {
        Calendar calendar = Calendar.getInstance();
        initDateTime = calendar.get(Calendar.YEAR) + "年"
                + calendar.get(Calendar.MONTH) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE);
        setVoteEndTimeTime(calendar);
    }

    private void initViews() {
        activityName = (EditText) mView.findViewById(R.id.bigstage_activity_name);
        activityName.setHint("如：青岛房价的是否该下降");
        TextView tv_vote_name = (TextView) mView.findViewById(R.id.bigstage_name_tv);
        tv_vote_name.setText("投票主题：");
        TextView tv_vote_content = (TextView) mView.findViewById(R.id.bigstage_content_tv);
        tv_vote_content.setText("投票内容：");
        TextView tv_vote_applytime = (TextView) mView.findViewById(R.id.bigstage_apply_tv);
        tv_vote_applytime.setText("投票截止时间：");
        voteEndTimeYear = (EditText) mView.findViewById(R.id.activity_sign_end_year);
        voteEndTimeMonth = (EditText) mView.findViewById(R.id.activity_sign_end_month);
        voteEndTimeDay = (EditText) mView.findViewById(R.id.activity_sign_end_day);
        voteContent = (EditText) mView.findViewById(R.id.bigstage_activity_content);
        voteContent.setHint("如：投票的注意事项、投票具体内容描述等");
        EditText voteResult = (EditText) mView.findViewById(R.id.bigstage_vote_result_content);
        tv_belong_commmunity = (TextView) mView.findViewById(R.id.tv_community_choose);
        linear_vote_add = (LinearLayout) mView.findViewById(R.id.linear_vote_type);
        RadioGroup voteTypeRadioGroup = (RadioGroup) mView.findViewById(R.id.bigstage_vote_choice_type_rg);
        voteTypeRadioGroup.setOnCheckedChangeListener(this);
        RadioButton singleRadioButton = (RadioButton) mView.findViewById(R.id.bigstage_vote_choice_type_single);
        RadioButton manyRadioButton = (RadioButton) mView.findViewById(R.id.bigstage_vote_choice_type_many);
        RadioButton nolimitRadioButton = (RadioButton) mView.findViewById(R.id.bigstage_vote_choice_type_nolimit);
        ImageButton ib_addVote = (ImageButton) mView.findViewById(R.id.ib_add_vote);
        ib_addVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type_num < 8) {
                    initTypeItemView();
                } else {
                    ToastUtil.showToast(getContext(), "最多可以添加8个");
//                    Toast.makeText(getActivity(),"最多可以添加8个",Toast.LENGTH_LONG).show();
                }
            }
        });
        initTimerDialog();
        initVoteTimeSet();
    }

    /**
     * 下一步
     */
    public void next(){
        String activity_type = "投票";
        String activity_name = activityName.getText().toString().trim();//获取活动名称
        String community_name = tv_belong_commmunity.getText().toString();//所属社区
        String voteEndTime = voteEndTimeYear.getText().toString()+voteEndTimeMonth.getText().toString()
                +voteEndTimeDay.getText().toString();//投票结束时间
        String introduction = voteContent.getText().toString();//活动描述
        String apply_end_time = DateUtils.changeDate(voteEndTime);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < editTextList.size(); i++) {
            builder.append(editTextList.get(i).getText().toString() + ";");
        }
        if (activity_name.equals("") || introduction.equals("")) {
            ToastUtil.showToast(getContext(),"请填写活动名称以及描述");
//            Toast.makeText(getActivity(), "请填写活动名称以及描述", Toast.LENGTH_SHORT).show();
        } else {
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getActivity_id,
                    new String[]{"title", "site", "introduction", "tag", "apply_start_time", "apply_end_time", "activity_start_time", "activity_end_time", "max_participants_num", "activity_type", "options", "is_released", "community_name"},
                    new String[]{activity_name, "无", introduction, "无", DateUtils.getCurrentTime() + "", "", DateUtils.getCurrentTime() + "", apply_end_time, "1000", activity_type, builder.toString(), "0", community_name}, handler, Constant.GET_ACTIVITY_ID);
        }
    }



    private void initVoteTimeSet(){
        voteEndTimeYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chooseTime();
                }
            }
        });
        voteEndTimeMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chooseTime();
                }
            }
        });
        voteEndTimeDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chooseTime();
                }
            }
        });
        voteEndTimeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
        voteEndTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
        voteEndTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
    }
    /**
     * 选择时间
     */
    private void chooseTime(){
        if(dateTimePicKDialog == null){
            dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), initDateTime);
        }
        dateTimePicKDialog.dateTimePick(BigStage_voteFragment.this);
    }
    class Vote_itemView {
        EditText et;
        TextView tv;
        ImageView iv;
    }

    private void initTypeItemView() {
        final Vote_itemView itemView = new Vote_itemView();
        final View vote_itemView = LayoutInflater.from(getActivity()).inflate(R.layout.vote_type_item, null);
        linear_vote_add.addView(vote_itemView);
        final View view = new View(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10));
        linear_vote_add.addView(view);
        type_num++;
        itemView.iv = (ImageView) vote_itemView.findViewById(R.id.iv_vote_remove);
        itemView.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_vote_add.removeView(vote_itemView);
                linear_vote_add.removeView(view);
                textViewList.remove(itemView.tv);
                editTextList.remove(itemView.et);
                type_num--;
                if(type_num > 0){
                    for (int i = 0;i<type_num;i++){
                        textViewList.get(i).setText("第"+(i+1)+"项");
                    }
                }
            }
        });
        itemView.et = (EditText) vote_itemView.findViewById(R.id.tv_vote_name);
        itemView.et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog("选项内容", itemView.et);
            }
        });
        itemView.tv = (TextView) vote_itemView.findViewById(R.id.tv_vote_num);
        itemView.tv.setText("第"+type_num+"项");
        editTextList.add(itemView.et);
        textViewList.add(itemView.tv);
    }

    private void inputDialog(String str, final TextView tv) {
        View pop_view = LayoutInflater
                .from(getActivity()).inflate(R.layout.popwindow_edittext, null);
        et_pop = (EditText) pop_view.findViewById(R.id.et_input);
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //    设置Title的内容
        builder.setTitle(str);
        builder.setView(pop_view);
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String position = et_pop.getText().toString();
                tv.setText(position);
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv.setText("点击填写");
            }
        });
        //    显示出该对话框
        builder.show();
    }

    public void initData(Bean_Bigstage_List data){
        Bean_Bigstage_List mData = data;
        if(mData != null){

        }
    }
    @Override
    public void changeCalendar(Calendar calendar) {
        if(calendar != null){
            setVoteEndTimeTime(calendar);
        }
    }
    private void setVoteEndTimeTime(Calendar calendar){
        voteEndTimeYear.setText(calendar.get(Calendar.YEAR)+"");
        voteEndTimeMonth.setText((calendar.get(Calendar.MONTH)+1)<10 ? "0"+(calendar.get(Calendar.MONTH)+1) : (calendar.get(Calendar.MONTH)+1)+"");
        voteEndTimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH)<10 ? "0"+calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH)+"");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.bigstage_vote_choice_type_single:
                checkWhich = 0;
                break;
            case R.id.bigstage_vote_choice_type_many:
                checkWhich = 1;
                break;
            case R.id.bigstage_vote_choice_type_nolimit:
                checkWhich = 2;
                break;
        }
    }
}
