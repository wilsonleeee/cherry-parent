/*	
 * @(#)BINBEMBARC01_Service.java     1.0 2012/06/04		
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
package com.cherry.mb.arc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 官网会员资料数据导入处理Service
 * 
 * @author WangCT
 * @version 1.0 2012/06/04
 */
public class BINBEMBARC01_Service extends BaseService {
	
	/**
	 * 把会员接口表中数据读取状态为未处理的数据全部更新成即将处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateGetStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC01.updateGetStatus");
		return ifServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询会员接口表中数据读取状态为即将处理的会员数据
	 * 
	 * @param map 查询条件
	 * @return 会员数据List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC01.getMemberInfoList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新处理过的会员数据的状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateGetStatusEnd(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC01.updateGetStatusEnd");
		return ifServiceImpl.update(paramMap);
	}
	
	/**
	 * 批量更新处理过的会员数据的状态
	 * 
	 * @param list 更新条件
	 */
	public void updateGetStatusEnd(List<Map<String, Object>> list) {
		ifServiceImpl.updateAll(list, "BINBEMBARC01.updateGetStatusEnd");
	}

}
