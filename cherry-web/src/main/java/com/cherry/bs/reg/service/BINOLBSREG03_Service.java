/*
 * @(#)BINOLBSREG03_Service.java     1.0 2011/11/23
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
package com.cherry.bs.reg.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域更新画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG03_Service extends BaseService {
	
	/**
	 * 更新区域信息
	 * 
	 * @param map 更新内容
	 */
	public int updateRegion(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG03.updateRegion");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 区域结构节点移动
	 * 
	 * @param map 更新内容
	 */
	public int updateRegionNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG03.updateRegionNode");
		return baseServiceImpl.update(parameterMap);
	}

}
