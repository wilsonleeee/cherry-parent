/*	
 * @(#)BINOLCM42_Service.java     1.0 2011/03/29		
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

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 组织结构共通Service
 * 
 * @author lipc
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLCM42_Service extends BaseService {
	

	/**
	 * 取得品牌所以部门List
	 * 
	 * @param paramMap
	 * @return
	 */
	@Cacheable(value="CherryAllDepartCache")
	public List<Map<String, Object>> getAllDepartList(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getAllDepartList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getCounterList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得渠道List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getChannelList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得用户权限部门
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Integer> getDepartPrivilegeList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getDepartPrivilegeList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得用户权限部门类型
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<String> getDepartType(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getDepartType");

		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 取得实体仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepotList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getDepotList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得逻辑仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getLgcInventoryList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM42.getLgcInventoryList");

		return baseServiceImpl.getList(paramMap);
	}
}
