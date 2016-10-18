/*	
 * @(#)BINOLJNCOM01_Service.java     1.0 2011/4/18		
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

import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员入会共通 Service
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM01_Service extends BaseService{
	
	/**
     * 取得页面对应的活动模板List
     * 
     * @param map
     * @return
     * 		活动模板List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCamTempList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCamTempList");
        return baseServiceImpl.getList(paramMap);
    }
	
	
	/**
	 * 取得会员等级有效期
	 * 
	 * @param map 
	 * 				查询条件
	 * @return List
	 * 				会员等级有效期
	 */
	@SuppressWarnings("unchecked")
	public List getMemberLevelList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getMemberLevelList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
     * 取得会员子活动List
     * 
     * @param map
     * @return
     * 		会员子活动List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCampaignRuleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCampaignRuleList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得会员活动List
     * 
     * @param map
     * @return
     * 		会员子活动List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCampaignList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得会员子活动信息
     * 
     * @param map
     * @return
     * 		会员子活动信息
     */
	@SuppressWarnings("unchecked")
    public Map<String, Object> getCampaignRuleMap(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		// 子活动ID
		paramMap.put("campaignRuleId", map.get("campaignRuleId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCampaignRuleMap");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
     * 取得会员子活动条数
     * 
     * @param map
     * @return
     * 		会员子活动条数
     */
	@SuppressWarnings("unchecked")
    public int getCampaignRuleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCampaignRuleCount");
        return baseServiceImpl.getSum(paramMap);
	}
	
	/**
     * 取得默认规则数
     * 
     * @param map
     * @return
     * 		默认规则数
     */
    public int getDefaultCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getDefaultCount");
        return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 插入会员活动表并返回会员活动ID
	 * 
	 * @param CampaignDTO
	 * @return int
	 */
	public int insertCampaign(CampaignDTO campaignDTO) {
		return baseServiceImpl.saveBackId(campaignDTO, "BINOLJNCOM01.insertCampaign");
	}
	
	/**
	 * 插入会员子活动表并返回子活动ID
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int insertCampaignRule(CampaignRuleDTO campaignRuleDTO) {
		return baseServiceImpl.saveBackId(campaignRuleDTO, "BINOLJNCOM01.insertCampaignRule");
	}
	
	/**
	 * 更新会员子活动表
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int updateCampaignRule(CampaignRuleDTO campaignRuleDTO){
		return baseServiceImpl.update(campaignRuleDTO, "BINOLJNCOM01.updateCampaignRule");
		 
	}
	
	/**
	 * 生成会员活动代号
	 * 
	 * @param String
	 * 			前缀
	 * @param String
	 * 			后缀
	 * @return String
	 * 			会员活动代号
	 */
	public String createCampaignCode(String prefix, String suffix) {
		// 系统时间
		String sysDate = this.getSYSDate();
		sysDate = sysDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		String campaignCode = prefix + sysDate.substring(2, 14) + suffix;
		return campaignCode;
	}
	
	/**
	 * 插入会员活动基础属性表并返回会员活动基础属性ID
	 * 
	 * @param CamRuleConditionDTO
	 * @return int
	 */
	public int insertCampaignBaseProp(CampRuleConditionDTO camRuleConditionDTO) {
		return baseServiceImpl.saveBackId(camRuleConditionDTO, "BINOLJNCOM01.insertCampaignBaseProp");
	}
	
	
	/**
	 * 取得基础属性ID
	 * 
	 * @param camRuleConditionDTO
	 * @return 基础属性ID
	 */
	public String getCampaignBasePropID(CampRuleConditionDTO camRuleConditionDTO) {
		return (String) baseServiceImpl.get(camRuleConditionDTO, "BINOLJNCOM01.getCampaignBasePropID");
	}
	
	/**
	 * 插入会员活动规则条件明细表
	 * 
	 * @param CampRuleConditionDTO
	 * @return
	 */
	public void insertCamRuleCondition(CampRuleConditionDTO camRuleConditionDTO) {
		baseServiceImpl.save(camRuleConditionDTO, "BINOLJNCOM01.insertCamRuleCondition");
	}
	
	/**
	 *删除会员活动规则条件明细
	 * 
	 * @param CampRuleConditionDTO
	 * @return
	 */
	public void deleteCamRuleCondition(CampRuleConditionDTO camRuleConditionDTO) {
		baseServiceImpl.remove(camRuleConditionDTO, "BINOLJNCOM01.deleteCamRuleCondition");
	}
	
	/**
	 * 取得规则id
	 * 
	 * @param map
	 * @return String 规则Id
	 * 
	 */
	public String getCampaignId(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.getCampaignID");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新停用标志
	 * 
	 * @param map
	 * @return 无
	 * 
	 */
	public int updateValid(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLJNCOM01.updateValid");
		return baseServiceImpl.update(paramMap);
	}
}
