/*
 * @(#)BINBEPLDPL03_Service.java     1.0 2012.04.12
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
package com.cherry.pl.dpl.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 权限表维护共通Service
 * 
 * @author WangCT
 * @version 1.0 2012.04.12
 */
public class BINBEPLDPL03_Service extends BaseService {
	
	/**
	 * 重建部门权限表
	 */
	public void createDepartPrivilegeTable() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartPrivilegeTable");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 重建部门权限临时表
	 */
	public void createDepartPrivilegeTableTemp() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartPrivilegeTableTemp");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 给部门权限表创建索引
	 */
	public void createDepartPrivilegeIndex() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartPrivilegeIndex");
		baseServiceImpl.update(parameterMap);
	}

	/**
	 * 重建人员权限表
	 */
	public void createEmployeePrivilegeTable() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createEmployeePrivilegeTable");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 重建人员权限临时表
	 */
	public void createEmployeePrivilegeTableTemp() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createEmployeePrivilegeTableTemp");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 给人员权限表创建索引
	 */
	public void createEmployeePrivilegeIndex() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createEmployeePrivilegeIndex");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 重建部门从属关系表
	 */
	public void createDepartRelationTable() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartRelationTable");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 重建部门从属关系临时表
	 */
	public void createDepartRelationTableTemp() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartRelationTableTemp");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 给部门从属关系表创建索引
	 */
	public void createDepartRelationIndex() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.createDepartRelationIndex");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空部门权限表数据
	 * 
	 * @param map
	 */
	public void truncateDepartPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.truncateDepartPrivilege");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空人员权限表数据
	 * 
	 * @param map
	 */
	public void truncateEmployeePrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.truncateEmployeePrivilege");
		baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 清空部门从属关系表数据
	 * 
	 * @param map
	 */
	public void truncateDepartRelation(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEPLDPL03.truncateDepartRelation");
		baseServiceImpl.update(parameterMap);
	}
}
