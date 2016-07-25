package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class HeadLineBean {

    /**
     * result : 1
     * data : [{"activity_id":"22","create_time":"2016-04-06 14:48:39.0","description":"nous","push_id":"32935","title":"测试"},{"activity_id":"22","create_time":"2016-04-06 14:48:39.0","description":"nous","push_id":"32936","title":"测试"},{"activity_id":"28","create_time":"2016-04-06 14:47:44.0","description":"recommend","push_id":"32933","title":"测试"},{"activity_id":"28","create_time":"2016-04-06 14:47:44.0","description":"recommend","push_id":"32934","title":"测试"}]
     */

    private int result;
    /**
     * activity_id : 22
     * create_time : 2016-04-06 14:48:39.0
     * description : nous
     * push_id : 32935
     * title : 测试
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
        private String business_icon;
        private String activity_id;
        private String create_time;
        private String description;
        private String push_id;
        private String title;
        private String is_read;

        public String getActivity_id() {
            return activity_id;
        }

        public String getBusiness_icon() {
            return business_icon;
        }

        public void setBusiness_icon(String business_icon) {
            this.business_icon = business_icon;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
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

        public String getPush_id() {
            return push_id;
        }

        public void setPush_id(String push_id) {
            this.push_id = push_id;
        }

        public String getTitle() {
            return title;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
