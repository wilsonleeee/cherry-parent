/*
 * @(#)BINOLBSRES01_Service.java     1.0 2014/10/29
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
package com.cherry.bs.wem.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;


/**
 * 微商申请一览
 * 
 * @author hujh
 * @version 1.0 2015/08/03
 */
public class BINOLBSWEM01_Service extends BaseService{
	

	public int getWechatMerchantApplyCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getWechatMerchantApplyCount");
		return baseServiceImpl.getSum(map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWechatMerchantApplyList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getWechatMerchantApplyList");
		return baseServiceImpl.getList(map);
	}

	public int getEmpCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getEmpCount");
		return baseServiceImpl.getSum(map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmpList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getEmpList");
		return baseServiceImpl.getList(map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getApplyInfoById(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getApplyInfoById");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	public void updateAssignInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.updateAssignInfo");
		baseServiceImpl.update(map);
	}

	public void addToLog(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.addToLog");
		baseServiceImpl.save(map);
	}

	public void audit(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.audit");
		baseServiceImpl.update(map);
	}

	/**
	 * 获取某员工下级的总数
	 * @param map
	 * @return
	 */
	public int getSubAmount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getSubAmount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 只获取下级的employeeId
	 * @param employeeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubEmployeeIdList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getSubEmployeeIdList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取员工部门类型，即微商等级
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmpAgentLevel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM01.getEmpAgentLevel");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
}