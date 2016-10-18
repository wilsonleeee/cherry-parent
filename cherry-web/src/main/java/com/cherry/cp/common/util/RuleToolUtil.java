/*	
 * @(#)RuleToolUtil.java     1.0 2011/7/18		
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
package com.cherry.cp.common.util;

import java.util.Map;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;

/**
 * 规则处理工具类
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class RuleToolUtil {
	
	/**
	 * 取得规则
	 * 
	 * @param map
	 * 			规则内容
	 * @param template
	 *          规则模板
	 * @return String 
	 * 			规则
	 */
	public static String getRuleCondition(String conditon, String template) {
		if (!CherryChecker.isNullOrEmpty(template)) {
			if (conditon == null) {
				conditon = "";
			}
			String key = "\\$\\{CONDITION\\}";
			template = template.replaceAll(key, conditon);
		}
		return template;
	}
	
	/**
	 * 取得规则
	 * 
	 * @param map
	 * 			规则内容
	 * @param template
	 *          规则模板
	 * @return String 
	 * 			规则
	 */
	public static String getRule(Map<String, Object> map, String template) {
		if (!CherryChecker.isNullOrEmpty(template)) {
			for(Map.Entry<String, Object> en : map.entrySet()){
				String key = "\\$\\{" + en.getKey() + "\\}";
				Object value = en.getValue();
				value = (null == value)? "" : value;
				template = template.replaceAll(key, String.valueOf(value));
			}
			if (template.indexOf("${") >= 0) {
				return null;
			} 
		}
		return template;
	}
	
	
	
	/**
	 * 取得查询字段对应的KEY
	 * 
	 * @param name
	 * 			要查询的字段
	 * @return String 
	 * 			字段对应的KEY
	 */
	public static String getSearchNameKey(String name) {
		return "var_" + name + "";
	}
	
	/**
	 * 取得资源文件中的定义内容
	 * 
	 * @param key
	 * 			定义的字段
	 * @return String 
	 * 			定义的内容
	 */
	public static String getText(String key) {
		return PropertiesUtil.pps.getProperty(key);
	}
}