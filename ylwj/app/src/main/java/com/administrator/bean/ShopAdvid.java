package com.administrator.bean;

import java.util.List;

/**
 * Created by acer on 2016/1/29.
 */
public class ShopAdvid {


    /**
     * result : 1
     * data : [{"acid":1001,"aid":1002,"aname":"头部长图片","attachment":"","atturl":"http://static.v5.javamall.com.cn/attachment/adv/1.jpg","atype":0,"begintime":1420041600,"clickcount":0,"cname":"","company":"","contact":"","disabled":"false","endtime":1420041600,"httpAttUrl":"http://static.v5.javamall.com.cn/attachment/adv/1.jpg","isclose":0,"linkman":"","url":"278"},{"acid":1001,"aid":1003,"aname":"下面三个小图片1","attachment":"","atturl":"http://static.v5.javamall.com.cn/attachment/adv/201512091708414759.jpg","atype":0,"begintime":1420041600,"clickcount":0,"cname":"","company":"","contact":"","disabled":"false","endtime":1420041600,"httpAttUrl":"http://static.v5.javamall.com.cn/attachment/adv/201512091708414759.jpg","isclose":0,"linkman":"","url":"278"},{"acid":1001,"aid":1004,"aname":"下面三个小图片2","attachment":"","atturl":"http://static.v5.javamall.com.cn/attachment/adv/201511240215017846.jpg","atype":0,"begintime":1420041600,"clickcount":0,"cname":"","company":"","contact":"","disabled":"false","endtime":1420041600,"httpAttUrl":"http://static.v5.javamall.com.cn/attachment/adv/201511240215017846.jpg","isclose":0,"linkman":"","url":"178"},{"acid":1001,"aid":1005,"aname":"下面三个小图片3","attachment":"","atturl":"http://static.v5.javamall.com.cn/attachment/adv/201512091708414759.jpg","atype":0,"begintime":1420041600,"clickcount":0,"cname":"","company":"","contact":"","disabled":"false","endtime":1420041600,"httpAttUrl":"http://static.v5.javamall.com.cn/attachment/adv/201512091708414759.jpg","isclose":0,"linkman":"","url":"278"}]
     */

    private int result;
    /**
     * acid : 1001
     * aid : 1002
     * aname : 头部长图片
     * attachment :
     * atturl : http://static.v5.javamall.com.cn/attachment/adv/1.jpg
     * atype : 0
     * begintime : 1420041600
     * clickcount : 0
     * cname :
     * company :
     * contact :
     * disabled : false
     * endtime : 1420041600
     * httpAttUrl : http://static.v5.javamall.com.cn/attachment/adv/1.jpg
     * isclose : 0
     * linkman :
     * url : 278
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
        private int acid;
        private int aid;
        private String aname;
        private String attachment;
        private String atturl;
        private int atype;
        private int begintime;
        private int clickcount;
        private String cname;
        private String company;
        private String contact;
        private String disabled;
        private int endtime;
        private String httpAttUrl;
        private int isclose;
        private String linkman;
        private String url;

        public void setAcid(int acid) {
            this.acid = acid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public void setAname(String aname) {
            this.aname = aname;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public void setAtturl(String atturl) {
            this.atturl = atturl;
        }

        public void setAtype(int atype) {
            this.atype = atype;
        }

        public void setBegintime(int begintime) {
            this.begintime = begintime;
        }

        public void setClickcount(int clickcount) {
            this.clickcount = clickcount;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }

        public void setEndtime(int endtime) {
            this.endtime = endtime;
        }

        public void setHttpAttUrl(String httpAttUrl) {
            this.httpAttUrl = httpAttUrl;
        }

        public void setIsclose(int isclose) {
            this.isclose = isclose;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getAcid() {
            return acid;
        }

        public int getAid() {
            return aid;
        }

        public String getAname() {
            return aname;
        }

        public String getAttachment() {
            return attachment;
        }

        public String getAtturl() {
            return atturl;
        }

        public int getAtype() {
            return atype;
        }

        public int getBegintime() {
            return begintime;
        }

        public int getClickcount() {
            return clickcount;
        }

        public String getCname() {
            return cname;
        }

        public String getCompany() {
            return company;
        }

        public String getContact() {
            return contact;
        }

        public String getDisabled() {
            return disabled;
        }

        public int getEndtime() {
            return endtime;
        }

        public String getHttpAttUrl() {
            return httpAttUrl;
        }

        public int getIsclose() {
            return isclose;
        }

        public String getLinkman() {
            return linkman;
        }

        public String getUrl() {
            return url;
        }
    }
}
