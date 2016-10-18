/*	
 * @(#)BINOLCM34_Service.java     1.0 @2013-1-9		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 帮助Service
 *
 * @author jijw
 *
 * @version  2013-1-9
 */
public class BINOLCM34_Service extends BaseService {
	
	/**
	 * 取得菜单ID对应的是否有帮助文档(HelpFlag)
	 * @param map
	 * @return
	 */
	public String getHelpFlagByMenu(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM34.getHelpFlagByMenu");
		return (String)baseServiceImpl.get(paramMap);
	}

}
