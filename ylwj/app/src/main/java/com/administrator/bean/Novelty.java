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
 * @Description: 身边-新鲜事实体类
 * @date 2016年2月16日 上午10:13:57
 */
public class Novelty implements Serializable {
    private String id;//主键
    private String publisher_id;//发布人id
    private String publisher_name;//发布人昵称
    private String message_title;//消息标题
    private String message_content;//消息内容
    private String message_status;//消息状态，0 无效；1 有效；9 暂存
    private String publish_time;//发布时间
    private String coordinate;//发布地点坐标名称
    private String coordinate_x;//坐标x
    private String coordinate_y;//坐标y
    private String like_num;//点赞的数量
    private String comment_num;//评论数量
    private String share_num;//分享数量
    private String face;//头像所存放的路径
    private String is_like;//当前用户是否对该新鲜事点赞
    private String is_activity;//是否是活动
    private String activity_id;//活动编号
    private String activity_address;//活动地址
    private String activity_title;//活动标题
    private String apply_num;//活动报名人数
    private String is_apply;//当前用户是否报名活动
    private String activity_type;

    private List<ThumbnailPhoto> photos;//新鲜事图片list

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public void setCoordinate_x(String coordinate_x) {
        this.coordinate_x = coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }

    public void setCoordinate_y(String coordinate_y) {
        this.coordinate_y = coordinate_y;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getIs_activity() {
        return is_activity;
    }

    public void setIs_activity(String is_activity) {
        this.is_activity = is_activity;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_address() {
        return activity_address;
    }

    public void setActivity_address(String activity_address) {
        this.activity_address = activity_address;
    }

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getApply_num() {
        return apply_num;
    }

    public void setApply_num(String apply_num) {
        this.apply_num = apply_num;
    }

    public String getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(String is_apply) {
        this.is_apply = is_apply;
    }

    public List<ThumbnailPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ThumbnailPhoto> photos) {
        this.photos = photos;
    }
}

