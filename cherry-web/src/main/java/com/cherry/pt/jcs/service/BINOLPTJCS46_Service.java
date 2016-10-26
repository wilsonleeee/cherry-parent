/*  
 * @(#)BINOLPTJCS46_Service.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLPTJCS46_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 插入产品价格方案主表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPrtPriceSolu(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS46.insertPrtPriceSolu");
		return baseServiceImpl.saveBackId(map);
	}
	

	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS46.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
}