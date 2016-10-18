/*	
 * @(#)BINOLPLUPM04_Service.java     1.0 2010/12/28		
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

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 安全策略Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM04_Service extends BaseService{

	/**
	 * 取得密码安全配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getPwConfInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPLUPM04.getPwConfInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
}
