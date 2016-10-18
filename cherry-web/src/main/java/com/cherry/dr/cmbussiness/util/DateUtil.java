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
package com.cherry.dr.cmbussiness.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	/** 年月日时分秒 */
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/** 时间格式间隔符号 */
	public static final String DATE_LINE_MARK = "-";
	
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
		return null;
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
	 * 比较两个日期是否是同年同月
	 * 
	 * @param date1
	 *            待比较的日期1
	 * @param date2
	 *            待比较的日期2
	 * @return 相等则返回true，否则返回false
	 */
	public static boolean monthEquals(String dateStr1, String dateStr2) {
		Date date1 = coverString2Date(dateStr1);
		Date date2 = coverString2Date(dateStr2);
		if (null == date1 || null == date2) {
			return false;
		}
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		ca1.setTime(date1);
		ca2.setTime(date2);
		if (ca1.get(Calendar.YEAR) == ca2.get(Calendar.YEAR) &&
				ca1.get(Calendar.MONTH) == ca2.get(Calendar.MONTH)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个日期是否是同月
	 * 
	 * @param date1
	 *            待比较的日期1
	 * @param date2
	 *            待比较的日期2
	 * @return 相等则返回true，否则返回false
	 */
	public static boolean isSameMonth(String dateStr1, String dateStr2) {
		Date date1 = coverString2Date(dateStr1);
		Date date2 = coverString2Date(dateStr2);
		if (null == date1 || null == date2) {
			return false;
		}
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		ca1.setTime(date1);
		ca2.setTime(date2);
		if (ca1.get(Calendar.MONTH) == ca2.get(Calendar.MONTH)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个日期是否是同月同日
	 * 
	 * @param date1
	 *            待比较的日期1
	 * @param date2
	 *            待比较的日期2
	 * @return 相等则返回true，否则返回false
	 */
	public static boolean isSameMonthDay(String dateStr1, String dateStr2) {
		Date date1 = coverString2Date(dateStr1);
		Date date2 = coverString2Date(dateStr2);
		if (null == date1 || null == date2) {
			return false;
		}
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		ca1.setTime(date1);
		ca2.setTime(date2);
		if (ca1.get(Calendar.MONTH) == ca2.get(Calendar.MONTH) &&
				ca1.get(Calendar.DAY_OF_MONTH) == ca2.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个日期是否是同月同日
	 * 
	 * @param date1
	 *            待比较的日期1
	 * @param m
	 *            待比较的月
	 * @param d
	 *            待比较的日
	 * @return 相等则返回true，否则返回false
	 */
	public static boolean isSameMonthDay(String dateStr1, int m, int d) {
		Date date1 = coverString2Date(dateStr1);
		if (null == date1) {
			return false;
		}
		Calendar ca1 = Calendar.getInstance();
		ca1.setTime(date1);
		if (ca1.get(Calendar.MONTH) == (m - 1) &&
				ca1.get(Calendar.DAY_OF_MONTH) == d) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个日期是否是同周
	 * 
	 * @param date1
	 *            待比较的日期1
	 * @param date2
	 *            待比较的日期2
	 * @return 相等则返回true，否则返回false
	 */
	public static boolean isSameWeek(String dateStr1, String dateStr2) {
		Date date1 = coverString2Date(dateStr1);
		Date date2 = coverString2Date(dateStr2);
		if (null == date1 || null == date2) {
			return false;
		}
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		ca1.setTime(date1);
		ca2.setTime(date2);
		if (ca1.get(Calendar.WEEK_OF_MONTH) == ca2.get(Calendar.WEEK_OF_MONTH)) {
			return true;
		}
		return false;
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
	 * 改变日期的天数(如果指定日超过了该月的范围，将取该月最后一天)
	 * 
	 * @param date
	 *            改变前的日期
	 * @param day
	 *            新的天数
	 * @param formatType
	 *            日期格式
	 * @return 返回改变后的日期
	 */
	public static String changeDateByDay(String date, int day, String formatType) {
		Date date1 = coverString2Date(date);
		if (null != date1) {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(date1);
			int maxDay = ca1.getActualMaximum(Calendar.DAY_OF_MONTH);
			day = (day > maxDay) ? maxDay : day;
			ca1.set(Calendar.DAY_OF_MONTH, day);
			return date2String(ca1.getTime(), formatType);
		}
		return null;
	}
	
	/**
	 * 取得指定日期所在的月头或者月末
	 * 
	 * @param date
	 *            指定日期
	 * @param flag
	 *            0:月头   1: 月末
	 * @return 头或者月末
	 */
	public static String getMonthStartOrEnd(String date, int flag) {
		Date date1 = coverString2Date(date);
		if (null != date1) {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(date1);
			if (0 == flag) {
				ca1.set(Calendar.DAY_OF_MONTH, 1);
			} else {
				int maxDay = ca1.getActualMaximum(Calendar.DAY_OF_MONTH);
				ca1.set(Calendar.DAY_OF_MONTH, maxDay);
			}
			return date2String(ca1.getTime(), DATE_PATTERN);
		}
		return null;
	}

	/**
	 * 是否为指定的第几个星期几
	 * 
	 * @param date
	 *            改变前的日期
	 * @param weekNum
	 *            第几个星期
	 * @param weekDay
	 *            星期几
	 * @return 验证结果
	 */
	public static boolean isSameWeekDay(String date, int weekNum, int weekDay) {
		Date date1 = coverString2Date(date);
		if (null != date1) {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(date1);
			// 年
			int year = ca1.get(Calendar.YEAR);
			// 月
			int month = ca1.get(Calendar.MONTH);
			int week = ca1.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			int dayWeek = ca1.get(Calendar.DAY_OF_WEEK);
			Calendar ca2 = Calendar.getInstance();
			ca2.set(Calendar.YEAR, year);
			ca2.set(Calendar.MONTH, month);
			ca2.set(Calendar.DAY_OF_MONTH, 1);
			int firstWeekDay = ca2.get(Calendar.DAY_OF_WEEK);
			
			Calendar ca3 = Calendar.getInstance();
			ca3.set(Calendar.YEAR, year);
			ca3.set(Calendar.MONTH, month);
			ca3.set(Calendar.DAY_OF_MONTH, ca3.getActualMaximum(Calendar.DAY_OF_MONTH));
			int lastWeekDay = ca3.get(Calendar.DAY_OF_WEEK);
			// 第一至第四个星期
			if (weekNum < 5) {
				// 星期一至星期天
				if (weekDay < 8) {
					if (week == weekNum && dayWeek == weekDay) {
						return true;
					}
					// 工作日
				} else if (8 == weekDay && dayWeek > 1 && dayWeek < 7) {
					if (dayWeek < firstWeekDay) {
						week++;	
					}
					if (week == weekNum) {
						return true;
					}
					// 双休日
				} else if (9 == weekDay && (1 == dayWeek || 7 == dayWeek)) {
					if (1 == firstWeekDay && 7 == dayWeek) {
						week++;
					}
					if (week == weekNum) {
						return true;
					}
				}
				// 最后一个星期
			} else if (isMaxWeekNum(date)) {
				// 星期一至星期天
				if (weekDay < 8) {
					if (dayWeek == weekDay) {
						return true;
					}
					// 工作日
				} else if (8 == weekDay && dayWeek > 1 && dayWeek < 7) {
					if (lastWeekDay > 1 && lastWeekDay < 7 && dayWeek > lastWeekDay) {
						return false;
					}
					return true;
					// 双休日
				} else if (9 == weekDay && (1 == dayWeek || 7 == dayWeek)) {
					if (1 == dayWeek && lastWeekDay == 7) {
						return false;
					} 
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否为最后一个星期
	 * 
	 * @param date
	 *            验证的日期
	 * @return 验证结果
	 */
	private static boolean isMaxWeekNum(String date) {
		Date date1 = coverString2Date(date);
		if (null != date1) {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(date1);
			// 年
			int year = ca1.get(Calendar.YEAR);
			// 月
			int month = ca1.get(Calendar.MONTH);
			int week = ca1.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			int dayWeek = ca1.get(Calendar.DAY_OF_WEEK);
			week++;
			Calendar ca2 = Calendar.getInstance();
			ca2.set(Calendar.YEAR, year);
			ca2.set(Calendar.MONTH, month);
			ca2.set(Calendar.DAY_OF_WEEK, dayWeek);
			ca2.set(Calendar.DAY_OF_WEEK_IN_MONTH, week);
			// 年
			int year2 = ca2.get(Calendar.YEAR);
			// 月
			int month2 = ca2.get(Calendar.MONTH);
			if (year != year2 || month != month2) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 *            年份
	 * @return 闰年返回true，平年返回false
	 */
	public static boolean isLeapYear(int year) {
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return true;
		}
		return false;
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
	 * 比较第一个参数时间和第二个参数时间的大小
	 * 
	 * @param value1
	 *            时间1
	 * @param value2
	 *            时间2
	 * @return int 小于0 : value1 小于 value2  
	 * 				0 : value1 等于value2
	 * 				大于0 : value1 大于value2
	 */
	public static int compDateTime(String value1, String value2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.coverString2Date(value1, DateUtil.DATETIME_PATTERN));
		cal2.setTime(DateUtil.coverString2Date(value2, DateUtil.DATETIME_PATTERN));
		return cal1.compareTo(cal2);
	}
}
