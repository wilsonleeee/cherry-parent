/*		
 * @(#)BINOLSSPRM73_Service.java     1.0 2016/05/05		
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
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 优惠券Service
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM77_Service extends BaseService{
	
	/**
	 * 取得优惠券规则总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getCouponInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM77.getCouponInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得优惠券规则List
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCouponInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM77.getCouponInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得优惠券信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券信息map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCouponInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM77.getCouponInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 通过brandCode获取品牌组织信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBrandInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM77.getBrandInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
}
