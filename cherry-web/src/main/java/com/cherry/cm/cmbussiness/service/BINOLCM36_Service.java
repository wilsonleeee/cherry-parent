/*
 * @(#)BINOLCM36_Service.java     1.0 2013/04/08
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
 * 会员信息修改履历管理Service
 * 
 * @author WangCT
 * @version 1.0 2013/04/08
 */
public class BINOLCM36_Service extends BaseService {
	
	/**
	 * 添加会员信息修改履历主表
	 * 
	 * @param map 添加内容
	 * @return 会员信息修改履历ID
	 */
	public int addMemInfoRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM36.addMemInfoRecord");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 添加会员信息修改履历明细表
	 * 
	 * @param list 添加内容
	 */
	public void addMemInfoRecordDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLCM36.addMemInfoRecordDetail");
	}
	
	/**
	 * 查询会员信息
	 * 
	 * @param map 查询条件
	 * @return 会员信息
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM36.getMemberInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询会员地址信息
	 * 
	 * @param map 查询条件
	 * @return 会员地址信息
	 */
	public List<Map<String, Object>> getMemberAddress(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM36.getMemberAddress");
		return baseServiceImpl.getList(paramMap);
	}

}
