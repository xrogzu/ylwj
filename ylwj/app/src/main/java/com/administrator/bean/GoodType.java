package com.administrator.bean;

import java.util.List;

/**
 * Created by xu on 2016/3/4.
 */
public class GoodType {

    /**
     * id : 1061
     * cat_order : 10
     * cat_path : 0|1001|1061|
     * children : []
     * goods_count : 0
     * hasChildren : false
     * image :
     * list_show : 1
     * text : 居家礼包
     * parent_id : 1001
     * state :
     * totle_num : 0
     * type_id : 9
     * type_text : 果汁
     */

    private int id;
    private int cat_order;
    private String cat_path;
    private int goods_count;
    private boolean hasChildren;
    private String image;
    private String list_show;
    private String text;
    private int parent_id;
    private String state;
    private int totle_num;
    private int type_id;
    private String type_text;
    private List<?> children;

    public void setId(int id) {
        this.id = id;
    }

    public void setCat_order(int cat_order) {
        this.cat_order = cat_order;
    }

    public void setCat_path(String cat_path) {
        this.cat_path = cat_path;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setList_show(String list_show) {
        this.list_show = list_show;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTotle_num(int totle_num) {
        this.totle_num = totle_num;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public int getCat_order() {
        return cat_order;
    }

    public String getCat_path() {
        return cat_path;
    }

    public int getGoods_count() {
        return goods_count;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public String getImage() {
        return image;
    }

    public String getList_show() {
        return list_show;
    }

    public String getText() {
        return text;
    }

    public int getParent_id() {
        return parent_id;
    }

    public String getState() {
        return state;
    }

    public int getTotle_num() {
        return totle_num;
    }

    public int getType_id() {
        return type_id;
    }

    public String getType_text() {
        return type_text;
    }

    public List<?> getChildren() {
        return children;
    }
}
