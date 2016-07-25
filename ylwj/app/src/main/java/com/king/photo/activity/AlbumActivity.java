package com.king.photo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.administrator.bean.Constant;
import com.administrator.elwj.EditUserInfoActivity;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.king.photo.adapter.AlbumGridViewAdapter;
import com.king.photo.util.AlbumHelper;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageBucket;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 这个是进入相册显示所有图片的界面
 *
 * @author king
 * @version 2014年10月18日  下午11:47:15
 * @QQ:595163260
 */
public class AlbumActivity extends Activity {
    //显示手机里的所有图片的列表控件
    private GridView gridView;
    //当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    //完成按钮
    private Button okButton;
    // 返回按钮
    private Button back;
    // 取消按钮
    private Button cancel;
    private Intent intent;
    private String maxNum;
    private Button preview;
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public Bitmap bitmap;
    public ArrayList<ImageItem> curTempSelectImage = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Res.getLayoutID("plugin_camera_album"));
        Intent intent = getIntent();
        maxNum = intent.getStringExtra("maxNum");
        PublicWay.num = Integer.valueOf(maxNum);
//        justSave = new ArrayList<ImageItem>();
        mContext = this;
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), Res.getDrawableID("plugin_camera_no_pictures"));
        requirePermission();
//        instance = this;

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub  
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        helper.clear();
    }

    // 预览按钮的监听
    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
//            Bimp.tempSelectBitmap.addAll(justSave);
//            justSave.clear();
            if (Bimp.tempSelectBitmap.size() > 0) {
                intent.putExtra("position", "1");
                intent.setClass(AlbumActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
//            Bimp.tempSelectBitmap.addAll(justSave);
//            justSave.clear();
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, StartPickPhotoActivity.class);
//			startActivity(intent);
            if (PublicWay.num == 1) {

                Intent intent = new Intent();
                intent.setClass(AlbumActivity.this, EditUserInfoActivity.class);
                setResult(RESULT_OK, intent);
//                instance = null;
                finish();
            } else {
//                instance = null;
                finish();
            }


        }

    }

    // 返回按钮监听
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            intent.setClass(AlbumActivity.this, ImageFile.class);
            startActivity(intent);
            finish();
        }
    }

    private void cancelSelect(){
        if(curTempSelectImage.size() > 0){
            for(int i = 0; i < curTempSelectImage.size(); ++i)
                Bimp.tempSelectBitmap.remove(curTempSelectImage.get(i));
        }
    }

    // 取消按钮的监听
    private class CancelListener implements OnClickListener {
        public void onClick(View v) {
            cancelSelect();
//			Bimp.tempSelectBitmap.clear();
//			Bimp.max=0;
//            justSave.clear();
//			intent.setClass(mContext, StartPickPhotoActivity.class);
//			startActivity(intent);
//            instance = null;
            finish();
        }
    }


    // 请求权限
    private void requirePermission() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.CALL_EXTRNAL_WRITE_REQUIRE);
        }else{
            realInit();
        }

    }

    private void realInit(){
        init();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    private void init(){
        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }
        Collections.sort(dataList);

        back = (Button) findViewById(Res.getWidgetID("back"));
        cancel = (Button) findViewById(Res.getWidgetID("cancel"));
        cancel.setOnClickListener(new CancelListener());
        back.setOnClickListener(new BackListener());
        preview = (Button) findViewById(Res.getWidgetID("preview"));
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
                Bimp.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(Res.getWidgetID("myText"));
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
        okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size()
                + "/" + PublicWay.num + ")");
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked, ImageView iv_choose) {
                        int aaa = Bimp.tempSelectBitmap.size();
//                        int bbb = justSave.size();
                        if (PublicWay.num == 1) {
                            if (isChecked) {
//                                if (justSave.size() >= PublicWay.num) {
                                if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
                                    toggleButton.setChecked(false);
                                    iv_choose.setVisibility(View.GONE);
                                    if (!removeOneData(dataList.get(position))) {
                                        ToastUtil.showToast(AlbumActivity.this,Res.getString("only_choose_num"));
//                                        Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"),
//                                                Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                            } else {

                            }
                        } else {

//                            if ((Bimp.tempSelectBitmap.size() + justSave.size()) >= PublicWay.num) {
                            if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
                                toggleButton.setChecked(false);
                                iv_choose.setVisibility(View.GONE);
                                if (!removeOneData(dataList.get(position))) {
                                    ToastUtil.showToast(AlbumActivity.this,Res.getString("only_choose_num"));
//                                    Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"),
//                                            Toast.LENGTH_SHORT).show();
                                }
                                return ;
                            }
                        }

                        if (isChecked) {
                            curTempSelectImage.add(dataList.get(position));
                            iv_choose.setVisibility(View.VISIBLE);
//                            selectedImages.add(dataList.get(position));
							Bimp.tempSelectBitmap.add(dataList.get(position));
//                            justSave.add(dataList.get(position));
                            okButton.setText(Res.getString("finish") + "(" + (Bimp.tempSelectBitmap.size() )
                                    + "/" + PublicWay.num + ")");
                        } else {
                            curTempSelectImage.remove(dataList.get(position));
//                            justSave.remove(dataList.get(position));
                            Bimp.tempSelectBitmap.remove(dataList.get(position));
                            iv_choose.setVisibility(View.GONE);
                            okButton.setText(Res.getString("finish") + "(" + (Bimp.tempSelectBitmap.size()) + "/" + PublicWay.num + ")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(ImageItem imageItem) {
        if (Bimp.tempSelectBitmap.contains(imageItem)) {
            Bimp.tempSelectBitmap.remove(imageItem);
            okButton.setText(Res.getString("finish") + "(" + (Bimp.tempSelectBitmap.size()) + "/" + PublicWay.num + ")");
            if(curTempSelectImage.contains(imageItem))
                curTempSelectImage.remove(imageItem);
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
//        if (Bimp.tempSelectBitmap.size() > 0 || justSave.size() > 0) {
        if (Bimp.tempSelectBitmap.size() > 0) {
            okButton.setText(Res.getString("finish") + "(" + (Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")"));
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelSelect();
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
//            instance = null;
            finish();
        }
        return super.onKeyDown(keyCode,event);

    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Constant.CALL_EXTRNAL_WRITE_REQUIRE:
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    realInit();
                }
                break;
        }
    }
}
