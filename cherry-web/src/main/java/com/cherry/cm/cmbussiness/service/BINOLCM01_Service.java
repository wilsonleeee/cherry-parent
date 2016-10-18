/*		
 * @(#)BINOLCM01_Service.java     1.0 2010/10/12		
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
 * 
 * 共通基础处理
 * 
 * @author dingyc
 * @version 1.0 2010.10.12
 */
@SuppressWarnings("unchecked")
public class BINOLCM01_Service extends BaseService{

	
	/**
	 * 根据部门ID取得部门的相关信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getControlOrgListPrivilege(Map<String, Object> paramMap) {		
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getControlOrgListPrivilege");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}

	
	/**
	 * 取得指定部门的实体仓库List
	 * 
	 * @param Map
	 *			查询条件
	 *            
	 * @return List
	 * 				实体仓库List
	 */
	public List<Map<String, Object>> getInventoryList(String organizationId,String language) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		// 语言类型
		paramMap.put("language", language);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getInventoryList");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}

	/**
	 * 根据部门ID取得部门的相关信息
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getOrganizationInfoByID(Map<String, Object> paramMap) {		
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getOrganizationInfoByID");
		Map<String, Object>ret = (Map<String, Object>)baseServiceImpl.get(paramMap);
		return ret;
	}

	
	
	/**
	 * 根据指定条件取得部门的所有下级部门
	 * 加入了部门权限过滤
	 * @param organizationId 指定的部门ID
	 * @param userid 用户ID
	 * @param bussinesstype 业务类型
	 * @param language 语言
	 * @return
	 */
	public List<Map<String, Object>> getConDepartList(String organizationId,String userid,String bussinesstype,String language,String operationtype) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门ID
		paramMap.put("BIN_OrganizationID", organizationId);
		// 语言类型
		paramMap.put("language", language);
		// 用户ID
		paramMap.put("BIN_UserID", userid);
		// 业务类型
		paramMap.put("BusinessType", bussinesstype);
		// 操作类型
		paramMap.put("OperationType", operationtype);
		
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getConDepartList");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	
	/**
	 * 根据指定条件取得部门的所有下级部门
	 * 加入了部门权限过滤
	 * @param organizationId 指定的部门ID
	 * @param userid 用户ID
	 * @param bussinesstype 业务类型
	 * @param language 语言
	 * @return
	 */
	public List<Map<String, Object>> getManagerDepartsByOrgIDP(Map<String, Object> paramMap) {		
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getManagerDepartsByOrgIDP");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	
	public List<Map<String, Object>> getManagerOrgByOrgPrivilege(Map<String, Object> paramMap) {		
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getManagerOrgByOrgPrivilege");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	/**
	 * 取得指定部门的下级部门
	 * 不考虑部门数据权限过滤
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>>  getChildDepartList(Map<String, Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getChildDepartList");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	/**
	 * 取得指定部门的上级部门
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>>  getParentDepartList(Map<String, Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getParentDepartList");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	
	/**
	 * 取得指定部门的同级部门
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>>  getSiblingDepartList(Map<String, Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM01.getSiblingDepartList");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
		return ret;
	}
	
	/**
	 * 指定部门是否在用户的部门权限里
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> checkDepartByDepartPrivilege(Map<String, Object> map){
	    Map<String,Object> paramMap = new HashMap<String,Object>();
	    paramMap.putAll(map);
	    paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM01.checkDepartByDepartPrivilege");
	    List<Map<String, Object>>  ret = baseServiceImpl.getList(paramMap);
	    return ret;
	}
	
	/**
	 * 询品牌总部部门信息 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBrandDepartInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM01.getBrandDepartInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
}
