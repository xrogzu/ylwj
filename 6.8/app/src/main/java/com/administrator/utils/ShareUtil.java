package com.administrator.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.administrator.bean.Constant;
import com.administrator.elwj.R;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 第三方分享工具类
 * Created by Administrator on 2016/3/31.
 */
public class ShareUtil {

    //新鲜事分享
    public static final String NOVELTY_SHARE = "1";
    //商品分享
    public static final String GOODS = "2";
    //一般活动分享
    public static final String NORMAL_ACTIVITY_SHARE = "3";
    //投票活动分享
    public static final String VOTE_ACTIVITY_SHARE = "4";
    //公共活动分享
    public static final String PUBLIC_ACTVITY_SHARE = "5";
    //二维码分享
    public static final String SCAN_QR_SHARE = "6";


    private UMSocialService mController;
    private UMQQSsoHandler qqSsoHandler;
    private QZoneSsoHandler qZoneSsoHandler;
    private UMWXHandler wxHandler;
    private UMWXHandler wxCircleHandler;
    private CustomPlatform customPlatform;

    private boolean isInit = false;

    private Activity activity;

    public ShareUtil(Activity activity) {
        this.activity = activity;
        initShare();
    }

    public void initShare() {
        if (!isInit) {
            // 首先在您的Activity中添加如下成员变量
            mController = UMServiceFactory.getUMSocialService("com.umeng.share");
            mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.TENCENT);
            //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
            qqSsoHandler = new UMQQSsoHandler(activity, "1105216477",
                    "fC1vo6TkEG3OLppQ");
            qqSsoHandler.addToSocialSDK();

            //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
            qZoneSsoHandler = new QZoneSsoHandler(activity, "1105216477",
                    "fC1vo6TkEG3OLppQ");
            qZoneSsoHandler.addToSocialSDK();

            mController.getConfig().setSsoHandler(new SinaSsoHandler());

            String appID = "wx5af49d8b42d6fb27";
            String appSecret = "df5e3301da4cd22c3c29a738f2b3bc74";
            // 添加微信平台
            wxHandler = new UMWXHandler(activity, appID, appSecret);
            wxHandler.addToSocialSDK();
            // 添加微信朋友圈
            wxCircleHandler = new UMWXHandler(activity, appID, appSecret);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();
            isInit = true;
        }
    }


    /**
     * 分享
     *
     * @param popWindowParentView 要显示的popwindow的父view
     * @param context 上下文环境
     * @param content 分享文本内容
     * @param imageURL 分享图片的url
     * @param hasNearBy 是否有分享到身边的选项
     * @param listener 如果有分享到身边的选项，点击分享到是身边后要执行的事件
     */
    public void openShare(final View popWindowParentView, final Activity context, final String content, final String imageURL, boolean hasNearBy, final SocializeListeners.OnSnsPlatformClickListener listener, String type, String id) {
//        ToastUtil.showToast(context,content);
        final String targetURL = Constant.SHARE_URL + "type=" + type + "&id=" + id;
        Log.d("xu_share", targetURL);
        View view = LayoutInflater.from(activity).inflate(R.layout.popwindow_share, null);
        LinearLayout ll_weixin = (LinearLayout) view.findViewById(R.id.ll_weixin_share);
        LinearLayout ll_weixin_circle = (LinearLayout) view.findViewById(R.id.ll_weixin_circle_share);
        LinearLayout ll_weibo = (LinearLayout) view.findViewById(R.id.ll_weibo_share);
        LinearLayout ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq_share);
        LinearLayout ll_qzone = (LinearLayout) view.findViewById(R.id.ll_qzone_share);
        LinearLayout ll_nearby = (LinearLayout) view.findViewById(R.id.ll_nearby_share);
        ll_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinaShareContent sinaShareContent = new SinaShareContent();
                sinaShareContent.setTargetUrl(targetURL);
                sinaShareContent.setShareContent(content + targetURL);
                sinaShareContent.setTitle("华夏e生活");
                if (imageURL != null && !imageURL.equals(""))
                    sinaShareContent.setShareImage(new UMImage(activity, imageURL));
                mController.setShareMedia(sinaShareContent);
                mController.postShare(activity, SHARE_MEDIA.SINA, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        tips(i);
                    }
                });
            }
        });
        ll_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置微信分享
                WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
                if (content != null && !content.equals("")) {
//                    mController.setShareContent(content);
                    weiXinShareContent.setShareContent(content);
                }
                if (imageURL != null && !"".equals(imageURL)) {
                    UMImage image = new UMImage(context, imageURL);
//                    mController.setShareImage(image);
                    weiXinShareContent.setShareImage(image);
                }
                weiXinShareContent.setTitle("华夏e生活");

                weiXinShareContent.setTargetUrl(targetURL);
                mController.setShareMedia(weiXinShareContent);

                mController.postShare(activity, SHARE_MEDIA.WEIXIN, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        tips(i);
                    }
                });
            }
        });
        ll_weixin_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置朋友圈分享
                CircleShareContent circleShareContent = new CircleShareContent();
                if (content != null && !content.equals("")) {
                    mController.setShareContent(content);
                    circleShareContent.setShareContent(content);
                }
                if (imageURL != null && !"".equals(imageURL)) {
                    UMImage image = new UMImage(context, imageURL);
                    mController.setShareImage(image);
                    circleShareContent.setShareImage(image);
                }
                circleShareContent.setTitle(content);
                circleShareContent.setTargetUrl(targetURL);
                mController.setShareMedia(circleShareContent);
                mController.postShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        tips(i);
                    }
                });
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQShareContent qqShareContent = new QQShareContent();
                //设置分享文字
                qqShareContent.setShareContent(content);
                //设置分享title
                qqShareContent.setTitle("华夏e生活");
                //设置分享图片
                if (imageURL != null && !"".equals(imageURL)) {
                    qqShareContent.setShareImage(new UMImage(activity, imageURL));
                }
                //设置点击分享内容的跳转链接
                qqShareContent.setTargetUrl(targetURL);
                mController.setShareMedia(qqShareContent);
                mController.postShare(activity, SHARE_MEDIA.QQ, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        tips(i);
                    }
                });
            }
        });
        ll_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QZoneShareContent qzone = new QZoneShareContent();
                //设置分享文字
                qzone.setShareContent(content);
                //设置点击消息的跳转URL
                qzone.setTargetUrl(targetURL);
                //设置分享内容的标题
                qzone.setTitle("华夏e生活");
                //设置分享图片
                if (imageURL != null && !"".equals(imageURL)) {
                    qzone.setShareImage(new UMImage(activity, imageURL));
                }
                mController.setShareMedia(qzone);
                mController.postShare(activity, SHARE_MEDIA.QZONE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        tips(i);
                    }
                });
            }
        });

        final PopupWindow popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        if (hasNearBy) {
            ll_nearby.setVisibility(View.VISIBLE);
            ll_nearby.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    listener.onClick(activity, null, null);
                }
            });
        } else {
            ll_nearby.setVisibility(View.GONE);
            ll_nearby.setOnClickListener(null);
        }
        popupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        backgroundAlpha(0.5f);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // 在参照的View控件下方显示
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示

        popupWindow.showAtLocation(popWindowParentView,
                Gravity.BOTTOM, 0, 0);
        // popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //窗体背景透明度取消
                backgroundAlpha(1f);
                System.out.println("popWindow消失");
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    private void tips(int i) {
        if (i == 200) {
            ToastUtil.showToast(activity, "分享成功");
//            Toast.makeText(activity, "分享成功.", Toast.LENGTH_SHORT).show();
        } else {
            String eMsg = "";
            if (i == -101) {
                eMsg = "没有授权";
            }
            ToastUtil.showToast(activity, "分享失败" +
                    eMsg);
        }
    }
}
