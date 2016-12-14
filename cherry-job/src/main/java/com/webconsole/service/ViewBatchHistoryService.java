/*
 * @(#)LoginService.java     1.0 2010/10/12
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
package com.webconsole.service;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ViewBatchHistoryService {

	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 *  取得Job运行履历
	 *
	 * @param map
	 * @return List<Map<String,Object>>
	 * 		运行履历
	 */
	public List<Map<String,Object>> getJobFailureRunHistory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "ViewBatchHistory.getJobFailureRunHistory");
		return baseServiceImpl.getList(parameterMap);
	}

}
