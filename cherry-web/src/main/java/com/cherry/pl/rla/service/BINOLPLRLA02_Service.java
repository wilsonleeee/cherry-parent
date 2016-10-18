/*
 * @(#)BINOLPLRLA02_Service.java     1.0 2010/11/01
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

package com.cherry.pl.rla.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 岗位类别角色分配Service
 * 
 * @author WangCT
 * @version 1.0 2010.11.01
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA02_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA02.getPositionCategoryList");
		return baseServiceImpl.getList(parameterMap);
	}

}
