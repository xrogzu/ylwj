package com.administrator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterBigstageChoose;
import com.administrator.elwj.R;
import com.library.listview.XListView;

/**
 * 前人创建的fragment，目前没有用到，可以删除
 * Created by acer on 2016/1/24.
 */
public class BigStage_ChooseFragment extends BaseFragment {

    private View headView;
    private RelativeLayout rl_headback;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_organization;
    private TextView tv_time;
    private TextView tv_ticketNum;
    private TextView tv_starNum;
    private RatingBar ratingBar;
    private ImageButton ib_drop;
    private TextView tv_introduce;
    private int tv_intruduceLines;
    private boolean isDrop=true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_bigstage_choose, null);
        XListView listView = (XListView) mView.findViewById(R.id.listView_bigstage);
        ListAdapterBigstageChoose adapter = new ListAdapterBigstageChoose(getActivity());
        listView.setAdapter(adapter);
        headView = inflater.inflate(R.layout.headview_bigstage_choose, null);
        listView.addHeaderView(headView);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        initViews();
        return mView;
    }

    private void initViews() {
        rl_headback = (RelativeLayout) headView.findViewById(R.id.rl_headback);
        iv_head = (ImageView) headView.findViewById(R.id.iv_normal_details_head);
        tv_name = (TextView)  headView.findViewById(R.id.tv_normal_details_name);
        tv_organization = (TextView) headView. findViewById(R.id.tv_normal_details_location);
        tv_time = (TextView)  headView.findViewById(R.id.tv_normal_details_time);
        tv_starNum = (TextView)  headView.findViewById(R.id.tv_normal_details_starNum);
        ratingBar = (RatingBar) headView. findViewById(R.id.ratingbar);
        ib_drop = (ImageButton)  headView.findViewById(R.id.ib_normal_details_drop);
        tv_introduce = (TextView)  headView.findViewById(R.id.tv_normal_details_introduce);
        tv_introduce.post(new Runnable() {
            @Override
            public void run() {
                tv_intruduceLines = tv_introduce.getLineCount();
                tv_introduce.setMaxLines(3);
            }
        });
        ib_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrop){//点击下拉
                    tv_introduce.setMaxLines(tv_intruduceLines);
                    isDrop=false;
                    ib_drop.setImageResource(R.mipmap.img_up);
                }else {
                    tv_introduce.setMaxLines(3);
                    isDrop=true;
                    ib_drop.setImageResource(R.mipmap.img_down);
                }
            }
        });
    }
}
