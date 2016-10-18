/*
 * @(#)MemDetailInfoService.java     1.0 2012/12/06
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
package com.cherry.shindig.gadgets.service.mb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 查看会员详细信息Service
 * 
 * @author WangCT
 * @version 1.0 2012/12/06
 */
public class MemDetailInfoService extends BaseService {
	
	/**
	 * 查询会员基本信息
	 * 
	 * @param map 查询条件
	 * @return 会员基本信息
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getMemberInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询销售信息List
	 * 
	 * @param map 查询条件
	 * @return 销售信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getSaleInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计销售总金额和总数量
	 * 
	 * @param map 查询条件
	 * @return 销售总金额和总数量
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleAmountSum(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getSaleAmountSum");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询积分计算信息List
	 * 
	 * @param map 查询条件
	 * @return 积分计算信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointCalInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getPointCalInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询积分计算信息总数
	 * 
	 * @param map 查询条件
	 * @return 积分计算信息总数
	 */
	public int getPointCalInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getPointCalInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 统计不同时间段的销售金额、数量、次数
	 * 
	 * @param map 查询条件
	 * @return 销售金额、数量、次数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleCountInfoByTime(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getSaleCountInfoByTime");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 统计不同产品类别的销售数量
	 * 
	 * @param map 查询条件
	 * @return 不同产品类别的销售数量
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleCountInfoByProCat(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getSaleCountInfoByProCat");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计会员的总销售数量
	 * 
	 * @param map 查询条件
	 * @return 会员的总销售数量
	 */
	public int getSaleTotalQuantity(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getSaleTotalQuantity");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品类别List
	 * 
	 * @param map 查询条件
	 * @return 产品类别List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtCatPropertyList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MemDetailInfo.getPrtCatPropertyList");
		return baseServiceImpl.getList(parameterMap);
	}

}
