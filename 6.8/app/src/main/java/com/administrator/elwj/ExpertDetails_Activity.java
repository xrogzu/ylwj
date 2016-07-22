package com.administrator.elwj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.bean.FinancialExpert;
import com.administrator.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

//金融专家详情页面
public class ExpertDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_head;
    private ImageView iv_sex;
    private TextView tv_sex;
    private TextView tv_begoodat;
    private TextView tv_introduction;
    private FinancialExpert.DataEntity mExpert;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_details_);
        initViews();
        initImageLoader();
        getIntentData();
    }

    private void initImageLoader() {
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mExpert = intent.getParcelableExtra("expert");
            if (mExpert != null) {
                imageLoader.displayImage(mExpert.getAvatar_url(), iv_head, options);
                tv_sex.setText("性别:" + (mExpert.getGender() == 1 ? "男" : "女"));
                if (mExpert.getGender() == 1) {
                    iv_sex.setImageResource(R.mipmap.wd_icon_man);
                } else {
                    iv_sex.setImageResource(R.mipmap.wd_icon_woman);
                }
                tv_introduction.setText(mExpert.getSummary());
                tv_begoodat.setText(mExpert.getField());
            }
        }
    }

    private void initViews() {
        ImageButton mback = (ImageButton) findViewById(R.id.hot_ib_back);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("金融专家");
        iv_head = (ImageView) findViewById(R.id.iv_details_head);
        iv_sex = (ImageView) findViewById(R.id.iv_expertdetails_sex);
        tv_sex = (TextView) findViewById(R.id.tv_expertdetails_sex);
        LinearLayout lr_call = (LinearLayout) findViewById(R.id.rl_expertdetails_call);
        tv_begoodat = (TextView) findViewById(R.id.tv_expertdetails_begoodat);
        tv_introduction = (TextView) findViewById(R.id.tv_expertdetails_introduction);
        lr_call.setOnClickListener(this);
        mback.setOnClickListener(this);
        iv_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_expertdetails_call://拨打电话
//                int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.CALL_PHONE_REQUIRE);
//                    return;
//                } else {
                    callPhone();
//                }
                break;
            case R.id.hot_ib_back://
                finish();
                break;
            case R.id.iv_details_head://
                String url = mExpert.getAvatar_url();
                Intent intent = new Intent(getBaseContext(), LargeHeadPictureActivity.class);
                intent.putExtra("ExpertHeadPicture", url);
                startActivity(intent);
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Constant.CALL_PHONE_REQUIRE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    callPhone();
//                } else {
//                    ToastUtil.showToast(this,"取消打电话");
////                    Toast.makeText(this, "取消打电话", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        }
//    }

    private void callPhone() {
        String tel = mExpert.getMobile();
        if (tel != null && !"".equals(tel)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
