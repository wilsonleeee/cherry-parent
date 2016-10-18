/*	
 * @(#)BINOLCPACT12_Service.java     1.0 @2014-12-16	
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
package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 活动产品库存一览Service
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT12_Service extends BaseService {
	
	/**
	 * 活动产品库存List数
	 * 
	 * @param map
	 * @return
	 */
	public int getCampaignStockCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT12.getCampaignStockCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 活动产品库存List
	 * 
	 * @param map 
	 * @returnList
	 */
	public List<Map<String, Object>> getCampaignStockList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT12.getCampaignStockList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定活动柜台的库存主信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampaignStockDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT12.getCampaignStockDetail");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询指定活动柜台的库存详细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampaignStockProductDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT12.getCampaignStockProductDetail");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCampName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCPACT12.getCampName");
	}
	
	/**
	 * 根据输入字符串模糊查询会员活动名称信息
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSubCampName(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINOLCPACT12.getSubCampName");
	}
	
	/**
	 * 更新活动柜台产品库存
	 * @param list
	 */
	public void updateCampaignStock(List<Map<String, Object>> list) {
		baseServiceImpl.updateAllByPage(list,"BINOLCPACT12.updateCampaignStock");
	}
}
