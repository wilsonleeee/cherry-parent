/*
 * @(#)BINOLPLUPM01_Service.java     1.0 2010/12/24
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
package com.cherry.pl.upm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 用户信息查询
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM01_Service extends BaseService {

	
	/**
	 * 取得促销产品分类总数
	 * 
	 * @param map
	 * @return
	 */
	public int getUserCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM01.getUserCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品分类信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getUserList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM01.getUserList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得用户信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getUser (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM01.getUser");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 删除用户信息
	 * 
	 * @param map
	 * @return int
	 */
	public int inValidUser(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM01.inValidUser");
		return baseServiceImpl.remove(map);
	}
	
}
