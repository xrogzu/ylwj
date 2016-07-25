package com.administrator.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.bean.ThumbnailPhoto;
import com.administrator.bean.UserInfo;
import com.administrator.elwj.BaseApplication;
import com.administrator.elwj.BigPictureActivity;
import com.administrator.elwj.CommentActivity;
import com.administrator.elwj.HomePageActivity;
import com.administrator.elwj.MapActivity;
import com.administrator.elwj.R;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.TimeTipsUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.administrator.view.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 个人主页中新鲜事的适配器
 * Created by wujian on 2016/3/2.
 */
public class ListAdapterHomePage extends BaseAdapter {
    private Context mContext;
    private List<Novelty> novelties;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private DisplayImageOptions optionhead;
    private BaseApplication appContext;
    private UserInfo userInfo;
    private static final int LIKE_NOVELTY = 1; //点赞
    private static final int CANCEL_LIKE = 2; //取消点赞
    private static final int TRANS_NOVELTY = 0;
    private HomePageActivity homePageActivity;
    private int mCurItem = -1;
    private TextView tvCurLikeNum;
    private Handler handler = new MyHandler(this);

    public ListAdapterHomePage() {
    }

    public ListAdapterHomePage(Context mContext, BaseApplication appContext, UserInfo userInfo, ImageLoader imageLoader, DisplayImageOptions options, HomePageActivity fragment) {
        this.mContext = mContext;
        this.userInfo = userInfo;
        this.imageLoader = imageLoader;
        this.options = options;
        this.appContext = appContext;
        this.homePageActivity = fragment;
        this.optionhead = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang)
                .showImageForEmptyUri(R.mipmap.touxiang)
                .showImageOnFail(R.mipmap.touxiang)
                .cacheInMemory(false).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }

    private static class MyHandler extends Handler {
        private WeakReference<ListAdapterHomePage> mListAdapterHomePage;

        public MyHandler(ListAdapterHomePage listAdapterHomePage) {
            mListAdapterHomePage = new WeakReference<ListAdapterHomePage>(listAdapterHomePage);
        }

        @Override
        public void handleMessage(Message msg) {
            ListAdapterHomePage listAdapterHomePage = mListAdapterHomePage.get();
            if (listAdapterHomePage != null) {
                String json = (String) msg.obj;
                if (json != null) {
                    switch (msg.what) {
                        case LIKE_NOVELTY:
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.optString("message");
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).setLike_num(String.format("%d", Long.parseLong(listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).getLike_num()) + 1));
//                                    listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).setIs_like(0);
                                    if (listAdapterHomePage.tvCurLikeNum != null) {
                                        long num = Long.parseLong(listAdapterHomePage.tvCurLikeNum.getText().toString());
                                        listAdapterHomePage.tvCurLikeNum.setText(String.format("%d", num + 1));
                                    } else {
                                        ToastUtil.showToast(listAdapterHomePage.mContext,message);
//                                        Toast.makeText(listAdapterHomePage.mContext, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //Toast.makeText(listAdapterHomePage.mContext,message,Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case CANCEL_LIKE:
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.optString("message");
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).setLike_num(String.format("%d", Long.parseLong(listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).getLike_num()) - 1));
//                                    listAdapterHomePage.novelties.get(listAdapterHomePage.mCurItem).setIs_like(1);
                                    if (listAdapterHomePage.tvCurLikeNum != null) {
                                        long num = Long.parseLong(listAdapterHomePage.tvCurLikeNum.getText().toString());
                                        listAdapterHomePage.tvCurLikeNum.setText(String.format("%d", num - 1));
                                    }
                                } else {
                                    ToastUtil.showToast(listAdapterHomePage.mContext, message);
//                                    Toast.makeText(listAdapterHomePage.mContext, message, Toast.LENGTH_SHORT).show();
                                }
                                //
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TRANS_NOVELTY: {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    ToastUtil.showToast(listAdapterHomePage.mContext, "转发成功");
//                                    Toast.makeText(listAdapterHomePage.mContext, "转发成功", Toast.LENGTH_SHORT).show();
                                    listAdapterHomePage.homePageActivity.updateNovelty();
                                } else {
                                    ToastUtil.showToast(listAdapterHomePage.mContext,jsonObject.get("message").toString());
//                                    Toast.makeText(listAdapterHomePage.mContext, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        break;
                    }
                }
            }
        }
    }


    @Override
    public int getCount() {
        if (novelties != null) {
            return novelties.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (novelties != null) {
            return novelties.get(position);
        } else {
            return position;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.homepage_contentlist_item, parent, false);
            viewHolder.mFaceImage = (ImageView) convertView.findViewById(R.id.homepage_item_faceimage);
            viewHolder.mNickName = (TextView) convertView.findViewById(R.id.homepage_item_nickname);
            viewHolder.mLocation = (TextView) convertView.findViewById(R.id.homepage_item_location);
            viewHolder.mUpdateTime = (TextView) convertView.findViewById(R.id.homepage_item_time);
            viewHolder.mItemTitle = (TextView) convertView.findViewById(R.id.homepage_item_title);
            viewHolder.mGallery = (MyGridView) convertView.findViewById(R.id.homepage_item_gallery);
            viewHolder.mShareNum = (TextView) convertView.findViewById(R.id.homepage_item_share);
            viewHolder.mRemarkNum = (TextView) convertView.findViewById(R.id.homepage_item_remark);
            viewHolder.mPinLun = (LinearLayout) convertView.findViewById(R.id.homepage_pin);
            viewHolder.mLikeLayout = (LinearLayout) convertView.findViewById(R.id.homepage_care);
            viewHolder.mCareNum = (TextView) convertView.findViewById(R.id.homepage_item_care);
            viewHolder.mLikecb = (CheckBox) convertView.findViewById(R.id.homepage_item_checkbox);
            viewHolder.mShare = (LinearLayout) convertView.findViewById(R.id.linearlayout_share);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mLikecb.setOnCheckedChangeListener(null);
        if (novelties.get(position).getIs_like().equals("1")) {
            viewHolder.mLikecb.setChecked(true);
        } else {
            viewHolder.mLikecb.setChecked(false);
        }

//        viewHolder.mGallery.setOnTouchInvalidPositionListener(new MyGridView.OnTouchInvalidPositionListener() {
//            @Override
//            public boolean onTouchInvalidPosition(int motionEvent) {
//                Intent intent = new Intent(mContext, CommentActivity.class);
//                intent.putExtra("Novelty", novelties.get(position));
//                mContext.startActivity(intent);
//                return true;
//            }
//        });

        //显示头像
        imageLoader.displayImage(novelties.get(position).getFace(), viewHolder.mFaceImage, optionhead);
        viewHolder.mItemTitle.setText(novelties.get(position).getMessage_content());
        viewHolder.mUpdateTime.setText(TimeTipsUtil.getTimeTips(Long.parseLong(novelties.get(position).getPublish_time())));
        viewHolder.mShareNum.setText(novelties.get(position).getShare_num());
        viewHolder.mCareNum.setText(novelties.get(position).getLike_num());

        if (novelties.get(position).getCoordinate() != null && !"".equals(novelties.get(position).getCoordinate()))
            viewHolder.mLocation.setText(novelties.get(position).getCoordinate());
        viewHolder.mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapActivity.class);
                if (novelties.get(position).getCoordinate_x() != null && !"".equals(novelties.get(position).getCoordinate_x()))
                    intent.putExtra("x", Double.parseDouble(novelties.get(position).getCoordinate_x()));
                else intent.putExtra("x", 120.393318);
                if (novelties.get(position).getCoordinate_y() != null && !"".equals(novelties.get(position).getCoordinate_y()))
                    intent.putExtra("y", Double.parseDouble(novelties.get(position).getCoordinate_y()));
                else intent.putExtra("y", 36.058942);
                if (novelties.get(position).getCoordinate() != null && !"".equals(novelties.get(position).getCoordinate()))
                    intent.putExtra("address", novelties.get(position).getCoordinate());
                else intent.putExtra("address", "青岛市市南区");
                mContext.startActivity(intent);
            }
        });
//        viewHolder.mRemarkNum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CommentActivity.class);
//                intent.putExtra("Novelty", novelty);
//                mContext.startActivity(intent);
//            }
//        });
        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.isLogin) {
                    ShareUtil shareUtil = new ShareUtil(homePageActivity);
                    List<ThumbnailPhoto> photos = novelties.get(position).getPhotos();
                    if (BaseApplication.member_id != null && BaseApplication.member_id.equals(novelties.get(position).getPublisher_id())) {
                        shareUtil.openShare(homePageActivity.getMainView(), homePageActivity, novelties.get(position).getMessage_content(), (photos != null && photos.size() > 0) ? photos.get(0).getOriginal() : null, false, null, ShareUtil.NOVELTY_SHARE, novelties.get(position).getId());
                    } else
                        shareUtil.openShare(homePageActivity.getMainView(), homePageActivity, novelties.get(position).getMessage_content(), (photos != null && photos.size() > 0) ? photos.get(0).getOriginal() : null, true, new SocializeListeners.OnSnsPlatformClickListener() {
                            @Override
                            public void onClick(Context context, SocializeEntity socializeEntity, SocializeListeners.SnsPostListener snsPostListener) {
                                new AlertDialog.Builder(mContext).setMessage("是否转发？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.transpondNovelty, new String[]{"novelty_id"}, new String[]{novelties.get(position).getId()}, handler, TRANS_NOVELTY);
                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        }, ShareUtil.NOVELTY_SHARE, novelties.get(position).getId());
                } else {
                    ToastUtil.showToast(mContext, "请先登录");
//                    Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.mPinLun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("Novelty", novelties.get(position));
                mContext.startActivity(intent);
//                Intent intent = new Intent(mContext, CommitCommentActivity.class);
//                intent.putExtra("Novelty", novelties.get(position));
//                homePageActivity.startActivity(intent);
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.mLikecb.toggle();
            }
        });
        viewHolder.mLikecb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCurItem = position;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.likeNovelty, new String[]{"novelty_id"}, new String[]{novelties.get(position).getId()}, handler, LIKE_NOVELTY);
                    tvCurLikeNum = finalViewHolder.mCareNum;
                    //finalViewHolder.mCareNum.setText(String.format("%d", Long.parseLong(novelties.get(position).getLike_num()) + 1));
                } else {
                    mCurItem = position;
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancelLikeNovelty, new String[]{"novelty_id"}, new String[]{novelties.get(position).getId()}, handler, CANCEL_LIKE);
                    //finalViewHolder.mCareNum.setText(String.format("%d", Long.parseLong(novelties.get(position).getLike_num()) - 1));
                    tvCurLikeNum = finalViewHolder.mCareNum;
                }
            }
        });
        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("Novelty", novelties.get(position));
                mContext.startActivity(intent);
            }
        });

        viewHolder.mRemarkNum.setText(novelties.get(position).getComment_num());
        viewHolder.mNickName.setText(novelties.get(position).getPublisher_name());
        //viewHolder.mLocation.setText();
        if (novelties.get(position).getPhotos() == null || (novelties.get(position).getPhotos() != null && novelties.get(position).getPhotos().size() == 0)) {
            viewHolder.mGallery.setVisibility(View.GONE);
        } else if (novelties.get(position).getPhotos() != null && novelties.get(position).getPhotos().size() > 0) {
            viewHolder.mGallery.setVisibility(View.VISIBLE);
            MyGalleryAdapter galleryAdapter = new MyGalleryAdapter(novelties.get(position));
            viewHolder.mGallery.setAdapter(galleryAdapter);
        }
        return convertView;
    }

    public void updateUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private class ViewHolder {
        ImageView mFaceImage;
        TextView mNickName;
        TextView mLocation;
        TextView mUpdateTime;
        TextView mItemTitle;
        MyGridView mGallery;
        TextView mShareNum;
        TextView mRemarkNum;
        TextView mCareNum;
        LinearLayout mPinLun;
        LinearLayout mLikeLayout;
        LinearLayout mShare;
        CheckBox mLikecb;
    }

    /**
     * <p>往适配器中添加数据</p>
     */
    public void addData(List<Novelty> novelties) {
        this.novelties = novelties;
        notifyDataSetChanged();
    }

    public void clear() {
        if (novelties != null && novelties.size() > 0) {
            novelties.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 图片浏览
     */
    private class MyGalleryAdapter extends BaseAdapter {
        private List<ThumbnailPhoto> itemImages;
        private Novelty mNovelty;

        public MyGalleryAdapter(Novelty mNovelty) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_layout, parent, false);
            imageLoader.displayImage(itemImages.get(position).getThumbnail(), (ImageView) convertView, options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BigPictureActivity.class);
                    intent.putExtra("novelty", mNovelty);
                    intent.putExtra("default", position);
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }


    }
}
