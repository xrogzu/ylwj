package com.administrator.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Bean_Bigstage_List;
import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.bean.ThumbnailPhoto;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.BigPictureActivity;
import com.administrator.elwj.BigStageNormalDetailsActivity;
import com.administrator.elwj.BigStageVoteDetailsActivity;
import com.administrator.elwj.CommentActivity;
import com.administrator.elwj.HomeActivity;
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.MapActivity;
import com.administrator.elwj.R;
import com.administrator.fragment.NearByFragment;
import com.administrator.utils.LocationUtil;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.view.MyGridView;
import com.amap.api.location.AMapLocation;
import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 身边新鲜事adapter
 * Created by acer on 2016/1/27.
 */
public class ListAdapterNearbyHome extends BaseAdapter implements LocationUtil.MyLocationListener {
    private Context context;
    private BaseApplication appContext;
    Bean_Bigstage_List bean_bigstage_list;
    Bean_Bigstage_List bean_bigstage_list1;
    private List<Novelty> lists = new ArrayList<>();
    private String TransNoveltyID;
    private static final int NOVELTY_TYPE = 0;
    private static final int ACTVITY_TYPE = 1;

    private PopupWindow commentPop;
    private static final int TRANS_NOVELTY = 0;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private DisplayImageOptions optionhead;

    private HomePageActivity homePageActivity;
    private NearByFragment nearByFragment;

    public ListAdapterNearbyHome(Context context, BaseApplication appContext, NearByFragment fragment) {
        this.context = context;
        this.appContext = appContext;
        nearByFragment = fragment;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public ListAdapterNearbyHome(Context context, BaseApplication appContext, HomePageActivity homePageActivity) {
        this.context = context;
        this.appContext = appContext;
        this.homePageActivity = homePageActivity;
        // 获取图片加载实例
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(false).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        optionhead = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    public void setFragment(NearByFragment nearByFragment) {
        this.nearByFragment = nearByFragment;
    }

    public void addData(List<Novelty> lists, Activity context) {
        this.lists = lists;
        this.context = context;
        notifyDataSetChanged();
    }

    public void clear() {
        if (lists != null && lists.size() > 0) {
            lists.clear();
            notifyDataSetChanged();
        }
    }

    public void addbean_bigstage_list(Bean_Bigstage_List bean_bigstage_list) {
        this.bean_bigstage_list = bean_bigstage_list;
        notifyDataSetChanged();
    }

    public void addbean_bigstage_list1(Bean_Bigstage_List bean_bigstage_list1) {
        this.bean_bigstage_list1 = bean_bigstage_list1;
        notifyDataSetChanged();
    }

    private static final int TYPE_COUNT = 3;//item类型的总数
    private static final int TYPE_PICTURES = 0;//图片类型
    private static final int TYPE_NORMAL = 1;//参加互动


    private static final int LIKE_NOVELTY = 1; //点赞
    private static final int CANCEL_LIKE = 2; //取消点赞

    private int mCurItem = -1;
    private TextView tvCurLikeNum;

    private String coordinate;
    private double coordinate_x;
    private double coordinate_y;

    private ProgressDialog mWaitLocationDialog;


    @Override
    public void onLoactionChanged(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                coordinate_x = location.getLongitude();//获取纬度
                coordinate_y = location.getLatitude();//获取经度
                coordinate = location.getProvince() + location.getCity() + location.getDistrict();
                LogUtils.d("xu_location_nearby", coordinate);
            }
        }
        if (mWaitLocationDialog != null) {
            mWaitLocationDialog.dismiss();
        }
        if (TransNoveltyID != null && !TransNoveltyID.equals(""))
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.transpondNovelty, new String[]{"novelty_id", "coordinate", "coordinate_x", "coordinate_y"}, new String[]{TransNoveltyID, coordinate, String.format("%f", coordinate_x), String.format("%f", coordinate_y)}, handler, TRANS_NOVELTY);
    }


    public static class MyHandler extends Handler {

        private WeakReference<ListAdapterNearbyHome> mListAdapterNearbyHome;

        public MyHandler(ListAdapterNearbyHome listAdapterNearbyHome) {
            mListAdapterNearbyHome = new WeakReference<ListAdapterNearbyHome>(listAdapterNearbyHome);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterNearbyHome listAdapterNearbyHome = mListAdapterNearbyHome.get();
            if (listAdapterNearbyHome != null) {
                String json = (String) msg.obj;
                if (json != null) {
                    switch (msg.what) {
                        case Constant.ISLOGIN:
                            try {
                                JSONObject object = new JSONObject(json);
                                int result = object.optInt("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LIKE_NOVELTY: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).setLike_num(String.format("%d", Long.parseLong(listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).getLike_num()) + 1));
                                    listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).setIs_like("1");
                                    long num = Long.parseLong(listAdapterNearbyHome.tvCurLikeNum.getText().toString());
                                    listAdapterNearbyHome.tvCurLikeNum.setText(String.format("%d", num + 1));
                                } else {
                                    LogUtils.d("xu_position", listAdapterNearbyHome.tvCurLikeNum.getText().toString());
                                    ToastUtil.showToast(listAdapterNearbyHome.context, jsonObject.get("message").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case CANCEL_LIKE: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).setLike_num(String.format("%d", Long.parseLong(listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).getLike_num()) - 1));
                                    listAdapterNearbyHome.lists.get(listAdapterNearbyHome.mCurItem).setIs_like("0");
                                    long num = Long.parseLong(listAdapterNearbyHome.tvCurLikeNum.getText().toString());
                                    listAdapterNearbyHome.tvCurLikeNum.setText(String.format("%d", num - 1));
                                } else {
                                    LogUtils.d("xu_position", listAdapterNearbyHome.tvCurLikeNum.getText().toString());
                                    ToastUtil.showToast(listAdapterNearbyHome.context, jsonObject.get("message").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case TRANS_NOVELTY: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    ToastUtil.showToast(listAdapterNearbyHome.context, "转发成功");
                                    if (listAdapterNearbyHome.nearByFragment != null)
                                        listAdapterNearbyHome.nearByFragment.updateNovelty();
                                    else {
                                        listAdapterNearbyHome.homePageActivity.updateNovelty();
                                    }
                                } else {
                                    ToastUtil.showToast(listAdapterNearbyHome.context, jsonObject.get("message").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        break;
                        case ADD_COMMENT:
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    ToastUtil.showToast(listAdapterNearbyHome.context, jsonObject.get("message").toString());
//                                        Toast.makeText(listAdapterNearbyHome.context, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    ToastUtil.showToast(listAdapterNearbyHome.context,jsonObject.get("message").toString());
//                                        Toast.makeText(listAdapterNearbyHome.context, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                }
            }
        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    public int getCount() {
        return lists.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static int getTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View picturesView;
        View normalView;
        boolean isActivity = lists.get(position).getIs_activity().equals("1");
        int currentType = TYPE_PICTURES;
        if (lists.get(position).getIs_activity().equals("1"))
            currentType = TYPE_NORMAL;
        ParentViewHolder parentViewHolder = null;
        if (convertView != null) {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
            if (parentViewHolder.getType() == ACTVITY_TYPE) {
                if (!isActivity) {
                    convertView = null;
                }
            } else if (isActivity) {
                convertView = null;
            }
        }
        if (currentType == TYPE_PICTURES) {
            PicturesViewHolder pcvh = null;
            if (convertView == null) {
                parentViewHolder = new ParentViewHolder(false);
                pcvh = new PicturesViewHolder();
                parentViewHolder.setPicturesViewHolder(pcvh);
                parentViewHolder = new ParentViewHolder(isActivity);
                parentViewHolder.setPicturesViewHolder(pcvh);
                picturesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_neaby1, null);
                pcvh.iv_head = (ImageView) picturesView.findViewById(R.id.iv_nearby_head);
                pcvh.ll_gridview_parent = (LinearLayout) picturesView.findViewById(R.id.ll_gridview_parent);
                pcvh.tv_name = (TextView) picturesView.findViewById(R.id.tv_nearby_name);
                pcvh.tv_time = (TextView) picturesView.findViewById(R.id.tv_nearby_time);
                pcvh.tv_introduce = (TextView) picturesView.findViewById(R.id.tv_nearby_introduce);
                pcvh.mGallery = (MyGridView) picturesView.findViewById(R.id.nearby_item_gallery);
                pcvh.tv_location = (TextView) picturesView.findViewById(R.id.tv_nearby_location);
                pcvh.linear_share = (LinearLayout) picturesView.findViewById(R.id.linear_item_share);
                pcvh.linear_recomment = (LinearLayout) picturesView.findViewById(R.id.linear_item_comment);
                pcvh.linear_check = (LinearLayout) picturesView.findViewById(R.id.linear_item_praise);
                pcvh.tv_shareNum = (TextView) picturesView.findViewById(R.id.tv_shareNum);
                pcvh.tv_recommentNum = (TextView) picturesView.findViewById(R.id.tv_recommentNum);
                pcvh.tv_praiseNum = (TextView) picturesView.findViewById(R.id.tv_praiseNum);
                pcvh.checkBox = (CheckBox) picturesView.findViewById(R.id.checkbox);
                picturesView.setTag(parentViewHolder);
                convertView = picturesView;
            } else {
                parentViewHolder = (ParentViewHolder) convertView.getTag();
                pcvh = parentViewHolder.getPicturesViewHolder();
            }
            final Novelty bean = lists.get(position);
            String aaaa = lists.get(position).getId();
            final String location = lists.get(position).getCoordinate();
            if (location != null && !"".equals(location))
                pcvh.tv_location.setText(lists.get(position).getCoordinate());

            pcvh.ll_gridview_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("Novelty", lists.get(position));
                    if (nearByFragment != null) {
                        nearByFragment.setNovelty(lists.get(position));
                        nearByFragment.startActivityForResult(intent, Constant.LIKE_IN_COMMENT);
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
            final PicturesViewHolder finalPcvh = pcvh;
            finalPcvh.tv_praiseNum.setText(bean.getLike_num());
            pcvh.linear_recomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showCommentPop(bean);
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("Novelty", lists.get(position));
                    if (nearByFragment != null) {
                        nearByFragment.setNovelty(lists.get(position));
                        nearByFragment.startActivityForResult(intent, Constant.LIKE_IN_COMMENT);
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
            String aaaaa = lists.get(position).getIs_like();
            pcvh.checkBox.setOnCheckedChangeListener(null);
            if (aaaaa.equals("1")) {
                pcvh.checkBox.setChecked(true);
            } else {
                pcvh.checkBox.setChecked(false);
            }
            finalPcvh.tv_recommentNum.setText(bean.getComment_num());
            pcvh.linear_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(BaseApplication.isLogin)
                        finalPcvh.checkBox.toggle();
                    else
                        ToastUtil.showToast(context,"请先登录");
                        //Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
                }
            });

            pcvh.tv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapActivity.class);
                    if (lists.get(position).getCoordinate_x() != null && !"".equals(lists.get(position).getCoordinate_x()))
                        intent.putExtra("x", Double.parseDouble(lists.get(position).getCoordinate_x()));
                    else intent.putExtra("x", 120.393318);
                    if (lists.get(position).getCoordinate_y() != null && !"".equals(lists.get(position).getCoordinate_y()))
                        intent.putExtra("y", Double.parseDouble(lists.get(position).getCoordinate_y()));
                    else intent.putExtra("y", 36.058942);
                    if (lists.get(position).getCoordinate() != null && !"".equals(lists.get(position).getCoordinate()))
                        intent.putExtra("address", lists.get(position).getCoordinate());
                    else intent.putExtra("address", "青岛市市南区");
                    context.startActivity(intent);
                }
            });


            pcvh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCurItem = position;
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.likeNovelty, new String[]{"novelty_id"}, new String[]{lists.get(position).getId()}, handler, LIKE_NOVELTY);
                        tvCurLikeNum = finalPcvh.tv_praiseNum;
                        //finalPcvh.tv_praiseNum.setText(String.format("%d", Long.parseLong(lists.get(position).getLike_num()) + 1));
                    } else {
                        mCurItem = position;
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancelLikeNovelty, new String[]{"novelty_id"}, new String[]{lists.get(position).getId()}, handler, CANCEL_LIKE);
                        tvCurLikeNum = finalPcvh.tv_praiseNum;
                        //finalPcvh.tv_praiseNum.setText(String.format("%d", Long.parseLong(lists.get(position).getLike_num()) - 1));
                    }
                }
            });

            pcvh.tv_introduce.setText(bean.getMessage_content());
            pcvh.tv_name.setText(bean.getPublisher_name());

            pcvh.tv_shareNum.setText(bean.getShare_num());
            pcvh.tv_time.setText(TimeTipsUtil.getTimeTips(Long.parseLong(bean.getPublish_time())));

            imageLoader.displayImage(lists.get(position).getFace(), pcvh.iv_head, optionhead);

            if (bean.getPhotos() == null || (bean.getPhotos().size() == 0)) {
                pcvh.mGallery.setVisibility(View.GONE);
            } else if (bean.getPhotos() != null && bean.getPhotos().size() > 0) {
                pcvh.mGallery.setVisibility(View.VISIBLE);
                pcvh.mGallery.setOnTouchBlankListener(new MyGridView.OnTouchBlankListener() {
                    @Override
                    public void onTouchBlank(MotionEvent e) {
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("Novelty", lists.get(position));
                        if (nearByFragment != null) {
                            nearByFragment.setNovelty(lists.get(position));
                            nearByFragment.startActivityForResult(intent, Constant.LIKE_IN_COMMENT);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                });
                NearByAdapter galleryAdapter = new NearByAdapter(bean);



                pcvh.mGallery.setAdapter(galleryAdapter);
            }

            pcvh.linear_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseApplication.isLogin) {
                        String imageURL = null;
                        List<ThumbnailPhoto> photos = lists.get(position).getPhotos();
                        if (photos != null && photos.size() > 0)
                            imageURL = photos.get(0).getOriginal();
                        ShareUtil shareUtil;
                        View MainView;
                        if (nearByFragment != null) {
                            shareUtil = new ShareUtil(nearByFragment.getActivity());
                            MainView = nearByFragment.getMainView();
                        } else {
                            MainView = homePageActivity.getMainView();
                            shareUtil = new ShareUtil(homePageActivity);
                        }
                        shareUtil.openShare(MainView, getActivity(), lists.get(position).getMessage_content(), imageURL, true, new SocializeListeners.OnSnsPlatformClickListener() {
                            @Override
                            public void onClick(final Context context, SocializeEntity socializeEntity, SocializeListeners.SnsPostListener snsPostListener) {
                                new AlertDialog.Builder(context).setMessage("是否转发？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coordinate = null;
                                        if (coordinate == null) {
                                            TransNoveltyID = lists.get(position).getId();
                                            if (mWaitLocationDialog == null) {
                                                mWaitLocationDialog = new ProgressDialog(context);
                                                mWaitLocationDialog.setMessage("正在获取位置信息...");
                                                mWaitLocationDialog.setCanceledOnTouchOutside(false);
                                                mWaitLocationDialog.show();
                                            } else {
                                                mWaitLocationDialog.show();
                                            }
                                            initLocation();
                                        } else {
                                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.transpondNovelty, new String[]{"novelty_id", "coordinate", "coordinate_x", "coordinate_y"}, new String[]{lists.get(position).getId(), coordinate, String.format("%f", coordinate_x), String.format("%f", coordinate_y)}, handler, TRANS_NOVELTY);
                                        }
                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        }, ShareUtil.NOVELTY_SHARE, lists.get(position).getId());
                    } else {
                        ToastUtil.showToast(appContext, "请先登录");
                    }


                }
            });
            pcvh.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("member_id", bean.getPublisher_id());
                    context.startActivity(intent);
                }
            });
            convertView.setClickable(true);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("Novelty", lists.get(position));
                    if (nearByFragment != null) {
                        nearByFragment.setNovelty(lists.get(position));
                        nearByFragment.startActivityForResult(intent, Constant.LIKE_IN_COMMENT);
                    } else {
                        context.startActivity(intent);
                    }
                }
            });


        } else {
            NormalViewHolder nvh = null;
            if (convertView == null) {
                parentViewHolder = new ParentViewHolder(true);
                nvh = new NormalViewHolder();
                parentViewHolder.setNormalViewHolder(nvh);
                normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_nearby2, null);
                nvh.iv_head = (ImageView) normalView.findViewById(R.id.iv_nearby_head);
                nvh.tv_name = (TextView) normalView.findViewById(R.id.tv_nearby_name);
                nvh.tv_time = (TextView) normalView.findViewById(R.id.tv_nearby_time);
                nvh.tv_introduce = (TextView) normalView.findViewById(R.id.tv_nearby_introduce);
                nvh.iv1 = (ImageView) normalView.findViewById(R.id.iv_nearby2);
                nvh.tv_acname = (TextView) normalView.findViewById(R.id.tv_nearby2_acname);
                nvh.tv_acLocation = (TextView) normalView.findViewById(R.id.tv_nearby2_aclocation);
                nvh.tv_acHadNum = (TextView) normalView.findViewById(R.id.tv_nearby2_acnum);
                nvh.tv_location = (TextView) normalView.findViewById(R.id.tv_nearby_location);
                nvh.linear_share = (LinearLayout) normalView.findViewById(R.id.linear_item_share);
                nvh.linear_recomment = (LinearLayout) normalView.findViewById(R.id.linear_item_comment);
                nvh.tv_shareNum = (TextView) normalView.findViewById(R.id.tv_shareNum);
                nvh.tv_recommentNum = (TextView) normalView.findViewById(R.id.tv_recommentNum);
                nvh.tv_praiseNum = (TextView) normalView.findViewById(R.id.tv_praiseNum);
                nvh.checkBox = (CheckBox) normalView.findViewById(R.id.checkbox);
                nvh.tv_bigstage = (TextView) normalView.findViewById(R.id.tv_bigstage);
                nvh.linear_praise = (LinearLayout) normalView.findViewById(R.id.linear_item_praise);
                nvh.rl_item2_activity = (RelativeLayout) normalView.findViewById(R.id.rl_item2_activity);
                nvh.applyType = (ImageView) normalView.findViewById(R.id.iv_apply);
                normalView.setTag(parentViewHolder);
                convertView = normalView;
            } else {
                parentViewHolder = (ParentViewHolder) convertView.getTag();
                nvh = parentViewHolder.getNormalViewHolder();
            }

            if (lists.get(position).getActivity_type().equals("投票")) {
                nvh.applyType.setImageResource(R.mipmap.vote_activity_nearby);
            } else if (lists.get(position).getActivity_type().equals("公共")) {
                nvh.applyType.setImageResource(R.mipmap.public_activity_nearby);
            } else {
                nvh.applyType.setImageResource(R.mipmap.apply_activity_nearby);
            }

            nvh.tv_bigstage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.JUMP);
                    intent.putExtra("jump", HomeActivity.JUMP_BIGSTAGE);
                    context.sendBroadcast(intent);
                }
            });
            nvh.tv_name.setText(lists.get(position).getPublisher_name());
            String content = lists.get(position).getMessage_content();
            if (!content.equals(""))
                nvh.tv_introduce.setText(lists.get(position).getMessage_content());
            else
                nvh.tv_introduce.setVisibility(View.GONE);
            nvh.tv_acname.setText(lists.get(position).getActivity_title());
            nvh.tv_location.setText(lists.get(position).getCoordinate());
            nvh.tv_acLocation.setText(lists.get(position).getActivity_address());
            nvh.tv_acHadNum.setText(lists.get(position).getApply_num());
            nvh.tv_praiseNum.setText(lists.get(position).getLike_num());
            nvh.tv_recommentNum.setText(lists.get(position).getComment_num());
            nvh.tv_shareNum.setText(lists.get(position).getShare_num());
            List<ThumbnailPhoto> photos = lists.get(position).getPhotos();


            nvh.linear_recomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentPop(lists.get(position));
                }
            });
            String aaaaa = lists.get(position).getIs_like();
            nvh.checkBox.setOnCheckedChangeListener(null);
            if (aaaaa.equals("1")) {
                nvh.checkBox.setChecked(true);
            } else {
                nvh.checkBox.setChecked(false);
            }
            final NormalViewHolder finalViewHolder = nvh;
            nvh.tv_recommentNum.setText(lists.get(position).getComment_num());
            nvh.linear_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalViewHolder.checkBox.toggle();
                }
            });

            nvh.tv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapActivity.class);
                    if (lists.get(position).getCoordinate_x() != null && !"".equals(lists.get(position).getCoordinate_x()))
                        intent.putExtra("x", Double.parseDouble(lists.get(position).getCoordinate_x()));
                    else intent.putExtra("x", 120.393318);
                    if (lists.get(position).getCoordinate_y() != null && !"".equals(lists.get(position).getCoordinate_y()))
                        intent.putExtra("y", Double.parseDouble(lists.get(position).getCoordinate_y()));
                    else intent.putExtra("y", 36.058942);
                    if (lists.get(position).getCoordinate() != null && !"".equals(lists.get(position).getCoordinate()))
                        intent.putExtra("address", lists.get(position).getCoordinate());
                    else intent.putExtra("address", "青岛市市南区");
                    context.startActivity(intent);
                }
            });

            nvh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCurItem = position;
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.likeNovelty, new String[]{"novelty_id"}, new String[]{lists.get(position).getId()}, handler, LIKE_NOVELTY);
                        tvCurLikeNum = finalViewHolder.tv_praiseNum;
                    } else {
                        mCurItem = position;
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancelLikeNovelty, new String[]{"novelty_id"}, new String[]{lists.get(position).getId()}, handler, CANCEL_LIKE);
                        tvCurLikeNum = finalViewHolder.tv_praiseNum;
                    }
                }
            });
            nvh.tv_time.setText(TimeTipsUtil.getTimeTips(Long.parseLong(lists.get(position).getPublish_time())));
            if (photos != null && photos.size() > 0)
                imageLoader.displayImage(lists.get(position).getPhotos().get(0).getThumbnail(), nvh.iv1, options);
            imageLoader.displayImage(lists.get(position).getFace(), nvh.iv_head, optionhead);

            nvh.linear_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageURL = null;
                    if (BaseApplication.isLogin) {
                        List<ThumbnailPhoto> photos = lists.get(position).getPhotos();
                        if (photos != null && photos.size() > 0)
                            imageURL = photos.get(0).getOriginal();
                        View MainView;
                        ShareUtil shareUtil;
                        if (nearByFragment != null) {
                            MainView = nearByFragment.getMainView();
                            shareUtil = new ShareUtil(nearByFragment.getActivity());
                        } else {
                            MainView = homePageActivity.getMainView();
                            shareUtil = new ShareUtil(homePageActivity);
                        }
                        shareUtil.openShare(MainView, getActivity(), lists.get(position).getMessage_content(), imageURL, true, new SocializeListeners.OnSnsPlatformClickListener() {
                            @Override
                            public void onClick(final Context context, SocializeEntity socializeEntity, SocializeListeners.SnsPostListener snsPostListener) {
                                new AlertDialog.Builder(context).setMessage("是否转发？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        coordinate = null;
                                        if (coordinate == null) {
                                            TransNoveltyID = lists.get(position).getId();
                                            if (mWaitLocationDialog == null) {
                                                mWaitLocationDialog = new ProgressDialog(context);
                                                mWaitLocationDialog.setMessage("正在获取位置信息...");
                                                mWaitLocationDialog.setCanceledOnTouchOutside(false);
                                                mWaitLocationDialog.show();
                                            } else {
                                                mWaitLocationDialog.show();
                                            }
                                            initLocation();
                                        } else {
                                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.transpondNovelty, new String[]{"novelty_id", "coordinate", "coordinate_x", "coordinate_y"}, new String[]{lists.get(position).getId(), coordinate, String.format("%f", coordinate_x), String.format("%f", coordinate_y)}, handler, TRANS_NOVELTY);
                                        }
                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        }, ShareUtil.NOVELTY_SHARE, lists.get(position).getId());

                    } else {
                        ToastUtil.showToast(context, "请先登录");
                    }
                }
            });
            nvh.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("member_id", lists.get(position).getPublisher_id());
                    context.startActivity(intent);
                }
            });
            convertView.setClickable(true);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (lists.get(position).getActivity_type().equals("投票")) {
                        intent = new Intent(context, BigStageVoteDetailsActivity.class);
                        intent.putExtra("activity_id", lists.get(position).getActivity_id());
                    } else {
                        intent = new Intent(context, BigStageNormalDetailsActivity.class);
                        Bean_Bigstage_List bean_bigstage_list = new Bean_Bigstage_List();
                        bean_bigstage_list.setActivity_id(lists.get(position).getActivity_id());
                        intent.putExtra("bean", bean_bigstage_list);
                    }
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    public void initLocation() {
        LocationUtil locationUtil = new LocationUtil();
        locationUtil.startLocation(context, this);
    }

    public static void setGridViewWidth(GridView gridView, NearByAdapter adapter, int count) {
        if (gridView == null)
            return;
        if (adapter == null) {
            return;
        }
        if (count <= 0)
            return;
        int singleHeight = 0;
        View listItem = adapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        singleHeight = listItem.getMeasuredWidth();
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = singleHeight * count;
        gridView.setLayoutParams(params);

    }

    public class ParentViewHolder {
        private int type;
        private PicturesViewHolder picturesViewHolder;
        private NormalViewHolder normalViewHolder;

        public ParentViewHolder(boolean isActivity) {
            if (isActivity)
                type = ACTVITY_TYPE;
            else type = NOVELTY_TYPE;
        }

        public PicturesViewHolder getPicturesViewHolder() {
            return picturesViewHolder;
        }

        public void setPicturesViewHolder(PicturesViewHolder picturesViewHolder) {
            this.picturesViewHolder = picturesViewHolder;
        }

        public NormalViewHolder getNormalViewHolder() {
            return normalViewHolder;
        }

        public void setNormalViewHolder(NormalViewHolder normalViewHolder) {
            this.normalViewHolder = normalViewHolder;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }


    public class PicturesViewHolder {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_introduce;
        TextView tv_location;
        CheckBox checkBox;
        TextView tv_shareNum;
        TextView tv_recommentNum;
        TextView tv_praiseNum;
        LinearLayout linear_share;
        LinearLayout linear_recomment;
        LinearLayout linear_check;
        MyGridView mGallery;
        LinearLayout ll_gridview_parent;
    }

    public class NormalViewHolder {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_introduce;
        ImageView iv1;
        CheckBox checkBox;
        TextView tv_acname;
        TextView tv_acLocation;
        TextView tv_acHadNum;
        TextView tv_location;
        TextView tv_shareNum;
        TextView tv_recommentNum;
        TextView tv_praiseNum;
        LinearLayout linear_praise;
        LinearLayout linear_share;
        LinearLayout linear_recomment;
        TextView tv_bigstage;
        RelativeLayout rl_item2_activity;
        ImageView applyType;
    }

    public class VoteViewHolder {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_introduce;
        ImageView iv1;
        CheckBox checkBox;
        TextView tv_acname;
        TextView tv_acLocation;
        TextView tv_acHadNum;
        TextView tv_location;
        TextView tv_shareNum;
        TextView tv_recommentNum;
        TextView tv_praiseNum;
        LinearLayout linear_share;
        LinearLayout linear_recomment;
        TextView tv_acsign_up;
        RelativeLayout rl_item2_activity;
        ImageView iv_shenbian_pen;
    }

    /**
     * 图片浏览
     */
    private class NearByAdapter extends BaseAdapter {
        private List<ThumbnailPhoto> itemImages;
        private Novelty mNovelty;

        public NearByAdapter(Novelty mNovelty) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
            }
            imageLoader.displayImage(itemImages.get(position).getThumbnail(), (ImageView) convertView, options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BigPictureActivity.class);
                    intent.putExtra("novelty", mNovelty);
                    intent.putExtra("default", position);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }


    }

    private EditText commentContent;
    private Button publishButton;
    private static final int ADD_COMMENT = 8;

    private void showCommentPop(final Novelty bean) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_comment_popup, null);
        commentContent = (EditText) view.findViewById(R.id.nearby_comment_popup_content);
        publishButton = (Button) view.findViewById(R.id.nearby_comment_popup_publish);
        commentPop = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        if (nearByFragment != null)
            nearByFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        else homePageActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        if (nearByFragment != null)
            commentPop.showAtLocation(nearByFragment.getActivity().findViewById(R.id.main),
                    Gravity.BOTTOM, 0, 0);
        else
            commentPop.showAtLocation(homePageActivity.findViewById(R.id.main), Gravity.BOTTOM, 0, 0);
        // popWindow消失监听方法
        commentPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //窗体背景透明度取消
                backgroundAlpha(1f);
            }
        });

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
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.addNoveltyComment, new String[]{"novelty_id", "comment_content"}, new String[]{bean.getId(), commentContent.getText().toString()}, handler, ADD_COMMENT);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private Activity getActivity() {
        if (nearByFragment != null) {
            return nearByFragment.getActivity();
        }
        return homePageActivity;
    }
}

