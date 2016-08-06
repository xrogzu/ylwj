package com.administrator.elwj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.bean.BeanDetails_Banner;
import com.administrator.bean.Bean_DetailsImgs;
import com.administrator.bean.Bean_GoodsList;
import com.administrator.bean.Constant;
import com.administrator.utils.DoubleUtils;
import com.administrator.utils.LogUtils;
import com.administrator.utils.ShareUtil;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;
import com.king.photo.util.RemovedPicUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情界面
 * intent传过来的type为Constant.SHOP_TYPE_MONEY，表示是现金商品
 * intent传过来的type为Constant.SHOP_TYPE_CREDIT,表示是积分商品
 * intent传过来的type为Constant.SHOP_TYPE_AUTO，表示是传过来商品id，然后从服务器获取商品详情，根据服务器返回的详情中的flag来判断是积分商品还是现金商品
 */
public class Interalshop_detailsactivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager_shopdetails;
    private List<ImageView> iv_lists = new ArrayList<>();
    private List<View> dots = new ArrayList<>();
    private ImageButton ib_back;
    private ImageButton ib_details_redduce;
    private ImageButton ib_details_add;
    private TextView tv_details_num;
    private ImageView delivery_img,add_details;
    private int num = 1;
    private LinearLayout shuliang;
    private RelativeLayout rl_shopdetails_choosecolor;
    private RelativeLayout rl_shopdetails_parameter;
    private LinearLayout linear_shopdetails_share;
    private LinearLayout linear_service;
    private CheckBox linear_collect;
    private Button bt_addshopcars;
    private TextView delivery_detail;
    private Button bt_buy_immediately;
    private PopupWindow popupWindow;
    private ImageButton ib_shoppingcar;
    public static final int SHOPCAR = 0x00;
    public static final int Add_SHOPCAR = 0x01;
    public static final int BUY_NOW = 0x02;
    private int click_which = 0;
    private WebView wv;
    private double myCredit;
    private TextView popPriceTV;
    private TextView poptvMyCredit;
    private Button popSureButton;
    private EditText popCountEditText;
    private int count = 1;//用户选择的商品类型数量

    private String removePic;

    public static class MyHandler extends Handler {
        private WeakReference<Interalshop_detailsactivity> mActivity;

        public MyHandler(Interalshop_detailsactivity interalshop_detailsactivity) {
            mActivity = new WeakReference<Interalshop_detailsactivity>(interalshop_detailsactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            Interalshop_detailsactivity activity = mActivity.get();
            if (activity != null) {
                int which = msg.what;
                String json = (String) msg.obj;
                if (which == Constant.DETAILS_IMGS) {
//                LogUtils.e("DETAILS", json);
                    Gson gson = new Gson();
                    Bean_DetailsImgs imgs_bean = gson.fromJson(json, Bean_DetailsImgs.class);
                    List<Bean_DetailsImgs.DataEntity> imgdata = imgs_bean.getData();
                    if (activity.removePic != null) {
                        for (int i = 0; i < imgdata.size(); ++i) {
                            if (activity.beanDetails != null) {
                                if (RemovedPicUtils.isEqual(imgdata.get(i).getBig(), activity.beanDetails.getSmall())) {
                                    imgdata.remove(i);
                                    break;
                                }
                            } else {
                                if (RemovedPicUtils.isEqual(imgdata.get(i).getBig(), activity.removePic)) {
                                    imgdata.remove(i);
                                    break;
                                }
                            }
                        }
                    }

                    activity.initVipager(imgdata);
                    //查询商品收藏状态
                    if (activity.beanDetails != null) {
                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.CollectGoods_state, new String[]{"goods_id"}, new String[]{activity.beanDetails.getGoods_id() + ""}, activity.handler, Constant.STATE_COLLECT_GOODS);
                    }
                }
                //判断收藏状态
                if (which == Constant.STATE_COLLECT_GOODS) {
                    try {
                        LogUtils.e("wj", json);
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        String message = object.optString("message");
                        if (result == 1 && message != null) {
                            if (message.equals("1")) {
                                activity.linear_collect.setChecked(true);
                            }
                            if (message.equals("0")) {
                                activity.linear_collect.setChecked(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == Constant.ADDTO_SHOPCAR) {
                    try {
                        LogUtils.e("TAG", json);
                        JSONObject obj = new JSONObject(json);
                        int result = obj.optInt("result");
                        if (result == 1) {
                            int count = obj.optInt("count");
                            ToastUtil.showToast(activity, "加入成功，目前购物车有" + count + "件商品");
                        } else {
                            String message = obj.optString("message");
                            ToastUtil.showToast(activity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == Constant.GOODS_DETAILS) {//优惠活动产品
                    Gson gson = new Gson();
                    LogUtils.e("Main", json);
                    BeanDetails_Banner beanDetails_banner = gson.fromJson(json, BeanDetails_Banner.class);
//                    if(!TextUtils.isEmpty(beanDetails_banner.getData().getDelivery_android())) {
//                        activity.imageLoader.displayImage(beanDetails_banner.getData().getDelivery_android(), activity.delivery_img, activity.options);
//                    }
                    if(!TextUtils.isEmpty(beanDetails_banner.getData().getDelivery_detail())) {
                        activity.delivery_detail.setVisibility(View.VISIBLE);
                        activity.delivery_detail.setText(beanDetails_banner.getData().getDelivery_detail());
                    }
                    if (beanDetails_banner != null) {
                        //一元活动，限购一次
                        if(!TextUtils.isEmpty(beanDetails_banner.getData().getType())&&beanDetails_banner.getData().getPrice()==1.0) {
                            activity.restrictionShopping=true;
                            activity.bt_addshopcars.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                            activity.ib_details_redduce.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                            activity.ib_details_add.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                            activity.linear_collect.setClickable(false);
                            if(beanDetails_banner.getData().getPrivilegeFlag()==1){
                                activity.bt_buy_immediately.setClickable(false);
                                activity.bt_buy_immediately.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                            }
                        }
                        //判断是有华夏价的产品
                        if(!TextUtils.isEmpty(beanDetails_banner.getData().getMktprice())||!beanDetails_banner.getData().getMktprice().equals("0.00")){
                            activity.huaxiaProduct=true;
                            activity.bt_addshopcars.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                        }
                        activity.details_banner = beanDetails_banner.getData();
                        if (activity.beanDetails == null) {
                            activity.beanDetails = new Bean_GoodsList.DataEntity();
                        }
                        //需要转化一下类
                        activity.beanDetails.setName(activity.details_banner.getName());
                        activity.beanDetails.setPrice(activity.details_banner.getPrice());
                        activity.beanDetails.setGoods_id(activity.details_banner.getGoods_id());
                        activity.beanDetails.setMktprice(activity.details_banner.getMktprice() + "");
                        activity.beanDetails.setBuy_count(activity.details_banner.getBuy_count() + "");
                        activity.beanDetails.setView_count(activity.details_banner.getView_count() + "");
                        activity.beanDetails.setIntro(activity.details_banner.getIntro());
                        activity.beanDetails.setStore(activity.details_banner.getStore() + "");
                        activity.beanDetails.setSmall(Constant.baseUrl + activity.details_banner.getSmall().substring(3));
                        activity.beanDetails.setOriginal(activity.details_banner.getOriginal());
                        activity.beanDetails.setFlag(activity.details_banner.getFlag());

                    }
                    if (activity.details_banner != null) {
                        if (activity.details_banner.getFlag().equals("1")) {//积分
                            activity.type = Constant.SHOP_TYPE_CREDIT;
                            activity.initViews_gredit();
                        } else {
                            activity.type = Constant.SHOP_TYPE_MONEY;
                            activity.initView_carsh();//现金
                        }
                    }
                    if (activity.beanDetails != null) {
                        VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.detailsImg, new String[]{"id"}, new String[]{activity.beanDetails.getGoods_id() + ""}, activity.handler, Constant.DETAILS_IMGS);
                    }
                }
                if (which == Constant.COLLECT_GOODS) {
                    try {
                        LogUtils.e("wj", json);
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        if (result == 0) {//收藏时未登录
                            String message = object.optString("message");
                            activity.linear_collect.setChecked(false);
                            ToastUtil.showToast(activity, message);
                        } else if (result == 1) {
                            ToastUtil.showToast(activity, "收藏成功，可在我的收藏中查看");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == Constant.CANCLE_COLLECT_GOODS) {
                    try {
                        LogUtils.e("wj", json);
                        JSONObject object = new JSONObject(json);
                        int result = object.optInt("result");
                        if (result == 1) {
                            activity.linear_collect.setChecked(false);
                            ToastUtil.showToast(activity, "取消收藏成功");
                        } else {
                            String message = object.optString("message");
                            ToastUtil.showToast(activity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (which == Constant.ISLOGIN) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 0) {
                            Intent LoginIntent = new Intent(activity, LoginActivity.class);
                            activity.startActivity(LoginIntent);
                            if (activity.click_which == BUY_NOW)
                                activity.bt_buy_immediately.setEnabled(true);
                        } else if (result == 1) {
                            if (activity.click_which == Add_SHOPCAR) {
                                String num = activity.tv_details_num.getText().toString();
                                VolleyUtils.NetUtils(activity.appContext.getRequestQueue(), Constant.baseUrl + Constant.shopcarURL, new String[]{"productid", "num"}, new String[]{activity.beanDetails.getGoods_id() + "", num}, activity.handler, Constant.ADDTO_SHOPCAR);
                            } else if (activity.click_which == SHOPCAR) {
                                Intent carIntent = new Intent(activity, ShopCarActivity.class);
                                activity.startActivity(carIntent);
                            } else if (activity.click_which == BUY_NOW) {
                                //立即购买后
                                activity.showPopwindow();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (activity.click_which == BUY_NOW)
                            activity.bt_buy_immediately.setEnabled(true);
                    }
                }
                if (which == Constant.GETCREDIT) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("result");
                        if (result == 0) {
                            String message = jsonObject.optString("message");
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            activity.poptvMyCredit.setText("目前拥有:" + 0.0 + "积分");
                            activity.setCreditTips(0);
                        } else if (result == 1) {
                            String data = jsonObject.optString("data");
                            jsonObject = new JSONObject(data);
                            String credit = jsonObject.getString("credit");
                            if (credit != null && !"".equals(credit)) {
                                activity.myCredit = Double.parseDouble(credit);
                                activity.setCreditTips(activity.myCredit);
                            } else {
                                activity.setCreditTips(0);
                            }
                            activity.poptvMyCredit.setText("目前拥有:" + String.format("%d", (long) activity.myCredit) + "积分");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private Handler handler = new MyHandler(this);


    private BaseApplication appContext;

    private TextView tv_name;
    private LinearLayout song_jifen;
    private ImageView ben;
    private TextView tv_shopdetails_price;
    private TextView tv_shopdetails_freepostage;
    private Bean_GoodsList.DataEntity beanDetails;
    private List<Bean_GoodsList.DataEntity> dataEntities;//用来存放用户选择的商品实体，传到下一个Activity
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private BeanDetails_Banner.DataEntity details_banner;
    private int type;
    private static String encoding = "UTF-8";
    private static String mimeType = "text/html";
    private LinearLayout li_dotsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interalshop_detailsactivity);
        li_dotsList = (LinearLayout) findViewById(R.id.li_details_dots);
        wv = (WebView) findViewById(R.id.webview);
        //支持javascript
        wv.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= 19)
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        else
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
        initPriView();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        appContext = (BaseApplication) getApplication();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", Constant.SHOP_TYPE_CREDIT);
        if (type == Constant.SHOP_TYPE_MONEY) {//现金产品
//            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsDetails, new String[]{"id"}, new String[]{id}, handler, Constant.GOODS_DETAILS);
            beanDetails = (Bean_GoodsList.DataEntity) intent.getSerializableExtra("details_info");//商品详情参数
            if(!TextUtils.isEmpty(beanDetails.getDelivery_detail())) {//邮递方式信息展示
                delivery_detail.setVisibility(View.VISIBLE);
                delivery_detail.setText(beanDetails.getDelivery_detail());
            }
            if(!TextUtils.isEmpty(beanDetails.getDelivery_android())){//额外添加的产品详情图片
                imageLoader.displayImage(beanDetails.getDelivery_android(),delivery_img, options);
            }
            if(!TextUtils.isEmpty(beanDetails.getBack_certificate())){//额外添加的产品详情图片
                imageLoader.displayImage(beanDetails.getBack_certificate(),add_details, options);
            }
            if (beanDetails != null)
                removePic = beanDetails.getBig();
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.detailsImg, new String[]{"id"}, new String[]{beanDetails.getGoods_id() + ""}, handler, Constant.DETAILS_IMGS);
            initView_carsh();
        } else if (type == Constant.SHOP_TYPE_CREDIT) {//积分产品
            beanDetails = (Bean_GoodsList.DataEntity) intent.getSerializableExtra("details_info");//商品详情参数
            if(!TextUtils.isEmpty(beanDetails.getDelivery_detail())) {//邮递方式信息展示
                delivery_detail.setVisibility(View.VISIBLE);
                delivery_detail.setText(beanDetails.getDelivery_detail());
            }
            if(!TextUtils.isEmpty(beanDetails.getDelivery_android())){//额外添加的产品详情图片
                imageLoader.displayImage(beanDetails.getDelivery_android(),delivery_img, options);
            }
            if(!TextUtils.isEmpty(beanDetails.getBack_certificate())){//额外添加的产品详情图片
                imageLoader.displayImage(beanDetails.getBack_certificate(),add_details, options);
            }
            if (beanDetails != null)
                removePic = beanDetails.getBig();
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.detailsImg, new String[]{"id"}, new String[]{beanDetails.getGoods_id() + ""}, handler, Constant.DETAILS_IMGS);
            initViews_gredit();
        }
        if (type == Constant.SHOP_TYPE_AUTO) {//自动判断商品（优惠活动产品）
            String id = intent.getStringExtra("id");
            productId=intent.getStringExtra("id");
            //获取要去除的图片
            removePic = intent.getStringExtra("imageRemoved");
            LogUtils.e("wj", "id" + id);
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsDetails, new String[]{"id"}, new String[]{id}, handler, Constant.GOODS_DETAILS);
        }

        viewPager_shopdetails = (ViewPager) findViewById(R.id.viewPager_shopdetails);
        viewPager_shopdetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.size(); i++) {
                    dots.get(i).setBackgroundResource(R.drawable.dot_normal);
                }
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private String productId="";
    @Override
    protected void onRestart() {
        super.onRestart();
        if(restrictionShopping){//限购产品加入购物车后返回刷新
            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.goodsDetails, new String[]{"id"}, new String[]{productId}, handler, Constant.GOODS_DETAILS);
        }
    }

    /**
     * 不需要数据，原始的view
     */
    private void initPriView() {
        add_details= (ImageView) findViewById(R.id.add_details);
        delivery_img= (ImageView) findViewById(R.id.delivery_img);
        delivery_detail= (TextView) findViewById(R.id.delivery_detail);
        song_jifen = (LinearLayout) findViewById(R.id.song);
        song_jifen.setVisibility(View.GONE);
        shuliang = (LinearLayout) findViewById(R.id.shuliang);
        shuliang.setVisibility(View.GONE);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_shopdetails_price = (TextView) findViewById(R.id.tv_shopdetails_price);
        tv_shopdetails_freepostage = (TextView) findViewById(R.id.tv_shopdetails_freepostage);
        ib_shoppingcar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_details_redduce = (ImageButton) findViewById(R.id.ib_details_redduce);
        ib_details_add = (ImageButton) findViewById(R.id.ib_details_add);
        tv_details_num = (TextView) findViewById(R.id.tv_details_num);
        rl_shopdetails_choosecolor = (RelativeLayout) findViewById(R.id.rl_shopdetails_choosecolor);
        rl_shopdetails_parameter = (RelativeLayout) findViewById(R.id.rl_shopdetails_parameter);
        linear_shopdetails_share = (LinearLayout) findViewById(R.id.linear_shopdetails_share);
        linear_service = (LinearLayout) findViewById(R.id.linear_service);
        linear_collect = (CheckBox) findViewById(R.id.linear_collect);
        bt_addshopcars = (Button) findViewById(R.id.bt_addshopcars);
        bt_buy_immediately = (Button) findViewById(R.id.bt_buy_immediately);
        bt_buy_immediately.setOnClickListener(this);
        bt_addshopcars.setOnClickListener(this);
        linear_collect.setOnClickListener(this);
        linear_service.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_details_redduce.setOnClickListener(this);
        ib_details_add.setOnClickListener(this);
        rl_shopdetails_choosecolor.setOnClickListener(this);
        rl_shopdetails_parameter.setOnClickListener(this);
        linear_shopdetails_share.setOnClickListener(this);
        ib_shoppingcar.setOnClickListener(this);
    }

    /**
     * 现金支付
     */
    private void initView_carsh() {//现金
        if(!TextUtils.isEmpty(beanDetails.getMktprice())||!beanDetails.getMktprice().equals("0.00")){//判断是有华夏价的产品
            huaxiaProduct=true;
            bt_addshopcars.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        bt_buy_immediately.setText("立即购买");
        bt_addshopcars.setVisibility(View.VISIBLE);
        bt_addshopcars.setOnClickListener(this);
        tv_name.setText(beanDetails.getName());
        tv_shopdetails_price.setText("￥" + String.format("%.2f",beanDetails.getPrice()) + "");
        tv_shopdetails_freepostage.setVisibility(View.INVISIBLE);
        TextView tv_shopdetails_markprice = (TextView) findViewById(R.id.tv_shopdetails_markprice);
        tv_shopdetails_markprice.setText("市场价：￥" + beanDetails.getMktprice());
        TextView tv_shopdetails_linear_hadsold = (TextView) findViewById(R.id.tv_shopdetails_linear_hadsold);
        tv_shopdetails_linear_hadsold.setText("已售" + beanDetails.getBuy_count() + "件");
        TextView tv_shopdetails_linear_praise = (TextView) findViewById(R.id.tv_shopdetails_linear_praise);
        tv_shopdetails_linear_praise.setText(beanDetails.getView_count() + "");
        wv.loadDataWithBaseURL(null, formatImageURL(beanDetails.getIntro()), mimeType, encoding, "about:blank");
        invokeJSRefresh();
        tv_shopdetails_markprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 积分支付
     */
    private void initViews_gredit() {//积分
        bt_buy_immediately.setText("立即兑换");
        ib_shoppingcar.setVisibility(View.GONE);
        bt_addshopcars.setVisibility(View.GONE);
        tv_name.setText(beanDetails.getName());
        tv_shopdetails_price.setText("积分" + String.format("%.1f",beanDetails.getPrice()) + "");
        TextView tv_shopdetails_markprice = (TextView) findViewById(R.id.tv_shopdetails_markprice);
        tv_shopdetails_markprice.setVisibility(View.INVISIBLE);
        TextView tv_divider = (TextView) findViewById(R.id.tv_divider);
        tv_divider.setVisibility(View.GONE);
        TextView tv_shopdetails_linear_hadsold = (TextView) findViewById(R.id.tv_shopdetails_linear_hadsold);
        tv_shopdetails_linear_hadsold.setText("已售" + beanDetails.getBuy_count() + "件");
        TextView tv_shopdetails_linear_praise = (TextView) findViewById(R.id.tv_shopdetails_linear_praise);
        tv_shopdetails_linear_praise.setText(beanDetails.getView_count());
        wv.loadDataWithBaseURL(null, formatImageURL(beanDetails.getIntro()), mimeType, encoding, "about:blank");
        invokeJSRefresh();
        tv_shopdetails_markprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 图片显示完全
     *
     * @param info
     * @return
     */
    private String formatImageURL(String info) {
        String content = "";
        if (Build.VERSION.SDK_INT >= 19) {
            content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "    //JavaScript方法，刷新界面\n" +
                    "    function myFunction()\n" +
                    "    {\n" +
                    "       var imgs = document.getElementsByTagName('img');\n" +
                    "       for(var i = 0; i<imgs.length; i++)\n" +
                    "       {\n" +
                    "           imgs[i].style.width = '100%';\n" +
                    "           imgs[i].style.height = 'auto';\n" +
                    "       }\n" +
                    "    }\n" +
                    "</script>" +
                    "</head>\n" +
                    "<body>\n" + info + "</body>";
            return content;
        } else {
            return info;
        }

    }

    private void initViewFavourable() {//优惠活动

        song_jifen = (LinearLayout) findViewById(R.id.song);
        song_jifen.setVisibility(View.GONE);
        shuliang = (LinearLayout) findViewById(R.id.shuliang);
        shuliang.setVisibility(View.GONE);

        tv_shopdetails_price = (TextView) findViewById(R.id.tv_shopdetails_price);
        tv_shopdetails_freepostage = (TextView) findViewById(R.id.tv_shopdetails_freepostage);
        tv_shopdetails_freepostage.setVisibility(View.INVISIBLE);
        TextView tv_shopdetails_markprice = (TextView) findViewById(R.id.tv_shopdetails_markprice);

        ib_shoppingcar = (ImageButton) findViewById(R.id.ib_shoppingcar);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_details_redduce = (ImageButton) findViewById(R.id.ib_details_redduce);
        ib_details_add = (ImageButton) findViewById(R.id.ib_details_add);
        tv_details_num = (TextView) findViewById(R.id.tv_details_num);
        rl_shopdetails_choosecolor = (RelativeLayout) findViewById(R.id.rl_shopdetails_choosecolor);
        rl_shopdetails_parameter = (RelativeLayout) findViewById(R.id.rl_shopdetails_parameter);
        linear_shopdetails_share = (LinearLayout) findViewById(R.id.linear_shopdetails_share);
        linear_service = (LinearLayout) findViewById(R.id.linear_service);
        bt_addshopcars = (Button) findViewById(R.id.bt_addshopcars);
        bt_addshopcars.setVisibility(View.VISIBLE);
        bt_buy_immediately = (Button) findViewById(R.id.bt_buy_immediately);

        tv_shopdetails_markprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        ib_back.setOnClickListener(this);
        ib_shoppingcar.setOnClickListener(this);
    }

    private void invokeJSRefresh() {
        if (Build.VERSION.SDK_INT >= 19) {
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:myFunction()");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }

    private void initVipager(List<Bean_DetailsImgs.DataEntity> imgdata) {
        if (imgdata != null && imgdata.size() > 0) {
            beanDetails.setSmall(imgdata.get(0).getSmall());
            for (int i = 0; i < imgdata.size(); i++) {
                ImageView iv = new ImageView(this);
                imageLoader.displayImage(imgdata.get(i).getBig(), iv, options);
                iv_lists.add(iv);
                if (imgdata.size() > 1) {
                    View dotsView = LayoutInflater.from(appContext).inflate(R.layout.dots_view, null);
                    li_dotsList.addView(dotsView);
                    dots.add(dotsView);
                }
            }
            MyPagerAdapter adapter = new MyPagerAdapter();
            viewPager_shopdetails.setAdapter(adapter);
        }

    }


    private boolean restrictionShopping=false;//限购活动
    private boolean huaxiaProduct=false;//华夏价产品
    private static final int YINGLIAN_PAY=1004;
    private static final int HUAXIA_PAY=1006;
    private int  payType=YINGLIAN_PAY;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_shoppingcar://购物车
                click_which = SHOPCAR;
//                if(isGoodDetailsDone)
//                    ib_shoppingcar.setEnabled(false);
                if(BaseApplication.isLogin){
                    if (click_which == Add_SHOPCAR) {
                        String num = tv_details_num.getText().toString();
                        VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shopcarURL, new String[]{"productid", "num"}, new String[]{beanDetails.getGoods_id() + "", num}, handler, Constant.ADDTO_SHOPCAR);
                    } else if (click_which == SHOPCAR) {
                        Intent carIntent = new Intent(this, ShopCarActivity.class);
                        startActivity(carIntent);
                    } else if (click_which == BUY_NOW) {
                        //立即购买后
                        showPopwindow();
                    }
                }
                else{
                    Intent LoginIntent = new Intent(this, LoginActivity.class);
                    startActivity(LoginIntent);
                    if (click_which == BUY_NOW)
                        bt_buy_immediately.setEnabled(true);
                }
//                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);

                break;
            case R.id.ib_back://返回键
                finish();
                break;
            case R.id.linear_shopdetails_share://分享
                if (BaseApplication.isLogin) {
                    ShareUtil shareUtil = new ShareUtil(this);
                    shareUtil.openShare(Interalshop_detailsactivity.this.findViewById(R.id.start), this, beanDetails.getName(), beanDetails.getOriginal(), false, null, ShareUtil.GOODS, String.format("%d", beanDetails.getGoods_id()));
                } else {
                    ToastUtil.showToast(this, "请先登录");
                }
                break;
            case R.id.ib_details_redduce:
                    if (num > 1) {
                        num--;
                    }
                    tv_details_num.setText(num + "");
                break;
            case R.id.ib_details_add:
                    num++;
                    tv_details_num.setText(num + "");
                break;
            case R.id.rl_shopdetails_choosecolor://颜色分类
                showPopwindow();
                break;
            case R.id.rl_shopdetails_parameter://产品参数
                ToastUtil.showToast(Interalshop_detailsactivity.this, "无参数");
                break;
            case R.id.linear_service://客服
//                int checkPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//                if (checkPhonePermission != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.CALL_PHONE_REQUIRE);
//                } else {
                    callPhone();
//                }
                break;
            case R.id.linear_collect://收藏
                if (linear_collect.isChecked()) {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.collectGoods, new String[]{"goods_id"}, new String[]{beanDetails.getGoods_id() + ""}, handler, Constant.COLLECT_GOODS);
                } else {
                    VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.cancleCollectGoods, new String[]{"goods_id"}, new String[]{beanDetails.getGoods_id() + ""}, handler, Constant.CANCLE_COLLECT_GOODS);
                }
                break;
            //点击加入购物车和立即购买，都会出现popupwindow
            case R.id.bt_addshopcars://加入购物车
                if(!restrictionShopping&&!huaxiaProduct) {//一元限购或有华夏价的商品，不允许加入购物车
                    if (BaseApplication.isLogin) {
                        String num = tv_details_num.getText().toString();
                        if (beanDetails != null)
                            VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.shopcarURL, new String[]{"productid", "num"}, new String[]{beanDetails.getGoods_id() + "", num}, handler, Constant.ADDTO_SHOPCAR);
                    } else {
                        Intent LoginIntent = new Intent(this, LoginActivity.class);
                        startActivity(LoginIntent);
                    }
                }
//                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
//                click_which = Add_SHOPCAR;
                break;
            case R.id.bt_buy_immediately://立即购买
                bt_buy_immediately.setEnabled(false);
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.isLogin, null, null, handler, Constant.ISLOGIN);
                click_which = BUY_NOW;
                break;
        }
    }

    private void callPhone() {
        String tel = getString(R.string.service_phone);
        if (!"".equals(tel)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void setCreditTips(double credit) {
        if (credit <= 0) {//积分为0的时候
            popSureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(Interalshop_detailsactivity.this, "积分不足");
                    popupWindow.dismiss();
                }
            });
        } else {
            if (type == Constant.SHOP_TYPE_CREDIT) {
                if (beanDetails != null && Integer.parseInt(beanDetails.getStore()) > 1) {
                    count = Integer.parseInt(popCountEditText.getText().toString().trim());
                    if (count * beanDetails.getPrice() > credit) {
                        ToastUtil.showToast(this, "积分不足");
                        popupWindow.dismiss();
                    } else {
                        popSureButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (click_which == BUY_NOW) {
                                    //设置用户购买数量
                                    count = Integer.parseInt(popCountEditText.getText().toString().trim());
                                    beanDetails.setBuy_count(count + "");
                                    dataEntities = new ArrayList<Bean_GoodsList.DataEntity>();
                                    dataEntities.add(beanDetails);
                                    //立即购买的确认
                                    Intent intent = new Intent(Interalshop_detailsactivity.this, ConfirmOrderActivity.class);
                                    intent.putExtra("type", type);
                                    intent.putExtra("from", Constant.FROMDETIAL);
                                    intent.putExtra("dataEntities", (Serializable) dataEntities);
                                    intent.putExtra("num", String.valueOf(count));
                                    startActivity(intent);
                                    popupWindow.dismiss();
                                } else if (click_which == Add_SHOPCAR) {
                                    //加入购物车的确认
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.shopdetails_popwindow, null);
        ImageView popImageView = (ImageView) view.findViewById(R.id.pop_image);
        popPriceTV = (TextView) view.findViewById(R.id.pop_price);
        TextView popName = (TextView) view.findViewById(R.id.pop_name);
        TextView popStockTV = (TextView) view.findViewById(R.id.pop_stock);
        Button popDeleteButton = (Button) view.findViewById(R.id.bt_pop_delete);
        Button popAddBuBbtton = (Button) view.findViewById(R.id.bt_pop_add);
        popCountEditText = (EditText) view.findViewById(R.id.et_pop_count);
        popSureButton = (Button) view.findViewById(R.id.bt_pop_sure);
        ImageView popDismissImage = (ImageView) view.findViewById(R.id.pop_dismiss_iv);
        poptvMyCredit = (TextView) view.findViewById(R.id.pop_my_credit);
        ImageView yinlian_pay= (ImageView) view.findViewById(R.id.yinlian_pay);
        ImageView huaxia_pay= (ImageView) view.findViewById(R.id.huaxia_pay);
        final ImageView choose_yinlian= (ImageView)view. findViewById(R.id.choose_yinlian);
        final ImageView choose_huaxia= (ImageView) view.findViewById(R.id.choose_huaxia);
        yinlian_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_yinlian.setImageResource(R.mipmap.choose);
                choose_huaxia.setImageResource(R.mipmap.no_choose);
                payType=YINGLIAN_PAY;
                count = Integer.parseInt(popCountEditText.getText().toString().trim());
                popPriceTV.setText("￥" + String.format("%.2f",beanDetails.getPrice()*count) + "");//显示银联价
            }
        });
        huaxia_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_yinlian.setImageResource(R.mipmap.no_choose);
                choose_huaxia.setImageResource(R.mipmap.choose);
                payType=HUAXIA_PAY;
                count = Integer.parseInt(popCountEditText.getText().toString().trim());
                popPriceTV.setText("￥" +  Integer.valueOf(beanDetails.getMktprice())*count+"");//显示华夏价
//                ToastUtil.showToast(Interalshop_detailsactivity.this,"华夏支付开发中");
            }
        });
        if (beanDetails != null) {
            imageLoader.displayImage(beanDetails.getSmall(), popImageView, options);
            popName.setText(beanDetails.getName());
            if (type == Constant.SHOP_TYPE_MONEY) {
                popPriceTV.setText("￥" + String.format("%.2f",beanDetails.getPrice()) + "");
            } else {
                VolleyUtils.NetUtils(appContext.getRequestQueue(), Constant.baseUrl + Constant.getCredit, null, null, handler, Constant.GETCREDIT);
                popPriceTV.setText("共需：" + beanDetails.getPrice() + "积分");
                poptvMyCredit.setText("还剩：" + "积分");
                poptvMyCredit.setVisibility(View.VISIBLE);
            }
            popStockTV.setText("库存" + " " + beanDetails.getStore());
            //判断库存
            if (Integer.parseInt(beanDetails.getStore()) < 1) {
                popDeleteButton.setEnabled(false);
                popAddBuBbtton.setEnabled(false);
                popSureButton.setEnabled(false);
                ToastUtil.showToast(Interalshop_detailsactivity.this, "库存不足");
            } else {
                popDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = Integer.parseInt(popCountEditText.getText().toString().trim());
                        if (count <= 1) {
                            //数量为1的时候，点击减少数量按钮，不起作用
                            return;
                        } else {
                            //数量加1
                            popCountEditText.setText(--count + "");
                            if (type == Constant.SHOP_TYPE_MONEY) {
                                if(payType==HUAXIA_PAY){
                                    popPriceTV.setText("￥" + Double.parseDouble(beanDetails.getMktprice()) * count + "");
                                }else {
                                    popPriceTV.setText("￥" + String.format("%.2f", beanDetails.getPrice() * count) + "");
                                }
                            } else {
                                popPriceTV.setText(DoubleUtils.convert2String(beanDetails.getPrice() * count) + "积分");
                                setCreditTips(myCredit);
                            }
                        }
                    }
                });
                popAddBuBbtton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!restrictionShopping) {//一元限购不允许选择数量
                                count = Integer.parseInt(popCountEditText.getText().toString().trim());
                                if (1 <= count && count == Integer.parseInt(beanDetails.getStore())) {
                                    //用户选择的数量不能大于商品总得库存量
                                    ToastUtil.showToast(Interalshop_detailsactivity.this, "所选数量不能大于商品库存量");
                                    return;
                                } else {
                                    //数量加1
                                    popCountEditText.setText(++count + "");
                                    if (type == Constant.SHOP_TYPE_MONEY) {
                                        if(payType==HUAXIA_PAY){
                                            popPriceTV.setText("￥" +  Double.parseDouble(beanDetails.getMktprice()) * count + "");
                                        }else {
                                            popPriceTV.setText("￥" + String.format("%.2f", beanDetails.getPrice() * count) + "");
                                        }

                                    } else {
                                        popPriceTV.setText(DoubleUtils.convert2String(beanDetails.getPrice() * count) + "积分");
                                        setCreditTips(myCredit);
                                    }
                                }
                        }
                    }
                });
                //确定按钮
                popSureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (click_which == BUY_NOW) {
                            //设置用户购买数量
                            count = Integer.parseInt(popCountEditText.getText().toString().trim());
                            beanDetails.setBuy_count(count + "");
                            dataEntities = new ArrayList<Bean_GoodsList.DataEntity>();
                            dataEntities.add(beanDetails);
                            //立即购买的确认
                            Intent intent = new Intent(Interalshop_detailsactivity.this, ConfirmOrderActivity.class);
                            intent.putExtra("type", type);
                            intent.putExtra("from", Constant.FROMDETIAL);
                            intent.putExtra("dataEntities", (Serializable) dataEntities);
                            intent.putExtra("num", String.valueOf(count));
                            intent.putExtra("payType",payType);
                            startActivity(intent);
                            popupWindow.dismiss();
                        } else if (click_which == Add_SHOPCAR) {
                            //加入购物车的确认
                        }
                    }
                });
            }
        }
        popDismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭popupwindow
                popupWindow.dismiss();
            }
        });
        //popwindow界面初始化end

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //获取窗口高度
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(type == Constant.SHOP_TYPE_CREDIT){//积分产品
            View payType=view.findViewById(R.id.pop_pay_type);
            payType.setVisibility(View.GONE);
            popupWindow.setHeight(dm.heightPixels * 1/2);//窗口高度
        }else {
            popupWindow.setHeight(dm.heightPixels * 7 / 10);
        }
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(dw);
        //popwindow出现的时候，半透明
        backgroundAlpha(0.5f);
        // 在参照的View控件下方显示
        // window.showAsDropDown(MainActivity.this.findViewById(R.id.start));
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示

        popupWindow.showAtLocation(this.findViewById(R.id.start),
                Gravity.BOTTOM, 0, 0);
        // popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bt_buy_immediately.setEnabled(true);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return iv_lists.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(iv_lists.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(iv_lists.get(position));
            return iv_lists.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case Constant.REFRESH:
                ToastUtil.showToast(Interalshop_detailsactivity.this, "登录测试1111");
                break;

            default:
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Constant.CALL_PHONE_REQUIRE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    callPhone();
//                } else {
//                    ToastUtil.showToast(this, "打电话已取消");
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ib_shoppingcar != null)
            ib_shoppingcar.setEnabled(true);
    }
}
