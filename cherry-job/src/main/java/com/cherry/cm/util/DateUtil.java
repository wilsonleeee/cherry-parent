/*  
 * @(#)DateUtil.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.cm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*	
 * @(#)DateUtil.java     1.0 2010/10/12		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */

/**
 * <p>
 * 共通 时间Util
 * </p>
 * 
 * @author huzd
 * 
 */
public class DateUtil {
	
	/** 年月日格式 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	/** 时分秒 */
	public static final String TIME_PATTERN = "HH:mm:ss";
	
	/** 年月日时分秒 */
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 年月日时分秒毫秒 */
	public static final String DATETIME2_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
	
	/**
	 * 把Date转换成String型日期
	 * @param date
	 * @return
	 */
	public static String date2String (Date date,String formatType){
		DateFormat format = new SimpleDateFormat(formatType);        
		String dateStr = format.format(date);
		return dateStr;
	}
	
	/**
	 * 把Date转换成String型日期
	 * @param date
	 * @return
	 */
	public static String date2String (Date date){
		return date2String(date,DATE_PATTERN);
	}
	
    /**
     * 根据分钟添加日期
     * @param pattern
     * @param date
     * @param days
     * @return
     */
    public static String addDateByMinutes (String pattern, String date, int minute){
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(coverString2Date(date)); 
        cal.add(Calendar.MINUTE, minute);
        return date2String(cal.getTime(),pattern);
    }
	
	/**
	 * 根据小时添加日期
	 * @param date
	 * @param formatType
	 * @return
	 */
	public static String addDateByHours (String pattern, String date, int hours){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(coverString2Date(date)); 
		cal.add(Calendar.HOUR_OF_DAY, hours);//24小时制 
		return date2String(cal.getTime(),pattern);
	}
	
	/**
	 * 根据小时添加日期
	 * @param date
	 * @param formatType
	 * @return
	 */
	public static Date addDateByHours (Date date,int hours){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.HOUR_OF_DAY, hours);//24小时制 
		date = cal.getTime(); 
		return date;
	}
	
	/**
	 * 日期增加指定天数
	 * @param pattern
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDateByDays (String pattern, String date, int days){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(coverString2Date(date)); 
		cal.add(Calendar.DAY_OF_MONTH, days);
		return date2String(cal.getTime(),pattern);
	}
	
	/**
	 * 日期增加指定年
	 * @param pattern
	 * @param date
	 * @param years
	 * @return
	 */
	public static String addDateByYears (String pattern, String date, int years){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(coverString2Date(date)); 
		cal.add(Calendar.YEAR, years);
		return date2String(cal.getTime(),pattern);
	}
	
	/**
	 * 把字符串转成指定格式的日期
	 * 
	 * @param s 待转换的字符串
	 * @param pattern 指定的日期格式
	 * @return 如果转换成功返回日期，否则返回null
	 */
	public static Date coverString2Date(String s, String pattern) {
		
		if(s == null || "".equals(s))
			return null;
		if(pattern == null || "".equals(pattern))
			return null;
		try {
			DateFormat df = new SimpleDateFormat(pattern);
			Date d = df.parse(s);
	        if(df.format(d).equals(s)) {
	        	return d;
	        } else {
	        	return null;
	        }
	    } catch (ParseException e) {
	         return null;
	    }
	}
	
	/**
	 * 把字符串转成日期(支持多种格式)
	 * 
	 * @param s 待转换的字符串
	 * @return 如果转换成功返回日期，否则返回null
	 */
	public static Date coverString2Date(String s) {
		
		if(s == null || "".equals(s))
			return null;
		Date d = coverString2Date(s, "yyyy-MM-dd");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy-M-d");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy-MM-d");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy-M-dd");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy/MM/dd");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy/M/d");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy/MM/d");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyy/M/dd");
		if(d != null)
			return d;
		d = coverString2Date(s, "yyyyMMdd");
		if(d != null)
			return d;
		d = coverString2Date(s,DATETIME_PATTERN);
		if(d != null)
			return d;
        d = coverString2Date(s, "yyyy-MM-dd HH:mm:ss.S");
        if(d != null)
            return d;
        d = coverString2Date(s, "yyyy-MM-dd HH:mm:ss.SS");
        if(d != null)
        	return d;
        d = coverString2Date(s, "yyyy-MM-dd HH:mm:ss.SSS");
        if(d != null)
        	return d;
		return null;
	}
	
	/**
	 * 通过一个日期，取得该日期所在月份的第一天的日期(年月日)
	 * 
	 * @param date 任何一个日期(年月日)
	 * @return 第一天的日期(年月日)
	 * @throws Exception 
	 */
	public static String getFirstDateYMD(String date) throws Exception {
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(sf.parse(date));
    	ca.set(Calendar.DAY_OF_MONTH,ca.getMinimum(Calendar.DAY_OF_MONTH));
		return sf.format(ca.getTime());
	}
	
	/**
	 * 把字符串转成年月日格式的日期(yyyy-MM-dd)
	 * 
	 * @param s 待转换的字符串
	 * @param pattern 指定的日期格式
	 * @return 如果转换成功返回日期，否则返回null
	 * @throws Exception 
	 */
	public static String coverTime2YMD(String time, String pattern) throws Exception {
		if(time == null || "".equals(time))
			return null;
		if(pattern == null || "".equals(pattern))
			return null;
		try {
			DateFormat df = new SimpleDateFormat(pattern);
			Date d = df.parse(time);
			String dateStr = date2String(d, "yyyy-MM-dd");
			return dateStr;
	    } catch (ParseException e) {
	    	throw new Exception("datetime conversion error");
	    }
	}
	
	/**
	 * 设置日期的时分秒毫秒
	 * 
	 * @param String
	 *            转换前的日期 int 日期区分(0:开始日期,1:结束日期)
	 * @return String 转换后的日期
	 */
	public static String suffixDate(String date, int dateKbn) {
		if (null != date && !"".equals(date)) {
			if (checkDate(date)) {
				if (0 == dateKbn) {
					return date + " 00:00:00.000";
				} else {
					return date + " 23:59:59.000";
				}
			}
		}
		return date;
	}
	
	/**
	 * 检测字符串是否符合日期格式(支持多种格式)
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串符合日期格式则返回true，否则返回false
	 */
	public static boolean checkDate(String value) {
		if (checkDate(value, "yyyy-MM-dd"))
			return true;
		if (checkDate(value, "yyyy-M-d"))
			return true;
		if (checkDate(value, "yyyy-MM-d"))
			return true;
		if (checkDate(value, "yyyy-M-dd"))
			return true;
		if (checkDate(value, "yyyy/MM/dd"))
			return true;
		if (checkDate(value, "yyyy/M/d"))
			return true;
		if (checkDate(value, "yyyy/MM/d"))
			return true;
		if (checkDate(value, "yyyy/M/dd"))
			return true;
		if (checkDate(value, "yyyyMMdd"))
			return true;
		return false;
	}
	
	/**
	 * 检测字符串是否符合指定的日期格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @param pattern
	 *            指定的日期格式
	 * @return 如果字符串符合指定的日期格式则返回true，否则返回false
	 */
	public static boolean checkDate(String value, String pattern) {
		if (value == null || "".equals(value))
			return false;
		if (pattern == null || "".equals(pattern))
			return false;
		boolean ret = true;
		try {
			DateFormat df = new SimpleDateFormat(pattern);
			ret = df.format(df.parse(value)).equals(value);
			if (ret) {
				Calendar ca = Calendar.getInstance();
				ca.setTime(df.parse(value));
				ret = ca.get(Calendar.YEAR) < 10000;
			}
		} catch (ParseException e) {
			ret = false;
		}
		return ret;
	}
	
	/**
	 * 创建日期
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param formatType
	 *            日期格式
	 * @return 返回生成的日期
	 */
	public static String createDate(int year, int month, int day, String formatType) {
		Calendar ca1 = Calendar.getInstance();
		ca1.set(Calendar.YEAR, year);
		ca1.set(Calendar.MONTH, month);
		ca1.set(Calendar.DAY_OF_MONTH, day);
		return date2String(ca1.getTime(), formatType);
	}
	
	/**
	 * 日期增加指定月数
	 * 
	 * @param pattern
	 * @param date
	 * @param months
	 * @return
	 */
	public static String addDateByMonth(String pattern, String date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(coverString2Date(date));
		cal.add(Calendar.MONTH, months);
		return date2String(cal.getTime(), pattern);
	}
	
	/**
	 * 两个字符串的日期比较
	 * 
	 * @param value1
	 *            待比较的字符串1
	 * @param value2
	 *            待比较的字符串2
	 * @return 返回0表示两个日期相等，返回比0小的值表示value1在value2之前，返回比0大的值表示value1在value2之后
	 */
	public static int compareDate(String value1, String value2) {
		Date d1 = coverString2Date(value1);
		Date d2 = coverString2Date(value2);
		return d1.compareTo(d2);
	}
	
	/**
	 * 日期减少指定月数
	 * @param pattern
	 * @param date
	 * @param days
	 * @return
	 */
	public static String subDateByMonth (String pattern, String date, int month){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(coverString2Date(date)); 
		cal.add(Calendar.MONTH, - month);
		int dateYear = cal.get(Calendar.YEAR);
		int dateMonth = cal.get(Calendar.MONTH);
		cal.set(dateYear, dateMonth, 1);
		return date2String(cal.getTime(),pattern);
	}
	
	/**
	 * 判断是否为闰年
	 * @param yearStr
	 * @return
	 * true:闰年 false:平年
	 */
	public static boolean getLeapYear(String yearStr){
		int year = Integer.valueOf(yearStr);
		boolean bol = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
		return bol;
	}
	
	/**
     * 取得当前时间
     * @return
     */
    public static String getCurrTime(){
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssS");
    	return sf.format(calendar.getTime());
    }
    
   	/**
   	 * 创建日期
   	 * @param year
   	 * @param month
   	 * @param day
   	 * @return
   	 */
   	public static Date createDate(int year, int month, int day){
   		return getCalendar(year,month,day).getTime();
   	}
   	
   	public static Calendar getCalendar(String date){
   		Calendar cal = Calendar.getInstance();
   		cal.setTime(DateUtil.coverString2Date(date));
   		return cal;
   	}
   	
   	
   	public static Calendar getCalendar(int year, int month, int day){
   		Calendar cal = Calendar.getInstance();
   		// 平年
   		if(!getLeapYear(String.valueOf(year)) && month == 2 && day == 29){
   			day--;
   		}
   		cal.set(year, month-1, day);
   		return cal;
   	}
   	
   	/**
	 * 通过一个日期，取得该日期所在月份的第一天或者最后天的日期(年月日)
	 * 
	 * @param date 任何一个日期(年月日)
	 * @param flag 0：取得该日期的第一天，1：取得该日期的最后天
	 * @return 第一天或者最后天的日期(年月日)
	 */
	public static String getFirstOrLastDateYMD(String date, int flag) {
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(coverString2Date(date));
    	if(flag == 0) {
    		ca.set(Calendar.DAY_OF_MONTH,ca.getActualMinimum(Calendar.DAY_OF_MONTH));
    	} else {
    		ca.set(Calendar.DAY_OF_MONTH,ca.getActualMaximum(Calendar.DAY_OF_MONTH));
    	}
		return sf.format(ca.getTime());
	}
	
	/**
	 * 通过一个月份，取得该月份的最后天
	 * 
	 * @param month 任何一个月份
	 * @return 最后天
	 */
	public static String getLastDateByMonth(String month) {
		
		int _month = Integer.parseInt(month);
		if(_month == 2) {
			return "29";
		} else if (_month == 4 || _month == 6 || _month == 9 || _month == 11) {
			return "30";
		} else {
			return "31";
		}
	}
	
	/**
	 * 取得固定格式的时间字符串
	 * @param timeParam
	 * @return
	 */
	public static String getSpecificDate(String timeParam,String pattern){
		DateFormat format = new SimpleDateFormat(pattern);
		Date date = coverString2Date(timeParam);
		String dateStr = format.format(date);
		return dateStr;
	}
}
