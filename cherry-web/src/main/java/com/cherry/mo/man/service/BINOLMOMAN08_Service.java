/*
 * @(#)BINOLMOMAN08_Service.java     1.0 2012/11/15
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
package com.cherry.mo.man.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * POS配置项Service
 * 
 * @author liuminghao
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN08_Service extends BaseService {

	/**
	 * 取得POS配置项总数
	 * 
	 * @param map
	 * @return POS配置项总数
	 */
	public int getPosConfigInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN08.getConfigInfoCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得POS配置项List 新数据
	 * 
	 * @param map
	 * @return POS配置项List
	 */
	public List getPosConfigNewInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN08.getConfigNewInfoList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得POS配置项INFO
	 * 
	 * @param map
	 * @return
	 */
	public Map getPosConfig(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN08.getPosConfig");
		return (Map) baseServiceImpl.get(map);
	}

	/**
	 * 新增配置项数据
	 * 
	 * @param map
	 */
	public int addPosConfig(Map<String, Object> map) throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN08.addPosConfig");
		return baseServiceImpl.saveBackId(parameterMap);
	}

	/**
	 * 新增配置项数据
	 * 
	 * @param map
	 */
	public void updatePosConfig(Map<String, Object> map) throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN08.updatePosConfig");
		baseServiceImpl.update(parameterMap);
	}

	/**
	 * 取得配置项数据List(WS结构组装使用)
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPosConfigWithWS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMOMAN08.getPosConfigWithWS");
		return baseServiceImpl.getList(map);
	}
}
