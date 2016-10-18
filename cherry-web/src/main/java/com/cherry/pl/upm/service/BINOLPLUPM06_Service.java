/*	
 * @(#)BINOLPLUPM06_Service.java     1.0 2010/12/28		
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
 * 安全策略编辑Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
public class BINOLPLUPM06_Service extends BaseService {
	/**
	 * 更新密码安全配置表
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePasswordConfig(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPLUPM06.updatePasswordConfig");
		return baseServiceImpl.update(map);

	}
}
