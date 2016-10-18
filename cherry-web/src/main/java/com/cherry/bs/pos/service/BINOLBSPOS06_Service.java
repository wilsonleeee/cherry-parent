/*
 * @(#)BINOLBSPOS06_Service.java     1.0 2010/10/27
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

package com.cherry.bs.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 岗位类别一览画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS06_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 查询岗位类别总数
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别总数
	 */
	public int getPositionCategoryCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS06.getPositionCategoryCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 检索条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS06.getPositionCategoryList");
		return baseServiceImpl.getList(parameterMap);
	}

}
