/*	
 * @(#)BINOLCPCOM02_Service.java     1.0 2011/7/18		
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
package com.cherry.cp.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.dto.CampBasePropDTO;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.dto.RuleDTO;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员活动共通 Service
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM02_Service extends BaseService{
	
	@Resource
	protected BINOLCPCOM02_Service binolcpcom02_Service;
	
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
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 页面ID
		paramMap.put("pageId", map.get("pageId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCamTempList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得页面对应的模板List
     * 
     * @param map
     * @return
     * 		页面对应的模板List
	 * @throws Exception 
     */
    public List<Map<String, Object>> getCampTempList(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 页面ID
		paramMap.put("pageId", map.get("pageId"));
		// 模板类型
		paramMap.put("templateType", map.get("templateType"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampTempList");
        List<Map<String, Object>> templateList = baseServiceImpl.getList(paramMap);
        // 设置模板的元数据集合
        metaDataSetting(templateList);
        return templateList;
    }
    
    
    /**
     * 设置模板的元数据集合
     * 
     * @param templateList
     * 				模板List
     * @throws Exception 
     */
    private void metaDataSetting(List<Map<String, Object>> templateList) throws Exception {
    	if (null != templateList) {
        	for (Map<String, Object> templateInfo : templateList) {
        		// 元数据集合
        		String metaDatas = (String) templateInfo.get("metaDatas");
        		if (!CherryChecker.isNullOrEmpty(metaDatas)) {
        			List<Map<String, Object>> metaDataList = new ArrayList<Map<String, Object>>();
        			Map<String, Object> metaDataMap = (Map<String, Object>) JSONUtil.deserialize(metaDatas);
        			for(Map.Entry<String,Object> en: metaDataMap.entrySet()) {
        				Map<String, Object> metaData = new HashMap<String, Object>();
        				// KEY
        				metaData.put("DATA_KEY", en.getKey());
        				// 值
        				metaData.put("DATA_VAL", en.getValue());
        				metaDataList.add(metaData);
        			}
        			templateInfo.put("metaDataList", metaDataList);
        		}
        	}
        }
    }
	
    /**
     * 取得业务模板对应的模板List
     * 
     * @param map
     * @return
     * 		页面对应的模板List
     * @throws Exception 
     */
    public List<Map<String, Object>> getLinkCampTempList(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动模板ID
		paramMap.put("campTempLinkId", map.get("campTempLinkId"));
		paramMap.put("pageId", map.get("pageId"));
		// 模板类型
		paramMap.put("templateType", map.get("templateType"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getLinkCampTempList");
		List<Map<String, Object>> templateList = baseServiceImpl.getList(paramMap);
        // 设置模板的元数据集合
        metaDataSetting(templateList);
        return templateList;
    }
    
	/**
     * 取得会员等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
    public List<Map<String, Object>> getMemberLevelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 会员俱乐部ID
		paramMap.put("memberClubId", map.get("memberClubId"));
		// 取得业务系统时间
		String busDate = binolcpcom02_Service.getBussinessDate(paramMap);
		// 插入业务日期
		paramMap.put("busDate", busDate);
		// 会员等级ID
		paramMap.put("memberLevelId", map.get("memberLevelId"));
		// 有效期开始日
		paramMap.put("campaignFromDate", map.get("campaignFromDate"));
		// 有效期结束日
		paramMap.put("campaignToDate", map.get("campaignToDate"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getMemberLevelList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得会员所有等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
    public List<Map<String, Object>> getLevelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 业务系统时间
		paramMap.put("busDate", map.get("busDate"));
		// 会员俱乐部ID
		paramMap.put("memberClubId", map.get("memberClubId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getLevelList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得升降级会员等级List
     * 
     * @param map
     * @return
     * 		升降级中会员等级List
     */
    public String getMemberLevelName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getMemberLevelName");
        return (String) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得升降级会员等级List
     * 
     * @param map
     * @return
     * 		升降级中会员等级List
     */
    public List<Map<String, Object>> getUpMemberLevelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		CampaignDTO campaign = (CampaignDTO) map.get("campInfo");
		if (null != campaign) {
    		// 规则开始日
			paramMap.put("ruleFromDate", campaign.getCampaignFromDate());
    		// 规则结束日
			paramMap.put("ruleToDate", campaign.getCampaignToDate());
		}
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getUpMemberLevelList");
        List<Map<String, Object>> levelList = baseServiceImpl.getList(paramMap);
        if (null != levelList) {
        	int index = -1;
        	int size = levelList.size();
        	for (int i = 0; i < size; i++) {
        		Map<String, Object> levelInfo = levelList.get(i);
        		// 会员等级Id
        		String memberLevelId = String.valueOf(levelInfo.get("memberLevelId"));
        		if (memberLevelId.equals(campaign.getMemberLevelId())) {
        			index = i;
        			break;
        		}
        	}
        	if (-1 != index) {
        		// 升级
        		if ("0".equals(String.valueOf(map.get("upordownFlag")))) {
        			levelList = levelList.subList(index + 1, size);
        			// 降级
        		} else {
        			levelList = levelList.subList(0, index);
        		}
        	}
        }
        return levelList;
    }
    
    /**
     * 取得会员等级有效期List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
    public List<Map<String, Object>> getMemberLevelDateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 有效期开始日期
		paramMap.put("campaignFromDate", map.get("campaignFromDate"));
		// 有效期结束日期
		paramMap.put("campaignToDate", map.get("campaignToDate"));
		// 会员等级ID
		paramMap.put("memberLevelId", map.get("memberLevelId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getMemberLevelDateList");
        return baseServiceImpl.getList(paramMap);
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
	 * 插入会员活动表并返回会员活动ID
	 * 
	 * @param CampaignDTO
	 * @return int
	 */
	@CacheEvict(value="CherryActivityCache",allEntries=true,beforeInvocation=false)
	public int insertCampaign(CampaignDTO campaignDTO) {
		return baseServiceImpl.saveBackId(campaignDTO, "BINOLCPCOM02.insertCampaign");
	}
	
	/**
	 * 插入会员子活动表并返回子活动ID
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	@CacheEvict(value="CherryActivityCache",allEntries=true,beforeInvocation=false)
	public int insertCampaignRule(CampaignRuleDTO campaignRuleDTO) {
		return baseServiceImpl.saveBackId(campaignRuleDTO, "BINOLCPCOM02.insertCampaignRule");
	}
	
	/**
	 * 更新会员活动表
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	@CacheEvict(value="CherryActivityCache",allEntries=true,beforeInvocation=false)
	public int updateCampaign(CampaignDTO campaignDTO){
		if (CherryChecker.isNullOrEmpty(campaignDTO.getValidFlag())) {
			campaignDTO.setValidFlag("1");
		}
		return baseServiceImpl.update(campaignDTO, "BINOLCPCOM02.updateCampaign");
		 
	}
	
	/**
	 * 更新会员子活动表
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	@CacheEvict(value="CherryActivityCache",allEntries=true,beforeInvocation=false)
	public int updateCampaignRule(CampaignRuleDTO campaignRuleDTO){
		return baseServiceImpl.update(campaignRuleDTO, "BINOLCPCOM02.updateCampaignRule");
		 
	}
	
	/**
	 * 更新规则文件
	 * 
	 * @param CampaignRuleDTO
	 * @return int
	 */
	public int updateRuleContent(CampaignDTO campaignDTO){
		return baseServiceImpl.update(campaignDTO, "BINOLCPCOM02.updateRuleContent");
		 
	}
	
	/**
     * 取得规则体详细信息
     * 
     * @param map
     * @return
     * 		规则体详细信息
     */
    public Map<String, Object> getRuleDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.put("campaignId", map.get("campaignId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getRuleDetail");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得会员活动信息
     * 
     * @param map
     * @return
     * 		会员活动信息
     */
    public CampaignDTO getCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.put("campaignId", map.get("campaignId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampaignInfo");
        return (CampaignDTO) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得会员保存信息
     * 
     * @param map
     * @return
     * 		会员活动保存信息
     */
    public List<Map<String, Object>> getCampSaveList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampSaveList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得编辑时的等级时间和等级ID信息
     * 
     * @param map
     * @return
     * 		会员活动保存信息
     */
    public Map<String, Object> getDateMap(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员活动子规则ID
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getDateInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    /**
     * 取得品牌名称
     * 
     * @param map
     * @return
     * 		品牌名称
     */
    public String getBrandName(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getBrandNameByID");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
     * 通过会员等级ID取得对应的名称
     * 
     * @param map
     * @return
     * 		品牌名称
     */
    public String getMemLevelNameById(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getMemLevelNameById");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
	 * 插入规则表返回规则ID
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int insertRuleDTO(RuleDTO ruleDTO) {
		return baseServiceImpl.saveBackId(ruleDTO, "BINOLCPCOM02.insertRuleDTO");
	}
	
	/**
	 * 更新规则表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int updateRuleDTO(RuleDTO ruleDTO){
		return baseServiceImpl.update(ruleDTO, "BINOLCPCOM02.updateRuleDTO");
		 
	}
	
	/**
	 * 停用子活动
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int invalidCampaignRule(CampaignRuleDTO campaignRuleDTO){
		return baseServiceImpl.update(campaignRuleDTO, "BINOLCPCOM02.invalidCampaignRule");
		 
	}
	
	/**
	 * 插入会员活动规则条件明细表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void addRuleConditions(List<CampRuleConditionDTO> ruleConditionList) {
		baseServiceImpl.saveAllByPage(ruleConditionList, "BINOLCPCOM02.insertRuleCondition");
	}
	
	/**
	 * 插入会员活动规则结果明细表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void addRuleResults(List<CampRuleResultDTO> ruleResultList) {
		baseServiceImpl.saveAllByPage(ruleResultList, "BINOLCPCOM02.insertRuleResult");
	}
	
	/**
	 * 停用会员活动规则条件明细
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int invalidRuleCondition(CampaignRuleDTO campaignRuleDTO){
		return baseServiceImpl.update(campaignRuleDTO, "BINOLCPCOM02.invalidRuleCondition");
		 
	}
	
	/**
	 * 停用会员活动规则结果明细
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int invalidRuleResult(CampaignRuleDTO campaignRuleDTO){
		return baseServiceImpl.update(campaignRuleDTO, "BINOLCPCOM02.invalidRuleResult");
		 
	}
	
	/**
	 * 停用规则
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int invalidRuleDTO(RuleDTO ruleDTO){
		return baseServiceImpl.update(ruleDTO, "BINOLCPCOM02.invalidRuleDTO");
		 
	}
	
	/**
     * 取得规则模板内容
     * 
     * @param map
     * @return
     * 		模板内容
     */
    public String getRuleTempContent(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getRuleTempContentList");
		// 规则模板内容List
		List<Map<String, Object>> tempContentList = baseServiceImpl.getList(paramMap);
		Map<String, Object> contentMap = new HashMap<String, Object>();
		if (null != tempContentList) {
			if (tempContentList.size() == 1) {
				return (String) tempContentList.get(0).get("templateContent");
			}
			for (Map<String, Object> tempContentMap : tempContentList) {
				contentMap.put((String) tempContentMap.get("templateKey"), tempContentMap.get("templateContent"));
			}
		}
		if (!contentMap.isEmpty()) {
			// 集合标识符
			String groupCode = (null == map.get("groupCode")) ? "A" : (String) map.get("groupCode");
			// 会员活动类型
			String campaignType = (String) map.get("campaignType");
			String combKey = groupCode + "_" + campaignType;
			if (!contentMap.containsKey(combKey)) {
				// 会员活动类型:共通
				combKey = groupCode + "_" + CherryConstants.CAMPAIGN_TYPE_ALL;
				if (!CherryConstants.GROUP_CODE_ALL.equals(groupCode) && !contentMap.containsKey(combKey)) {
					// 集合标识符:共通
					combKey = CherryConstants.GROUP_CODE_ALL + "_" + campaignType;
					if (!contentMap.containsKey(combKey)) {
						// 集合标识符:共通 + 会员活动类型:共通
						combKey = CherryConstants.GROUP_CODE_ALL + "_" + CherryConstants.CAMPAIGN_TYPE_ALL;
					}
				}
			}
			return (String) contentMap.get(combKey);
		}
        return null;
    }
    
    /**
     * 取得组织品牌代码信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    public Map<String, Object> getOrgBrandCodeInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getOrgBrandCodeInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得规则描述信息
     * 
     * @param map
     * @return Map
     * 		组织品牌代码信息
     */
    public List<Map<String, Object>> getDepList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getDepList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
	 * 插入会员活动基础属性表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public int insertCampaignBaseProp(CampBasePropDTO campBaseProp) {
		return baseServiceImpl.saveBackId(campBaseProp, "BINOLCPCOM02.insertCampaignBaseProp");
	}
	
	/**
     * 取得会员活动基础属性信息
     * 
     * @param map
     * @return
     * 		List
     * 		会员活动基础属性信息
     */
    public List<CampBasePropDTO> getCampBasePropList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampaignBasePropList");
        return (List<CampBasePropDTO>) baseServiceImpl.getList(map);
    }
	
    /**
     * 取得当前配置规则ID
     * 
     * @param map
     * @return
     * 		int
     * 		规则配置ID
     */
    public String getRuleConCount(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getRuleConCount");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
     * 取得规则配置中的优先级设置
     * 
     * @param map
     * @return
     * 		Map
     * 		某类型会员活动优先级信息
     */
    public String getPriorityMap(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getPriorityMap");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
     * 取得规则配置中的优先级设置
     * 
     * @param map
     * @return
     * 		Map
     * 		通过活动ID取得某类型会员活动优先级信息
     */
    public String getPriorityByIdMap(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getPriorityByIdMap");
        return (String) baseServiceImpl.get(map);
    }
    
    
    /**
	 * 插入会员活动组表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertCampaignConfig(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.insertCampaignConfig");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
     * 取得会员活动规则结果明细信息
     * 
     * @param map
     * @return
     * 		List
     * 		会员活动规则结果明细信息
     */
    public List<CampRuleResultDTO> getRuleResultList(CampaignRuleDTO campaignRuleDTO) {
        return (List<CampRuleResultDTO>) baseServiceImpl.getList(campaignRuleDTO, "BINOLCPCOM02.getRuleResultList");
    }
    
    /**
	 * 
	 * 物理删除会员活动规则结果明细表
	 * 
	 * @param campaignRuleDTO
	 * 			会员子活动信息
	 * 
	 */
	public int delRuleResult(CampaignRuleDTO campaignRuleDTO) {
		return baseServiceImpl.remove(campaignRuleDTO, "BINOLCPCOM02.delCPRuleResult");
	}
    
    /**
	 * 更新会员活动规则结果明细表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void updateRuleResults(List<CampRuleResultDTO> ruleResultList) {
		baseServiceImpl.updateAllByPage(ruleResultList, "BINOLCPCOM02.updateRuleResult");
	}
	
	 /**
	 * 更新会员活动组表
	 * 
	 * @param map
	 * @return int
	 */
	public int updateCampaignConfig(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.updateCampaignConfig");
		return baseServiceImpl.update(map);
	}
	/**
	 * 更新会员活动组表(规则文件)
	 * 
	 * @param map
	 * @return int
	 */
	public int updateConfigRuleFile(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.updateConfigRuleFile");
		return baseServiceImpl.update(map);
	}
	
	/**
     * 取得会员有效期设置信息
     * 
     * @param map
     * @return
     * 		String
     * 			会员有效期信息
     */
    public String getMemberDate(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getMemberDate");
        return (String) baseServiceImpl.get(map);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.updateValid");
		return baseServiceImpl.update(paramMap);
	}
	
	 /**
     * 取得活动组更新信息
     * 
     * @param map
     * @return Map
     * 		活动组更新信息
     */
    public Map<String, Object> getUpdateInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getUpdateInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得会员子活动信息
     * 
     * @param campaignRuleDTO
     * @return Map
     * 		会员子活动信息
     */
    public Map<String, Object> getSubCampaignRuleInfo(CampaignRuleDTO campaignRuleDTO) {
        return (Map<String, Object>) baseServiceImpl.get(campaignRuleDTO, "BINOLCPCOM02.getSubCampaignRuleInfo");
    }
    
    /**
     * 取得产品信息
     * 
     * @param map
     * @return Map
     * 		活动组更新信息
     */
    public Map<String, Object> getProInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getProInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    /**
     * 取得促销品信息
     * 
     * @param map
     * @return Map
     * 		活动组更新信息
     */
    public Map<String, Object> getPrmInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getPrmInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得产品信息和促销品信息
     * 
     * @param map
     * @return Map
     */
    public Map<String, Object> getProOrPrmInfo(Map<String, Object> map) {
    	Map<String, Object> param = new HashMap<String, Object>(map);
    	param.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getProOrPrmInfo");
        return (Map<String, Object>) baseServiceImpl.get(param);
    }
    /**
     * 取得产品分类名称信息
     * 
     * @param map
     * @return Map
     * 		活动组更新信息
     */
    public String getCateName(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCateName");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
     * 取得子活动列表 
     * 
     * @param map
     * @return List
     * 		子活动列表 
     */
    public List<Map<String, Object>> getSubCampList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getSubCampList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
     * 取得会员活动扩展信息
     * 
     * @param map
     * @return Map
     */
    public Map<String, Object> getCampaignExtInfo(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampaignExtInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得已生成的促销品信息
     * 
     * @param map
     * @return Map
     */
    public Map<String, Object> getSubPrmInfo(Map<String, Object> map) {
    	Map<String, Object> param = new HashMap<String, Object>(map);
    	param.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getSubPrmInfo");
        return (Map<String, Object>) baseServiceImpl.get(param);
    }
    /**
     * 取得活动已经生成的促销品厂商List
     * 
     * @param map
     * @return List
     * 促销品厂商List
     */
    public List<Map<String, Object>> getCampPrmVendorList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCampPrmVendorList");
    	return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
     * 取得可兑换积分截止日期LIST
     * 
     * @param map
     * @return List
     */
    public List<Map<String, String>> getExPointDeadDateList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getExPointDeadDateList");
    	return (List<Map<String, String>>) baseServiceImpl.getList(map);
    }
    
    /**
     * 取得可兑换积分截止日期LIST
     * 
     * @param map
     * @return List
     */
    public List<Integer> getActIdByName(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getActIdByName");
    	return baseServiceImpl.getList(map);
    }
    /**
	 * 取得子活动信息
	 * @param campaignRuleDTO
	 * @return
	 */
	public CampaignRuleDTO getCampaignRuleInfo(CampaignRuleDTO campaignRuleDTO){
        return (CampaignRuleDTO) baseServiceImpl.get(campaignRuleDTO,"BINOLCPCOM02.getCampaignRuleInfo");
	}
	
    
    /**
	 * 
	 * 物理删除会员活动规则结果明细表
	 * 
	 * @param campaignRuleDTO
	 * 			会员子活动信息
	 * 
	 */
	public int delPromotionRule(Map<String,Object> map) {
		return baseServiceImpl.remove(map, "BINOLCPCOM02.delPromotionRule");
	}
	
	/**
	 * 插入会员活动规则结果明细表
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void addPromotionRule(Map<String,Object> map) {
		baseServiceImpl.save(map, "BINOLCPCOM02.insertPromotionRule");
	}
	
	/**
	 * 
	 * 物理删除会员活动规则分类
	 * 
	 * @param campaignRuleDTO
	 * 			会员子活动信息
	 * 
	 */
	public int delPromotionRuleCate(Map<String,Object> map) {
		return baseServiceImpl.remove(map, "BINOLCPCOM02.delPromotionRuleCate");
	}
	
	/**
	 * 插入会员活动规则分类
	 * 
	 * @param ruleDTO
	 * @return int
	 */
	public void addPromotionRuleCate(Map<String,Object> map) {
		baseServiceImpl.save(map, "BINOLCPCOM02.insertPromotionRuleCate");
	}

	/**
	 * 取得活动对象总数
	 *
	 * @param map
	 * @return 活动对象总数
	 */
	public int getCustomerCount(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCustomerCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 活动对象分组
	 *
	 * @param map
	 * @return 更新件数
	 */
	public int updCampObjGroupType(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.updCampObjGroupType");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 活动对象分组
	 *
	 * @param map
	 * @return 更新件数
	 */
	public int updAllCampObjGroupType(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.updAllCampObjGroupType");
		return baseServiceImpl.update(paramMap);
	}

}
