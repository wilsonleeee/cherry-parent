/*
 * @(#)BINBETLBAT01_Service.java     1.0 2010/12/24
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
package com.cherry.tl.bat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 清空各种表service
 * 
 * 
 * @author ZHANGJIE
 * @version 1.0 2010.12.24
 */
public class BINBETLBAT01_Service extends BaseService {

	/**
	 * 
	 * 清空采番表
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void clearTicketNumber(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT01.clearTicketNumber");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 
	 * 从各类编号取号表中取得每种类型的最大番号
	 * 
	 * @param map 查询条件
	 * @return 各种类型的最大番号List
	 * 
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMaxSequenceCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT01.getMaxSequenceCode");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 删除各类编号取号表中小于最大番号的数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void clearSequenceCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT01.clearSequenceCode");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 
	 * 清空OS_STEPIDS表
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void clearOsStepIds(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT01.clearOsStepIds");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 清空OS_ENTRYIDS表
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */	
	public void clearOsEntryIds(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT01.clearOsEntryIds");
		baseServiceImpl.update(paramMap);
	}

}
