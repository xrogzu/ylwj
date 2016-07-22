package com.administrator.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer on 2016/1/30.
 */
public class Bean_Shopcarlist {

    /**
     * result : 1
     * data : {"total":72,"count":1,"goodslist":[{"addon":"","catid":46,"coupPrice":36,"goods_id":123,"id":130,"image_default":"http://static.v4.javamall.com.cn/attachment/goods/201202231834296191_thumbnail.jpg","itemtype":0,"limitnum":0,"mktprice":42,"name":"ፘ਩๜ក ᴠጽၾἓ፲ᩂᛂ6੒/ፋ","num":2,"others":{},"pmtList":[],"point":0,"price":36,"product_id":123,"sn":"0000575144","specs":"","subtotal":72,"unit":"","weight":0.06}]}
     */

    private int result;
    /**
     * total : 72
     * count : 1
     * goodslist : [{"addon":"","catid":46,"coupPrice":36,"goods_id":123,"id":130,"image_default":"http://static.v4.javamall.com.cn/attachment/goods/201202231834296191_thumbnail.jpg","itemtype":0,"limitnum":0,"mktprice":42,"name":"ፘ਩๜ក ᴠጽၾἓ፲ᩂᛂ6੒/ፋ","num":2,"others":{},"pmtList":[],"point":0,"price":36,"product_id":123,"sn":"0000575144","specs":"","subtotal":72,"unit":"","weight":0.06}]
     */

    private DataEntity data;

    public void setResult(int result) {
        this.result = result;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String total;
        private int count;
        /**
         * addon :
         * catid : 46
         * coupPrice : 36
         * goods_id : 123
         * id : 130
         * image_default : http://static.v4.javamall.com.cn/attachment/goods/201202231834296191_thumbnail.jpg
         * itemtype : 0
         * limitnum : 0
         * mktprice : 42
         * name : ፘ਩๜ក ᴠጽၾἓ፲ᩂᛂ6੒/ፋ
         * num : 2
         * others : {}
         * pmtList : []
         * point : 0
         * price : 36
         * product_id : 123
         * sn : 0000575144
         * specs :
         * subtotal : 72
         * unit :
         * weight : 0.06
         */

        private List<GoodslistEntity> goodslist;


        public void setCount(int count) {
            this.count = count;
        }

        public void setGoodslist(List<GoodslistEntity> goodslist) {
            this.goodslist = goodslist;
        }


        public int getCount() {
            return count;
        }

        public List<GoodslistEntity> getGoodslist() {
            return goodslist;
        }

        public static class GoodslistEntity implements Serializable {
            private String addon;
            private String catid;
            private String coupPrice;
            private String goods_id;
            private String id;
            private String image_default;
            private String itemtype;
            private String limitnum;
            private String mktprice;
            private String name;
            private String num;
            private String point;
            private String price;
            private String product_id;
            private String sn;
            private String specs;
            private String subtotal;
            private String unit;
            private double weight;
            private List<?> pmtList;

            public void setAddon(String addon) {
                this.addon = addon;
            }


            public void setImage_default(String image_default) {
                this.image_default = image_default;
            }


            public void setName(String name) {
                this.name = name;
            }


            public void setSn(String sn) {
                this.sn = sn;
            }

            public void setSpecs(String specs) {
                this.specs = specs;
            }


            public void setUnit(String unit) {
                this.unit = unit;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public void setPmtList(List<?> pmtList) {
                this.pmtList = pmtList;
            }

            public String getAddon() {
                return addon;
            }


            public String getImage_default() {
                return image_default;
            }


            public String getName() {
                return name;
            }


            public String getCoupPrice() {
                return coupPrice;
            }

            public void setCoupPrice(String coupPrice) {
                this.coupPrice = coupPrice;
            }

            public String getLimitnum() {
                return limitnum;
            }

            public void setLimitnum(String limitnum) {
                this.limitnum = limitnum;
            }

            public String getMktprice() {
                return mktprice;
            }

            public void setMktprice(String mktprice) {
                this.mktprice = mktprice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }


            public String getSn() {
                return sn;
            }

            public String getSpecs() {
                return specs;
            }

            public String getCatid() {
                return catid;
            }

            public void setCatid(String catid) {
                this.catid = catid;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getItemtype() {
                return itemtype;
            }

            public void setItemtype(String itemtype) {
                this.itemtype = itemtype;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getPoint() {
                return point;
            }

            public void setPoint(String point) {
                this.point = point;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(String subtotal) {
                this.subtotal = subtotal;
            }

            public String getUnit() {
                return unit;
            }

            public double getWeight() {
                return weight;
            }

            public List<?> getPmtList() {
                return pmtList;
            }
        }
    }
}
