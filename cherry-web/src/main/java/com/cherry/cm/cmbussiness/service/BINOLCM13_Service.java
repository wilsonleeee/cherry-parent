/*	
 * @(#)BINOLCM13_Service.java     1.0 2011/03/29		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 组织结构共通Service
 * 
 * @author lipc
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLCM13_Service extends BaseService {
	

	/**
	 * 取得下级组织结构子集List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getDepartList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据区域信息取得权限部门List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepartList2(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getDepartList2");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据部门Id取得柜台主管List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getEmployeeList");
		
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据柜台主管员工Id取得柜台List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getCounterList");

		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 取得渠道List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getChannelList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得第一级部门List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getFirstDepartList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getFirstDepartList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据柜台主管ID取得该柜台主管的部门ID
	 * 
	 * @param paramMap
	 * @return
	 */
	public String getDepartId(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getDepartId");

		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得用户权限部门类型
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<String> getDepartType(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getDepartType");

		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得实体仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getDepotList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getDepotList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得逻辑仓库List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getLgcInventoryList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getLgcInventoryList");

		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得渠道柜台信息List
	 * 
	 * @param map 查询条件
	 * @return 渠道柜台信息List
	 */
	public List<Map<String, Object>> getChannelCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM13.getChannelCounterList");
		return baseServiceImpl.getList(parameterMap);
	}
}
