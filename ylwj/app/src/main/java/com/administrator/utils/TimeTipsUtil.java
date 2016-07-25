package com.administrator.utils;

/**
 * 根据服务器返回的时间，转换成1天前，2小时前这样的格式
 * Created by xu on 2016/3/4.
 */
public class TimeTipsUtil {

    private static String[] tips = new String[]{"天前", "小时前", "分钟前", "秒前"};
    private static long[] tipsTime = new long[]{86400, 3600, 60, 0};

    /**
     * 根据time时间判断time到现在是多少分钟、多少秒...前
     * @param time 要判断的时间，UTC毫秒
     * @return
     */
    public static String getTimeTips(long time) {
        String result = "";
        long timeGap = (System.currentTimeMillis() - time) / 1000;
        for (int i = 0; i < tips.length; ++i) {
            if (timeGap < tipsTime[i]) {
                continue;
            } else {
                return result + (tipsTime[i] == 0 ? timeGap : timeGap/tipsTime[i]) + tips[i];
            }
        }
        return result;
    }

}
