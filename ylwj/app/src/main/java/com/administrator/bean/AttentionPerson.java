package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/12.
 */
public class AttentionPerson {

    /**
     * result : 1
     * data : [{"apply_time":"","attention_num":"4","attentionedNum":"6","community_id":"","community_name":"","credit_num":"","face":"http://121.42.189.35:8088/javamall/statics/attachment/faceFile/201603291111473570.jpg","is_attention":"","is_bind":"","member_id":"6","member_name":"晚自习","novelty_num":"","sex":"","tag":""}]
     */

    private int result;
    /**
     * apply_time :
     * attention_num : 4
     * attentionedNum : 6
     * community_id :
     * community_name :
     * credit_num :
     * face : http://121.42.189.35:8088/javamall/statics/attachment/faceFile/201603291111473570.jpg
     * is_attention :
     * is_bind :
     * member_id : 6
     * member_name : 晚自习
     * novelty_num :
     * sex :
     * tag :
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
        private String apply_time;
        private String attention_num;
        private String attentionedNum;
        private String community_id;
        private String community_name;
        private String credit_num;
        private String face;
        private String is_attention;
        private String is_bind;
        private String member_id;
        private String member_name;
        private String novelty_num;
        private String sex;
        private String tag;

        public String getApply_time() {
            return apply_time;
        }

        public void setApply_time(String apply_time) {
            this.apply_time = apply_time;
        }

        public String getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(String attention_num) {
            this.attention_num = attention_num;
        }

        public String getAttentionedNum() {
            return attentionedNum;
        }

        public void setAttentionedNum(String attentionedNum) {
            this.attentionedNum = attentionedNum;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
        }

        public String getCredit_num() {
            return credit_num;
        }

        public void setCredit_num(String credit_num) {
            this.credit_num = credit_num;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getIs_attention() {
            return is_attention;
        }

        public void setIs_attention(String is_attention) {
            this.is_attention = is_attention;
        }

        public String getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(String is_bind) {
            this.is_bind = is_bind;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getNovelty_num() {
            return novelty_num;
        }

        public void setNovelty_num(String novelty_num) {
            this.novelty_num = novelty_num;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
