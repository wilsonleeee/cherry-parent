/*	
 * @(#)BINOLBSFAC04_Service.java     1.0 2011/02/17	
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
package com.cherry.bs.fac.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 	生产厂商添加Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.17
 */
public class BINOLBSFAC04_Service extends BaseService{
	
	/**
	 * 取得生产厂商ID
	 * 
	 * @param map
	 * @return
	 */
	public String getFactoryId(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC04.getFactoryId");
		return (String)baseServiceImpl.get(map);
	}
	/**
	 * 插入生产厂商信息表 
	 * 
	 * @param map
	 * @return int
	 */
	public int insertFactory(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC04.insertFactory");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入地址信息表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertAddrInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC04.insertAddrInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入生产厂商地址表
	 * 
	 * @param map
	 * @return
	 */
	public void insertManufacturerAddress(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC04.insertManufacturerAddress");
		baseServiceImpl.save(map);
	}
}
