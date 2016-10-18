/*	
 * @(#)BINOLPLUPM05_Service.java     1.0 2010/12/28		
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
package com.cherry.pl.upm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 安全策略添加Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
public class BINOLPLUPM05_Service extends BaseService {
	/**
	 * 插入密码安全配置表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPasswordConfig(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPLUPM05.insertPasswordConfig");
		baseServiceImpl.save(map);
	}
}
