/*	
 * @(#)BINOLSSPRM36_Service.java     1.0 2010/12/03		
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
 * 出入库记录详细service
 * 
 * @author lipc
 * @version 1.0 2010.12.03
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM36_Service extends BaseService{
	
	/**
	 * 取得入出库单详细信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockInOutInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM36.getProStockInOutInfo");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得入出库物品清单LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockInOutList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM36.getProStockInOutList");
		return baseServiceImpl.getList(map);
	}
}
