/*	
 * @(#)BINOLSSPRM32_Service.java     1.0 2010/11/16		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 库存记录详细service
 * 
 * @author lipc
 * @version 1.0 2010.11.16
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM32_Service extends BaseService{
	
	/**
	 * 取得促销品信息
	 * 
	 * @param map
	 * @return
	 */
	public Object getProProduct(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM32.getProProduct");
		return baseServiceImpl.get(map);
	}
	
	/**
	 * 取得产品库存详细List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM32.getProStockDetails");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品库存出入明细单据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getdetailed(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM32.getdetailed");
		return baseServiceImpl.getList(map);
	}
}
