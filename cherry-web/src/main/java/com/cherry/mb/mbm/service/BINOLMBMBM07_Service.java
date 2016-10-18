/*
 * @(#)BINOLMBMBM07_Service.java     1.0 2012.12.07
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动记录画面Service
 * 
 * @author WangCT
 * @version 1.0 2012.12.07
 */
public class BINOLMBMBM07_Service extends BaseService {
	
	/**
	 * 查询会员参与活动履历总数
	 * 
	 * @param map 检索条件
	 * @return 参与活动履历总数
	 */
	public int getCampaignHistoryCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCampaignHistoryCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询会员参与活动履历List
	 * 
	 * @param map 检索条件
	 * @return 参与活动履历List
	 */
	public List<Map<String, Object>> getCampaignHistoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCampaignHistoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询所有进行中的会员活动List
	 * 
	 * @param map 检索条件
	 * @return 所有进行中的会员活动List
	 */
	public List<Map<String, Object>> getCurCampaignList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCurCampaignList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员已预约活动List
	 * 
	 * @param map 检索条件
	 * @return 会员已预约活动List
	 */
	public List<Map<String, Object>> getCampaignOrderList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCampaignOrderList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员已预约活动明细
	 * 
	 * @param map 检索条件
	 * @return 会员已预约活动明细
	 */
	public Map<String, Object> getCampaignOrderDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCampaignOrderDetail");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得礼品信息List
	 * 
	 * @param map 检索条件
	 * @return 礼品信息List
	 */
	public List<Map<String, Object>> getPrtInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getPrtInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询促销活动List
	 * 
	 * @param map 检索条件
	 * @return 促销活动List
	 */
	public List<Map<String, Object>> getPromotionActList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getPromotionActList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动基本信息
	 * 
	 * @param map 检索条件
	 * @return 活动基本信息
	 */
	public Map<String, Object> getSubBaseInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getSubBaseInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得活动结果信息
	 * 
	 * @param map 检索条件
	 * @return 活动结果信息
	 */
	public List<Map<String, Object>> getSubResultList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getSubResultList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动条件信息
	 * 
	 * @param map 检索条件
	 * @return 活动条件信息
	 */
	public List<Map<String, Object>> getSubConditionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getSubConditionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询促销活动信息
	 * 
	 * @param map 检索条件
	 * @return 促销活动信息
	 */
	public Map<String, Object> getPromotionActInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getPromotionActInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询促销活动条件
	 * 
	 * @param map 检索条件
	 * @return 促销活动条件
	 */
	public List<Map<String, Object>> getPromotionActConList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getPromotionActConList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询促销活动结果
	 * 
	 * @param map 检索条件
	 * @return 促销活动结果
	 */
	public List<Map<String, Object>> getPromotionActRelList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getPromotionActRelList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询柜台名称
	 * 
	 * @param map 检索条件
	 * @return 柜台名称
	 */
	public String getCounterName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCounterName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询渠道名称
	 * 
	 * @param map 检索条件
	 * @return 渠道名称
	 */
	public String getChannelName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getChannelName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询城市名称
	 * 
	 * @param map 检索条件
	 * @return 城市名称
	 */
	public String getCityName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM07.getCityName");
		return (String)baseServiceImpl.get(parameterMap);
	}

}
