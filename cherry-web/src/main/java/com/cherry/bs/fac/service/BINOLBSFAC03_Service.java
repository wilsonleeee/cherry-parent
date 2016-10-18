/*	
 * @(#)BINOLBSFAC03_Service.java     1.0 2011/02/21	
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 	生产厂商编辑Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.21
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC03_Service extends BaseService{
	
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
	 * 取得生产厂商基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getFacInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC02.getFacInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 厂商信息更新
	 * 
	 * @param map
	 * @return
	 */
	public int updFacInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC03.updFacInfo");
		return baseServiceImpl.update(map);
	}
	/**
	 * 取得厂商地址List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAddList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC02.getAddList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入厂商地址表
	 * 
	 * @param map
	 * @return
	 */
	public void insertManufacturerAddress(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC04.insertManufacturerAddress");
		baseServiceImpl.save(map);
	}
	/**
	 * 厂商地址更新
	 * 
	 * @param map
	 * @return
	 */
	public int updFacAddress (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC03.updFacAddress");
		return baseServiceImpl.update(map);
	}
	/**
	 * 厂商地址删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delFacAddress (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC03.delFacAddress");
		return baseServiceImpl.remove(map);
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
	 * 地址信息更新 
	 * 
	 * @param map
	 * @return
	 */
	public int updAddrInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC03.updAddrInfo");
		return baseServiceImpl.update(map);
	}
	/**
	 * 地址信息删除 
	 * 
	 * @param map
	 * @return
	 */
	public int delAddrInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSFAC03.delAddrInfo");
		return baseServiceImpl.remove(map);
	}
}
