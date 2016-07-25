package com.administrator.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * 自动更新信息解析
 * Created by Administrator on 2016/4/21.
 */
public class UpdataInfoParser {
    /*
 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
 */
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception{
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();//实体
        while(type != XmlPullParser.END_DOCUMENT ){
            switch (type) {
                case XmlPullParser.START_TAG:
                    if("versionCode".equals(parser.getName())){
                        info.setVersionCode(parser.nextText()); //获取版本号
                    }else if ("url".equals(parser.getName())){
                        info.setUrl(parser.nextText()); //获取要升级的APK文件
                    }else if ("description".equals(parser.getName())){
                        info.setDescription(parser.nextText()); //获取该文件的信息
                    }else if("versionName".equals(parser.getName())){
                        info.setVerstionName(parser.nextText());
                    }else if("forceupdate".equals(parser.getName())){
                        info.setForceUpdate(parser.nextText());
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
}
