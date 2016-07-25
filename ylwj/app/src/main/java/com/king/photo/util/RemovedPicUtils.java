package com.king.photo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 删除商品详情中图片的工具类
 * Created by Administrator on 2016/5/16.
 */
public class RemovedPicUtils {

    //判断是否需要去掉商品详情中的图片
    public static boolean isEqual(String pic1, String pic2) {
        boolean result = false;
        if (pic1 != null && pic2 != null) {
            int index1 = pic1.lastIndexOf('/');
            int index2 = pic2.lastIndexOf('/');
            if (index1 != -1 && index2 != -1) {
                index1 += 1;
                index2 += 1;
                String name1 = null;
                String name2 = null;
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher matcher = pattern.matcher(pic1.substring(index1));
                if (matcher.find()) {
                    name1 = matcher.group(0);
                }
                matcher = pattern.matcher(pic2.substring(index2));
                if(matcher.find()){
                    name2 = matcher.group(0);
                }
                if(name1 != null && name2 != null){
                    if(name1.equals(name2))
                        result = true;
                }
            }
        }
        return result;
    }
}
