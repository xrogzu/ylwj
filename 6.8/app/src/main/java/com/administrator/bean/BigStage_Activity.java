

package com.administrator.bean;

import java.io.Serializable;
import java.util.List;


/**
 * @author lin
 * @version V1.0
 * @Description: 社区大舞台-活动model
 * @date 2016年1月18日 上午10:27:22
 */
public class BigStage_Activity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String activity_id;//活动编号
    private String title;//活动标题
    private String site;//活动地点
    private String introduction;//活动介绍
    private String tag;//活动标签
    private String community_name;//所属社区
    private String apply_start_time;//报名开始时间
    private String apply_end_time;//报名截止时间
    private String activity_start_time;//活动开始时间
    private String activity_end_time;//活动结束时间
    private String[] photoIds;//活动图片编号数组
    private String organizer_id;//活动组织者
    private int participants_num;//参与人数
    private int max_participants_num;//最大参与人数
    private int attention_num;//关注量
    private float score;//活动得分，所有人打分的平均值
    private int score_num;//打分人数
    private int total_score;//打分总数
    private String activity_type;//活动类型
    private String del_sign;//删除标识
    private int is_released;//是否发布
    private int is_end;//是否已经结束
    private int is_recommend;//是否已推荐
    private String recommend_ageing;//推荐时效
    private String recommend_time;//推荐时间
    private List<User> userList;//参加活动用户list
    private String options;//投票活动选项
    private String[]optionsArray;//投票活动选项数组

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String[] getOptionsArray() {
        return optionsArray;
    }

    public void setOptionsArray(String[] optionsArray) {
        this.optionsArray = optionsArray;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getApply_start_time() {
        return apply_start_time;
    }

    public void setApply_start_time(String apply_start_time) {
        this.apply_start_time = apply_start_time;
    }

    public String getApply_end_time() {
        return apply_end_time;
    }

    public void setApply_end_time(String apply_end_time) {
        this.apply_end_time = apply_end_time;
    }

    public String getActivity_start_time() {
        return activity_start_time;
    }

    public void setActivity_start_time(String activity_start_time) {
        this.activity_start_time = activity_start_time;
    }

    public String getActivity_end_time() {
        return activity_end_time;
    }

    public void setActivity_end_time(String activity_end_time) {
        this.activity_end_time = activity_end_time;
    }

    public String[] getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(String[] photoIds) {
        this.photoIds = photoIds;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public int getParticipants_num() {
        return participants_num;
    }

    public void setParticipants_num(int participants_num) {
        this.participants_num = participants_num;
    }

    public int getMax_participants_num() {
        return max_participants_num;
    }

    public void setMax_participants_num(int max_participants_num) {
        this.max_participants_num = max_participants_num;
    }

    public int getAttention_num() {
        return attention_num;
    }

    public void setAttention_num(int attention_num) {
        this.attention_num = attention_num;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getScore_num() {
        return score_num;
    }

    public void setScore_num(int score_num) {
        this.score_num = score_num;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getDel_sign() {
        return del_sign;
    }

    public void setDel_sign(String del_sign) {
        this.del_sign = del_sign;
    }

    public String getRecommend_ageing() {
        return recommend_ageing;
    }

    public void setRecommend_ageing(String recommend_ageing) {
        this.recommend_ageing = recommend_ageing;
    }

    public String getRecommend_time() {
        return recommend_time;
    }

    public void setRecommend_time(String recommend_time) {
        this.recommend_time = recommend_time;
    }


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public int getIs_released() {
        return is_released;
    }

    public void setIs_released(int is_released) {
        this.is_released = is_released;
    }

    public int getIs_end() {
        return is_end;
    }

    public void setIs_end(int is_end) {
        this.is_end = is_end;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
