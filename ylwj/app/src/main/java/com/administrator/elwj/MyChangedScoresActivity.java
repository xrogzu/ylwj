package com.administrator.elwj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.administrator.adapter.ListAdapterMyChangedScores;
import com.library.listview.XListView;

import java.lang.ref.WeakReference;

/**
 * 积分变动界面，已经去掉，此页面没有使用
 * Created by Administrator on 2016/3/21.
 */
public class MyChangedScoresActivity extends AppCompatActivity {

    private static class MyHandler extends Handler{
        private WeakReference<MyChangedScoresActivity> mActivity;
        public MyHandler(MyChangedScoresActivity activity){
            mActivity = new WeakReference<MyChangedScoresActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyChangedScoresActivity activity = mActivity.get();
            if(activity != null){

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychangedscores);
        initViews();
    }

    private void initViews() {
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyChangedScoresActivity.this.finish();
            }
        });

        XListView listView = (XListView) findViewById(R.id.listview_mychangedscores);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        ListAdapterMyChangedScores mAdapter = new ListAdapterMyChangedScores(this);
        listView.setAdapter(mAdapter);
    }
}
