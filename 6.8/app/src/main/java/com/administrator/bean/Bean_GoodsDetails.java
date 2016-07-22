package com.administrator.bean;

/**
 * Created by acer on 2016/1/30.
 */
public class Bean_GoodsDetails {

    /**
     * result : 1
     * data : {"comment_count":0,"enable_store":"0","sn":"0000554687","weight":"1200.0","store":0,"product_id":"159","comment_percent":"100%","cost":"260.0","goodsLvPrices":null,"specList":null,"thumbnail":"http://static.v4.javamall.com.cn/attachment/goods/201202232056058080_thumbnail.jpg","price":"269.0","favorited":false,"goods_id":"159","name":"美赞臣安婴儿A+婴儿配方奶粉1200克3袋装/盒（0-1岁）","specsvIdJson":"[]","specs":null}
     */

    private int result;
    /**
     * comment_count : 0
     * enable_store : 0
     * sn : 0000554687
     * weight : 1200.0
     * store : 0
     * product_id : 159
     * comment_percent : 100%
     * cost : 260.0
     * goodsLvPrices : null
     * specList : null
     * thumbnail : http://static.v4.javamall.com.cn/attachment/goods/201202232056058080_thumbnail.jpg
     * price : 269.0
     * favorited : false
     * goods_id : 159
     * name : 美赞臣安婴儿A+婴儿配方奶粉1200克3袋装/盒（0-1岁）
     * specsvIdJson : []
     * specs : null
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
        private int comment_count;
        private String enable_store;
        private String sn;
        private String weight;
        private int store;
        private String product_id;
        private String comment_percent;
        private String cost;
        private Object goodsLvPrices;
        private Object specList;
        private String thumbnail;
        private String price;
        private boolean favorited;
        private String goods_id;
        private String name;
        private String specsvIdJson;
        private Object specs;

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public void setEnable_store(String enable_store) {
            this.enable_store = enable_store;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public void setStore(int store) {
            this.store = store;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public void setComment_percent(String comment_percent) {
            this.comment_percent = comment_percent;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public void setGoodsLvPrices(Object goodsLvPrices) {
            this.goodsLvPrices = goodsLvPrices;
        }

        public void setSpecList(Object specList) {
            this.specList = specList;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSpecsvIdJson(String specsvIdJson) {
            this.specsvIdJson = specsvIdJson;
        }

        public void setSpecs(Object specs) {
            this.specs = specs;
        }

        public int getComment_count() {
            return comment_count;
        }

        public String getEnable_store() {
            return enable_store;
        }

        public String getSn() {
            return sn;
        }

        public String getWeight() {
            return weight;
        }

        public int getStore() {
            return store;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getComment_percent() {
            return comment_percent;
        }

        public String getCost() {
            return cost;
        }

        public Object getGoodsLvPrices() {
            return goodsLvPrices;
        }

        public Object getSpecList() {
            return specList;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getPrice() {
            return price;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public String getName() {
            return name;
        }

        public String getSpecsvIdJson() {
            return specsvIdJson;
        }

        public Object getSpecs() {
            return specs;
        }
    }
}
