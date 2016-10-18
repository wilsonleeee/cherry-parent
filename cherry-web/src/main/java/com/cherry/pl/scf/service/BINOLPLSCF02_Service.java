/*
 * @(#)BINOLPLSCF02_Service.java     1.0 2010/10/27
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

package com.cherry.pl.scf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 审核审批配置管理Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF02_Service extends BaseService {
	
	/**
	 * 查询业务类型List
	 * 
	 * @param map 查询条件
	 * @return 业务类型List
	 */
	public List<Map<String, Object>> getBussinessTypeCodeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getBussinessTypeCodeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询审核审批配置信息总数
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息总数
	 */
	public int getAuditPrivilegeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getAuditPrivilegeCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询审核审批配置信息List
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息List
	 */
	public List<Map<String, Object>> getAuditPrivilegeList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getAuditPrivilegeList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询用户List
	 * 
	 * @param map 查询条件
	 * @return 用户List
	 */
	public List<Map<String, Object>> getUserInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getUserInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询岗位List
	 * 
	 * @param map 查询条件
	 * @return 岗位List
	 */
	public List<Map<String, Object>> getPosInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getPosInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询部门List
	 * 
	 * @param map 查询条件
	 * @return 部门List
	 */
	public List<Map<String, Object>> getOrgInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getOrgInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加审核审批配置信息
	 * 
	 * @param map 添加内容
	 */
	public void addAudit(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.addAudit");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 查询审核审批配置信息是否存在
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置ID
	 */
	public String getAuditVal(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getAuditVal");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询审核审批配置信息
	 * 
	 * @param map 查询条件
	 * @return 审核审批配置信息
	 */
	public Map<String, Object> getAuditInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.getAuditInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新审核审批配置信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateAudit(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.updateAudit");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除审核审批配置信息
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int deleteAudti(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF02.deleteAudti");
		return baseServiceImpl.remove(parameterMap);
	}

}
