/*	
 * @(#)BINOLJNMAN06_Service.java     1.0 2012/10/30		
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
package com.cherry.jn.man.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 积分规则配置添加Service
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN06_Service extends BaseService{
	/**
	 * 插入会员活动表并返回会员活动ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertConfCampaign(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLJNMAN06.insertConfCampaign");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入会员子活动表并返回会员子活动ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertConfCampaignRule(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLJNMAN06.insertConfCampaignRule");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入会员活动组表并返回会员活动组ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertConfCampaignGrp(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLJNMAN06.insertConfCampaignGrp");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新会员活动表
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfCampaign(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateConfCampaign");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得组合规则列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCombRuleList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.getCombRuleList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 停用组合规则
	 * 
	 * @param map
	 * @return int
	 */
	public int delCombRule(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.delCombRule");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 插入规则表 并返回规则内容ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertConfRuleContent(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLJNMAN06.insertConfRuleContent");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新规则文件
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfRuleFile(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateConfRuleFile");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新规则内容
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfRuleContent(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateConfRuleContent");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新会员活动组表
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfCampaignGrp(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateConfCampaignGrp");
		return baseServiceImpl.update(map);
		 
	}
	
	
	/**
	 * 更新配置表的优先级信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfPriority(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateConfPriority");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得规则配置列表
	 * 
	 * @param map
	 * @return List
	 * 			规则配置列表
	 */
	public List<Map<String, Object>> getRuleConfList (Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.getRuleConfList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新规则的配置ID
	 * 
	 * @param map
	 * @return int
	 */
	public int updateCampaignGrpId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNMAN06.updateCampaignGrpId");
		return baseServiceImpl.update(map);
		 
	}
}
