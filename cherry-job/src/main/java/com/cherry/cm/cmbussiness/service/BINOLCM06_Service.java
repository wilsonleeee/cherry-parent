/*
 * @(#)BINOLCM06_Service.java     1.0 2011/06/29
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 工作流 共通 Service
 * 
 * @author hub
 * @version 1.0 2011.06.29
 */
public class BINOLCM06_Service extends BaseService{
	
	/**
	 * 取得配置数据库品牌List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			配置数据库品牌List
	 */
	public List<Map<String, Object>> getConfBrandInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM06.getConfBrandInfoList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			品牌信息
	 */
	public Map<String, Object> getOSBrandInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织Code
		paramMap.put("orgCode", map.get("orgCode"));
		// 品牌code
		paramMap.put("brandCode", map.get("brandCode"));
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM06.getOSBrandInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据品牌ID取得品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//品牌ID
		paramMap.put("brandInfoId", map.get(CherryBatchConstants.BRANDINFOID));
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM06.getBrandCode");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 插入文件储存表
	 * 
	 * @param map
	 * @return
	 */
	public void insertFileStore(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOLCM06.insertFileStore");
		baseConfServiceImpl.save(map);
	}
	
	/**
	 * 查询指定工作流文件的件数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			查询指定工作流文件的件数
	 * 
	 */
	public int getBrandJobCount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM06.getBrandJobCount");
		return baseConfServiceImpl.getSum(map);
	}
}
