/*	
 * @(#)BINOLCM14_Service.java     1.0 2011/03/29		
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
package com.cherry.cm.cmbussiness.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM14_Service extends BaseService{	
	/**
	 * 取得系统配置项
	 * @param map
	 * @return
	 */
	public List getConfigValue(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getConfigValue");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得系统配置项
	 * @param map
	 * @return
	 */
	public List getConfigValueEfficient(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getConfigValueEfficient");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据分组号取得有效的系统配置项
	 * 
	 */
	public List getConfigValueByGroupNo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getConfigValueByGroupNo");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得审核人
	 * @param map
	 * @return
	 */
	public List getActorsAllType(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getActorsAllType");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 取得指定用户能操作的人的列表
	 * @param map
	 * @return
	 */
	public List getChildEmployee(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getChildEmployee");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得系统配置项
	 * @param map
	 * @return
	 */
	public List getWebposConfigValue(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM14.getWebposConfigValue");
		return baseServiceImpl.getList(map);
	}
}
