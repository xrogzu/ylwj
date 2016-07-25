package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/19.
 */
public class NormalPeopleInActivity {

    /**
     * result : 1
     * data : [{"apply_time":"1458376832535","attention_num":"","attentionedNum":"","community_id":"","community_name":"","credit_num":"","face":"http://localhost:8088/statics/attachment/faceFile/201603140950378804.png","member_id":"","member_name":"abcd","novelty_num":"","tag":""}]
     */

    private int result;
    /**
     * apply_time : 1458376832535
     * attention_num :
     * attentionedNum :
     * community_id :
     * community_name :
     * credit_num :
     * face : http://localhost:8088/statics/attachment/faceFile/201603140950378804.png
     * member_id :
     * member_name : abcd
     * novelty_num :
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
        private String member_id;
        private String member_name;
        private String novelty_num;
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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
