/*
 * @(#)BINOLSTBIL14_Service.java     1.0 2012/7/24
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
package com.cherry.st.bil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 退货申请单明细一览Service
 * @author nanjunbo
 * @version 1.0 2016.08.29
 */
public class BINOLSTBIL20_Service extends BaseService{

	
	/**
	 * 取得退货申请单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchSaleRerurnRequestInfo(Map<String, Object> map) {

		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL20.searchSaleRerurnRequestInfo");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得退货申请单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List searchSaleRerurnRequestDetailList(Map<String, Object> map) {

		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL20.searchSaleRerurnRequestDetailList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 获取支付详细信息
	 * @param map
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPayTypeDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL20.getPayTypeDetail");
		return baseServiceImpl.getList(map);
	}
}
