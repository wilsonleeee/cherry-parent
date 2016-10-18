/*	
 * @(#)BIRptQuery.java     1.0 2010/10/12		
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

/**
 * 
 * 拼装BI报表单个检索条件,支持多种符号的拼装
 * 
 * @author WangCT
 * 
 */
public class BIRptQuery {
	
	/**
	 * 
	 * BI检索条件拼装处理
	 * 
	 * @param name 待拼装的key
	 * @param name 待拼装的符号
	 * @param value 待拼装的value
	 * @param isDisPlay 拼装的检索条件是否用来画面表示(true：画面表示用，false：检索用)
	 * 
	 */
	public static String getBIRptQuery(String name, String mark, String[] value, boolean isDisPlay) {
		if(name != null && !"".equals(name) && mark != null && !"".equals(mark)) {
			if("EQ".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, EQUAL, value[0]);
				} else {
					return equal(name, value[0]);
				}
			} else if("GE".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, GREATEREQUAL, value[0]);
				} else {
					return greaterEqual(name, value[0]);
				}
			} else if("LE".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, LESSEQUAL, value[0]);
				} else {
					return lessEqual(name, value[0]);
				}
			} else if("NE".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, NOTEQUAL, value[0]);
				} else {
					return notEqual(name, value[0]);
				}
			} else if("BL".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, LIKE, value[0]+"%");
				} else {
					return beforeLike(name, value[0]);
				}
			} else if("AL".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, LIKE, "%"+value[0]);
				} else {
					return afterLike(name, value[0]);
				}
			} else if("LK".equals(mark)) {
				if(isDisPlay) {
					return getQueryDisPlay(name, LIKE, "%"+value[0]+"%");
				} else {
					return like(name, value[0]);
				}
			} else if("IN".equals(mark)) {
				if(isDisPlay) {
					if(value != null && value.length > 0) {
						StringBuffer _value = new StringBuffer();
						_value.append("(");
						for(String s : value) {
							_value.append(s+",");
						}
						_value.deleteCharAt(_value.length()-1);
						_value.append(")");
						return getQueryDisPlay(name, IN, _value.toString());
					} else {
						return null;
					}
				} else {
					return in(name, value);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 相等条件的拼装(key=value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String equal(String name, String value) {
		return getQuery(name, EQUAL, value);
	}
	
	/**
	 * 
	 * 不相等条件的拼装(key!=value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String notEqual(String name, String value) {
		return getQuery(name, NOTEQUAL, value);
	}
	
	/**
	 * 
	 * 大于条件的拼装(key>value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String greaterThan(String name, String value) {
		return getQuery(name, GREATERTHAN, value);
	}
	
	/**
	 * 
	 * 大于相等条件的拼装(key>=value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String greaterEqual(String name, String value) {
		return getQuery(name, GREATEREQUAL, value);
	}
	
	/**
	 * 
	 * 小于条件的拼装(key<value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String lessThan(String name, String value) {
		return getQuery(name, LESSTHAN, value);
	}
	
	/**
	 * 
	 * 小于等于条件的拼装(key<=value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String lessEqual(String name, String value) {
		return getQuery(name, LESSEQUAL, value);
	}
	
	/**
	 * 
	 * 前方一致条件的拼装(key LIKE value%)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String beforeLike(String name, String value) {
		return getQuery(name, LIKE, value+"%");
	}
	
	/**
	 * 
	 * 后方一致条件的拼装(key LIKE %value)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String afterLike(String name, String value) {
		return getQuery(name, LIKE, "%"+value);
	}
	
	/**
	 * 
	 * 中间一致条件的拼装(key LIKE %value%)
	 * 
	 * @param name 待拼装的key
	 * @param value 待拼装的value
	 * 
	 */
	public static String like(String name, String value) {
		return getQuery(name, LIKE, "%"+value+"%");
	}
	
	/**
	 * 
	 * IN条件的拼装(key IN (value1,value2))
	 * 
	 * @param name 待拼装的key
	 * @param values 待拼装的value
	 * 
	 */
	public static String in(String name, String[] values) {
		if(values != null && values.length > 0) {
			StringBuffer value = new StringBuffer();
			value.append("(");
			for(String s : values) {
				value.append("\"" + s + "\""+ ",");
			}
			value.deleteCharAt(value.length()-1);
			value.append(")");
			return getQuery(name, IN, value.toString());
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 单条件的拼装
	 * 
	 * @param name 待拼装的key
	 * @param mark 待拼装的符号
	 * @param value 待拼装的value
	 * 
	 */
	public static String getQuery(String name, String mark, String value) {
		
		if(name != null && !"".equals(name)) {
			return "("+name+" "+mark+" \""+value+"\")";
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 单条件的拼装(画面表示用)
	 * 
	 * @param name 待拼装的key
	 * @param mark 待拼装的符号
	 * @param value 待拼装的value
	 * 
	 */
	public static String getQueryDisPlay(String name, String mark, String value) {
		
		if(name != null && !"".equals(name)) {
			return "("+name+" "+mark+" "+value+")";
		} else {
			return null;
		}
	}
	
	private static String EQUAL = "=";
	private static String NOTEQUAL = "!=";
	private static String GREATERTHAN = ">";
	private static String GREATEREQUAL = ">=";
	private static String LESSTHAN = "<";
	private static String LESSEQUAL = "<=";
	private static String LIKE = "LIKE";
	private static String IN = "IN";

}
