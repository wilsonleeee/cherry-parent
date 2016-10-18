/*	
 * @(#)BINOLSSPRM42_BL.java     1.0 2010/11/23		
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

import com.cherry.ss.prm.service.BINOLSSPRM42_Service;

/**
 * 退库记录详细BL
 * 
 * @author lipc
 * @version 1.0 2010.11.23
 * 
 */
public class BINOLSSPRM42_BL {

	@Resource
	private BINOLSSPRM42_Service binolssprm42Service;

	/**
	 * 取得退库记录信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getReturnInfo(Map<String, Object> map) {
		return binolssprm42Service.getReturnInfo(map);
	}

	/**
	 * 取得退库详细记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReturnList(Map<String, Object> map) {
		return binolssprm42Service.getReturnList(map);
	}
}
