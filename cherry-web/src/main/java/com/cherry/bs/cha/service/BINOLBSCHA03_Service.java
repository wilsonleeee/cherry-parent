/*  
 * @(#)BINOLBSCHA03_Service.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLBSCHA03_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> channelDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA03.channelDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}

	public int updateChannel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA03.updateChannel");
		return baseServiceImpl.update(map);
	}
	
	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA03.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
}