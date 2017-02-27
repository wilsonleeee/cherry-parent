/*  
 * @(#)CherryChecker.java     1.0 2011/05/31      
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
package com.cherry.cm.core;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cherry.cm.util.DateUtil;

/**
 * 共通检查类
 * 
 * @author dingyc
 * @date 2010-09-10
 * 
 */
public class CherryChecker {

	/**
	 * 检测指定对象是否为null 如果为null则返回true 否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNull(Object value) {
		if (null == value) {
			return true;
		}
		return false;
	}

	/**
	 * 检测指定对象是否为空字符串 如果对象不为字符串或为长度大于0的字符串则返回false，否则返回true。即只有当对象为""时返回true
	 * 
	 * @param value
	 * @param trim
	 *            可选参数，是否要调用String的trim()方法
	 * @return
	 */
	public static boolean isEmptyString(Object value, boolean... trim) {

		if (!(value instanceof String)) {
			return false;
		} else {
			String s = (String) value;

			if (trim.length > 0 && trim[0] == true) {
				s = s.trim();
			}

			if (s.length() == 0) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 检测指定对象是否为null或者空字符串 如果对象为null或空字符串则返回true，否则返回false
	 * 
	 * @param value
	 * @param trim
	 *            可选参数，当value为String类型时，是否调用trim后再判断其长度
	 * @return
	 */
	public static boolean isNullOrEmpty(Object value, boolean... trim) {
		if (null == value) {
			return true;
		}
		if (value instanceof String) {
			String s = (String) value;
			if (trim.length > 0 && trim[0] == true) {
				s = s.trim();
			}
			if (s.length() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测指定字符串是否全由正数组成 如果字符串全由正数组成则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("[0-9]*");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定字符串是否全由正数和负数组成 如果字符串全由正数和负数组成则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPositiveAndNegative(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("^-?[0-9]+");
		if (ret) {
			return true;
		}
		return false;
	}

	/**
	 * 检测指定字符串是否全由英文字母组成 如果字符串全由字母组成则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isAlphabetic(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("[a-zA-Z]*");
		if (ret) {
			return true;
		}
		return false;
	}

	/**
	 * 检测指定字符串是否全由英文字母或者数字组成 如果字符串全由字母或者数字组成则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isAlphanumeric(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("[0-9a-zA-Z]*");
		if (ret) {
			return true;
		}
		return false;
	}

	/**
	 * 检测指定字符串是否符合邮件地址格式 如果字符串符合邮件地址格式则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value
				.matches("\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b");
		if (ret) {
			return true;
		}
		return false;
	}

	/**
	 * 检测指定字符串是否符合网址格式 如果字符串符合网址格式则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isURL(String value) {
		if (value == null || value.length() == 0) {
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
	 * 检测字符串是否符合时间格式hh:MM:ss
	 * 
	 * @param value
	 *            
	 * @return 如果字符串符合日期格式则返回true，否则返回false
	 */
	public static boolean checkTime(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value
				.matches("^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");
		if (ret) {
			return true;
		}
		return false;
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

	/**
	 * 检测指定字符串是否符合邮编
	 * 
	 * @param zip
	 *            待验证的字符串
	 * @return 如果字符串符合邮编格式则返回true，否则返回false
	 */
	public static boolean isZipValid(String zip) {
		if (zip == null || "".equals(zip)) {
			return false;
		}
		return zip.matches("^[0-9]\\d{5}(?!\\d)$");
	}

	/**
	 * 检测指定字符串是否符合浮点数，而且整数位数不大于传入参数len1，小数位数不大于传入参数len2
	 * 
	 * @param value
	 *            待验证的字符串
	 * @param len1
	 *            整数位数最大范围
	 * @param len2
	 *            小数位数最大范围
	 * @return 如果字符串符合浮点数，而且整数位数不大于传入参数len1，小数位数不大于传入参数len2，则返回true，否则返回false
	 */
	public static boolean isFloatValid(String value, int len1, int len2) {
		if (value == null || "".equals(value)) {
			return false;
		}
		boolean checkFloat = false;
		checkFloat = value.matches("^([0-9]\\d*)(\\.\\d+)?$");
		if (checkFloat) {
			String[] ar = value.split("[.]");
			if (ar.length == 2) {
				if (ar[0].length() > len1 || ar[1].length() > len2) {
					checkFloat = false;
				}
			} else if (ar.length == 1) {
				if (ar[0].length() > len1) {
					checkFloat = false;
				}
			} else {
				checkFloat = false;
			}
		}
		return checkFloat;
	}
	
	/**
	 * 检测指定字符串是否符合浮点数，而且整数位数不大于传入参数len1，小数位数不大于传入参数len2，可以为负数
	 * 
	 * @param value
	 *            待验证的字符串
	 * @param len1
	 *            整数位数最大范围
	 * @param len2
	 *            小数位数最大范围
	 * @return 如果字符串符合浮点数，而且整数位数不大于传入参数len1，小数位数不大于传入参数len2，则返回true，否则返回false
	 */
	public static boolean isDecimal(String value, int len1, int len2) {
		if (value == null || "".equals(value)) {
			return false;
		}
		boolean checkFloat = false;
		checkFloat = value.matches("^\\-?([0-9]\\d*)(\\.\\d+)?$");
		if (checkFloat) {
			String[] ar = value.split("[.]");
			if (ar.length == 2) {
				if (ar[0].replaceAll("-", "").length() > len1 || ar[1].length() > len2) {
					checkFloat = false;
				}
			} else if (ar.length == 1) {
				if (ar[0].replaceAll("-", "").length() > len1) {
					checkFloat = false;
				}
			} else {
				checkFloat = false;
			}
		}
		return checkFloat;
	}

	/**
	 * 检测指定字符串是否符合小时或分钟格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @param flg
	 *            为true时表示验证小时，为false时表示验证分钟
	 * @return 如果字符串否符合小时或分钟格式，则返回true，否则返回false
	 */
	public static boolean isTimeValid(String value, boolean flg) {
		if (value == null || "".equals(value)) {
			return false;
		}
		return (value.matches("^\\d+$") && Integer.parseInt(value) <= (flg ? 23
				: 59));
	}

	/**
	 * 检测指定字符串是否符合身份证号格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串否符合身份证号格式，则返回true，否则返回false
	 */
	public static boolean isICardValid(String value) {
		if (isNullOrEmpty(value)) {
			return false;
		}
		if (value.length() != 15 && value.length() != 18) {
			return false;
		}
		 return value.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
	}
	
	/**
	 * 检测指定字符串是否符合手机号码格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串否符合手机号码格式，则返回true，否则返回false
	 */
	public static boolean isPhoneValid(String value, String mobileRule) {
		if (isNullOrEmpty(value)) {
			return false;
		}
		if (value.length() != 11) {
			return false;
		}
		return value.matches(mobileRule);
	}
	
	/**
	 * 检测指定字符串是否符合电话号码格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串否符合电话号码格式，则返回true，否则返回false
	 */
	public static boolean isTelValid(String value) {
		if (isNullOrEmpty(value)) {
			return false;
		}
		if (value.length() > 20) {
			return false;
		}
		return value.matches("^[0-9-]+$");
	}
	
	/***
	 * 检测指定字符串是否符合柜台电话号码格式
	 * 目前格式为只能包含：【数字、"+"、"-"、括号、"#"】，顺序暂时不考虑
	 * @param value
	 * 				待验证的字符串
	 * @return 如果字符串否符合柜台电话号码格式，则返回true，否则返回false
	 */
	public static boolean isCounterTelValid(String value) {
		if(isNullOrEmpty(value)) {
			return false;
		}
		if(value.length() > 20) {
			return false;
		}
		return value.matches("^[0-9-\\+()#]+$");
	}
	
	/**
	 * 检测指定字符串是否有重复字符
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串没有重复，则返回true，否则返回false
	 */
	public static boolean isNoDuplicate(String value) {
		if (!isNullOrEmpty(value)) {
			char[] charArr = value.toCharArray();
			for (int i = 0; i < charArr.length; i++) {
				String str = String.valueOf(charArr[i]);
				if (i != value.lastIndexOf(str)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 检测指定字符串是否有重复
	 * 
	 * @param value
	 *            待验证的字符串
	 * @param splitChar
	 *            用于分割的字符
	 * @return 如果字符串没有重复，则返回true，否则返回false
	 */
	public static boolean isNoDupString(String value, String splitChar) {
		if (null != value && value.length() > 1) {
			String[] strArr = value.split(splitChar);
			for (int i = 0; i < strArr.length - 1; i++) {
				for (int j = i+1; j < strArr.length; j++) {
					if (strArr[i].equals(strArr[j])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 检测指定字符串是否全由英文字母或者数字或者横杠组成 如果字符串全由字母或者数字或者横杠组成则返回true，否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmployeeCode(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("[-0-9a-zA-Z]+");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 产品编码验证
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isUnitCode(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value.matches("[.0-9a-zA-Z]+");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 字节数超过指定长度验证
	 * 
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static boolean isByteLength(String value, int len) throws Exception {
		int count = value.getBytes("GBK").length;
		if(count > len){
			return true;
		}
		return false;
	}
	
	/**
	 * 厂商编码和促销品条码验证
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPrmCode(String value) {
		if (value != null && !"".equals(value)) {
		      return value.matches("[-_0-9a-zA-Z.]+");
		  }
		  return false;
	}
	
	/**
	 * 产品厂商编码和产品条码验证
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isProCode(String value) {
		if (value != null && !"".equals(value)) {
		      return value.matches("[-_0-9a-zA-Z.]+");
		  }
		  return false;
	}
	
	/**
	 * 数据库版本号验证
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkDBVersion(String value) {
		if (value == null) {
			return false;
		}else{
			return value.matches("\\d{2}\\.\\d{2}\\.\\d{4}");
		}
	}
	
	/**
	 * 会员卡号验证规则（9位纯数字，倒数第二位固定为2，最后一位等于前7位数和的个位数。例如：688498023）
	 * 
	 * @param value 会员卡号
	 * @return 返回true表示验证通过，返回false表示验证不通过
	 */
	public static boolean checkSpecialMemCode(String value) {
		if(value == null || "".equals(value)) {
			return false;
		}
		if(value.matches("\\d{7}2\\d{1}")) {
			int t = 0;
			String e = "";
			for(int i = 0; i < value.length(); i++) {
				if(i < value.length() - 2) {
					t += Integer.parseInt(String.valueOf(value.charAt(i)));
				}
				if(i == value.length() - 1) {
					e = String.valueOf(value.charAt(i));
				}
			}
			String lastStr = String.valueOf(t);
			lastStr = lastStr.substring(lastStr.length()-1, lastStr.length());
			if(lastStr.equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过指定方法名验证会员卡号
	 * 
	 * @param funName 方法名
	 * @param value 会员卡号
	 * @return 返回true表示验证通过，返回false表示验证不通过
	 */
	public static boolean checkMemCodeByFun(String funName, String value) {
		if("null".equals(funName)) {
			return true;
		}
		try {
			Method method = CherryChecker.class.getMethod(funName, new Class[] {String.class});
			return (Boolean)method.invoke(CherryChecker.class, new Object[]{value});
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 会员卡号验证规则（[9位纯数字，倒数第二位固定为2，最后一位等于前7位数和的个位数]或者[手机号]）
	 * 
	 * @param value 会员卡号
	 * @return 返回true表示验证通过，返回false表示验证不通过
	 */
	public static boolean checkMemCode_01(String value) {
		if(CherryChecker.isPhoneValid(value, "(1[35784])[0-9]{9}") || CherryChecker.checkSpecialMemCode(value)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测指定字符串是否银行卡号格式
	 * 
	 * @param value
	 *            待验证的字符串
	 * @return 如果字符串否符合手机号码格式，则返回true，否则返回false
	 */
	public static boolean isBankCardValid(String value, String bankCardRule) {
		if (isNullOrEmpty(value)) {
			return false;
		}
		return value.matches(bankCardRule);
	}

	/**
	 * 检测指定字符串是否包含如下非法字符   半角：分号;  逗号,  单双引号'"  斜杠/\  问号?
	 *
	 * @param value 待验证的字符串
	 *
	 * @return 如果字符串含有非法字符返回true，否则返回false
     */
	public static boolean hasIllegalChar(String value) {
		if (value == null || "".equals(value)) {
			return true;
		}
		if (value.matches("^[^;,'\"/?\\\\]+$")) {
			return false;
		}
		return true;
	}
}
