package com.administrator.utils;

import java.math.BigDecimal;

/**
 * 对double格式化
 * Created by Administrator on 2016/4/20.
 */
public class DoubleUtils {
    
    public static String convert2String(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
    }

    //去掉double多余的0并转换为string
    public static String convert2String(double number) {
        return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
    }
}
