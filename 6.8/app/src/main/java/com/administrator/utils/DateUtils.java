package com.administrator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	/**
	 *  获取当前时间戳
	 * @return 时间戳
	 */
	public static long getCurrentTime(){
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}
	/**
	 * 格式化12小时制<br>
	 * 格式：yyyy-MM-dd hh-MM-ss
	 * @param time 时间
	 * @return
	 */
	public static String format12Time(long time){
		return format(time,"yyyy-MM-dd hh:MM:ss");
	}
	
	/**
	 * 格式化24小时制<br>
	 * 格式：yyyy-MM-dd HH-MM-ss
	 * @param time 时间
	 * @return
	 */
	public static String format24Time(long time){
		return format(time,"yyyy-MM-dd HH:MM:ss");
	}
	
	/**
	 * 格式化时间,自定义标签
	 * @param time 时间
	 * @param pattern 格式化时间用的标签
	 * @return
	 */
	public static String format(long time,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}
	/**
	 * 格式化24小时制<br>
	 * 格式：HH-MM-ss
	 * @param time 时间
	 * @return
	 */
	public static String format24TimeHour(long time){
		return format(time, "HH:MM:ss");
	}
	
	/**
	 * 获取当前天
	 * @return 天
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentDay(){
		Calendar calendar = Calendar.getInstance();
		return calendar.DAY_OF_MONTH;
	}
	
	/** 获取当前月
	 * @return 月
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentMonth(){
		Calendar calendar = Calendar.getInstance();
		return calendar.MONTH + 1;
	}
	/** 获取当前年
	 * @return 年
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.YEAR;
	}

	/**
	 * 删除毫秒后面的点和数
	 * @param time
	 * @return
	 */
	public static String delDot(String time){
		if(time.indexOf('.') != -1)
			time = time.substring(0,time.indexOf('.'));
		return time;
	}

	/**
	 * <p>将时间字符串转化成long类型</p>
	 */
	public static String changeDate(String date){
		if(date == null || date.equals("")){
			return "";
		}
		Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyyMMdd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis() + "";
	}
	/**
	 * <p>根据字符串返回日历</p>
	 */
	public static Calendar getCalendar(String time){
		Calendar c = Calendar.getInstance();
		Date date = new Date(Long.parseLong(time)) ;
		c.setTime(date);
		return c;
	}
}
