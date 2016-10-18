/*
 * @(#)BINOLMBMBM16_Service.java     1.0 2013/05/15
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员启用停用处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/05/15
 */
public class BINOLMBMBM17_Service extends BaseService {
	
	/**
	 * 停用启用会员信息
	 * 
	 * @param list 更新条件
	 */
	public void updMemValidFlag(List<Map<String, Object>> list) {
		
		baseServiceImpl.updateAll(list, "BINOLMBMBM17.updMemValidFlag");
	}
	
	/**
	 * 停用启用会员卡信息
	 * 
	 * @param list 更新条件
	 */
	public void updMemCardValidFlag(List<Map<String, Object>> list) {
		
		baseServiceImpl.updateAll(list, "BINOLMBMBM17.updMemCardValidFlag");
	}
	
	/**
	 * 查询会员卡信息List
	 * 
	 * @param map 查询条件
	 * @return 会员List
	 */
	public List<Map<String, Object>> getMemCardInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM17.getMemCardInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员信息List
	 * 
	 * @param map 查询条件
	 * @return 会员List
	 */
	public List<Map<String, Object>> getMemInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM17.getMemInfoList");
		return baseServiceImpl.getList(paramMap);
	}

}
