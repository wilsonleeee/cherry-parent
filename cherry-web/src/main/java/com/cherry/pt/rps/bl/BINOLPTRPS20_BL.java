/*	
 * @(#)BINOLPTRPS20_BL.java     1.0 2010/11/29		
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.rps.service.BINOLPTRPS20_Service;

public class BINOLPTRPS20_BL {

	@Resource
	private BINOLPTRPS20_Service binolptrps20Service;
	
    /**
	 * 取得发货单详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDeliverMainInfo(Map<String, Object> map) {
		return binolptrps20Service.getDeliverMainInfo(map);
	}

	/**
	 * 取得发货单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverDetailList(Map<String, Object> map) {
		return binolptrps20Service.getDeliverDetailList(map);
	}
}
