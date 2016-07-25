package com.administrator.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6.
 */
public class NonList {

    /**
     * result : 1
     * data : [{"id":1,"title":"qq","imageurl":"66","creater":"uu","update_time":1234567,"create_time":1234567890,"end_time":2457681839240,"flag":1,"content_txt":null,"readedquantity":10,"sn1":1}]
     */

    private int result;
    /**
     * id : 1
     * title : qq
     * imageurl : 66
     * creater : uu
     * update_time : 1234567
     * create_time : 1234567890
     * end_time : 2457681839240
     * flag : 1
     * content_txt : null
     * readedquantity : 10
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
        private String title;
        private String imageurl;
        private String creater;
        private long update_time;
        private long create_time;
        private long end_time;
        private int flag;
        private String content_txt;
        private int readedquantity;
        private int sn1;
        private int is_banner;

        public int getIs_banner() {
            return is_banner;
        }

        public void setIs_banner(int is_banner) {
            this.is_banner = is_banner;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public void setCreater(String creater) {
            this.creater = creater;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public void setContent_txt(String content_txt) {
            this.content_txt = content_txt;
        }

        public void setReadedquantity(int readedquantity) {
            this.readedquantity = readedquantity;
        }

        public void setSn1(int sn1) {
            this.sn1 = sn1;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getCreater() {
            return creater;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public int getFlag() {
            return flag;
        }

        public String getContent_txt() {
            return content_txt;
        }

        public int getReadedquantity() {
            return readedquantity;
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
            dest.writeString(this.title);
            dest.writeString(this.imageurl);
            dest.writeString(this.creater);
            dest.writeLong(this.update_time);
            dest.writeLong(this.create_time);
            dest.writeLong(this.end_time);
            dest.writeInt(this.flag);
            dest.writeString(this.content_txt);
            dest.writeInt(this.readedquantity);
            dest.writeInt(this.sn1);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.imageurl = in.readString();
            this.creater = in.readString();
            this.update_time = in.readLong();
            this.create_time = in.readLong();
            this.end_time = in.readLong();
            this.flag = in.readInt();
            this.content_txt = in.readString();
            this.readedquantity = in.readInt();
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
