package com.cherry.cm.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.cherry.cm.util.DateUtil;

/**
 * 共通检查类
 * @author dingyc
 * @date 2010-09-10
 *
 */
public class CherryBatchChecker {
	
	/**
	 * 检测指定对象是否为null
	 * 如果为null则返回true
	 * 否则返回false
	 * @param value
	 * @return
	 */
	public static boolean isNull(Object value){
		if(null==value){
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定对象是否为空字符串
     * 如果对象不为字符串或为长度大于0的字符串则返回false，否则返回true。即只有当对象为""时返回true
	 * @param value
	 * @param trim 可选参数，是否要调用String的trim()方法
	 * @return 
	 */
	public static boolean isEmptyString(Object value,boolean... trim){
		
		if (!(value instanceof String)) {
            return false;
        } else {
            String s = (String) value;

            if (trim.length>0 && trim[0]==true) {
                s = s.trim();
            }

            if (s.length() == 0) {
                return true;
            }            
            return false;
        }
	}
	
	/**
	 * 检测指定对象是否为null或者空字符串
	 * 如果对象为null或空字符串则返回true，否则返回false
	 * @param value
	 * @param trim 可选参数，当value为String类型时，是否调用trim后再判断其长度
	 * @return 
	 */
	public static boolean isNullOrEmpty(Object value,boolean... trim){		
		if(null==value){
			return true;
		}		
		if(value instanceof String){			
			String s = (String) value;
            if (trim.length>0 && trim[0]==true) {
                s = s.trim();
            }
            if (s.length() == 0) {
                return true;
            } 
		}		
		return false;
	}
	
	/**
	 * 检测指定字符串是否全由数字组成
	 * 如果字符串全由数字组成则返回true，否则返回false	 
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value){
		if(value==null||value.length()==0){
			return false;
		}
		boolean ret = value.matches("[0-9]*");		
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定字符串是否全由英文字母组成
	 * 如果字符串全由字母组成则返回true，否则返回false	 
	 * @param value
	 * @return
	 */
	public static boolean isAlphabetic(String value){
		if(value==null||value.length()==0){
			return false;
		}		
		boolean ret = value.matches("[a-zA-Z]*");		
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定字符串是否符合邮件地址格式
	 * 如果字符串符合邮件地址格式则返回true，否则返回false 
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value){
		if(value==null||value.length()==0){
			return false;
		}		
		boolean ret = value.matches("\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b");		
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定字符串是否符合网址格式
	 * 如果字符串符合网址格式则返回true，否则返回false 
	 * @param value
	 * @return
	 */
	public static boolean isURL(String value){
		if(value==null||value.length()==0){
			return false;
		}		
		if (value.startsWith("https://")) {
            // URL doesn't understand the https protocol, hack it
			value = "http://" + value.substring(8);
        }
        try {
            new URL(value);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
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
		Date d1 = DateUtil.coverString2Date(value1);
		Date d2 = DateUtil.coverString2Date(value2);
		return d1.compareTo(d2);
	}
}
