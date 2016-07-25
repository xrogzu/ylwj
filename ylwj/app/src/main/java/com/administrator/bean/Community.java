package com.administrator.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class Community {

    /**
     * result : 1
     * data : [{"address":"广博路223号","city":"青岛","community_id":"2","community_name":"东凤馨苑","county":"高新区","memberList":[],"province":"山东"},{"address":"香港西路38号","city":"青岛","community_id":"1","community_name":"丰和社区","county":"市南区","memberList":[{"uname":"kingapex","face":null,"mobile":null,"sex":1},{"uname":"demowhj","face":null,"mobile":null,"sex":1},{"uname":"abcd","face":"fs:/attachment/faceFile/201603140950378804.png","mobile":"13112345678","sex":1}],"province":"山东"}]
     */

    private int result;
    /**
     * address : 广博路223号
     * city : 青岛
     * community_id : 2
     * community_name : 东凤馨苑
     * county : 高新区
     * memberList : []
     * province : 山东
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
        private String address;
        private String city;
        private String community_id;
        private String community_name;
        private String county;
        private String province;
        private List<?> memberList;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setMemberList(List<?> memberList) {
            this.memberList = memberList;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public String getCounty() {
            return county;
        }

        public String getProvince() {
            return province;
        }

        public List<?> getMemberList() {
            return memberList;
        }
    }
}
