/*  
 * @(#)PropertiesUtil.java     1.0 2011/05/31      
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

import java.util.Map;
import java.util.Properties;

import com.cherry.cm.util.CherryUtil;

public class PropertiesUtil {
	public static Properties pps;
	
	/**
	 * 取得BI配置属性信息
	 * @param brandCode
	 * @param code
	 * @return
	 * @throws Exception 
	 * @author jijw
	 */
	@SuppressWarnings("unchecked")
	public static String getBIConfigValue(String brandCode,String code) throws Exception{
		
		/** BIConfig配置信息格式
		{
		    "MGP": {
		        "IP": "192.168.101.28",
		        "Port": "6001",
		        "WebURL": "http: //192.168.101.28: 8180/datalizer/Lon.rse",
		        "UserName": "013264e18b349974834d3174a33c448d",
		        "UserPassword": "42b1a7e1c5ff2183"
		    },
		    "LQX": {
		        .....
		    }
		}
		**/
		
		// 读取配置文件的BIConfig配置信息
		String BIConfig = pps.getProperty("BIConfig", "");
		Map<String,Object> BIConfigMap = CherryUtil.json2Map(BIConfig);
		
		// 读取品牌BI配置信息
		Map<String,Object> brandBIConfigMap = (Map<String,Object>)BIConfigMap.get(brandCode);
		// 若未读取到brandCode对应的品牌BI配置信息，则读取-9999全体BI配置信息
		if(null == brandBIConfigMap){
			brandBIConfigMap = (Map<String,Object>)BIConfigMap.get(String.valueOf(CherryConstants.Brand_CODE_ALL));
		}
		
		// BI配置信息
		String configValue = String.valueOf(brandBIConfigMap.get(code));
		
		return configValue;
	}
}
