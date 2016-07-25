package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class LastOrderBean {

    /**
     * result : 1
     * data : {"address_id":1000,"admin_remark":"","allocation_time":0,"cancel_reason":"","consumepoint":0,"create_time":1459932590,"depotid":4,"disabled":0,"discount":0,"fields":{},"gainedpoint":0,"goods":"","goods_amount":17.6,"goods_num":0,"isCod":false,"isOnlinePay":true,"is_protect":0,"itemList":[{"addon":"","cat_id":1040,"change_goods_id":0,"change_goods_name":"","depotId":"","gainedpoint":0,"goods_id":327,"image":"http://121.42.189.35:8088/javamall/statics/attachment/goods/201603311741516860_thumbnail.jpg","item_id":108,"name":"威猛先生 柠檬草香 洁厕液  600g*2瓶 17600积分！","num":1,"order_id":108,"other":"","price":17.6,"product_id":431,"ship_num":0,"sn":"JF14","state":0,"store":0,"unit":""}],"items_json":"[{\"addon\":\" \",\"cat_id\":1040,\"change_goods_id\":0,\"change_goods_name\":\"\",\"depotId\":\"\",\"gainedpoint\":0,\"goods_id\":327,\"image\":\"http://121.42.189.35:8088/javamall/statics/attachment/goods/201603311741516860_thumbnail.jpg\",\"item_id\":108,\"name\":\"威猛先生 柠檬草香 洁厕液  600g*2瓶 17600积分！\",\"num\":1,\"order_id\":108,\"other\":\"\",\"price\":17.6,\"product_id\":431,\"ship_num\":0,\"sn\":\"JF14\",\"state\":0,\"store\":0,\"unit\":\"\"}]","logi_id":1,"logi_name":"\n\t\t\t\t\t\t\t\t\t圆通速递\n\t\t\t\t\t\t\t\t","memberAddress":null,"member_id":3,"needPayMoney":17.6,"need_pay_money":17.6,"orderItemList":[],"orderStatus":"已发货","orderType":"s","order_amount":17.6,"order_id":108,"orderprice":null,"payStatus":"已确认付款","pay_status":2,"payment_id":1000,"payment_name":"积分支付","payment_type":"creditPlugin","paymoney":17.6,"protect_price":0,"regionid":0,"remark":"","sale_cmpl":0,"sale_cmpl_time":1460000301,"shipStatus":"已发货","ship_addr":"山东省青岛市市南区咳咳 ","ship_cityid":33,"ship_day":"任意时间","ship_email":"","ship_mobile":"13112345678","ship_name":"测试","ship_no":"1","ship_provinceid":1,"ship_regionid":451,"ship_status":3,"ship_tel":"13112345678","ship_time":"","ship_zip":"266603","shipping_amount":0,"shipping_area":"山东-青岛-青岛","shipping_id":1,"shipping_type":"圆通速递","signing_time":0,"sn":"145993259020","status":5,"the_sign":"","weight":0}
     */

    private int result;
    /**
     * address_id : 1000
     * admin_remark :
     * allocation_time : 0
     * cancel_reason :
     * consumepoint : 0
     * create_time : 1459932590
     * depotid : 4
     * disabled : 0
     * discount : 0
     * fields : {}
     * gainedpoint : 0
     * goods :
     * goods_amount : 17.6
     * goods_num : 0
     * isCod : false
     * isOnlinePay : true
     * is_protect : 0
     * itemList : [{"addon":"","cat_id":1040,"change_goods_id":0,"change_goods_name":"","depotId":"","gainedpoint":0,"goods_id":327,"image":"http://121.42.189.35:8088/javamall/statics/attachment/goods/201603311741516860_thumbnail.jpg","item_id":108,"name":"威猛先生 柠檬草香 洁厕液  600g*2瓶 17600积分！","num":1,"order_id":108,"other":"","price":17.6,"product_id":431,"ship_num":0,"sn":"JF14","state":0,"store":0,"unit":""}]
     * items_json : [{"addon":" ","cat_id":1040,"change_goods_id":0,"change_goods_name":"","depotId":"","gainedpoint":0,"goods_id":327,"image":"http://121.42.189.35:8088/javamall/statics/attachment/goods/201603311741516860_thumbnail.jpg","item_id":108,"name":"威猛先生 柠檬草香 洁厕液  600g*2瓶 17600积分！","num":1,"order_id":108,"other":"","price":17.6,"product_id":431,"ship_num":0,"sn":"JF14","state":0,"store":0,"unit":""}]
     * logi_id : 1
     * logi_name :
     圆通速递

     * memberAddress : null
     * member_id : 3
     * needPayMoney : 17.6
     * need_pay_money : 17.6
     * orderItemList : []
     * orderStatus : 已发货
     * orderType : s
     * order_amount : 17.6
     * order_id : 108
     * orderprice : null
     * payStatus : 已确认付款
     * pay_status : 2
     * payment_id : 1000
     * payment_name : 积分支付
     * payment_type : creditPlugin
     * paymoney : 17.6
     * protect_price : 0
     * regionid : 0
     * remark :
     * sale_cmpl : 0
     * sale_cmpl_time : 1460000301
     * shipStatus : 已发货
     * ship_addr : 山东省青岛市市南区咳咳
     * ship_cityid : 33
     * ship_day : 任意时间
     * ship_email :
     * ship_mobile : 13112345678
     * ship_name : 测试
     * ship_no : 1
     * ship_provinceid : 1
     * ship_regionid : 451
     * ship_status : 3
     * ship_tel : 13112345678
     * ship_time :
     * ship_zip : 266603
     * shipping_amount : 0
     * shipping_area : 山东-青岛-青岛
     * shipping_id : 1
     * shipping_type : 圆通速递
     * signing_time : 0
     * sn : 145993259020
     * status : 5
     * the_sign :
     * weight : 0
     */

    private DataEntity data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int address_id;
        private String admin_remark;
        private int allocation_time;
        private String cancel_reason;
        private int consumepoint;
        private int create_time;
        private int depotid;
        private int disabled;
        private int discount;
        private FieldsEntity fields;
        private int gainedpoint;
        private String goods;
        private double goods_amount;
        private int goods_num;
        private boolean isCod;
        private boolean isOnlinePay;
        private int is_protect;
        private String items_json;
        private int logi_id;
        private String logi_name;
        private Object memberAddress;
        private int member_id;
        private double needPayMoney;
        private double need_pay_money;
        private String orderStatus;
        private String orderType;
        private double order_amount;
        private int order_id;
        private Object orderprice;
        private String payStatus;
        private int pay_status;
        private int payment_id;
        private String payment_name;
        private String payment_type;
        private double paymoney;
        private int protect_price;
        private int regionid;
        private String remark;
        private int sale_cmpl;
        private int sale_cmpl_time;
        private String shipStatus;
        private String ship_addr;
        private int ship_cityid;
        private String ship_day;
        private String ship_email;
        private String ship_mobile;
        private String ship_name;
        private String ship_no;
        private int ship_provinceid;
        private int ship_regionid;
        private int ship_status;
        private String ship_tel;
        private String ship_time;
        private String ship_zip;
        private int shipping_amount;
        private String shipping_area;
        private int shipping_id;
        private String shipping_type;
        private int signing_time;
        private String sn;
        private int status;
        private String the_sign;
        private int weight;
        /**
         * addon :
         * cat_id : 1040
         * change_goods_id : 0
         * change_goods_name :
         * depotId :
         * gainedpoint : 0
         * goods_id : 327
         * image : http://121.42.189.35:8088/javamall/statics/attachment/goods/201603311741516860_thumbnail.jpg
         * item_id : 108
         * name : 威猛先生 柠檬草香 洁厕液  600g*2瓶 17600积分！
         * num : 1
         * order_id : 108
         * other :
         * price : 17.6
         * product_id : 431
         * ship_num : 0
         * sn : JF14
         * state : 0
         * store : 0
         * unit :
         */

        private List<ItemListEntity> itemList;
        private List<?> orderItemList;

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getAdmin_remark() {
            return admin_remark;
        }

        public void setAdmin_remark(String admin_remark) {
            this.admin_remark = admin_remark;
        }

        public int getAllocation_time() {
            return allocation_time;
        }

        public void setAllocation_time(int allocation_time) {
            this.allocation_time = allocation_time;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }

        public int getConsumepoint() {
            return consumepoint;
        }

        public void setConsumepoint(int consumepoint) {
            this.consumepoint = consumepoint;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getDepotid() {
            return depotid;
        }

        public void setDepotid(int depotid) {
            this.depotid = depotid;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public FieldsEntity getFields() {
            return fields;
        }

        public void setFields(FieldsEntity fields) {
            this.fields = fields;
        }

        public int getGainedpoint() {
            return gainedpoint;
        }

        public void setGainedpoint(int gainedpoint) {
            this.gainedpoint = gainedpoint;
        }

        public String getGoods() {
            return goods;
        }

        public void setGoods(String goods) {
            this.goods = goods;
        }

        public double getGoods_amount() {
            return goods_amount;
        }

        public void setGoods_amount(double goods_amount) {
            this.goods_amount = goods_amount;
        }

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public boolean isIsCod() {
            return isCod;
        }

        public void setIsCod(boolean isCod) {
            this.isCod = isCod;
        }

        public boolean isIsOnlinePay() {
            return isOnlinePay;
        }

        public void setIsOnlinePay(boolean isOnlinePay) {
            this.isOnlinePay = isOnlinePay;
        }

        public int getIs_protect() {
            return is_protect;
        }

        public void setIs_protect(int is_protect) {
            this.is_protect = is_protect;
        }

        public String getItems_json() {
            return items_json;
        }

        public void setItems_json(String items_json) {
            this.items_json = items_json;
        }

        public int getLogi_id() {
            return logi_id;
        }

        public void setLogi_id(int logi_id) {
            this.logi_id = logi_id;
        }

        public String getLogi_name() {
            return logi_name;
        }

        public void setLogi_name(String logi_name) {
            this.logi_name = logi_name;
        }

        public Object getMemberAddress() {
            return memberAddress;
        }

        public void setMemberAddress(Object memberAddress) {
            this.memberAddress = memberAddress;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public double getNeedPayMoney() {
            return needPayMoney;
        }

        public void setNeedPayMoney(double needPayMoney) {
            this.needPayMoney = needPayMoney;
        }

        public double getNeed_pay_money() {
            return need_pay_money;
        }

        public void setNeed_pay_money(double need_pay_money) {
            this.need_pay_money = need_pay_money;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public double getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(double order_amount) {
            this.order_amount = order_amount;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public Object getOrderprice() {
            return orderprice;
        }

        public void setOrderprice(Object orderprice) {
            this.orderprice = orderprice;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public int getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(int payment_id) {
            this.payment_id = payment_id;
        }

        public String getPayment_name() {
            return payment_name;
        }

        public void setPayment_name(String payment_name) {
            this.payment_name = payment_name;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public double getPaymoney() {
            return paymoney;
        }

        public void setPaymoney(double paymoney) {
            this.paymoney = paymoney;
        }

        public int getProtect_price() {
            return protect_price;
        }

        public void setProtect_price(int protect_price) {
            this.protect_price = protect_price;
        }

        public int getRegionid() {
            return regionid;
        }

        public void setRegionid(int regionid) {
            this.regionid = regionid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getSale_cmpl() {
            return sale_cmpl;
        }

        public void setSale_cmpl(int sale_cmpl) {
            this.sale_cmpl = sale_cmpl;
        }

        public int getSale_cmpl_time() {
            return sale_cmpl_time;
        }

        public void setSale_cmpl_time(int sale_cmpl_time) {
            this.sale_cmpl_time = sale_cmpl_time;
        }

        public String getShipStatus() {
            return shipStatus;
        }

        public void setShipStatus(String shipStatus) {
            this.shipStatus = shipStatus;
        }

        public String getShip_addr() {
            return ship_addr;
        }

        public void setShip_addr(String ship_addr) {
            this.ship_addr = ship_addr;
        }

        public int getShip_cityid() {
            return ship_cityid;
        }

        public void setShip_cityid(int ship_cityid) {
            this.ship_cityid = ship_cityid;
        }

        public String getShip_day() {
            return ship_day;
        }

        public void setShip_day(String ship_day) {
            this.ship_day = ship_day;
        }

        public String getShip_email() {
            return ship_email;
        }

        public void setShip_email(String ship_email) {
            this.ship_email = ship_email;
        }

        public String getShip_mobile() {
            return ship_mobile;
        }

        public void setShip_mobile(String ship_mobile) {
            this.ship_mobile = ship_mobile;
        }

        public String getShip_name() {
            return ship_name;
        }

        public void setShip_name(String ship_name) {
            this.ship_name = ship_name;
        }

        public String getShip_no() {
            return ship_no;
        }

        public void setShip_no(String ship_no) {
            this.ship_no = ship_no;
        }

        public int getShip_provinceid() {
            return ship_provinceid;
        }

        public void setShip_provinceid(int ship_provinceid) {
            this.ship_provinceid = ship_provinceid;
        }

        public int getShip_regionid() {
            return ship_regionid;
        }

        public void setShip_regionid(int ship_regionid) {
            this.ship_regionid = ship_regionid;
        }

        public int getShip_status() {
            return ship_status;
        }

        public void setShip_status(int ship_status) {
            this.ship_status = ship_status;
        }

        public String getShip_tel() {
            return ship_tel;
        }

        public void setShip_tel(String ship_tel) {
            this.ship_tel = ship_tel;
        }

        public String getShip_time() {
            return ship_time;
        }

        public void setShip_time(String ship_time) {
            this.ship_time = ship_time;
        }

        public String getShip_zip() {
            return ship_zip;
        }

        public void setShip_zip(String ship_zip) {
            this.ship_zip = ship_zip;
        }

        public int getShipping_amount() {
            return shipping_amount;
        }

        public void setShipping_amount(int shipping_amount) {
            this.shipping_amount = shipping_amount;
        }

        public String getShipping_area() {
            return shipping_area;
        }

        public void setShipping_area(String shipping_area) {
            this.shipping_area = shipping_area;
        }

        public int getShipping_id() {
            return shipping_id;
        }

        public void setShipping_id(int shipping_id) {
            this.shipping_id = shipping_id;
        }

        public String getShipping_type() {
            return shipping_type;
        }

        public void setShipping_type(String shipping_type) {
            this.shipping_type = shipping_type;
        }

        public int getSigning_time() {
            return signing_time;
        }

        public void setSigning_time(int signing_time) {
            this.signing_time = signing_time;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getThe_sign() {
            return the_sign;
        }

        public void setThe_sign(String the_sign) {
            this.the_sign = the_sign;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<ItemListEntity> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListEntity> itemList) {
            this.itemList = itemList;
        }

        public List<?> getOrderItemList() {
            return orderItemList;
        }

        public void setOrderItemList(List<?> orderItemList) {
            this.orderItemList = orderItemList;
        }

        public static class FieldsEntity {
        }

        public static class ItemListEntity {
            private String addon;
            private int cat_id;
            private int change_goods_id;
            private String change_goods_name;
            private String depotId;
            private int gainedpoint;
            private int goods_id;
            private String image;
            private int item_id;
            private String name;
            private int num;
            private int order_id;
            private String other;
            private double price;
            private int product_id;
            private int ship_num;
            private String sn;
            private int state;
            private int store;
            private String unit;

            public String getAddon() {
                return addon;
            }

            public void setAddon(String addon) {
                this.addon = addon;
            }

            public int getCat_id() {
                return cat_id;
            }

            public void setCat_id(int cat_id) {
                this.cat_id = cat_id;
            }

            public int getChange_goods_id() {
                return change_goods_id;
            }

            public void setChange_goods_id(int change_goods_id) {
                this.change_goods_id = change_goods_id;
            }

            public String getChange_goods_name() {
                return change_goods_name;
            }

            public void setChange_goods_name(String change_goods_name) {
                this.change_goods_name = change_goods_name;
            }

            public String getDepotId() {
                return depotId;
            }

            public void setDepotId(String depotId) {
                this.depotId = depotId;
            }

            public int getGainedpoint() {
                return gainedpoint;
            }

            public void setGainedpoint(int gainedpoint) {
                this.gainedpoint = gainedpoint;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getOrder_id() {
                return order_id;
            }

            public void setOrder_id(int order_id) {
                this.order_id = order_id;
            }

            public String getOther() {
                return other;
            }

            public void setOther(String other) {
                this.other = other;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getShip_num() {
                return ship_num;
            }

            public void setShip_num(int ship_num) {
                this.ship_num = ship_num;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getStore() {
                return store;
            }

            public void setStore(int store) {
                this.store = store;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
    }
}
