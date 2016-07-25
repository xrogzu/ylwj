package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class CommunityList {

    /**
     * address : 香港路1号
     * city : 青岛市
     * community_id : 1
     * community_name : 华夏社区
     * county : 市南区
     * memberList : []
     * province : 山东省
     */

    private String address;
    private String city;
    private String community_id;
    private String community_name;
    private String county;
    private String province;
    private List<?> memberList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<?> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<?> memberList) {
        this.memberList = memberList;
    }
}
