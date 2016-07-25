/*   
 * Copyright (c) 2010-2020 ISCAS. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * ISCAS. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with ISCAS.   
 *   
 */

package com.administrator.bean;

import java.io.Serializable;
import java.util.List;


/**
 * @author lin
 * @version V1.0
 * @Description: 社区大舞台照片墙照片
 * @date 2016年1月18日 下午3:11:35
 */
public class Photo implements Serializable {

    /**
     *
     */
    private String activity_id;//活动id
    private String photo_id;//照片编号
    private String photo_name;//照片名称
    private String user_id;//所属用户
    private String intro;//照片简介
    private int like_num;//喜欢的人数
    private int dislike_num;//不喜欢的人数
    private String path;//照片存放路径
    private String add_time;//上传时间
    private int del_sign;//删除标识

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    private int is_recommend;
    private int recommend_user_id;
    private List<User> likeUserList;


    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getDislike_num() {
        return dislike_num;
    }

    public void setDislike_num(int dislike_num) {
        this.dislike_num = dislike_num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getDel_sign() {
        return del_sign;
    }

    public void setDel_sign(int del_sign) {
        this.del_sign = del_sign;
    }


    public List<User> getLikeUserList() {
        return likeUserList;
    }

    public void setLikeUserList(List<User> likeUserList) {
        this.likeUserList = likeUserList;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getRecommend_user_id() {
        return recommend_user_id;
    }

    public void setRecommend_user_id(int recommend_user_id) {
        this.recommend_user_id = recommend_user_id;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
