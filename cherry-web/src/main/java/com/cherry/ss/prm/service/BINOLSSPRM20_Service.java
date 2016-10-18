/*		
 * @(#)BINOLSSPRM20_Service.java     1.0 2010/11/16		
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
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
public class BINOLSSPRM20_Service {
	@Resource
	private BaseServiceImpl baseServeceImpl;
	
	/**
	 * 取得调拨申请单
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllOrder(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM20.getAllOrder");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得调拨申请单  工作流用
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllOrderJbpm(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM20.getAllOrderJbpm");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得调拨明细数据
	 * @param map
	 * @return
	 */
	public List getAllocationDetailList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM20.getAllocationDetailList");
		return baseServeceImpl.getList(map);
	}
	/**
	 * 取得调拨单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAllocationInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM20.getAllocationInfo");
		return (Map<String, Object>)baseServeceImpl.get(map);
	}
}
