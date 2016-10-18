/*		
 * @(#)BINBEMQMES01_Service.java     1.0 2010/12/01		
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
package com.cherry.mq.mes.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 线上购买线下提货订单状态变更处理Service
 * @author lzs
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES16_Service extends BaseService{
	/**
	 * 查询销售明细信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSaleDetailList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES16.getSaleDetailList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 更新电商订单表相关信息
	 * @param map
	 */
	public void updateOrderInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES16.updateOrderInfo");
		baseServiceImpl.update(map);
	}
	/**
	 * 查询销售主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getSaleRecordInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES16.getSaleRecordInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	/**
	 * 更新销售主表相关信息
	 * @param map
	 */
	public void updateSaleRecordInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES16.updateSaleRecordInfo");
		baseServiceImpl.update(map);
	}
}
