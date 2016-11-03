/*
 * @(#)BINBEIFCOU01_Service.java     1.0 2010/11/12
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

package com.cherry.ia.cou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 柜台下发Service
 * 
 * 
 * @author Jijw
 * @version 1.0 2013.12.08
 */
public class BINBEIFCOU02_Service extends BaseService {

	/**
	 * 取得过期的柜台组织结构ID
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExpiredOrganization(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU02.getExpiredOrganization");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 将过期的柜台无效掉
	 * @param cntList
	 */
	public void updateExpiredCnt(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU02.updateExpiredCnt");
		baseServiceImpl.update(paramMap);
	}
	/**
	 * 将过期的组织结构无效掉
	 * @param cntList
	 */
	public void updateExpiredOrg(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU02.updateExpiredOrg");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除柜台接口表
	 * @param map
	 */
	public void clearCounterSCS(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFCOU02.clearCounterSCS");
		ifServiceImpl.remove(paramMap);

	}
	
	/**
	 * 取得柜台信息(新老后台交互时使用)
	 * 
	 * @param map 查询条件
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFCOU02.getCounterInfo");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 
	 * 插入柜台接口表
	 * 
	 * @param counterMap 插入内容
	 * @return 柜台ID
	 * 
	 */
	public void insertCounterSCS(List<Map<String, Object>> cntList) {
		ifServiceImpl.saveAll(cntList, "BINBEIFCOU02.insertCounterSCS");
	}
	
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
		"BINBEIFCOU02.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}

}
