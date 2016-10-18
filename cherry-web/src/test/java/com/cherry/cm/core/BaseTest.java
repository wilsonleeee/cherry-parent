package com.cherry.cm.core;

import java.util.Map;
import java.util.Map.Entry;

import com.cherry.cm.util.DataUtil;
import com.thoughtworks.selenium.Selenium;

/*  
 * @(#)BaseTest.java    1.0 2011-12-20     
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
public class BaseTest {

	private static final String VALUE = "value=";
	
	/**
	 * 系统登录，使用selenium+Junit测试系统需要登录，所以将登陆共通出来，在这里可以设定用户登录名，登录密码，代码运行速度以及弹出框等待时间，默认的代码运行速度为2000毫秒，弹出框最长等待时间为30000毫秒。
	 * @param selenium
	 * @param map 系统登录参数
	 * 
	 * */
	public static void login(Selenium selenium, Map<String, Object> map) {

		//根据测试数据中设定的代码执行速度设定,默认为2000毫秒
		selenium.setSpeed(map.get("speed") == null ? "2000" : String
				.valueOf(map.get("speed")));
		selenium.open("/Cherry/init");
		selenium.type("txtname", (String) map.get("loginName"));
		selenium.type("txtpsd", (String) map.get("password"));
		selenium.click("btnlogin");
		selenium.waitForPageToLoad(map.get("waitTime") == null ? "30000"
				: String.valueOf(map.get("waitTime")));
	}

	/**
	 * 将测试数据填充到页面
	 * @param selenium Selenium对象
	 * @param clzz 测试类
	 * @param caseName 测试case名称(测试方法名)
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static void fillData2Page(Selenium selenium, Class clzz, String caseName) throws Exception{

		Map<String,Object> formData = DataUtil.getFormData(clzz,caseName);
		
		for (Entry<String, Object> et : formData.entrySet()) {
			String key = et.getKey();
			Map<String, Object> temp = (Map<String, Object>) et.getValue();
			
			int ElemType = Integer.parseInt(String.valueOf(temp.get("type")));
			
			switch (ElemType) {
			case 1:
				selenium.type(key, String.valueOf(temp.get("value")));
				break;
			case 3:
				selenium.check(key);
				break;
			case 2:
				selenium.select(key, VALUE
						+ String.valueOf(temp.get("value")));
				break;
			default:
				break;
			}
		}

	}
}
