/*	
 * @(#)BINOLPTRPS10_Service.java     1.0 2011/03/10		
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
 * 出入库记录详细service
 * 
 * @author lipc
 * @version 1.0 2011.03.10
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS10_Service extends BaseService{
	
	/**
	 * 取得入出库单详细信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProInOutInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS10.getProInOutInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得入出库物品清单LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProInOutList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS10.getProInOutList");
		return baseServiceImpl.getList(map);
	}
}
