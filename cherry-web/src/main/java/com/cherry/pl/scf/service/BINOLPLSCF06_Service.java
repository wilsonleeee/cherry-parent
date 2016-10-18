/*
 * @(#)BINOLPLSCF06_Service.java     1.0 2010/10/27
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
 * code值管理一览Service
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF06_Service extends BaseService {
	
	/**
	 * 查询code管理表信息总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getCodeMCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF06.getCodeMCount");
		return baseConfServiceImpl.getSum(parameterMap);
	}

	/**
	 * 查询code管理表信息List
	 * 
	 * @param map 查询条件
	 * @return List
	 */
	public List<Map<String, Object>> getCodeMList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF06.getCodeMList");
		return baseConfServiceImpl.getList(parameterMap);
	}

	/**
	 * 插入code管理表
	 * 
	 * @param map
	 * @return
	 */	
	public void insertCodeM(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF06.insertCodeM");
		baseConfServiceImpl.save(map);		
	}

	/**
	 * 取得所有组织List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所有的组织信息
	 */
	public List getOrgInfoList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF06.getOrgInfoList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得品牌CodeList
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			品牌CodeList
	 */
	public List<Map<String, Object>> getBrandCodeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织Code
		paramMap.put("orgCode", map.get("orgCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF06.getBrandCodeList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 刷新CodeList
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			CodeList
	 */
	public List<Map<String, Object>> getCodeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getCodeList");
		// 取得CODE值一览
		return baseConfServiceImpl.getList(paramMap);
	}
}
