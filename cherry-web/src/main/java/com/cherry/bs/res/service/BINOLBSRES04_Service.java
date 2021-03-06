/*  
 * @(#)BINOLBSRES04_Service.java     1.0 2011/05/31      
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
package com.cherry.bs.res.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLBSRES04_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	public int addRes(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES04.addRes");
		return baseServiceImpl.saveBackId(map);
	}

	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES04.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
}