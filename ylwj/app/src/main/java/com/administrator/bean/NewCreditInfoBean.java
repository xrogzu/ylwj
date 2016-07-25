package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class NewCreditInfoBean {

    /**
     * result : 1
     * data : [{"activity_id":"","business_icon":"","create_time":"","description":"credit","is_read":"1","open_type":"","push_id":"","title":"绑定成功，获得了22222211积分"}]
     */

    private int result;
    /**
     * activity_id :
     * business_icon :
     * create_time :
     * description : credit
     * is_read : 1
     * open_type :
     * push_id :
     * title : 绑定成功，获得了22222211积分
     */

    private List<DataEntity> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String activity_id;
        private String business_icon;
        private String create_time;
        private String description;
        private String is_read;
        private String open_type;
        private String push_id;
        private String title;

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getBusiness_icon() {
            return business_icon;
        }

        public void setBusiness_icon(String business_icon) {
            this.business_icon = business_icon;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getOpen_type() {
            return open_type;
        }

        public void setOpen_type(String open_type) {
            this.open_type = open_type;
        }

        public String getPush_id() {
            return push_id;
        }

        public void setPush_id(String push_id) {
            this.push_id = push_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
