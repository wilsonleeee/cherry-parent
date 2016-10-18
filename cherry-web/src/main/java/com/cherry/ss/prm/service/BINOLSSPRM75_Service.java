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
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.ss.prm.dto.CouponDTO;
import com.cherry.ss.prm.dto.CouponRuleDTO;

/**
 * 优惠券Service
 * @author ghq
 * @version 1.0 2016.05.05
 */
public class BINOLSSPRM75_Service extends BaseService{
	
	/**
	 * 取得优惠券规则总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getCouponInfoCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getCouponInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得优惠券规则List
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则List
	 */
	public List<Map<String, Object>> getCouponInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getCouponInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得优惠券信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券信息map
	 */
	public Map<String, Object> getCouponInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getCouponInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	
	/**
	 * 更新优惠券信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updateCouponInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.updateCouponInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取消优惠券
	 * 
	 * @param map
	 * @return int
	 */
	public int stopCoupon(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.stopCoupon");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得已使用的优惠券总数
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getUsedCouponCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getUsedCouponCount");
		return baseServiceImpl.getSum(map);
	}
	
	/***
	 * 取得已使用的优惠券信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getUsedCouponInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getUsedCouponInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/***
	 * 取得已使用的优惠券List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUsedCouponList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getUsedCouponList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 为Excel导出获取优惠券信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCouponInfoListForExport(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM75.getCouponInfoListForExport");
		return baseServiceImpl.getList(map);
	}
}
