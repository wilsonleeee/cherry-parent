/*  
 * @(#)PropertiesFile.java     1.0 2012/04/19      
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
 * 资源文件工具类
 * 
 * @author hub
 * 
 */
public class PropertiesUtil {
	
	public static PropertiesFile pf;
	
	public static String getText(String name) {
		if (null == pf) {
			pf = new PropertiesFile();
		}
		return pf.getText(name);
	}
	
	public static String getText(String name, String[] args) {
		if (null == pf) {
			pf = new PropertiesFile();
		}
		return pf.getText(name, args);
	}
}
