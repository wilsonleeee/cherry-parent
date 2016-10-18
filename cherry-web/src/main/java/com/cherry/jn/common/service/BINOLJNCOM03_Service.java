/*	
 * @(#)BINOLJNCOM03_Service.java     1.0 2011/4/18		
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
package com.cherry.jn.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动组添加共通 Service
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM03_Service extends BaseService{
	
	/**
	 * 插入会员活动组表并返回会员活动组ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertCampaignGrp(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLJNCOM03.insertCampaignGrp");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新会员活动组表
	 * 
	 * @param map
	 * @return int
	 */
	public int updateCampaignGrp(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.updateCampaignGrp");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得会员活动组信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getCampaignGrpInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动组ID
		paramMap.put("campaignGrpId", map.get("campaignGrpId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getCampaignGrpInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得会员活动总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCampaignCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getJncom03CampaignCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getCampaignList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getJncom03CampaignList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得规则总数
	 * 
	 * @param map
	 * @return
	 */
	public int getComCampRuleCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getComCampRuleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得规则List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getComCampRuleList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getComCampRuleList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 */
	public String getRuleName (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getRuleName");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getCampaignRuleList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getRuleInfo");
		return baseServiceImpl.getList(map);
	}
	

	/**
	 * 取得会员活动配置明细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getRuleDetail (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getRuleDetail");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得规则列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampRuleList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getCampRuleList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getRuleConfigInfo (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getRuleConfigInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得规则信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCampRuleInfo (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getCampRuleInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得有效的规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getValidConfigInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM03.getValidConfigInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
}
