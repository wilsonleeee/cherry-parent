/*	
 * @(#)BINOLPTRPS12_Service.java     1.0 2011/03/16		
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
 * 库存记录详细service
 * 
 * @author lipc
 * @version 1.0 2011.03.16
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS12_Service extends BaseService{
	
	/**
	 * 取得产品信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProProduct(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS12.getProProduct");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得产品库存详细List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS12.getProStockDetails");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品库存出入明细单据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getdetailed(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS12.getdetailed");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品条码
	 * 
	 * @param map
	 * @return
	 */
	public List<String> getBarCodeList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS12.getBarCodeList");
		return baseServiceImpl.getList(map);
	}
}
