/*
 * @(#)BINBEIFEMP02_Service.java     1.0 2010/11/12
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
 * 
 * 员工列表导入Service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP02_Service extends BaseService {
	
	/**
	 * 
	 * 从接口数据库中查询员工数据
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeesList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getEmployeesList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询员工信息
	 * 
	 * @param map 查询条件
	 * @return 员工信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getEmployeeInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 更新员工信息表
	 * 
	 * @param Map
	 * @return 更新件数
	 * 
	 */
	public int updateEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateEmployee");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新用户信息 
	 * 
	 * @param Map
	 * @return 更新件数
	 * 
	 */
	public int updateUser(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateUser");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新用户信息(配置数据库)
	 * 
	 * @param Map
	 * @return 更新件数
	 * 
	 */
	public int updateUserConf(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateUserConf");
		return baseConfServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询员工上级节点
	 * 
	 * @param map 查询条件
	 * @return 员工上级节点
	 * 
	 */
	public Object getSeniorEmpPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getSeniorEmpPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 员工新节点
	 * 
	 */
	public Object getNewEmpNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getNewEmpNodeId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入员工信息
	 * 
	 * @param Map
	 * @return 员工ID
	 * 
	 */
	public int insertEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.insertEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 更新员工入退职信息表
	 * 
	 * @param Map
	 * @return 更新件数
	 * 
	 */
	public int updateEmployeeQuit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateEmployeeQuit");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入员工入退职信息表
	 * 
	 * @param Map
	 * @return 员工ID
	 * 
	 */
	public void insertEmployeeQuit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.insertEmployeeQuit");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 备份员工信息表
	 * 
	 * @param map 查询条件
	 * @return 无
	 * 
	 */
	public void backupEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.backupEmployee");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 删除世代番号超过上限的数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void clearBackupData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.clearBackupData");
		baseServiceImpl.remove(paramMap);
	}

	/**
	 * 
	 * 更新世代番号
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */
	public void updateBackupCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询员工部门信息
	 * 
	 * @param map 查询条件
	 * @return 部门信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrganization(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getOrganization");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询员工岗位类别ID
	 * 
	 * @param map 查询条件
	 * @return 岗位类别ID
	 * 
	 */
	public Object getPositionCategoryId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getPositionCategoryId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入员工管辖部门对应表
	 * 
	 * @param Map
	 * @return 无
	 * 
	 */
	public void insertEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.insertEmployeeDepart");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新员工管辖部门对应表
	 * 
	 * @param 无
	 * @return 无
	 * 
	 */
	public void updateEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.updateEmployeeDepart");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询需要伦理删除的员工数据
	 * 
	 * @param map 查询条件
	 * @return 需要伦理删除的员工List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.getDelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 删除无效的员工数据
	 * 
	 * @param map 需要伦理删除的员工
	 * @return 无
	 * 
	 */
	public void delInvalidEmployees(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.delInvalidEmployees");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 删除无效的用户数据
	 * 
	 * @param map 需要伦理删除的用户
	 * @return 无
	 * 
	 */
	public void delInvalidUser(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.delInvalidUser");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 删除无效的用户数据(配置数据库)
	 * 
	 * @param map 需要伦理删除的用户
	 * @return 无
	 * 
	 */
	public void delInvalidUserConf(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.delInvalidUserConf");
		baseConfServiceImpl.update(paramMap);
	}

	/**
	 * 
	 * 删除无效的员工入退职信息
	 * 
	 * @param map 需要伦理删除的员工入退职信息
	 * @return 无
	 * 
	 */
	public void delInvalidEmployeeQuit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP02.delInvalidEmployeeQuit");
		baseServiceImpl.update(paramMap);
	}

}
