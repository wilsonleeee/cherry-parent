/*	
 * @(#)BINOLPTRPS37_Service.java     1.0 2015-1-21		
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
 * 
 * @ClassName: BINOLPTRPS37_Service 
 * @Description: TODO(实时库存预警Service) 
 * @author menghao
 * @version v1.0.0 2015-1-21 
 *
 */
public class BINOLPTRPS37_Service extends BaseService {

	/**
	 * 取得实时库存预警记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS37.getProStockCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得实时库存预警记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS37.getProStockList");
		return baseServiceImpl.getList(map);
	}
	
}
