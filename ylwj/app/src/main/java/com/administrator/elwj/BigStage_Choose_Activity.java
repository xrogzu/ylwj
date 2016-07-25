package com.administrator.elwj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterBigstageChoose;
import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.bean.Photo;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.ref.WeakReference;

//前人写的activity，目前没有用到，可以删除
public class BigStage_Choose_Activity extends AppCompatActivity {
    private View mView;
    private XListView listView;
    private View headView;
    private ListAdapterBigstageChoose adapter;
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
    private boolean isDrop = true;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Bean_Bigstage_List bean;
    private BaseApplication appContext;
    public static class MyHandler extends Handler{

        private WeakReference<BigStage_Choose_Activity> mActivity;

        public MyHandler(BigStage_Choose_Activity activity){
            mActivity = new WeakReference<BigStage_Choose_Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BigStage_Choose_Activity activity = mActivity.get();
            if(activity != null) {
                int where = msg.what;
                String json = (String) msg.obj;
                switch (where) {
                    case Constant.IMG_BYID:
                        Gson gson = new Gson();
                        Photo photo = gson.fromJson(json, Photo.class);
                        activity.imageLoader.displayImage(photo.getPath(), activity.iv_head, activity.options);
                        activity.imageLoader.displayImage(photo.getPath(), activity.iv_back, activity.options);
                        break;
                }
            }
        }
    }
    private Handler handler = new MyHandler(this);

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_stage__choose_);
        appContext = (BaseApplication) getApplication();
        //initImageLoader(this);
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        Intent intent = getIntent();
        bean = (Bean_Bigstage_List) intent.getSerializableExtra("bean");
//        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.big_choose, new String[]{"photo_id"}, new String[]{(String) bean.getPhotoIds().get(1)},handler,Constant.IMG_BYID );

        listView = (XListView) findViewById(R.id.listView_bigstage);
        adapter = new ListAdapterBigstageChoose(this,bean);
        listView.setAdapter(adapter);
        headView = LayoutInflater.from(this).inflate(R.layout.headview_bigstage_choose, null);
        listView.addHeaderView(headView);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) headView.findViewById(R.id.iv_back);
        rl_headback = (RelativeLayout) headView.findViewById(R.id.rl_headback);
        iv_head = (ImageView) headView.findViewById(R.id.iv_normal_details_head);
        tv_name = (TextView) headView.findViewById(R.id.tv_normal_details_name);
        tv_organization = (TextView) headView.findViewById(R.id.tv_normal_details_location);
        tv_time = (TextView) headView.findViewById(R.id.tv_normal_details_time);
        //tv_ticketNum = (TextView) headView.findViewById(R.id.tv_normal_details_personnum);
        tv_starNum = (TextView) headView.findViewById(R.id.tv_normal_details_starNum);
        ratingBar = (RatingBar) headView.findViewById(R.id.ratingbar);
        ib_drop = (ImageButton) headView.findViewById(R.id.ib_normal_details_drop);
        tv_introduce = (TextView) headView.findViewById(R.id.tv_normal_details_introduce);
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
                if (isDrop) {//点击下拉
                    tv_introduce.setMaxLines(tv_intruduceLines);
                    isDrop = false;
                    ib_drop.setImageResource(R.mipmap.img_up);
                } else {
                    tv_introduce.setMaxLines(3);
                    isDrop = true;
                    ib_drop.setImageResource(R.mipmap.img_down);
                }
            }
        });
    }

}
