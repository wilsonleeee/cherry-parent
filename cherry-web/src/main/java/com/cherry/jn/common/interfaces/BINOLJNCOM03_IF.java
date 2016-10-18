/*	
 * @(#)BINOLJNCOM03_IF.java     1.0 2011/4/18		
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
package com.cherry.jn.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 会员活动组添加 IF
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public interface BINOLJNCOM03_IF {
	
	/**
	 * 取得活动组对应的模板List
	 * 
	 * @param map
	 * @return 模板List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getGrpTemplateList(Map<String, Object> map) throws Exception;
	
	/**
	 * 会员活动组保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveGrp(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员活动组信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getCampaignGrpInfo(Map<String, Object> map);
	
	/**
	 * 取得页面显示的规则组模板List
	 * 
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @return 页面显示的规则组模板List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> convertGrpTempList(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员活动总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCampaignCount(Map<String, Object> map);
	
	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getCampaignList (Map<String, Object> map);

	/**
	 * 取得会员活动信息List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List getPopCampaignList (Map<String, Object> map) throws Exception;

	public int getPopCampaignCount(Map<String, Object> map);

	public Map<String, Object> getRuleDetail(Map<String, Object> map);
	
	/**
	 * 取得规则列表
	 * 
	 * @param map
	 * 			brandInfoId : 品牌ID
	 * 			organizationInfoId : 组织ID
	 * 			campaignType : 活动类型
	 * @return List
	 * 			规则列表
	 */
	public List<Map<String, Object>> getCampRuleList(Map<String, Object> map);
	
	/**
	 * 取得规则总数
	 * 
	 * @param map
	 * @return
	 */
	public int getComCampRuleCount(Map<String, Object> map);
	
	/**
	 * 取得规则List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getComCampaignRuleList(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getRuleConfigInfo(Map<String, Object> map);
	
	/**
	 * 取得规则信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCampRuleInfo (Map<String, Object> map);
	
	/**
	 * <p>
	 * 通过规则ID获取规则信息
	 * </p>
	 * 
	 * 
	 * @param ruleId
	 * 			规则ID
	 * @param ruleList
	 * 			规则列表
	 * @return Map 规则信息
	 * @throws Exception 
	 * 
	 */
    public Map<String, Object> findRuleById(String ruleId, List<Map<String, Object>> ruleList);
    
    /**
	 * 取得有效的规则配置信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getValidConfigInfo(Map<String, Object> map);
}
