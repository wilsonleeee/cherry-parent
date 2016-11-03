/*	
 * @(#)BINBEMBVIS02_Service.java     1.0 @2012-12-14		
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
package com.cherry.mb.vis.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 会员回访任务生成Service
 *
 * @author jijw
 *
 * @version  2012-12-14
 */
public class BINBEMBVIS02_Service extends BaseService{
	
	/**
	 * 查询会员信息List
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMembersList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEMBVIS02.getMemberInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台的BAList
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBAOfCounterList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEMBVIS02.getBAOfCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台List
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCounterList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEMBVIS02.getCounterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入会员回访信息表
	 * @param map
	 */
	public void insertMemVisitInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEMBVIS02.insertMemVisitInfo");
		baseServiceImpl.save(map);
	}

}
