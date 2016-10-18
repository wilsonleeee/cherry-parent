/*
 * @(#)BINOLMBMBM16_BL.java     1.0 2013/05/20
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员升降级履历
 * 
 * @author Luohong
 * @version 1.0 2013/05/20
 */
public class BINOLMBMBM16_Service extends BaseService {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 * 会员升降级履历List
	 * 
	 * @param map 检索条件
	 * @return 会员升降级履历List
	 */
	public List<Map<String, Object>> getMemLevelList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM16.getMemLevelList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 升降级原因List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLevelReasonList(Map<String, Object> map){
		// 升降级原因List
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM16.getLevelReasonList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	/**
	 * 取得升降级匹配规则List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampNameList(Map<String, Object> map){
		// 取得升降级匹配规则List
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM16.getCampNameList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得属性维护原因List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReasonList(Map<String, Object> map){
		// 取得升降级匹配规则List
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM16.getReasonList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
}
