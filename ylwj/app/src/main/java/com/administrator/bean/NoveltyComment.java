package com.administrator.bean;

import java.util.List;

/**
 * Created by xu on 2016/2/29.
 */
public class NoveltyComment {


    /**
     * result : 1
     * data : [{"comment_content":"哈路","comment_id":"203","comment_time":"1458530140774","comment_user_face":"http://180.76.148.177:8088/javamall/statics/attachment/faceFile/201603061513504344.jpg","comment_user_id":"1000","comment_user_name":"wangtao","like_num":"0","novelty_id":"395"}]
     */

    private int result;
    /**
     * comment_content : 哈路
     * comment_id : 203
     * comment_time : 1458530140774
     * comment_user_face : http://180.76.148.177:8088/javamall/statics/attachment/faceFile/201603061513504344.jpg
     * comment_user_id : 1000
     * comment_user_name : wangtao
     * like_num : 0
     * novelty_id : 395
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
        private String comment_content;
        private String comment_id;
        private String comment_time;
        private String comment_user_face;
        private String comment_user_id;
        private String comment_user_name;
        private String like_num;
        private String novelty_id;

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public String getComment_user_face() {
            return comment_user_face;
        }

        public void setComment_user_face(String comment_user_face) {
            this.comment_user_face = comment_user_face;
        }

        public String getComment_user_id() {
            return comment_user_id;
        }

        public void setComment_user_id(String comment_user_id) {
            this.comment_user_id = comment_user_id;
        }

        public String getComment_user_name() {
            return comment_user_name;
        }

        public void setComment_user_name(String comment_user_name) {
            this.comment_user_name = comment_user_name;
        }

        public String getLike_num() {
            return like_num;
        }

        public void setLike_num(String like_num) {
            this.like_num = like_num;
        }

        public String getNovelty_id() {
            return novelty_id;
        }

        public void setNovelty_id(String novelty_id) {
            this.novelty_id = novelty_id;
        }
    }
}
