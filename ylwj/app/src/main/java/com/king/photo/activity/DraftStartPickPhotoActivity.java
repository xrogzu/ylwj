package com.king.photo.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.ActivityDetails;
import com.administrator.bean.Constant;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.BigStageNormalDetailsActivity;
import com.administrator.elwj.DraftInvitePeopleActivity;
import com.administrator.elwj.InvitePeopleActivity;
import com.administrator.elwj.R;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.Res;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * 首页面activity
 *
 * @author king
 * @version 2014年10月18日  下午11:48:34
 *          上传图片
 * @QQ:595163260
 */
public class DraftStartPickPhotoActivity extends AppCompatActivity {


    private LocalBroadcastManager mLocalBroadcastManager;
//    public static final String MESSAGE_ACTION = "org.feng.message_ACTION";

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private ImageButton ib_back;
    private Button bt_review;
    private TextView tv_title;
    private Button bt_next;
    private TextView fabu;
    private EditText content;
    private LinearLayout next;
//    public static DraftStartPickPhotoActivity instance = null;
    private String activity_id;
    private BaseApplication appContext;
    private TextView id1;
    private int mImageUploadCount = 0;
    private int mImageUploadCountFailed = 0;
    private ProgressDialog mProgressDialog;
    private int ImageCount;
    private boolean isNew = true;
    private ActivityDetails.DataEntity mData;
    private String fileName;
    private int tag;

    private String ActivityContent;

    //	private BigStage_Activity bean;

    public static class MyHandler extends Handler {
        private WeakReference<DraftStartPickPhotoActivity> mActivity;

        public MyHandler(DraftStartPickPhotoActivity activity) {
            mActivity = new WeakReference<DraftStartPickPhotoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final DraftStartPickPhotoActivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.ADD_IMGS) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int result = object.optInt("result");
                    if (result == 0) {
                        activity.mImageUploadCountFailed++;
                        Log.e("插入图片失败", "333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
                        if (activity.mImageUploadCount + activity.mImageUploadCountFailed == activity.ImageCount) {
                            if (activity.mImageUploadCountFailed != 0) {
                                ToastUtil.showToast(activity,"有" + activity.mImageUploadCountFailed + "张照片上传失败");
//                                Toast.makeText(activity, "有" + activity.mImageUploadCountFailed + "张照片上传失败",
//                                        Toast.LENGTH_SHORT).show();
                            }
                            if (activity.tag == 0) {
                                Intent intent = new Intent(activity, DraftInvitePeopleActivity.class);
                                intent.putExtra("data", activity.mData);
                                activity.startActivity(intent);
                                if (activity.mProgressDialog != null) {
                                    activity.mProgressDialog.dismiss();
                                    activity.mProgressDialog = null;
                                }
                                activity.finish();

                            } else {
                                if (activity.mProgressDialog != null) {
                                    activity.mProgressDialog.dismiss();
                                    activity.mProgressDialog = null;
                                }
                                Intent intent = new Intent();
                                activity.setResult(Constant.ADD_PIC_NEXT, intent);
                                activity.finish();
                            }


                        }
                    } else {
                        Log.e("成功", "4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
                        activity.mImageUploadCount++;
                        if (activity.mImageUploadCount + activity.mImageUploadCountFailed == activity.ImageCount) {
                            if (activity.mImageUploadCountFailed != 0) {
                                ToastUtil.showToast(activity, "有" + activity.mImageUploadCountFailed + "张照片上传失败");
//                                Toast.makeText(activity, "有" + activity.mImageUploadCountFailed + "张照片上传失败",
//                                        Toast.LENGTH_SHORT).show();
                            }
                            if (activity.tag == 0) {
                                Intent intent = new Intent(activity, DraftInvitePeopleActivity.class);
                                intent.putExtra("data", activity.mData);
                                activity.startActivity(intent);
                                if (activity.mProgressDialog != null) {
                                    activity.mProgressDialog.dismiss();
                                    activity.mProgressDialog = null;
                                }
                                activity.finish();

                            } else {
                                if (activity.mProgressDialog != null) {
                                    activity.mProgressDialog.dismiss();
                                    activity.mProgressDialog = null;
                                }
                                Intent intent = new Intent();
                                activity.setResult(Constant.ADD_PIC_NEXT, intent);
                                activity.finish();
                            }
                            Log.e("Main添加照片", json);
                        }
                    }
                }


                if (which == Constant.UPLOAD_ACTIVTY) {
                    Log.e("Main", json);
                }

                if (which == Constant.UPLOAD_ACTIVTY) {
                    Log.e("Main", json);
                }


            }
        }
    }

    private Handler handler = new MyHandler(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (BaseApplication) getApplication();
        Res.init(getApplicationContext());
        if(Bimp.tempSelectBitmap != null)
            Bimp.tempSelectBitmap.clear();
        Intent intent = getIntent();
        tag = intent.getIntExtra("tag", 0);
        isNew = intent.getBooleanExtra("new", true);
        mData = (ActivityDetails.DataEntity) intent.getSerializableExtra("data");
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
        setContentView(parentView);

        mImageUploadCount = 0;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Init();

        initViews();

//        instance = this;
    }

    private void initViews() {

        id1 = (TextView) parentView.findViewById(R.id.id_1);
        ib_back = (ImageButton) parentView.findViewById(R.id.hot_ib_back);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bimp.tempSelectBitmap.size() != 0) {
                    Bimp.tempSelectBitmap.clear();
                    Bimp.max = 0;
                }
                finish();
            }
        });

        content = (EditText) parentView.findViewById(R.id.id_content);
        fabu = (TextView) parentView.findViewById(R.id.id_go);
        next = (LinearLayout) parentView.findViewById(R.id.id_next);

        tv_title = (TextView) parentView.findViewById(R.id.title);
        bt_review = (Button) parentView.findViewById(R.id.bt_review);
        bt_next = (Button) parentView.findViewById(R.id.bt_nextstep);
        if (tag == 1) {
            bt_review.setVisibility(View.GONE);
            bt_next.setText("添加完成");
        }

        content.setVisibility(View.GONE);
        fabu.setVisibility(View.GONE);
        id1.setVisibility(View.GONE);


        tv_title.setText(R.string.big_stage);
        bt_review.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ImageItem> list = null;
                try {
                    list = isTooBig(Bimp.tempSelectBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list.size() > 0) {
                    ImageCount = list.size();
                    mImageUploadCountFailed = 0;
                    mImageUploadCount = 0;
                    DraftStartPickPhotoActivity.this.publish();
                    for (int i = 0; i < list.size(); i++) {
                        final File file = new File(list.get(i).getImagePath());

                        RequestParams params = new RequestParams();
                        params.put("imageFileName", "tom.jpg");
                        params.put("subFolder", "mobile");
                        params.put("isthumb", "1");
                        params.put("subFolder", "mobile");
                        try {

                            params.put("image", new File(list.get(i).getImagePath())); // Upload a File
                            params.put("width", Bimp.getHight(list.get(i).getImagePath()));
                            params.put("height", Bimp.getHight(list.get(i).getImagePath()));

                            AsyncHttpClient client = new AsyncHttpClient();
                            client.setTimeout(60000);
                            //http://192.168.1.114:8088/api/base/upload-image.do
                            client.post(Constant.baseUrl + Constant.uploadImgs, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

//
                                    JSONArray array = null;
                                    try {
                                        String result = response.getString("result");
                                        if (result.equals("0")) {
                                            Log.e("返回缩略图失败" + result, "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//
                                            mImageUploadCountFailed++;
                                            if (mImageUploadCount + mImageUploadCountFailed == ImageCount) {
                                                if (mImageUploadCountFailed != 0) {
                                                    ToastUtil.showToast(DraftStartPickPhotoActivity.this,"有" + mImageUploadCountFailed + "张照片上传失败");
//                                                    Toast.makeText(DraftStartPickPhotoActivity.this, "有" + mImageUploadCountFailed + "张照片上传失败",
//                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                if (tag == 0) {
                                                    Intent intent = new Intent(DraftStartPickPhotoActivity.this, DraftInvitePeopleActivity.class);
                                                    intent.putExtra("data", mData);
                                                    startActivity(intent);
                                                    if (mProgressDialog != null) {
                                                        mProgressDialog.dismiss();
                                                        mProgressDialog = null;
                                                    }
                                                    finish();

                                                } else {
                                                    if (mProgressDialog != null) {
                                                        mProgressDialog.dismiss();
                                                        mProgressDialog = null;
                                                    }
                                                    Intent intent = new Intent();
                                                    setResult(Constant.ADD_PIC_NEXT, intent);
                                                   finish();
                                                }
                                            }
                                        } else {
                                            array = response.getJSONArray("data");
                                            JSONObject img = (JSONObject) array.get(0);
                                            JSONObject fsimg = (JSONObject) array.get(1);
                                            String imgPath = img.getString("img");//实际
                                            String fsimgPath = fsimg.getString("img");
                                            Log.d("xu", fsimgPath);
                                            Log.d("xu", imgPath);

                                            String path = fsimgPath + "," + imgPath;
                                            Bimp.tempSelectBitmap.clear();
                                            Bimp.max = 0;
                                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.newStageAddImgs, new String[]{"photo_name", "intro", "path", "activity_id"}, new String[]{"ceshi", "美如画", path, mData.getActivity_id()}, handler, Constant.ADD_IMGS);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mImageUploadCountFailed++;
                                    }
                                }


                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Log.e("超时", "2222222222222222222222222222222222222222222222222222222222222222222222222222");
                                    mImageUploadCountFailed++;
                                    if (mImageUploadCount + mImageUploadCountFailed == ImageCount) {
                                        if (mImageUploadCountFailed != 0) {
                                            ToastUtil.showToast(DraftStartPickPhotoActivity.this,"有" + mImageUploadCountFailed + "张照片上传失败");
//                                            Toast.makeText(DraftStartPickPhotoActivity.this, "有" + mImageUploadCountFailed + "张照片上传失败",
//                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        if (tag == 0) {
                                            Intent intent = new Intent(DraftStartPickPhotoActivity.this, DraftInvitePeopleActivity.class);
                                            intent.putExtra("data", mData);
                                            startActivity(intent);
                                            if (mProgressDialog != null) {
                                                mProgressDialog.dismiss();
                                                mProgressDialog = null;
                                            }
                                            finish();

                                        } else {
                                            if (mProgressDialog != null) {
                                                mProgressDialog.dismiss();
                                                mProgressDialog = null;
                                            }
                                            Intent intent = new Intent();
                                            setResult(Constant.ADD_PIC_NEXT, intent);
                                            finish();
                                        }
                                    }
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            mImageUploadCountFailed++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (tag == 0) {
                        Intent intent = new Intent(DraftStartPickPhotoActivity.this, DraftInvitePeopleActivity.class);
                        intent.putExtra("data", mData);
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent();
                        setResult(Constant.ADD_PIC_NEXT, intent);
                        finish();
                    }
                }

            }
        });
    }

    private void publish() {
        mProgressDialog = new ProgressDialog(DraftStartPickPhotoActivity.this);
        mProgressDialog.setMessage("正在上传图片...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void Init() {

        pop = new PopupWindow(DraftStartPickPhotoActivity.this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        CardView cardview_1 = (CardView) view.findViewById(R.id.cardview_1);
        CardView cardview_2 = (CardView) view.findViewById(R.id.cardview_2);
        CardView cardview_3 = (CardView) view.findViewById(R.id.item_popupwindows_camera);//拍照
        cardview_3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DraftStartPickPhotoActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DraftStartPickPhotoActivity.this, new String[]{android.Manifest.permission.CAMERA}, Constant.CALL_CAMERA_REQUIRE);
                } else {
                    takePhoto();
                }

            }
        });

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.btn_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
//        bt1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                photo();
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
        cardview_1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DraftStartPickPhotoActivity.this,
                        AlbumActivity.class);
                intent.putExtra("maxNum", "6");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        cardview_2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(DraftStartPickPhotoActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(DraftStartPickPhotoActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }

    private void takePhoto(){
        photo();
        pop.dismiss();
        ll_popup.clearAnimation();
    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 6) {
                return 6;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = String.valueOf(System.currentTimeMillis());
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg")));
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {

                    Bitmap bm = null;
                    try {
                        bm = Bimp.revitionImageSize(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    takePhoto.setImagePath(Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg");
                    Bimp.tempSelectBitmap.add(takePhoto);
                    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg")));

                }
                break;
        }
    }

    //    private Bitmap compressImage(Bitmap bitmap, String fileName) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        saveBitmapFile(bitmap2, fileName);
//        return bitmap2;
//    }
//
//    private void saveBitmapFile(Bitmap bitmap, String fileName) {
//        File file = new File(Environment.getExternalStorageDirectory()+"/"+ fileName + ".JPEG");//将要保存图片的路径
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    private ArrayList<ImageItem> isTooBig(ArrayList<ImageItem> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            FileInputStream fis = new FileInputStream(list.get(i).getImagePath());
            fis.available();
            double sizeFile = FormetFileSize(fis.available(), 3);
            if (sizeFile > 1) {
                Bitmap bitmap2 = list.get(i).getBitmap();
                save(bitmap2, list, i);
            }
        }
        return list;
    }

    private void save(Bitmap bitmap, ArrayList<ImageItem> list, int postion) {
        String path = list.get(postion).getImagePath();
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

    }

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case 2:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case 3:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Bimp.tempSelectBitmap.size() != 0) {
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
            }
            finish();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.CALL_CAMERA_REQUIRE:
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto();
                }
                break;
        }
    }
}

