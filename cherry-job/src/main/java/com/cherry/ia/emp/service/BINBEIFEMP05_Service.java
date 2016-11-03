/*
 * @(#)BINBEIFEMP04_Service.java     1.0 2013/10/29
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
package com.cherry.ia.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 刷新老后台U盘绑定柜台数据Service
 * 
 * @author JiJW
 * @version 1.0 2013-10-29
 */
public class BINBEIFEMP05_Service extends BaseService {
	
	/**
	 * 
	 * 从新后台取得具有U盘与柜台的关系数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUDiskCounterList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		if(paramMap.get("operationType") == null) {
			paramMap.put("operationType", "1");
		}
		if(paramMap.get("businessType") == null) {
			paramMap.put("businessType", "0");
		}
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP05.getUDiskCounterList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 清空老后台U盘柜台关系表
	 * 
	 * @param map
	 */
	public void truncateUDiskCounter(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP05.truncateUDiskCounter");
		witBaseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 添加用户信息
	 * 
	 * @param map 添加内容
	 */
	public void addUDiskCounter(List<Map<String, Object>> list) {
		witBaseServiceImpl.saveAll(list, "BINBEIFEMP05.addUDiskCounter");
	}
	
}
