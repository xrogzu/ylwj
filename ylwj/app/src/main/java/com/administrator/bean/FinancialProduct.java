package com.administrator.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**金融产品、精致生活、健康养生的bean
 * Created by Administrator on 2016/3/6.
 */
public class FinancialProduct {

    /**
     * result : 1
     * data : [{"id":15,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"},{"id":1,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"},{"id":14,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"},{"id":12,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"},{"id":13,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"},{"id":11,"name":"活期储蓄","type":"1","create_time":1457681839240,"end_time":1574920588000,"creater_id":"1","flag":"1","content":"<h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;<\/h3>\n\n<h3>功能特点<\/h3>\n\n<h3>1、存取便利：凭银行卡/存折您可随时存取。<\/h3>\n\n<h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。<\/h3>","imageurl":"http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg"}]
     */

    private int result;
    /**
     * id : 15
     * name : 活期储蓄
     * type : 1
     * create_time : 1457681839240
     * end_time : 1574920588000
     * creater_id : 1
     * flag : 1
     * content : <h3>您在存款时不约定存款期限，可以随时存取，每次存取金额不限的储蓄方式。1元起存，多存不限。产品载体为本行发行的各类银行卡和存折。&nbsp;&nbsp;</h3>

     <h3>功能特点</h3>

     <h3>1、存取便利：凭银行卡/存折您可随时存取。</h3>

     <h3>2、灵活通用：您可根据需要在本行全国任意营业网点、自助机具、客户服务中心（电话银行）、网上银行等渠道办理业务，享受服务。</h3>
     * imageurl : http://heilongjiang.sinaimg.cn/2013/0101/U9106P1274DT20130101191739.jpg
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
        private String name;
        private String type;
        private long create_time;
        private long end_time;
        private String creater_id;
        private String flag;
        //html代码，直接加载在webview中
        private String content;
        //图片url
        private String imageurl;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public void setCreater_id(String creater_id) {
            this.creater_id = creater_id;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public long getCreate_time() {
            return create_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public String getCreater_id() {
            return creater_id;
        }

        public String getFlag() {
            return flag;
        }

        public String getContent() {
            return content;
        }

        public String getImageurl() {
            return imageurl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.type);
            dest.writeLong(this.create_time);
            dest.writeLong(this.end_time);
            dest.writeString(this.creater_id);
            dest.writeString(this.flag);
            dest.writeString(this.content);
            dest.writeString(this.imageurl);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.type = in.readString();
            this.create_time = in.readLong();
            this.end_time = in.readLong();
            this.creater_id = in.readString();
            this.flag = in.readString();
            this.content = in.readString();
            this.imageurl = in.readString();
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
