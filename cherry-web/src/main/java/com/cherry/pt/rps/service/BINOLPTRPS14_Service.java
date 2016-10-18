/*	
 * @(#)BINOLPTRPS14_Service.java     1.0 2011/03/18		
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 销售记录相信查询service
 * 
 * @author zgl
 * @version 1.0 2011.03.18
 *
 */
public class BINOLPTRPS14_Service extends BaseService {
	
	/**
	 * 获取支付详细信息
	 * @param map
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPayTypeDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS14.getPayTypeDetail");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取产品销售记录单据详细
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getSaleRecordDetail(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS14.getSaleRecordDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品销售记录单据中的产品详细
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSaleRecordProductDetail(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS14.getSaleRecordProductDetail");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取操作员姓名
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getEmployeeName(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS14.getEmployeeName");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	
}
