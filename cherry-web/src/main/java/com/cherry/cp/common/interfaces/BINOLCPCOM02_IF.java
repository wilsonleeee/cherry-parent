/*	
 * @(#)BINOLCPCOM02_IF.java     1.0 2011/7/18		
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
package com.cherry.cp.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.RuleBodyDTO;

/**
 * 会员活动共通 IF
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public interface BINOLCPCOM02_IF {
	
	/**
	 * 取得页面对应的活动模板List
	 * 
	 * @param map
	 * @return 活动模板List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> searchCamTempList(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得页面显示的活动模板List
	 * 
	 * @param List
	 *            数据库取得的活动模板List
	 * @param Map
	 *            查询数据库里活动模板的参数
	 * @return 页面显示的活动模板List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> convertCamTempList(Map<String, Object> map) throws Exception;
	
	/**
	 * 会员活动保存处理
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return 保存是否成功消息String
	 * @throws Exception 
	 */
	public String tran_saveCampaign(Map<String, Object> map) throws Exception;
	
	/**
     * 取得规则体详细信息
     * 
     * @param map
     * @return
     * 		规则体详细信息
     */
	public Map<String, Object> getRuleDetail(Map<String, Object> map);
	
	/**
     * 取得活动保存信息
     * 
     * @param map
     * @return
     * 		活动保存信息
     */
	public Map<String, Object> getCampSaveInfo(Map<String, Object> map);
	
	/**
     * 取得等级时间和等级ID信息
     * 
     * @param map
     * @return
     * 		活动保存信息
     */
	public Map<String, Object> getDateMap(Map<String, Object> map);
	
	/**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
	public CampaignDTO getCampaignInfo(Map<String, Object> map);
	
	/**
     * 取得品牌名称
     * 
     * @param map
     * @return
     * 		品牌名称
     */
    public String getBrandName(Map<String, Object> map);
    
    /**
	 * 取得规则内容
	 * 
	 * @param camTempList
	 * 			规则设置内容
	 * @param map
	 * 			参数集合
	 * @param ruleBodyList
	 * 			规则内容 
	 * @return 无
	 * @throws Exception 
	 */
	public void getRuleContents(List<Map<String, Object>> camTempList, Map<String, Object> map, List<RuleBodyDTO> ruleBodyList) throws Exception;
	
	/**
	 * 转换页面提交的活动模板List
	 * 
	 * @param List
	 *            页面提交的活动模板List
	 *            
	 * @return 转换后的模板Map
	 */
	public void convertCamTempListToMap(List<Map<String, Object>> camTempList, Map<String, Object> camTempMap);
	
	/**
	 * 取得页面显示用的活动模板List
	 * 
	 * @param List
	 *            数据库取得的活动模板List
	 * @param Map
	 *            转换后的模板Map
	 *            
	 * @return 页面显示用的活动模板List
	 * @throws Exception 
	 */
	public void getCamTempList(List<Map<String, Object>> camTempDBList, Map<String, Object> camTempMap, Map<String, Object> baseMap) throws Exception;
	
	/**
     * 取得组织品牌代码信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    public Map<String, Object> getOrgBrandCodeInfo(Map<String, Object> map);

    /**
     * 取得规则配置中的优先级设置
     * 
     * @param map
     * @return
     * 		Map
     * 		某类型会员活动优先级信息
     */
	public String getPriorityMap(Map<String, Object> map);

	/**
	 * 插入会员活动组表（优先级配置）
	 * 
	 * @param map
	 */
	public int insertCampaignConfig(Map<String, Object> map);

	/**
	 * 更新会员活动组表（优先级配置）
	 * 
	 * @param map
	 */
	public int updateCampaignConfig(Map<String, Object> map);

	/**
     * 取得当前配置规则Id
     * 
     * @param map
     * @return
     * 		String
     * 		规则配置ID
     */
	public String getRuleConCount(Map<String, Object> map);
	
	/**
     * 解析规则体详细信息(Map)
     * 
     * @param ruleDetailMap
     * 			规则体详细信息(Map)
     * @return
     * 		List
     * 			活动设定内容
	 * @throws Exception 
     */
    public List<Map<String, Object>> deseRuleDetailMap(Map<String, Object> ruleDetailMap) throws Exception;

	/**
     * 停用规则
     * 
     * @param map
     * 			基本信息Map
     * @return
     * 		int
     * 			更新结果
	 * @throws Exception 
     */
	public int updateValid(Map<String, Object> map);

	/**
     * 通过活动id取得优先级信息
     * 
     * @param String
     * 			活动Id
     * @return
     * 		List
     * 			优先级信息
	 * @throws Exception 
     */
	public List<Map<String, Object>> getPriorityByIdMap(String campaignId) throws Exception;

	/**
     * 取得活动组更新信息
     * 
     * @param paramMap
     * 			参数(Map)
     * @return
     * 		Map
     * 			活动组更新时间和次数
	 * @throws Exception 
     */
	public Map<String, Object> getUpdateInfo(Map<String, Object> paramMap);

	/**
     * 停用规则并更新优先级信息
     * 
     * @param map
     * 			基本信息(Map)
     * @return
     * 		String
     * 			成功或失败标识
	 * @throws Exception 
	 * @throws Exception 
     */
	public void tran_editValid(Map<String, Object> map) throws Exception;

	/**
	 * 验证优先级组是否包含指定活动
	 * 
	 * @param campaignId
	 * 			活动ID
	 * @param priorityList
	 * 			优先级List
	 * @return 验证结果 true : 包含, false: 不包含
	 * 
	 */
	public boolean isContain(String campaignId, List<Map<String, Object>> priorityList);

	 /**
     * 取得产品信息
     * 
     * @param map
     * @return
     * 		产品信息
     */
	public Map<String, Object> getProInfo(Map<String, Object> paramMap);

	 /**
     * 取得产品分类名称
     * 
     * @param map
     * @return
     * 		String
     */
	public String getCateName(Map<String, Object> paramMap);
	
	/**
	 * 取得规则配置列表
	 * 
	 * @param map
	 * @return
	 * 			List
	 * 			规则配置列表
	 */
	public List<Map<String, Object>> getRuleConfList (Map<String, Object> map);
	
	/**
     * 更新配置表的优先级信息
     * 
     * @return
     * 		int
     */
    public int updateConfPriority(Map<String, Object> map);
    
    /**
	 * 取得所有子活动的内容
	 * 
	 * @param allSubCamp
	 * 				前几页子活动的内容
	 * @param curContent
	 * 				当前页子活动的内容
	 * @return
	 * 			Map
	 * 			所有子活动的内容
     * @throws Exception
	 */
	public Map<String, Object> getAllsubCamp(Map<String, Object> preSubCamp, String curContent) throws Exception;
	
	/**
     * 取得子活动列表 
     * 
     * @param map
     * @return List
     * 		子活动列表 
     */
    public List<Map<String, Object>> getSubCampList(Map<String, Object> map);
    
    /**
     * 取得会员活动扩展信息
     * 
     * @param map
     */
    public void getCampaignExtInfo(CampaignDTO campaignDTO);
    
    /**
	 * 删除当前页内容
	 * 
	 * @param allSubCamp
	 * 				前几页子活动的内容
	 * @param curContent
	 * 				当前页子活动的内容
	 * @return
	 * 			Map
	 * 			所有子活动的内容
     * @throws Exception 
	 */
	public void delCurContent(Map<String, Object> allSubCamp, String curContent) throws Exception;
	
	/**
     * 取得可兑换积分截止日期LIST
     * 
     * @param map
     * @return List
     */
    public List<Map<String, String>> getExPointDeadDateList(Map<String, Object> map);
    
    /**
	 * 取得主题活动代号
	 * 
	 * 
	 * @param Map
	 *            	更新数据库参数集合
	 * @return String
	 * 				主题活动代号
	 * @throws Exception 
	 */
	public String getMainCampainCode(Map<String, Object> map) throws Exception;
	
	public List<Integer> getActIdByName(String name, String brandInfoId);
}
