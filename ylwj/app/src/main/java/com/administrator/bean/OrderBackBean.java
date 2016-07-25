package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class OrderBackBean {

    /**
     * result : 1
     * order : {"address_id":131,"admin_remark":"","allocation_time":0,"cancel_reason":"","consumepoint":0,"create_time":1461808610,"depotid":4,"disabled":0,"discount":0,"fields":{},"gainedpoint":0,"goods":"","goods_amount":0,"goods_num":0,"isCod":false,"isOnlinePay":true,"is_protect":0,"itemList":[],"items_json":"","logi_id":0,"logi_name":"","memberAddress":{"addr":"","addr_id":131,"city":"","city_id":0,"country":"","def_addr":0,"isDel":0,"member_id":0,"mobile":"","name":"","province":"","province_id":0,"region":"","region_id":0,"remark":"","tel":"","zip":""},"member_id":47,"needPayMoney":0,"need_pay_money":0,"orderItemList":[],"orderStatus":"订单已生效","orderType":"s","order_amount":0,"order_id":240,"orderprice":{"discountItem":{},"discountPrice":0,"goodsPrice":0,"needPayMoney":0,"orderPrice":0,"point":0,"shippingPrice":0,"weight":0},"payStatus":"未付款","pay_status":0,"payment_id":1001,"payment_name":"中国银联支付","payment_type":"unPay","paymoney":0,"protect_price":0,"regionid":0,"remark":"remark","sale_cmpl":0,"sale_cmpl_time":0,"shipStatus":"未发货","ship_addr":"","ship_cityid":0,"ship_day":"任意时间","ship_email":"","ship_mobile":"","ship_name":"","ship_no":"","ship_provinceid":0,"ship_regionid":0,"ship_status":2,"ship_tel":"","ship_time":"任意时间","ship_zip":"","shipping_amount":0,"shipping_area":"null-null-null","shipping_id":1,"shipping_type":"圆通速递","signing_time":0,"sn":"146180861058","status":9,"the_sign":"","weight":0}
     */

    private int result;
    /**
     * address_id : 131
     * admin_remark :
     * allocation_time : 0
     * cancel_reason :
     * consumepoint : 0
     * create_time : 1461808610
     * depotid : 4
     * disabled : 0
     * discount : 0
     * fields : {}
     * gainedpoint : 0
     * goods :
     * goods_amount : 0
     * goods_num : 0
     * isCod : false
     * isOnlinePay : true
     * is_protect : 0
     * itemList : []
     * items_json :
     * logi_id : 0
     * logi_name :
     * memberAddress : {"addr":"","addr_id":131,"city":"","city_id":0,"country":"","def_addr":0,"isDel":0,"member_id":0,"mobile":"","name":"","province":"","province_id":0,"region":"","region_id":0,"remark":"","tel":"","zip":""}
     * member_id : 47
     * needPayMoney : 0
     * need_pay_money : 0
     * orderItemList : []
     * orderStatus : 订单已生效
     * orderType : s
     * order_amount : 0
     * order_id : 240
     * orderprice : {"discountItem":{},"discountPrice":0,"goodsPrice":0,"needPayMoney":0,"orderPrice":0,"point":0,"shippingPrice":0,"weight":0}
     * payStatus : 未付款
     * pay_status : 0
     * payment_id : 1001
     * payment_name : 中国银联支付
     * payment_type : unPay
     * paymoney : 0
     * protect_price : 0
     * regionid : 0
     * remark : remark
     * sale_cmpl : 0
     * sale_cmpl_time : 0
     * shipStatus : 未发货
     * ship_addr :
     * ship_cityid : 0
     * ship_day : 任意时间
     * ship_email :
     * ship_mobile :
     * ship_name :
     * ship_no :
     * ship_provinceid : 0
     * ship_regionid : 0
     * ship_status : 2
     * ship_tel :
     * ship_time : 任意时间
     * ship_zip :
     * shipping_amount : 0
     * shipping_area : null-null-null
     * shipping_id : 1
     * shipping_type : 圆通速递
     * signing_time : 0
     * sn : 146180861058
     * status : 9
     * the_sign :
     * weight : 0
     */

    private OrderEntity order;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public static class OrderEntity {
        private int address_id;
        private String admin_remark;
        private String allocation_time;
        private String cancel_reason;
        private int consumepoint;
        private String create_time;
        private int depotid;
        private int disabled;
        private int discount;
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
        /**
         * addr :
         * addr_id : 131
         * city :
         * city_id : 0
         * country :
         * def_addr : 0
         * isDel : 0
         * member_id : 0
         * mobile :
         * name :
         * province :
         * province_id : 0
         * region :
         * region_id : 0
         * remark :
         * tel :
         * zip :
         */

        private MemberAddressEntity memberAddress;
        private int member_id;
        private double needPayMoney;
        private double need_pay_money;
        private String orderStatus;
        private String orderType;
        private double order_amount;
        private String order_id;
        /**
         * discountItem : {}
         * discountPrice : 0
         * goodsPrice : 0
         * needPayMoney : 0
         * orderPrice : 0
         * point : 0
         * shippingPrice : 0
         * weight : 0
         */

        private OrderpriceEntity orderprice;
        private String payStatus;
        private int pay_status;
        private int payment_id;
        private String payment_name;
        private String payment_type;
        private int paymoney;
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
        private List<?> itemList;
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

        public String getAllocation_time() {
            return allocation_time;
        }

        public void setAllocation_time(String allocation_time) {
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
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

        public MemberAddressEntity getMemberAddress() {
            return memberAddress;
        }

        public void setMemberAddress(MemberAddressEntity memberAddress) {
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

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public OrderpriceEntity getOrderprice() {
            return orderprice;
        }

        public void setOrderprice(OrderpriceEntity orderprice) {
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

        public int getPaymoney() {
            return paymoney;
        }

        public void setPaymoney(int paymoney) {
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

        public List<?> getItemList() {
            return itemList;
        }

        public void setItemList(List<?> itemList) {
            this.itemList = itemList;
        }

        public List<?> getOrderItemList() {
            return orderItemList;
        }

        public void setOrderItemList(List<?> orderItemList) {
            this.orderItemList = orderItemList;
        }

        public static class MemberAddressEntity {
            private String addr;
            private int addr_id;
            private String city;
            private int city_id;
            private String country;
            private int def_addr;
            private int isDel;
            private int member_id;
            private String mobile;
            private String name;
            private String province;
            private int province_id;
            private String region;
            private int region_id;
            private String remark;
            private String tel;
            private String zip;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public int getAddr_id() {
                return addr_id;
            }

            public void setAddr_id(int addr_id) {
                this.addr_id = addr_id;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public int getDef_addr() {
                return def_addr;
            }

            public void setDef_addr(int def_addr) {
                this.def_addr = def_addr;
            }

            public int getIsDel() {
                return isDel;
            }

            public void setIsDel(int isDel) {
                this.isDel = isDel;
            }

            public int getMember_id() {
                return member_id;
            }

            public void setMember_id(int member_id) {
                this.member_id = member_id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public int getProvince_id() {
                return province_id;
            }

            public void setProvince_id(int province_id) {
                this.province_id = province_id;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public int getRegion_id() {
                return region_id;
            }

            public void setRegion_id(int region_id) {
                this.region_id = region_id;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }
        }

        public static class OrderpriceEntity {
            private double discountPrice;
            private double goodsPrice;
            private double needPayMoney;
            private double orderPrice;
            private double point;
            private double shippingPrice;
            private int weight;

            public double getDiscountPrice() {
                return discountPrice;
            }

            public void setDiscountPrice(double discountPrice) {
                this.discountPrice = discountPrice;
            }

            public double getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(double goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public double getNeedPayMoney() {
                return needPayMoney;
            }

            public void setNeedPayMoney(double needPayMoney) {
                this.needPayMoney = needPayMoney;
            }

            public double getOrderPrice() {
                return orderPrice;
            }

            public void setOrderPrice(double orderPrice) {
                this.orderPrice = orderPrice;
            }

            public double getPoint() {
                return point;
            }

            public void setPoint(double point) {
                this.point = point;
            }

            public double getShippingPrice() {
                return shippingPrice;
            }

            public void setShippingPrice(double shippingPrice) {
                this.shippingPrice = shippingPrice;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }
        }
    }
}
