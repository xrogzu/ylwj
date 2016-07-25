package com.administrator.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6.
 */
public class FinancialExpert {

    /**
     * result : 1
     * data : [{"id":1,"name":"付小凡","field":"零售业务分析","summary":"3年银行从业经验，擅长零售业务分析，个人与家庭理财、投资规划、资产配置等，具有丰富的行业研究经验。","mobile":15505321959,"tel":0,"avatar_url":"http://180.76.148.177:8088/javamall/statics/attachment/faceFile/1.jpg","gender":2,"version":1,"sn1":1}]
     */

    private int result;
    /**
     * id : 1
     * name : 付小凡
     * field : 零售业务分析
     * summary : 3年银行从业经验，擅长零售业务分析，个人与家庭理财、投资规划、资产配置等，具有丰富的行业研究经验。
     * mobile : 15505321959
     * tel : 0
     * avatar_url : http://180.76.148.177:8088/javamall/statics/attachment/faceFile/1.jpg
     * gender : 2
     * version : 1
     * sn1 : 1
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

    public static class DataEntity implements Parcelable {
        private int id;
        private String expert_name;
        private String field;
        private String summary;
        private String mobile;
        private String tel;
        private String avatar_url;
        private int gender;
        private int version;
        private int sn1;
        private String expert_type;

        public void setId(int id) {
            this.id = id;
        }

        public void setField(String field) {
            this.field = field;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setSn1(int sn1) {
            this.sn1 = sn1;
        }

        public int getId() {
            return id;
        }

        public String getExpert_name() {
            return expert_name;
        }

        public void setExpert_name(String expert_name) {
            this.expert_name = expert_name;
        }

        public String getField() {
            return field;
        }

        public String getSummary() {
            return summary;
        }

        public String getExpert_type() {
            return expert_type;
        }

        public void setExpert_type(String expert_type) {
            this.expert_type = expert_type;
        }

        public String getMobile() {
            return mobile;
        }

        public String getTel() {
            return tel;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public int getGender() {
            return gender;
        }

        public int getVersion() {
            return version;
        }

        public int getSn1() {
            return sn1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.expert_name);
            dest.writeString(this.field);
            dest.writeString(this.summary);
            dest.writeString(this.mobile);
            dest.writeString(this.tel);
            dest.writeString(this.avatar_url);
            dest.writeInt(this.gender);
            dest.writeInt(this.version);
            dest.writeInt(this.sn1);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.id = in.readInt();
            this.expert_name = in.readString();
            this.field = in.readString();
            this.summary = in.readString();
            this.mobile = in.readString();
            this.tel = in.readString();
            this.avatar_url = in.readString();
            this.gender = in.readInt();
            this.version = in.readInt();
            this.sn1 = in.readInt();
        }

        public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
    }
}
