/*	
 * @(#)BINOTYIN04_Service.java     1.0 2013/03/18		
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

package com.cherry.ot.yin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 颖通接口：产品入库单导出Service
 * 
 * @author menghao
 * 
 * @version 2013-03-18
 * 
 */
public class BINOTYIN04_Service extends BaseService {
	
	/**
	 * 取得需导出产品入库单数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtReceiveDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN04.getPrtReceiveDetail");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 状态更新
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN04.updateSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得指定导出状态的入库单号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPrtReceiveListBySynch(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN04.getPrtReceiveListBySynch");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据新后台的单据号->查询颖通接口表的单据号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOTIFListFromPrtReceive(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN04.getOTIFListFromPrtReceive");
		return tpifServiceImpl.getList(paramMap);
	}

	/**
	 * 批量插入list数据
	 * 
	 * @param list
	 */
	public void insertOTYINIFDbBatch(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTYIN04.insertOTYINIFDb");
	}

}
