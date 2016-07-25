package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public class BigstageUser {

    /**
     * result : 1
     * data : [{"attention_num":"0","attentionedNum":"0","community_id":"1","community_name":"","credit_num":"","face":"http://localhost:8088/statics/attachment/faceFile/201603140950378804.png","member_id":"1020","member_name":"ltz123","novelty_num":"20","tag":""},{"attention_num":"0","attentionedNum":"1","community_id":"1","community_name":"","credit_num":"","face":"","member_id":"2","member_name":"demowhj","novelty_num":"0","tag":""},{"attention_num":"0","attentionedNum":"0","community_id":"1","community_name":"","credit_num":"","face":"","member_id":"3","member_name":"asqsqsq","novelty_num":"0","tag":""}]
     */

    private int result;
    /**
     * attention_num : 0
     * attentionedNum : 0
     * community_id : 1
     * community_name :
     * credit_num :
     * face : http://localhost:8088/statics/attachment/faceFile/201603140950378804.png
     * member_id : 1020
     * member_name : ltz123
     * novelty_num : 20
     * tag :
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

        public String getNovelty_num() {
            return novelty_num;
        }

        public String getTag() {
            return tag;
        }
    }
}
