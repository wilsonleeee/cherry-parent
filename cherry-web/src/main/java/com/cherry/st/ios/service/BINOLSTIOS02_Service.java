/*  
 * @(#)BINOLSTIOS02_Service.java    1.0 2011-8-26     
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
package com.cherry.st.ios.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

public class BINOLSTIOS02_Service extends BaseService {

	/**
	 * 往产品移库主表中插入数据并返回记录ID
	 * 
	 * 
	 * */
	public int insertToProductShift(Map<String,Object> map){
		return (Integer)baseServiceImpl.get(map, "BINOLSTIOS02.insertToProductShift");
	}
	
	/**
	 * 往移库明细表中插入数据
	 * 
	 * 
	 * */
	public void insertToProductShiftDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTIOS02.insertToProductShiftDetail");
	}
	
	/**
	 * 根据部门Code获取部门ID
	 * 
	 * 
	 * */
	public int getOrganizationId(Map<String,Object> map){
		return (Integer)baseServiceImpl.get(map, "BINOLSTIOS02.getOrganizationId");
	}
	
	/**
	 * 根据产品条码以及厂商编码获取产品厂商ID以及对应的库存
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPrtVenIdAndStock(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map,"BINOLSTIOS02.getPrtVenIdAndStock");
	}
	
	public int getPrtVenId(Map<String,Object> map){
		return (Integer) baseServiceImpl.get(map,"BINOLSTIOS02.getPrtVenId");
	}
}
