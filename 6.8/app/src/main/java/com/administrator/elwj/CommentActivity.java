package com.administrator.elwj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.adapter.ListAdapterCommentAdapter;
import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.bean.NoveltyComment;
import com.administrator.bean.ThumbnailPhoto;
import com.administrator.utils.LogUtils;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.view.MyGridView;
import com.google.gson.Gson;
import com.library.listview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 身边新鲜事详细页面
 * Created by xu on 2016/2/29.
 */
public class CommentActivity extends AppCompatActivity {
    //评论列表
    private View mHeadView;
    //评论列表
    private XListView mListView;
    //新鲜事文字
    private TextView tvIntroduce;
    //发布人名称
    private TextView tvName;
    //发布时间
    private TextView tvTime;
    //坐标
    private TextView tvLocation;
    //喜欢总人数
    private TextView tvLikeCount;
    //gridview
    private MyGridView mGridView;
    //喜欢小图片
    private ImageView ivLike;
    //图片容器
    private LinearLayout llImageContainer;
    //新鲜事数据
    private Novelty mNovelty;

    private ImageLoader imageLoader;

    private EditText commentContent;
    private Button publishButton;
    private PopupWindow commentPop;


    private DisplayImageOptions optionhead;
    private DisplayImageOptions options;
    //是否点过赞
    private boolean isLiked = false;

    private ImageView ivHead;

    private static final int GET_COMMENT = 1;
    private static final int LIKE_COMMENT = 2;
    private static final int CANCEL_LIKE_COMMENT = 3;
    private static final int GET_LIKE_NUM = 4;
    private static final int GET_LIKE_PEOPLE = 5;
    private static final int ADD_COMMENT = 6;
    private static final int GET_NOVELTY = 7;

    private int curPage = 1;
    private int pageSize = 10;
    //是否还有未加载完的评论
    private boolean hasMoreComment = true;

    private boolean initLikeState = false;
    private boolean finalLikeState = false;


    private ListAdapterCommentAdapter mCommentAdapter;

    public static class MyHandler extends Handler {

        private WeakReference<CommentActivity> mActivity;

        public MyHandler(CommentActivity activity) {
            mActivity = new WeakReference<CommentActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentActivity activity = mActivity.get();
            if (activity != null) {
                activity.mListView.stopRefresh();
                activity.mListView.stopLoadMore();
                LogUtils.d("xu_page", String.format("%d", activity.curPage));
                String json = (String) msg.obj;
                if (json != null) {
                    switch (msg.what) {
                        case GET_COMMENT: {
                            Gson gson = new Gson();
                            NoveltyComment noveltyComment = gson.fromJson(json, NoveltyComment.class);
                            if (noveltyComment != null) {
                                List<NoveltyComment.DataEntity> data = noveltyComment.getData();
                                if (data != null) {
                                    if (activity.curPage == 1) {
                                        activity.hasMoreComment = true;
                                        activity.mCommentAdapter.clearData();
                                    }
                                    if (data.size() < activity.pageSize)
                                        activity.hasMoreComment = false;
                                    else activity.hasMoreComment = true;
                                    activity.mCommentAdapter.addData(noveltyComment.getData());
                                } else {
                                    activity.hasMoreComment = false;
                                }
                            } else {
                                activity.hasMoreComment = false;
                            }
                        }
                        break;
                        case LIKE_COMMENT: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    activity.mNovelty.setLike_num(String.format("%d", Long.parseLong(activity.mNovelty.getLike_num()) + 1));
                                    activity.tvLikeCount.setText("喜欢 " + activity.mNovelty.getLike_num());
                                    activity.finalLikeState = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //VolleyUtils.NetUtils(appContext.getRequestQueue(),Constant.baseUrl + Constant.getLikeNoveltyCount,new String[]{"novelty_id"},new String[]{mNovelty.getId()},handler,GET_LIKE_NUM);
                        }

                        break;
                        case CANCEL_LIKE_COMMENT: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    activity.mNovelty.setLike_num(String.format("%d", Long.parseLong(activity.mNovelty.getLike_num()) - 1));
                                    activity.tvLikeCount.setText("喜欢 " + activity.mNovelty.getLike_num());
                                    activity.finalLikeState = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //VolleyUtils.NetUtils(appContext.getRequestQueue(),Constant.baseUrl + Constant.getLikeNoveltyCount,new String[]{"novelty_id"},new String[]{mNovelty.getId()},handler,GET_LIKE_NUM);
                        }
                        break;
                        case GET_LIKE_PEOPLE: {
                        }
                        break;
                        case ADD_COMMENT:
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    ToastUtil.showToast(activity, "添加评论成功");
//                                    Toast.makeText(activity, "添加评论成功", Toast.LENGTH_SHORT).show();
                                    activity.curPage = 1;
                                    if (activity.mNovelty != null) {
                                        LogUtils.d("xu_test", "id = " + activity.mNovelty.getId() + ":" + String.format("%d", activity.curPage) + ":" + String.format("%d", activity.pageSize));
                                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyComment, new String[]{"novelty_id", "page", "pageSize"}, new String[]{activity.mNovelty.getId(), String.format("%d", activity.curPage), String.format("%d", activity.pageSize)}, activity.handler, GET_COMMENT);
                                    }
                                } else {
                                    ToastUtil.showToast(activity, jsonObject.get("message").toString());
//                                    Toast.makeText(activity, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case GET_NOVELTY:
                            LogUtils.d("xu_novelty", json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getInt("result") == 1) {
                                    String data = jsonObject.getString("data");
                                    Gson gson = new Gson();
                                    activity.mNovelty = gson.fromJson(data, Novelty.class);
                                    if (activity.mNovelty != null)
                                        activity.initData();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                } else {


                }
            }
        }
    }

    private Handler handler = new MyHandler(this);
    private BaseApplication appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_comment_detail);

        mHeadView = RelativeLayout.inflate(this, R.layout.nearby_comment_details_head, null);

        appContext = (BaseApplication) getApplication();

        initImageLoader();
        initViews();
        getIntentData();

        //VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyLikePeople, new String[]{"novelty_id"}, new String[]{mNovelty.getId()}, handler, GET_LIKE_PEOPLE);
    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    /**
     * 获取数据
     */
    private void getIntentData() {
        LogUtils.d("xu_scheme_comment", "getIntentData0");
        Intent data = getIntent();
        if (data != null) {
            LogUtils.d("xu_scheme_comment", "getIntentData1");
            mNovelty = (Novelty) data.getSerializableExtra("Novelty");
            if (mNovelty != null) {
                LogUtils.d("xu_scheme_comment", "getIntentData2");
                initData();
            } else {
                LogUtils.d("xu_scheme_comment", "getIntentData3");
                String novelty_id = data.getStringExtra("id");
                if (novelty_id != null) {
                    LogUtils.d("xu_scheme_comment", "getIntentData4");
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyByID, new String[]{"novelty_id"}, new String[]{novelty_id}, handler, GET_NOVELTY);
                }
            }
        }
    }

    private void initData() {
        if (mNovelty != null) {
            mGridView.setAdapter(new GalleyAdapter(mNovelty));
            imageLoader.displayImage(mNovelty.getFace(), ivHead, optionhead);
            tvIntroduce.setText(mNovelty.getMessage_content());
            if (mNovelty.getIs_like().equals("1")) {
                ivLike.setImageResource(R.mipmap.icon_love2);
                isLiked = true;
            }
            tvName.setText(mNovelty.getPublisher_name());
            tvTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(mNovelty.getPublish_time())));
            String location = mNovelty.getCoordinate();
            if (location != null && !"".equals(location))
                tvLocation.setText(mNovelty.getCoordinate());
            tvLikeCount.setText("喜欢 " + mNovelty.getLike_num());
            LogUtils.d("xu_test", "id = " + mNovelty.getId() + ":" + String.format("%d", curPage) + ":" + String.format("%d", pageSize));
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyComment, new String[]{"novelty_id", "page", "pageSize"}, new String[]{mNovelty.getId(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMENT);
            initLikeState = (mNovelty.getIs_like().equals("1"));
        }
    }


    private void initViews() {

        ivHead = (ImageView) mHeadView.findViewById(R.id.iv_nearby_head);
        mGridView = (MyGridView) mHeadView.findViewById(R.id.comment_gallery);
        ImageView ivBack = (ImageView) findViewById(R.id.hot_ib_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.this.finish();
            }
        });

        TextView tvTitle = (TextView) findViewById(R.id.title);
        ImageView ivLocation = (ImageView) mHeadView.findViewById(R.id.iv_nearby_map);
        tvName = (TextView) mHeadView.findViewById(R.id.tv_nearby_name);
        tvLikeCount = (TextView) mHeadView.findViewById(R.id.nearby_commet_detail_likecount);
        tvIntroduce = (TextView) mHeadView.findViewById(R.id.tv_nearby_introduce);
        tvLocation = (TextView) mHeadView.findViewById(R.id.tv_nearby_location);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        tvTime = (TextView) mHeadView.findViewById(R.id.tv_nearby_time);
        tvIntroduce.setText("");

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNovelty != null) {
                    Intent intent = new Intent(CommentActivity.this, MapActivity.class);
                    if (mNovelty.getCoordinate_x() != null && !"".equals(mNovelty.getCoordinate_x()))
                        intent.putExtra("x", Double.parseDouble(mNovelty.getCoordinate_x()));
                    else intent.putExtra("x", 120.393318);
                    if (mNovelty.getCoordinate_y() != null && !"".equals(mNovelty.getCoordinate_y()))
                        intent.putExtra("y", Double.parseDouble(mNovelty.getCoordinate_y()));
                    else intent.putExtra("y", 36.058942);
                    if (mNovelty.getCoordinate() != null && !"".equals(mNovelty.getCoordinate()))
                        intent.putExtra("address", mNovelty.getCoordinate());
                    else intent.putExtra("address", "青岛市市南区");
                    startActivity(intent);
                }
            }
        });


        ivLocation.setImageResource(R.mipmap.shenbian_icon_07);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            cbBottomLike.setBackground(ContextCompat.getDrawable(this, R.mipmap.shenbian_icon_06));
//        }
        tvTitle.setText("身边");

        mListView = (XListView) findViewById(R.id.listview_nearby_commet_detail);
        LinearLayout llComment = (LinearLayout) findViewById(R.id.linear_item_comment);
        LinearLayout llPraise = (LinearLayout) findViewById(R.id.linear_item_praise);
        llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CommentActivity.this, CommitCommentActivity.class);
//                intent.putExtra("Novelty", mNovelty);
//                startActivityForResult(intent, Constant.ADD_COMMENT);
                if (BaseApplication.isLogin)
                    showCommentPop();
                else ToastUtil.showToast(CommentActivity.this, "请先登录");
//                    Toast.makeText(CommentActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            }
        });

        llPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.isLogin) {
                    isLiked = !isLiked;
                    if (isLiked) {
                        ivLike.setImageResource(R.mipmap.icon_love2);
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.likeNovelty, new String[]{"novelty_id"}, new String[]{mNovelty.getId()}, handler, LIKE_COMMENT);
                    } else {
                        ivLike.setImageResource(R.mipmap.icon_love);
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancelLikeNovelty, new String[]{"novelty_id"}, new String[]{mNovelty.getId()}, handler, CANCEL_LIKE_COMMENT);
                    }
                } else {
                    ToastUtil.showToast(CommentActivity.this, "请先登录");
//                    Toast.makeText(CommentActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCommentAdapter = new ListAdapterCommentAdapter(this);
        mListView.addHeaderView(mHeadView);

        mListView.setAdapter(mCommentAdapter);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                hasMoreComment = true;
                if (mNovelty != null) {
                    LogUtils.d("xu_test", "id = " + mNovelty.getId() + ":" + String.format("%d", curPage) + ":" + String.format("%d", pageSize));
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyComment, new String[]{"novelty_id", "page", "pageSize"}, new String[]{mNovelty.getId(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMENT);

                }

            }

            @Override
            public void onLoadMore() {
                if (!hasMoreComment) {
                    mListView.stopLoadMore();
                    ToastUtil.showToast(CommentActivity.this, "没有更多");
//                    Toast.makeText(CommentActivity.this, "没有更多",
//                            Toast.LENGTH_SHORT).show();
                } else {
                    curPage++;
                    LogUtils.d("xu_test", "" + pageSize + "：" + curPage);
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyComment, new String[]{"novelty_id", "page", "pageSize"}, new String[]{mNovelty.getId(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMENT);
                }
//
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_COMMENT:
                if (resultCode == RESULT_OK) {
                    curPage = 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            curPage = 1;
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getNoveltyComment, new String[]{"novelty_id", "page", "pageSize"}, new String[]{mNovelty.getId(), String.format("%d", curPage), String.format("%d", pageSize)}, handler, GET_COMMENT);
                        }
                    }, 3000);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (initLikeState != finalLikeState) {
            int likecount = Integer.parseInt(mNovelty.getLike_num());
            Intent data = new Intent();
            data.putExtra("likecount", likecount);
            setResult(RESULT_OK, data);
        }
        super.onBackPressed();
    }


    /**
     * 图片浏览
     */
    private class GalleyAdapter extends BaseAdapter {
        private List<ThumbnailPhoto> itemImages;
        private Novelty mNovelty;

        public GalleyAdapter(Novelty mNovelty) {
            this.mNovelty = mNovelty;
            this.itemImages = mNovelty.getPhotos();
        }

        @Override
        public int getCount() {
            if (itemImages != null) {
                return itemImages.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (itemImages != null) {
                return itemImages.get(position).getOriginal();
            } else {
                return position;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.image_layout, parent, false);
            }
            imageLoader.displayImage(itemImages.get(position).getThumbnail(), (ImageView) convertView, options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommentActivity.this, BigPictureActivity.class);
                    intent.putExtra("novelty", mNovelty);
                    int defaultPage = position;
                    intent.putExtra("default", defaultPage);
                    CommentActivity.this.startActivity(intent);
                }
            });
            return convertView;
        }


    }


    private void showCommentPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.nearby_comment_popup, null);
        commentContent = (EditText) view.findViewById(R.id.nearby_comment_popup_content);
        publishButton = (Button) view.findViewById(R.id.nearby_comment_popup_publish);
        commentPop = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口高度
        //commentPop.setHeight(dm.heightPixels * 2 / 4);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        commentPop.setFocusable(true);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        commentPop.setBackgroundDrawable(dw);
        //popwindow出现的时候，半透明
        backgroundAlpha(0.5f);
        // 在参照的View控件下方显示
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        // 设置popWindow的显示和消失动画
        commentPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        commentPop.showAtLocation(findViewById(R.id.main),
                Gravity.BOTTOM, 0, 0);
        // popWindow消失监听方法
        commentPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //窗体背景透明度取消
                backgroundAlpha(1f);
            }
        });

        commentContent.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) commentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        commentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.e("wj", s.toString());
                String comment = s.toString();
                if (comment != null && comment.length() > 0) {
                    publishButton.setEnabled(true);
                } else {
                    publishButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.addNoveltyComment, new String[]{"novelty_id", "comment_content"}, new String[]{mNovelty.getId(), commentContent.getText().toString()}, handler, ADD_COMMENT);
                commentPop.dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
