/*		
 * @(#)BINOLBSFAC02_BL.java     1.0 2011/02/16		
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
package com.cherry.bs.fac.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.fac.service.BINOLBSFAC02_Service;

/**
 * 生产厂商详细BL
 * 
 * @author lipc
 * @version 1.0 2011.02.16
 */
public class BINOLBSFAC02_BL {

	@Resource
	private BINOLBSFAC02_Service binolbsfac02Service;
	
	/**
	 * 取得生产厂商基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFacInfo(Map<String, Object> map) {
		
		return binolbsfac02Service.getFacInfo(map);
	}
	
	/**
	 * 取得厂商地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAddList (Map<String, Object> map) {
		
		return binolbsfac02Service.getAddList(map);
	}
}
