/*		
 * @(#)BINOLBSFAC01_BL.java     1.0 2011/02/14		
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

import com.cherry.bs.fac.service.BINOLBSFAC01_Service;

/**
 * 生产厂商查询 BL
 * 
 * @author lipc
 * @version 1.0 2011.02.14
 */
public class BINOLBSFAC01_BL {

	@Resource
	private BINOLBSFAC01_Service binolbsfac01Service;
	
	/**
	 * 取得生产厂商总数
	 * 
	 * @param map
	 * @return
	 */
	public int getFacCount(Map<String, Object> map) {

		return binolbsfac01Service.getFacCount(map);
	}
	
	/**
	 * 取得生产厂商信息List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getFactoryList (Map<String, Object> map) {
		
		return binolbsfac01Service.getFactoryList(map);
	}
}
