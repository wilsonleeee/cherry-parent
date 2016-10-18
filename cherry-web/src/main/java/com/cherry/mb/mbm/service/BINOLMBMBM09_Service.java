/*	
 * @(#)BINOLMBMBM09_Service.java     1.0 2012/01/07		
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员搜索画面Service
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLMBMBM09_Service extends BaseService {
	
	/**
	 * 查询会员问卷信息
	 * 
	 * @param map 检索条件
	 * @return 会员问卷信息
	 */
	public List<Map<String, Object>> getMemPaperList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM09.getMemPaperList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 检索条件
	 * @return 会员等级信息List
	 */
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM09.getMemberLevelInfoList");
		return baseServiceImpl.getList(parameterMap);
	}

}
