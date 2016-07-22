package com.administrator.elwj;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.fragment.BigStageItemFragment;
import com.administrator.fragment.DraftLaunch_activityFragment;
import com.administrator.utils.LogUtils;

/**
 * 草稿箱页面
 * Created by Administrator on 2016/3/13.
 */
public class DraftBoxActivity extends AppCompatActivity {


    private UpdateReceiver updateReceiver;

    private boolean needDisplayList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draftbox);
        updateReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.EDIT_ACTIVITY");
        filter.addAction("android.intent.action.DRAFT_UPDATE");
        registerReceiver(updateReceiver, filter);
        initViews();
    }

    private void initViews() {
        ImageView ivBack = (ImageView) findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DraftBoxActivity.this.onBackPressed();
            }
        });
        displayList(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(needDisplayList){
            delayDisplayList();
            needDisplayList = false;
        }
    }

    private void delayDisplayList(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("detail");
        if (fragment != null) {
            manager.popBackStack();
        }
        fragment = manager.findFragmentByTag("draft");
        if (fragment != null)
            displayList(true);
        else
            displayList(false);
    }

    private void displayList(boolean replace) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", BigStageItemFragment.EDIT_ACTIVITY);
        bundle.putString("url", Constant.getMyNoPublishActivity);
        bundle.putBoolean("publish", false);
        BigStageItemFragment fragment = new BigStageItemFragment();
        fragment.setNeedShowProcessDialog(true);
        fragment.setArguments(bundle);
        if (replace) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "draft").commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "draft").commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateReceiver != null)
            unregisterReceiver(updateReceiver);
    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isGoList = intent.getBooleanExtra("golist", false);
            if (!isGoList) {
                LogUtils.d("xu", "onReceive !isGoList");
                Bean_Bigstage_List bean_bigstage_list = (Bean_Bigstage_List) intent.getSerializableExtra("data");
                if (bean_bigstage_list != null) {
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment fragment = manager.findFragmentByTag("draft");
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.hide(fragment);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Constant.DRAFT_TYPE_NORMAL);
                    bundle.putString("activity_id", bean_bigstage_list.getActivity_id());
                    DraftLaunch_activityFragment f = new DraftLaunch_activityFragment();
                    f.setArguments(bundle);
                    transaction = transaction.add(R.id.container, f, "detail").addToBackStack(null);
                    transaction.show(f);
                    transaction.commit();
                }
            } else {
                LogUtils.d("xu", "onReceive isGoList");
                needDisplayList = true;
            }
        }
    }
}
