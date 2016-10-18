/*
 * @(#)BINOLPLRLA04_Service.java     1.0 2010/10/27
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
 * 用户角色分配Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA04_Service {
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 查询用户总数
	 * 
	 * @param Map
	 * @return 返回用户总数
	 */
	public int getUserInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA04.getUserInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询用户信息
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getUserInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLRLA04.getUserInfoList");
		return baseServiceImpl.getList(parameterMap);
	}

}
