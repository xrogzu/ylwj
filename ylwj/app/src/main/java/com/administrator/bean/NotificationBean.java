package com.administrator.bean;

/**
 * Created by Administrator on 2016/4/19.
 */
public class NotificationBean {

    /**
     * create_time : 1461033085000
     * title : 账号登陆异常
     * business_icon :
     * activity_id :
     * is_read : 0
     * push_id : 34876
     * description : 您的账号在别的设备登陆，如果不是本人操作建议您修改密码!
     */

    private String create_time;
    private String title;
    private String business_icon;
    private String activity_id;
    private String is_read;
    private String push_id;
    private String description;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusiness_icon() {
        return business_icon;
    }

    public void setBusiness_icon(String business_icon) {
        this.business_icon = business_icon;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
