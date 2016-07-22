package com.administrator.bean;

/**
 * 静态常量数据，包括所有与服务器的接口，跳转等各种标志flag
 * Created by acer on 2016/1/7.
 */
public class Constant {
    public static final String SERVICEMAP = "https://m.hxb.com.cn/pwxweb/NearbyQry.do?from=singlemessage&isappinstalled=0";//服务地图\
    public static final String SHARE_URL = "http://www.shequchina.cn/hxb/client/app.do?";
    public static final String UPDATE_URL = "http://www.shequchina.cn/javamall/statics/attachment/apk/version.xml";
    public static final int IN_FINANCIAL_EXPERT = 1;//进入金融管家
    public static final int OUT_FINANCIAL = 2;//退出
    public static final int IN_INTEGRAL_SHOP = 3;//进入积分商城
    public static final int IN_COMMUNITY_STAGE = 5;//进入社区大舞台

    public static final int IN_DELICATE_LIFE = 7;//进入精致生活
    public static final int IN_REGIMEN = 8;//进入精致生活
    public static final int DRAFT_TYPE_NORMAL = 0;
    public static final int DRAFT_TYPE_VOTE = 1;
    public static final int DRAFT_TYPE_PUBLIC = 2;

    public static final String headDB = "db";

    //http://180.76.148.177:8080
//    public static final String baseUrl = "http://192.168.1.110:8088";//ip地址

//    public static final String baseUrltemp = "http://192.168.1.114:8088";


//    public static final String baseUrl = "http://180.76.148.177:8088";//ip地址

    public static final String baseUrl = "http://www.shequchina.cn/javamall";//ip地址
    //public static final String baseUrl = "http://192.168.1.114:8080/mall";//ip地址


    public static final String getAdImg = "/api/mobile/fin!getNonBannerList.do";//精致生活和金融常识的轮播

    public static final String advidUrl = "/api/mobile/adv!getOneAdv.do";//商城的广告
    public static final String getHeadLineInfo = "/api/titleApi!getTitleList.do";//获得最新数据
    public static final String shopcarURL = "/api/mobile/cart!add.do";//加一个商品到购物车
    public static final String getOrderDetail = "/api/mobile/order!detail.do";//获取订单详情
    public static final String getAllShopcar = "/api/mobile/cart!list.do";//获取购物车商品列表
    public static final String isLogin = "/api/mobile/member!isLogin.do";//判断是否已经登录
    public static final String deleOneGood = "/api/mobile/cart!delete.do";//删除购物车中的一项商品
    public static final String goodsPrice = "/api/mobile/cart!total.do";//计算过后商品总价
    public static final String cartClean = "/api/mobile/cart!clean.do";//清空购物车
    public static final String goodsList = "/api/mobile/goods!listByCat.do";//根据类型商城，积分兑换，优惠活动的列表
    public static final String goodListByTag = "/api/mobile/adv!getAdvList.do";//根据广告点击获取列表
    public static final String getThisWeekActivity = "/api/titleApi!getThisWeekActivity.do?";//获取本周活动
    public static final String getTwoWeekActivty = "/api/titleApi!getThisAndNextWeekActivity.do?";//获取最近两周活动
    public static final String getHistoryActivity = "/api/titleApi!getHistoryActivity.do?";
    public static final String goodsDetails = "/api/mobile/goods!detail.do";//获取商品详情
    public static final String detailsImg = "/api/mobile/goods!gallery.do";//获取商品相册
    public static final String big_all = "/api/stage/activityApi!listActivity.do?";//所有的活动列表
    public static final String getPushActivity = "/api/titleApi!getPushActivityList.do";
    public static final String getNoveltyByID = "/api/noveltyApi!getNoveltyById.do?";
    public static final String readHeadLine = "/api/titleApi!readTitle.do?";
    public static final String getPublishActivity = "/api/stage/activityApi!listPublishActivity.do";
    public static final String getMyNoPublishActivity = "/api/stage/activityApi!listMyNoPublishActivity.do";
    public static final String searchCommunity = "/api/communityApi!searchCommunity.do";
    public static final String listAttentionActivities = "/api/attentionApi!listAttentionActivities.do?";
    public static final String big_recommend = "/api/stage/activityApi!listRecommendActivity.do?";//推荐活动
    public static final String agreeRecommend = "/api/stage/activityApi!recommendActivity.do?";
    public static final String big_going = "/api/stage/activityApi! listActiveActivity.do";//活动中
    public static final String getNewAddedCreditInfo = "/api/titleApi!getAddCredit.do?";
    public static final String big_join = "/api/stage/activityApi!listApplyingActivity.do?";//参与活动
    public static final String getLastOrder = "/api/mobile/order!getNewOrder.do";
    public static final String big_choose = "/api/stage/activityApi!listElectActivity.do?";//评选活动
    public static final String cancelAttention = "/api/attentionApi!cancelAttentionActivity.do?";
    public static final String delPushActivity = "/api/titleApi!deletePushActivity.do";
    public static final String validCode = "/api/shop/member!valid.do";
    public static final String confirmRec = "/api/mobile/order!rogConfirm.do";
    public static final String big_pictures = "";//照片墙
    //    public static final String add_activity = "/api/stage/activityApi!addActivity.do?";//添加活动
    public static final String img_byId = "/api/stage/photoApi!getPhotoById.do?photo_id=";//通过编号查询id
    public static final String img_recommmend = "/api/stage/photoApi!listRecommendPhotos.do?";//列出被推荐的照片
    public static final String activity_attention = "/api/stage/activityApi!attentionActivity.do?";//关注活动
    public static final String login_url = "/api/mobile/member!login.do";//登录的url
    public static final String getActivity_id = "/api/stage/activityApi!addActivityStep.do?";//获取活动id
    public static final String editActivity = "/api/stage/activityApi!editActivity.do?";//编辑活动信息
    //public static final String listNoPublishedActivity = "/api/stage/activityApi!listNoPublishedActivity.do?";//列出所以未发布的活动
    public static final String newGetActivity_id = "/api/noveltyApi!addNovelty.do?";//获取新鲜事活动id
    public static final String getActivityInviteMembers = "/api/stage/activityApi!getActivityInviteMembers.do?";
    public static final String uploadImgs = "/api/base/upload-image.do";//上传图片
    public static final String getNormalApplayPeople = "/api/stage/activityApi!listActivityApplyMembers.do?";
    public static final String addImgs = "/api/stage/photoApi!addPhoto.do?";//添加图片
    public static final String newsAddImgs = "/api/noveltyApi!addNoveltyPhoto.do?";//新鲜事添加图片
    public static final String onekeyInvition = "/api/stage/activityApi!oneKeyInvite.do?";//一键邀请
    public static final String resetpwd = "/api/shop/member!forgetPassword.do";
    public static final String getFansList = "/api/attentionApi!getMyFansList.do";
    public static final String invite = "/api/stage/activityApi!inviteMember.do?";//邀请人参加活动
    public static final String newStageAddImgs = "/api/stage/photoApi!addPhoto.do?";//活动添加照片
    public static final String uploadActivity = "/api/stage/activityApi!releaseActivity.do?";//上传活动
    public static final String getActivity_list = "/api/stage/activityApi!getActivityList.do?";//获取
    public static final String getActivity_listMy = "/api/noveltyApi!listMyNovelties.do?";//获取我的新鲜事
    public static final String getActivity_SomeOne = "/api/noveltyApi!listMemberNovelties.do";//列出某用户的新鲜事
    public static final String getActivity_listAll = "/api/noveltyApi!listAllNovelties.do?";//获取
    public static final String getUserInfo = "/api/shop/member!getMember.do?";//获取用户信息
    public static final String getMemberLoginMsg = "/api/memberBindApi!getMemberLoginMsg.do?";//我关注的人数
    public static final String create_order_credit = "/api/shop/order!create.do?";//积分提交订单
    public static final String create_order_money = "/api/mobile/order!create.do?";//现金提交订单

    public static final String getPayType = "/api/mobile/order!paymentShipping.do";
    public static final String doPayment_point = "/api/shop/payment.do?";//积分支付
    public static final String getActivityByID = "/api/stage/activityApi!getActivityById.do?";
    public static final String getCommunityActivity = "/api/stage/activityApi!listCommunityActivity.do?";
    public static final String getApplyActivity = "/api/stage/activityApi!listMemberApplyActivity.do?";
    public static final String getMyPublishActivity = "/api/stage/activityApi!listOrganizerActivity.do?";
    public static final String getNoveltyComment = "/api/noveltyCommentApi!listAllComment.do?";
    public static final String addNoveltyComment = "/api/noveltyCommentApi!addNoveltyComment.do?";
    public static final String getMyAttentionGoods = "/api/mobile/favorite!list.do";
    public static final String getInviteActivity = "/api/stage/activityApi!myInviteActivity.do?";//
    public static final String getRankActivity = "/api/stage/activityApi!listRankActivity.do?";
    public static final String deleteAttentionGoods = "/api/mobile/favorite!delete.do";
    public static final String getRecAddress = "/api/mobile/address!list.do";
    public static final String shared2Nearby = "/api/noveltyApi!transpondActivity.do";
    public static final String getArea = "/api/base/region!getChildren.do";
    public static final String addRecAddress = "/api/mobile/address!add.do";
    public static final String delRecAddress = "/api/mobile/address!delete.do";
    public static final String setDefaultAddress = "/api/mobile/address!isdefaddr.do";
    public static final String editAddress = "/api/mobile/address!edit.do";
    public static final String goodsListJiFen = "/api/mobile/goods!listByCredit.do";//根据积分大小
    public static final String myMoneyWaitForPay = "/api/mobile/order!listOfCashNoPaid.do";//金钱代付款
    public static final String myMoneyPaid = "/api/mobile/order!listOfCashPaid.do";//金钱已付款
    public static final String myMoneyPayAll = "/api/mobile/order!listCash.do";//金钱全部
    public static final String myCreditPayed = "/api/mobile/order!listOfCreditPaid.do";//积分已付款
    public static final String delActivity = "/api/stage/activityApi!deleteActivity.do?";
    public static final String myCreditWaitForPay = "/api/mobile/order!listOfCreditNoPaid.do";//积分待付款
    public static final String getShopTypeList = "/api/shop/cat!listJson.do";
    public static final String getBindAuthCode = "/api/memberBindApi!sendCode.do?";//发送会员绑定验证码
    public static final String getNoveltyLikePeople = "/api/likeApi!listLikeNoveltyMembers.do?";
    public static final String getExpert = "/api/mobile/fin!getExpertList.do?";//金融专家
    public static final String getCommList = "/api/mobile/fin!getCommList.do?";
    public static final String getNonList = "/api/mobile/fin!getNonList.do?";//金融常识
    public static final String getProduct = "/api/mobile/fin!getProdList.do?";//精致生活list
    public static final String applyActivity = "/api/stage/activityApi!applyActivity.do";
    public static final String cancelPayOrder = "/api/mobile/order!cancel.do";
    public static final String getLikeNum = "api/likeApi!getNoveltyLikeNum.do?";
    public static final String transpondNovelty = "/api/noveltyApi!transpondNovelty.do";
    public static final String getRecommByID = "/api/mobile/fin!getCommById.do?";
    public static final String getProductByID = "/api/mobile/fin!getProductById.do?";//精致生活list->详情|热销产品List-》详情
    public static final String getNousByID = "/api/mobile/fin!getNousById.do?";
    public static final String myCreditPayAll = "/api/mobile/order!listCredit.do";//全部
    public static final String getMyAttention = "/api/attentionApi!getMemberAttentionNum.do?";
    public static final String saveUserInfo = "/api/shop/member!saveInfo.do";//保存用户信息
    public static final String getMyRegion = "/api/base/region!getChildren.do";//获取用户的区域信息
    public static final String getCredit = "/api/shop/credit!getCredit.do";//获取积分信息
    public static final String likeNovelty = "/api/likeApi!likeNovelty.do?";//对新鲜事点赞
    public static final String cancelLikeNovelty = "/api/likeApi!cancelLikeNovelty.do?";//取消对新鲜事点赞
    public static final String getLikeNoveltyCount = "/api/likeApi!getNoveltyLikeNum.do?";//获取对新鲜事点赞的数量

    public static final String getDefultAddress = "/api/mobile/address!defaultAddress.do";//获取用户默认地址
    public static final String deleteAttentionPerson = "/api/attentionApi!cancelAttentionMember.do?";//取消关注
    public static final String AttentionMessage = "/api/memberBindApi!getMemberById.do?";//


    public static final String register = "/api/shop/member!regMobile.do";//注册用户
    public static final String menberBinding = "/api/memberBindApi!memberBind.do?";//会员绑定
    public static final String getAuthcode = "/api/shop/member!sendCode.do";//获取验证码
    public static final String forgetpwd = "/api/shop/member!sendForgetCode.do";
    public static final String logOut = "/api/shop/member!logout.do";//退出登录
    public static final String collectGoods = "/api/mobile/collect!addCollect.do";//收藏商品
    public static final String cancleCollectGoods = "/api/mobile/collect!cancelCollect.do";//取消收藏商品
    public static final String CollectGoods_state = "/api/mobile/collect!getCollectStatus.do";//商品是否已收藏
    public static final String attentionActivity = "/api/attentionApi!attentionActivity.do?";//关注活动
    public static final String cancleAttentionActivity = "/api/attentionApi!cancelAttentionActivity.do?";//关注活动
    public static final String joinActivity = "/api/stage/activityApi!applyActivity.do";//参加活动
    public static final String old_activity = "/api/stage/activityApi! listHistoryActivity.do";//历届活动
    public static final String Attention = "/api/attentionApi!attentionMember.do?";//关注
    public static final String getIsAttention = "/api/attentionApi!getIsAttention.do";//是否关注
    public static final String MyAttentionPerson = "/api/attentionApi!listAttentionMembers.do?";//关注用户
    public static final String getAddress = "/api/communityApi!getCommunityById.do";
    public static final String activityVote = "/api/stage/activityApi!activityVote.do";
    public static final String getVoteResult = "/api/stage/activityApi!getVoteResult.do?";

    public static final String basenull = "";//空的ip地址

    public static final String IMAGE_CACHE_PATH = "imageloader/Cache";//图片缓存路径
    public static final String IMAGE_CACHE_PATHA = "imageloader/Cachea";//图片缓存路径
    public static final int TRYTO_LOGIN = 0x001;//登录标记
    public static final int ADDTO_SHOPCAR = 0x002;//加入购物车
    public static final int GET_ALLSHOPCAR = 0x003;//获取购物车商品列表
    public static final int ISLOGIN = 0x004;//判断是否已经登录
    public static final int DELETE_ONE = 0x005;//删除一个商品
    public static final int GoodsPrice = 0x006;//购物车商品总价
    public static final int CART_CLEAN = 0x007;//清空购物车
    public static final int GOODS_LIST = 0x008;//商城列表
    public static final int GOODS_DETAILS = 0x009;//商品详情
    public static final int DETAILS_IMGS = 0x010;//获取商品相册
    public static final int GOOD_LIST_TAG1 = 0x011;//tag商城列表

    public static final int RECOMMEND_BIG = 0x012;//推荐
    public static final int HISTORY_BIG = 0x013;//历届
    public static final int GOing_BIG = 0x014;//活动中
    public static final int JOIN_BIG = 0x015;//参与
    public static final int CHOOSE_BIG = 0x016;//评选
    public static final int ADD_ACTIVITY = 0x017;//添加活动
    public static final int IMG_BYID = 0x018;//通过id找图片
    public static final int MY_INVITE = 0x019;//我的邀请
    public static final int IMG_RECOMMEND = 0x020;//被推荐的照片
    public static final int GOOD_LIST_TAG2 = 0x021;//tag积分
    public static final int GOOD_LIST_TAG3 = 0x022;//tag商城
    public static final int GOOD_LIST_TAG4 = 0x023;//tag商城优惠活动
    public static final int ACTIVITY_ATTENTION = 0x024;//关注活动
    public static final int GET_ACTIVITY_ID = 0x025;//获取活动id
    public static final int UPLOAD_Imgs = 0x026;//上传图片
    public static final int ADD_IMGS = 0x027;//添加图片
    public static final int UPLOAD_ACTIVTY = 0x028;//上传活动
    public static final int GET_ACTIVITYLIST = 0x0029;//活动列表
    public static final int RegisterMember = 0x0030;//用户注册
    public static final int GETAUTHCODE = 0x0031;//获取验证码
    public static final int LOG_OUT = 0x0032;//退出登录
    public static final int COLLECT_GOODS = 0x0033;//收藏商品
    public static final int ATTENTION_ACTIVITY = 0x0034;//关注活动
    public static final int JOIN_ACTIVITY = 0x0035;//参加活动
    public static final int OLD_ACTIVITY = 0x0036;//历届活动
    public static final int GET_USERINFO = 0x0037;//获取用户信息
    public static final int GET_ORDERID = 0x0038;//获取订单id
    public static final int DO_PAYMENT = 0x0039;//支付操作
    public static final int MemberBinding = 0x0037;//会员绑定
    public static final int LESS_FIVE = 0x038;//<5
    public static final int FIVE_TEN = 0x039;//5-10
    public static final int TEN_FIF = 0x040;//10-15
    public static final int MORE_FIF = 0x041;//>15
    public static final int WAIT_PAY = 0x042;//代付款
    public static final int PATED = 0x043;//已付款
    public static final int ALL_PAY = 0x044;//全部消费
    public static final int ATTENTION = 0x045;//关注人
    public static final int ATTENTION_PERSON = 0x046;//
    public static final int IS_ATTENTION = 0x047;//
    public static final int NEW_ADDRESS = 0x048;//
    public static final int RELOGIN = 0x049;//
    public static final int ADD_PIC = 0x050;//
    public static final int ADD_PIC_NEXT = 0x051;//


    //acyivity跳转码
    public static final int MY_SETTING = 0x010;//跳转到我的设置
    public static final int MY_backlog = 0x011;//返回登录
    public static final int MY_backunlog = 0x012;//返回登出
    public static final int ADD_COMMENT = 1;
    public static final int SELECT_ADDRESS = 1;
    public static final int EDIT_MY_INFO = 1;

    public static final int LIKE_IN_COMMENT = 1;//在

    public static final int ADD_ADDRESS_REQUEST_CODE = 1;//添加地址请求码
    public static final int EDIT_ADDRESS_REQUEST_CODE = 2;//编辑地址请求码
    public static final int ADD_NOVELTY_SUCCESS_REQUEST_CODE = 3;//发布新鲜事成功
    public static final int ADD_COMMENT_SUCCESS_REQUEST_CODE = 4;

    //保存用户信息
    public static final int GETMYATTENTION = 0x058;//获取用户关注数量
    public static final int SAVEUSERINFO = 0x050;
    public static final int MYREGION = 0x051;//获取我的地址区域
    public static final int My_NOVELTIES = 0x052;//获取我的新鲜事

    //跳转到订单确认页面的类型
    public static final int FROMDETIAL = 0x053;//从订单详情页面跳转过来
    public static final int FROMWAITPAY = 0x054;//从待支付页面跳转过来
    public static final int GETCREDIT = 0x055;//从待支付页面跳转过来
    public static final int GETDEFULTADDRESS = 0x068;//获取用户的默认地址
    public static final int FROMSHOPCAR = 0x73; //从购物车里跳转过来

    public static final int CANCLE_COLLECT_GOODS = 0x0069;//取消收藏商品
    public static final int STATE_COLLECT_GOODS = 0x0070;//商品收藏状态
    public static final int SCAN = 0x0071;//商品收藏状态
    public static final int REFRESH = 0x0072;//商品收藏状态


    public static final int PERMISSION_LOCATION = 1;

    public static final int CANCLE_ATTENTION_ACTIVITY = 0x0080;//取消关注活动

    public static final int CALL_PHONE_REQUIRE = 0;//请求打电话权限

    public static final int CALL_CAMERA_REQUIRE = 1;//摄像头请求

    public static final int CALL_EXTRNAL_WRITE_REQUIRE = 2;//读写外部存储卡请求

    public static final int SHOP_TYPE_MONEY = 1;//现金详情

    public static final int SHOP_TYPE_CREDIT = 2;//积分详情

    public static final int SHOP_TYPE_AUTO = 12;//自动详情

    public static final int PAY_RESULT = 13;//银联付款结果

    //现金商品图片宽高比
    public static double radio = 776/360.0;

}
