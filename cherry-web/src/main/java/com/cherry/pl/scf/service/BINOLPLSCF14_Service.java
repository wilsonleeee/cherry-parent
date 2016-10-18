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
public class BINOLPLSCF14_Service extends BaseService {
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF14.getOrgInfoList");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF14.getBrandCodeList");
		return baseConfServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入文件
	 * 
	 * @param Map
	 *			插入信息
	 * @return 无
	 *			
	 */
	public void insertFile(List<Map<String, Object>> list) {
		baseConfServiceImpl.saveAll(list, "BINOLPLSCF14.insertFile");
	}
	
	/**
	 * 更新文件
	 * 
	 * @param Map
	 *			插入信息
	 * @return 无
	 *			
	 */
	public void updateFile(List<Map<String, Object>> list) {
		baseConfServiceImpl.updateAll(list, "BINOLPLSCF14.updateFile");
	}
	
	/**
	 * 取得当前文件在数据库中信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所有的组织信息
	 */
	public Map<String, Object> getFileCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF14.getFileCount");
		return (Map<String, Object>) baseConfServiceImpl.get(map);
	}
}
