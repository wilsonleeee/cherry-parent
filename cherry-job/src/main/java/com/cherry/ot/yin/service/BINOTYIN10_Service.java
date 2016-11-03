/*	
 * @(#)BINOTYIN10_Service.java     1.0 2013-04-22		
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
 * 颖通接口：支付方式导入Service
 * 
 * @author menghao
 * 
 * @version 2013-04-22
 * 
 */
public class BINOTYIN10_Service extends BaseService {

	/**
	 * 取得颖通接口表中的支付方式明细
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPayTypeIFList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN10.getPayTypeIFList");
		return tpifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getSYSDate(){	
		return baseServiceImpl.getSYSDate();
	}
	
	/**
	 * 取得新后台的已经存在的支付方式 的数量
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getHvPayTypeCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN10.getHvPayTypeCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 更新新后台中已经存在的支付方式
	 * @param map
	 * @return
	 */
	public int updateOldPayType(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN10.updateOldPayType");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 插入一条新的支付方式
	 * @param map
	 */
	public void insertNewPayType(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINOTYIN10.insertNewPayType");
		baseServiceImpl.save(paramMap);
	}
}
