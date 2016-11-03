/*
 * @(#)BINBEIFEMP04_Service.java     1.0 2013/04/25
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
 * CPA账号同步处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/04/25
 */
public class BINBEIFEMP04_Service extends BaseService {
	
	/**
	 * 从老后台CPA数据库中查询用户信息List
	 * 
	 * @param map 查询条件
	 * @return 用户信息List
	 */
	public List<Map<String, Object>> getDAUserList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getDAUserList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据CPA中的用户姓名查询新后台的员工表
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getEmployeeList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据CPA中的用户账号查询新后台的用户表
	 * 
	 * @param map 查询条件
	 * @return 用户List
	 */
	public List<Map<String, Object>> getUserList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getUserList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 添加用户信息
	 * 
	 * @param map 添加内容
	 */
	public void addUser(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.addUser");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 添加用户信息(配置表)
	 * 
	 * @param map 添加内容
	 */
	public void addUserConf(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.addUserConf");
		baseConfServiceImpl.save(paramMap);
	}
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewEmpNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getNewEmpNodeId");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 添加员工信息
	 * 
	 * @param map 添加内容
	 * @return 员工ID
	 */
	public int addEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.addEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 取得新后台岗位List
	 * 
	 * @param map 查询条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getPositionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据CPA中的用户ID查询该用户能访问的柜台
	 * 
	 * @param map 查询条件
	 * @return 柜台List
	 */
	public List<Map<String, Object>> getUserCounterAccess(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getUserCounterAccess");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据柜台号查询部门ID
	 * 
	 * @param map 查询条件
	 * @return 部门IDList
	 */
	public List<Map<String, Object>> getOrganizationIdList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getOrganizationIdList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 添加员工管辖部门信息
	 * 
	 * @param list 添加内容
	 */
	public void addEmployeeDepart(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEIFEMP04.addEmployeeDepart");
	}

	/**
	 * 取得部门新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewOrgNodeId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getNewOrgNodeId");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 添加柜台主管部门
	 * 
	 * @param map 添加内容
	 * @return 部门ID
	 */
	public int addOrganization(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.addOrganization");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 取得员工和部门代码的CodeTable信息
	 * 
	 * @return 员工和部门代码的CodeTable信息
	 */
	public List<Map<String, Object>> getCodeTabelInfoList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getCodeTabelInfoList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 组织结构节点移动
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateOrganizationNode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.updateOrganizationNode");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新员工的所属部门
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.updateEmployee");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 根据部门ID取得部门节点
	 * 
	 * @param map 查询条件
	 * @return 部门节点
	 */
	public String getOrgPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.getOrgPath");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateUser(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.updateUser");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除员工管辖部门对应关系
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delEmployeeDepart(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP04.delEmployeeDepart");
		return baseServiceImpl.remove(paramMap);
	}
}
