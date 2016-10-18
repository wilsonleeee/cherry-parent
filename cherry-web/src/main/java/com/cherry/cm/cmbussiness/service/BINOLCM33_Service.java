/*	
 * @(#)BINOLCM33_Service.java     1.0 2012/01/07		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员检索画面共通Service
 * 
 * @author WangCT
 * @version 1.0 2012/01/07
 */
public class BINOLCM33_Service extends BaseService {
	
	/**
	 * 取得会员搜索条件记录数
	 * 
	 * @param map 查询条件
	 * @return 会员搜索条件记录数
	 */
	public int getSearchRequestCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getSearchRequestCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得会员搜索条件List
	 * 
	 * @param map 查询条件
	 * @return 会员搜索条件List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSearchRequestList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getSearchRequestList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 添加会员搜索条件
	 * 
	 * @param map 添加内容
	 */
	public void addSearchRequest(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.addSearchRequest");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 删除会员搜索条件
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int deleteSearchRequest(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.deleteSearchRequest");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberLevelInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员俱乐部信息
	 * 
	 * @param map 查询条件
	 * @return 会员俱乐部信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberClubInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberClubInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询产品信息List
	 * 
	 * @param map 查询条件
	 * @return 产品信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getProInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询产品分类信息List
	 * 
	 * @param map 查询条件
	 * @return 产品分类信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProTypeInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getProTypeInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据会员卡号查询会员ID
	 * 
	 * @param map 检索条件
	 * @return 会员ID
	 */
	public String getMemberInfoId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberInfoId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询会员List
	 * 
	 * @param map 检索条件
	 * @return 会员List
	 */
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberClubInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberInfoClubCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询会员List
	 * 
	 * @param map 检索条件
	 * @return 会员List
	 */
	public List<Map<String, Object>> getMemberClubInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemberInfoClubList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询区域信息List
	 * 
	 * @param map 查询条件
	 * @return 区域信息List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRegionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getRegionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询渠道信息List
	 * 
	 * @param map 查询条件
	 * @return 渠道信息List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getChannelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getChannelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询柜台信息List
	 * 
	 * @param map 查询条件
	 * @return 柜台信息List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrganizationList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getOrganizationList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员活动名称
	 * 
	 * @param map 查询条件
	 * @return 会员活动名称
	 */
	public String getMemCampaignName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemCampaignName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询促销活动名称
	 * 
	 * @param map 查询条件
	 * @return 促销活动名称
	 */
	public String getPrmCampaignName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getPrmCampaignName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询发卡BA姓名
	 * 
	 * @param map 查询条件
	 * @return 发卡BA姓名
	 */
	public String getEmployeeName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getEmployeeName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得所有区域信息List
	 * 
	 * @param map 查询条件
	 * @return 所有区域信息List
	 */
	public List<Map<String, Object>> getAllRegionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getAllRegionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员问卷问题信息List
	 * 
	 * @param map 查询条件
	 * @return 会员问卷问题信息List
	 */
	public List<Map<String, Object>> getPaperQuestionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getPaperQuestionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得会员销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 会员销售统计信息
	 */
	public Map<String, Object> getMemSaleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemSaleCount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @param map 查询条件
	 * @return 会员销售信息List
	 */
	public List<Map<String, Object>> getMemSaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemSaleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得会员销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 会员销售统计信息
	 */
	public Map<String, Object> getMemClubSaleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemSaleClubCount");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @param map 查询条件
	 * @return 会员销售信息List
	 */
	public List<Map<String, Object>> getMemClubSaleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM33.getMemSaleClubList");
		return baseServiceImpl.getList(paramMap);
	}
}
