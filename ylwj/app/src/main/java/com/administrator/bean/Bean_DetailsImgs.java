package com.administrator.bean;

import java.util.List;

/**
 * Created by acer on 2016/1/30.
 */
public class Bean_DetailsImgs {

    /**
     * result : 1
     * data : [{"big":"http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_big.jpg","goods_id":78,"img_id":73,"isdefault":1,"original":"http://static.v4.javamall.com.cn/attachment/goods/201202222209292400.jpg","small":"http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_small.jpg","thumbnail":"http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_thumbnail.jpg","tiny":"http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_thumbnail.jpg"}]
     */

    private int result;
    /**
     * big : http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_big.jpg
     * goods_id : 78
     * img_id : 73
     * isdefault : 1
     * original : http://static.v4.javamall.com.cn/attachment/goods/201202222209292400.jpg
     * small : http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_small.jpg
     * thumbnail : http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_thumbnail.jpg
     * tiny : http://static.v4.javamall.com.cn/attachment/goods/201202222209292400_thumbnail.jpg
     */

    private List<DataEntity> data;

    public void setResult(int result) {
        this.result = result;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String big;
        private int goods_id;
        private int img_id;
        private int isdefault;
        private String original;
        private String small;
        private String thumbnail;
        private String tiny;

        public void setBig(String big) {
            this.big = big;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public void setImg_id(int img_id) {
            this.img_id = img_id;
        }

        public void setIsdefault(int isdefault) {
            this.isdefault = isdefault;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public void setTiny(String tiny) {
            this.tiny = tiny;
        }

        public String getBig() {
            return big;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public int getImg_id() {
            return img_id;
        }

        public int getIsdefault() {
            return isdefault;
        }

        public String getOriginal() {
            return original;
        }

        public String getSmall() {
            return small;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getTiny() {
            return tiny;
        }
    }
}
