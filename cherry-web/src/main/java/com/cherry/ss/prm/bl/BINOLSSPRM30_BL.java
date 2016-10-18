/*	
 * @(#)BINOLSSPRM30_BL.java     1.0 2010/11/29		
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM30_Service;

/**
 * 调拨记录查询BL
 * 
 * @author lipc
 * @version 1.0 2010.11.29
 * 
 */
public class BINOLSSPRM30_BL {

	@Resource
	private BINOLSSPRM30_Service binolssprm30Service;

	/**
	 * 取得调拨单详细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAllocationInfo(Map<String, Object> map) {
		return binolssprm30Service.getAllocationInfo(map);
	}

	/**
	 * 取得调拨单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		return binolssprm30Service.getAllocationList(map);
	}
}
