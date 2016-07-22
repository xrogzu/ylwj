package com.administrator.bean;

/**
 * Created by acer on 2015/12/25.
 */
public class RecommentList {
    private String contenturl;//点击详情
    private String id;//id
    private String imageurl;//图片
    private String readedquantity;//阅读数量
    private String source;//来源
    private String title;//标题

    /**
     * @param contenturl     点击详情
     * @param id             id
     * @param imageurl       图片
     * @param readedquantity 阅读数量
     * @param source         来源
     * @param title          标题
     */
    public RecommentList(String contenturl, String id, String imageurl, String readedquantity, String source, String title) {
        this.contenturl = contenturl;
        this.id = id;
        this.imageurl = imageurl;
        this.readedquantity = readedquantity;
        this.source = source;
        this.title = title;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getReadedquantity() {
        return readedquantity;
    }

    public void setReadedquantity(String readedquantity) {
        this.readedquantity = readedquantity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
