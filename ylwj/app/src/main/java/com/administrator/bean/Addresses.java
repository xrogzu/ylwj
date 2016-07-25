package com.administrator.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 收货地址
 * Created by xu on 2016/3/2.
 */
public class Addresses {

    /**
     * result : 1
     * data : {"addressList":[{"addr":"asdfdasd","addr_id":1001,"city":"密云县","city_id":33,"country":"","def_addr":1,"isDel":0,"member_id":1004,"mobile":"18866624108","name":"王涛","province":"北京","province_id":1,"region":"密云县","region_id":451,"remark":"","tel":"18866624108","zip":"266600"},{"addr":"asdfdasd","addr_id":1002,"city":"密云县","city_id":33,"country":"","def_addr":0,"isDel":0,"member_id":1004,"mobile":"18866624108","name":"王涛","province":"北京","province_id":1,"region":"密云县","region_id":451,"remark":"","tel":"18866624108","zip":"266600"},{"addr":"asdfdasd","addr_id":1003,"city":"密云县","city_id":33,"country":"","def_addr":0,"isDel":0,"member_id":1004,"mobile":"18866624108","name":"王涛","province":"北京","province_id":1,"region":"密云县","region_id":451,"remark":"","tel":"18866624108","zip":"266600"},{"addr":"asdfdasd","addr_id":1004,"city":"密云县","city_id":33,"country":"","def_addr":0,"isDel":0,"member_id":1004,"mobile":"18866624108","name":"王涛","province":"北京","province_id":1,"region":"密云县","region_id":451,"remark":"","tel":"18866624108","zip":"266600"}]}
     */

    private int result;
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
        /**
         * addr : asdfdasd
         * addr_id : 1001
         * city : 密云县
         * city_id : 33
         * country :
         * def_addr : 1
         * isDel : 0
         * member_id : 1004
         * mobile : 18866624108
         * name : 王涛
         * province : 北京
         * province_id : 1
         * region : 密云县
         * region_id : 451
         * remark :
         * tel : 18866624108
         * zip : 266600
         */

        private List<AddressListEntity> addressList;

        public void setAddressList(List<AddressListEntity> addressList) {
            this.addressList = addressList;
        }

        public List<AddressListEntity> getAddressList() {
            return addressList;
        }

        public static class AddressListEntity implements Parcelable {
            private String addr;
            private int addr_id;
            private String city;
            private int city_id;
            private String country;
            private int def_addr;
            private int isDel;
            private int member_id;
            private String mobile;
            private String name;
            private String province;
            private int province_id;
            private String region;
            private int region_id;
            private String remark;
            private String tel;
            private String zip;

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public void setAddr_id(int addr_id) {
                this.addr_id = addr_id;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public void setDef_addr(int def_addr) {
                this.def_addr = def_addr;
            }

            public void setIsDel(int isDel) {
                this.isDel = isDel;
            }

            public void setMember_id(int member_id) {
                this.member_id = member_id;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public void setProvince_id(int province_id) {
                this.province_id = province_id;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public void setRegion_id(int region_id) {
                this.region_id = region_id;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getAddr() {
                return addr;
            }

            public int getAddr_id() {
                return addr_id;
            }

            public String getCity() {
                return city;
            }

            public int getCity_id() {
                return city_id;
            }

            public String getCountry() {
                return country;
            }

            public int getDef_addr() {
                return def_addr;
            }

            public int getIsDel() {
                return isDel;
            }

            public int getMember_id() {
                return member_id;
            }

            public String getMobile() {
                return mobile;
            }

            public String getName() {
                return name;
            }

            public String getProvince() {
                return province;
            }

            public int getProvince_id() {
                return province_id;
            }

            public String getRegion() {
                return region;
            }

            public int getRegion_id() {
                return region_id;
            }

            public String getRemark() {
                return remark;
            }

            public String getTel() {
                return tel;
            }

            public String getZip() {
                return zip;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.addr);
                dest.writeInt(this.addr_id);
                dest.writeString(this.city);
                dest.writeInt(this.city_id);
                dest.writeString(this.country);
                dest.writeInt(this.def_addr);
                dest.writeInt(this.isDel);
                dest.writeInt(this.member_id);
                dest.writeString(this.mobile);
                dest.writeString(this.name);
                dest.writeString(this.province);
                dest.writeInt(this.province_id);
                dest.writeString(this.region);
                dest.writeInt(this.region_id);
                dest.writeString(this.remark);
                dest.writeString(this.tel);
                dest.writeString(this.zip);
            }

            public AddressListEntity() {
            }

            protected AddressListEntity(Parcel in) {
                this.addr = in.readString();
                this.addr_id = in.readInt();
                this.city = in.readString();
                this.city_id = in.readInt();
                this.country = in.readString();
                this.def_addr = in.readInt();
                this.isDel = in.readInt();
                this.member_id = in.readInt();
                this.mobile = in.readString();
                this.name = in.readString();
                this.province = in.readString();
                this.province_id = in.readInt();
                this.region = in.readString();
                this.region_id = in.readInt();
                this.remark = in.readString();
                this.tel = in.readString();
                this.zip = in.readString();
            }

            public static final Parcelable.Creator<AddressListEntity> CREATOR = new Parcelable.Creator<AddressListEntity>() {
                public AddressListEntity createFromParcel(Parcel source) {
                    return new AddressListEntity(source);
                }

                public AddressListEntity[] newArray(int size) {
                    return new AddressListEntity[size];
                }
            };
        }
    }
}
