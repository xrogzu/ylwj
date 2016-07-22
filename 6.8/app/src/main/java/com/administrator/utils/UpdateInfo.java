package com.administrator.utils;

/**
 * 自动更新信息类
 * Created by Administrator on 2016/4/21.
 */
public class UpdateInfo {
    //主要根据versionCode进行判断是否自动更新
    private String versionCode;
    private String verstionName;
    //是否强制更新，为1表示强制更新，0表示非强制更新
    private String forceUpdate;
    private String url;
    private String description;
    public String getVersionCode() {
        return versionCode;
    }
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVerstionName() {
        return verstionName;
    }

    public void setVerstionName(String verstionName) {
        this.verstionName = verstionName;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
