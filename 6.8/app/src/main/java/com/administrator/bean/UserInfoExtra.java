package com.administrator.bean;

/**
 * Created by Administrator on 2016/3/8.
 */
public class UserInfoExtra {

    /**
     * result : 1
     * data : {"attention_num":"2","attentionedNum":"3","community_id":"1","community_name":"华夏社区","credit_num":"252770.0","face":"http://180.76.148.177:8088/javamall/statics/attachment/faceFile/201603061513504344.jpg","member_id":"1000","member_name":"wangtao","novelty_num":"120","tag":""}
     */

    private int result;
    /**
     * attention_num : 2
     * attentionedNum : 3
     * community_id : 1
     * community_name : 华夏社区
     * credit_num : 252770.0
     * face : http://180.76.148.177:8088/javamall/statics/attachment/faceFile/201603061513504344.jpg
     * member_id : 1000
     * member_name : wangtao
     * novelty_num : 120
     * tag :
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
        private String attention_num;
        private String attentionedNum;
        private String community_id;
        private String community_name;
        private String credit_num;
        private String face;
        private String member_id;
        private String member_name;
        private String novelty_num;
        private String tag;
        private String is_attention;
        private String is_bind;
        private String city;
        private String province;
        private String sex;

        public void setAttention_num(String attention_num) {
            this.attention_num = attention_num;
        }

        public void setAttentionedNum(String attentionedNum) {
            this.attentionedNum = attentionedNum;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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

        public void setCredit_num(String credit_num) {
            this.credit_num = credit_num;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public void setNovelty_num(String novelty_num) {
            this.novelty_num = novelty_num;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getAttention_num() {
            return attention_num;
        }

        public String getAttentionedNum() {
            return attentionedNum;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public String getCredit_num() {
            return credit_num;
        }

        public String getFace() {
            return face;
        }

        public String getMember_id() {
            return member_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNovelty_num() {
            return novelty_num;
        }

        public String getTag() {
            return tag;
        }
    }
}
