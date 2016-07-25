package com.administrator.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.Constant;
import com.administrator.bean.FinancialExpert;
import com.administrator.elwj.ExpertDetails_Activity;
import com.administrator.elwj.R;
import com.administrator.fragment.FinancialExpertFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;


/**
 * 金融专家apdater
 * Created by acer on 2016/1/7.
 */
public class ListAdapterExpert extends BaseAdapter {

    private List<FinancialExpert.DataEntity> mData;

    private Activity activity;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;
    private String curCallPhoneNum;

    public ListAdapterExpert(Activity context) {
        this.activity = context;
        mData = new ArrayList<>();
        initImageLoader();

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

    @Override
    public int getCount() {
        return mData.size() % 2 == 0 ? (mData.size() / 2) : (mData.size() / 2 + 1);
//        return appList.size()/2
    }

    @Override
    public Object getItem(int position) {
        return position * 2;
    }

    @Override
    public long getItemId(int position) {
        return position * 2;
    }

    public void clear() {
        if (mData != null)
            mData.clear();
        else mData = new ArrayList<>();
    }


    public class ViewHolder_EXPERT {
        RelativeLayout rl_left;
        RelativeLayout rl_right;
        ImageView iv_headLeft;
        ImageView iv_sealLeft;
        ImageView iv_headRight;
        ImageView iv_sealRight;
        TextView tv_nameLeft;
        TextView tv_nameRight;
        TextView tv_positionLeft;
        TextView tv_positionRight;
        ImageButton ib_left;
        ImageButton ib_right;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder_EXPERT vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_expert, null);
            vh = new ViewHolder_EXPERT();
            vh.iv_headLeft = (ImageView) convertView.findViewById(R.id.iv_expert_left);
            vh.iv_headRight = (ImageView) convertView.findViewById(R.id.iv_expert_right);
            vh.rl_left = (RelativeLayout) convertView.findViewById(R.id.expert_list_left);
            vh.rl_right = (RelativeLayout) convertView.findViewById(R.id.expert_list_right);
            vh.tv_nameLeft = (TextView) convertView.findViewById(R.id.tv_expertname_left);
            vh.tv_nameRight = (TextView) convertView.findViewById(R.id.tv_expertname_right);
            vh.tv_positionLeft = (TextView) convertView.findViewById(R.id.tv_expertposition_left);
            vh.tv_positionRight = (TextView) convertView.findViewById(R.id.tv_expertposition_right);
            vh.ib_left = (ImageButton) convertView.findViewById(R.id.ib_expert_left);
            vh.ib_right = (ImageButton) convertView.findViewById(R.id.ib_expert_right);
            vh.iv_sealLeft = (ImageView) convertView.findViewById(R.id.iv_seal_left);
            vh.iv_sealRight = (ImageView) convertView.findViewById(R.id.iv_seal_right);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder_EXPERT) convertView.getTag();
        }

        final int finalPostion = position * 2;

        if (mData.get(finalPostion).getExpert_type().equals("1")) {
            vh.iv_sealLeft.setImageResource(R.mipmap.seal_manager);
        } else {
            vh.iv_sealLeft.setImageResource(R.mipmap.seal_expert);
        }
        vh.tv_nameLeft.setText(mData.get(finalPostion).getExpert_name());
        vh.tv_positionLeft.setText(mData.get(finalPostion).getField());
        imageLoader.displayImage(mData.get(finalPostion).getAvatar_url(), vh.iv_headLeft, options);
        vh.ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curCallPhoneNum = mData.get(finalPostion).getMobile();
                callPhone();
//                requestCallPhonePermission();
            }
        });

        vh.rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetails(finalPostion);
            }
        });


        if (finalPostion + 1 < mData.size()) {
            vh.tv_nameRight.setText(mData.get(finalPostion + 1).getExpert_name());
            vh.tv_positionRight.setText(mData.get(finalPostion + 1).getField());

            imageLoader.displayImage(mData.get(finalPostion + 1).getAvatar_url(), vh.iv_headRight, options);
            vh.ib_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curCallPhoneNum = mData.get(finalPostion + 1).getMobile();
                    callPhone();
//                    requestCallPhonePermission();
                }
            });

            vh.rl_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoDetails(finalPostion + 1);
                }
            });
            vh.rl_right.setVisibility(View.VISIBLE);
            if (mData.get(finalPostion + 1).getExpert_type().equals("1"))
                vh.iv_sealRight.setImageResource(R.mipmap.seal_manager);
            else
                vh.iv_sealRight.setImageResource(R.mipmap.seal_expert);
        } else {
            vh.rl_right.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }


    public void callPhone() {
        if (curCallPhoneNum != null && !"".equals(curCallPhoneNum)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + curCallPhoneNum));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    private void gotoDetails(int position) {
        Intent intent = new Intent(activity, ExpertDetails_Activity.class);
        intent.putExtra("expert", mData.get(position));
        activity.startActivity(intent);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<FinancialExpert.DataEntity> data) {
        if (data != null) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

//    private void requestCallPhonePermission() {
//        int checkPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
//        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, Constant.CALL_PHONE_REQUIRE);
//        } else {
//            callPhone();
//        }
//    }

}
