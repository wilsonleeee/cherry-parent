/*	
 * @(#)BINOLJNMAN06_BL.java     1.0 2012/10/30		
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
package com.cherry.jn.man.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;
import com.cherry.cp.common.util.CampUtil;
import com.cherry.cp.common.util.RuleToolUtil;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.jn.man.interfaces.BINOLJNMAN06_IF;
import com.cherry.jn.man.service.BINOLJNMAN06_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 积分规则配置添加BL
 * 
 * @author hub
 * @version 1.0 2012.10.30
 */
public class BINOLJNMAN06_BL implements BINOLJNMAN06_IF{
	
	@Resource
	private BINOLJNMAN06_Service binoljnman06_Service;
	
	@Resource
	private BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	/**
	 * 保存规则配置
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void tran_saveConfig(Map<String, Object> map) throws Exception {
		// 插表时的共通字段
		Map<String, Object> baseMap = getBaseMap();
		// 系统时间
		String sysDate = (String) baseMap.get(CherryConstants.UPDATE_TIME);
		// 配置ID
		String campaignGrpId = (String) map.get("campaignGrpId");
		// 会员活动类型
		map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
		map.putAll(baseMap);
		int campGrpId = 0;
		if (CherryChecker.isNullOrEmpty(campaignGrpId)) {
			// 插入会员活动组表并返回会员活动组ID
			campGrpId = binoljnman06_Service.insertConfCampaignGrp(map);
			map.put("campaignGrpId", String.valueOf(campGrpId));
			map.put("grpUpdateTime", sysDate);
			map.put("grpModifyCount", 0);
			map.put("grpValidFlag", "0");
		} else {
			campGrpId = Integer.parseInt(String.valueOf(campaignGrpId));
		}
		// 取得规则配置积分计算模板
		String grpRuleFile = getConfRuleFile(map);
		// 优先级策略
		String execType = (String) map.get("execType");
		// 积分上限
		String limit = (String) map.get("limit");
		// 配置页面其他内容
		Map<String, Object> detailInfo = new HashMap<String, Object>();
		Map<String, Object> pointLimit = null;
		if (!CherryChecker.isNullOrEmpty(limit, true)) {
			pointLimit = (Map<String, Object>) JSONUtil.deserialize(limit);
			detailInfo.put("limitInfo", pointLimit);
		}
		// 优先级列表
		List<Map<String, Object>> priorityList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mainRuleList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> usedRuleList = null;
		// 已启用规则
		String usedRules = (String) map.get("usedRules");
		if (!CherryChecker.isNullOrEmpty(usedRules)) {
			usedRuleList = (List<Map<String, Object>>) JSONUtil.deserialize(usedRules);
		}
		usedRuleList = (usedRuleList == null) ? new ArrayList<Map<String, Object>>() : usedRuleList;
		// 默认规则
		String deftRule = (String) map.get("deftRule");
		if (!CherryChecker.isNullOrEmpty(deftRule)) {
			List<Map<String, Object>> deftRuleList = (List<Map<String, Object>>) JSONUtil.deserialize(deftRule);
			if (null != deftRuleList && !deftRuleList.isEmpty()) {
				Map<String, Object> deftRuleMap = deftRuleList.get(0);
				deftRuleMap.put("defaultFlag", "1");
				usedRuleList.add(deftRuleMap);
			}
		}
		if (!usedRuleList.isEmpty()) {
			for (Map<String, Object> usedRule : usedRuleList) {
				Map<String, Object> priorityMap = new HashMap<String, Object>();
				// 执行策略
				String combType = (String) usedRule.get("combType");
				// 规则ID
				String campaignId = (String) usedRule.get("campaignId");
				// 匹配成功时执行的动作
				String afterMatch = (String) usedRule.get("afterMatch");
				if (!CherryChecker.isNullOrEmpty(afterMatch)) {
					priorityMap.put("afterMatch", afterMatch);
				}
				priorityMap.put("execType", execType);
				if (null != pointLimit) {
					priorityMap.put("pointLimit", pointLimit);
				}
				List<String> keys = new ArrayList<String>();
				keys.add(campaignId);
				// 普通规则
				if (CherryChecker.isNullOrEmpty(combType)) {
					// 默认积分规则
					if ("1".equals(usedRule.get("defaultFlag"))) {
						priorityMap.put("defaultFlag", "1");
					}
					// 附属规则
					List<Map<String, Object>> extraRuleList = (List<Map<String, Object>>) usedRule.get("extraRules");
					if (null != extraRuleList && !extraRuleList.isEmpty()) {
						mainRuleList.add(usedRule);
						for (Map<String, Object> extraRule : extraRuleList) {
							keys.add(String.valueOf(extraRule.get("campaignId")));
						}
					}
				} else {
					List<String> combRules = new ArrayList<String>();
					String geneRulesStr = (String) usedRule.get("geneRules");
					if (!CherryChecker.isNullOrEmpty(geneRulesStr)) {
						// 组内规则
						List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) JSONUtil.deserialize(geneRulesStr);
						if (null != geneRuleList) {
							for (Map<String, Object> geneRule : geneRuleList) {
								combRules.add(String.valueOf(geneRule.get("campaignId")));
							}
						}
					}
					priorityMap.put("combRules", combRules);
					// 匹配方式
					priorityMap.put("matchType", usedRule.get("matchType"));
					// 执行策略
					priorityMap.put("combType", usedRule.get("combType"));
				}
				// 规则ID
				priorityMap.put("campaignId", campaignId);
				priorityMap.put("keys", keys);
				priorityList.add(priorityMap);
			}
		}
		
		// 未启用规则
		String unusedRules = (String) map.get("unusedRules");
		if (!CherryChecker.isNullOrEmpty(unusedRules)) {
			List<Map<String, Object>> unusedRuleList = (List<Map<String, Object>>) JSONUtil.deserialize(unusedRules);
			if (null != unusedRuleList) {
				for (Map<String, Object> unusedRule : unusedRuleList) {
					// 执行策略
					String combType = (String) unusedRule.get("combType");
					// 普通规则
					if (CherryChecker.isNullOrEmpty(combType)) {
						// 附属规则
						List<Map<String, Object>> extraRuleList = (List<Map<String, Object>>) unusedRule.get("extraRules");
						if (null != extraRuleList && !extraRuleList.isEmpty()) {
							mainRuleList.add(unusedRule);
						}
					}
				}
			}
		}
		// 优先级内容
		String priority = null;
		if (!priorityList.isEmpty()) {
			priority = JSONUtil.serialize(priorityList);
		}
		// 支付方式
		if (map.containsKey("pTypes")) {
			// 配置参数集合
			Map<String, Object> gpInfo = new HashMap<String, Object>();
			gpInfo.put("pTypes", map.get("pTypes"));
			detailInfo.put("gpInfo", gpInfo);
		}
		if ("1".equals(map.get("zkPrt"))) {
			// 配置参数集合
			Map<String, Object> gpInfo = (Map<String, Object>) detailInfo.get("gpInfo");
			if (null == gpInfo) {
				gpInfo = new HashMap<String, Object>();
				detailInfo.put("gpInfo", gpInfo);
			}
			gpInfo.put("zkPrt", "1");
		}
		// 配置内容
		String ruleDetail = null;
		if (!mainRuleList.isEmpty()) {
			detailInfo.put("mainRules", mainRuleList);
		}
		if (!detailInfo.isEmpty()) {
			ruleDetail = JSONUtil.serialize(detailInfo);
		}
		map.put("ruleDetail", ruleDetail);
		map.put("priorityRuleDetail", priority);
		map.put("grpRuleFile", grpRuleFile);
		// 更新会员活动组表
		int result = binoljnman06_Service.updateConfCampaignGrp(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
		if ("1".equals(map.get("groupValidFlag"))) {
			// 更新规则的配置ID
			binoljnman06_Service.updateCampaignGrpId(map);
			// 刷新该组规则
			knowledgeEngine.refreshGrpRule(campGrpId);
		}
	}
	
	/**
	 * 保存组合规则
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void tran_saveComb(Map<String, Object> map) throws Exception {
		// 插表时的共通字段
		Map<String, Object> baseMap = getBaseMap();
		// 系统时间
		String sysDate = (String) baseMap.get(CherryConstants.UPDATE_TIME);
		// 组织ID
		String organizationInfoId = String.valueOf(map.get("organizationInfoId"));
		// 品牌ID
		String brandInfoId = (String) map.get("brandInfoId");
		// 创建者
		String campaignSetBy = String.valueOf(map.get("campaignSetBy"));
		// 会员活动类型
		map.put("campaignType", DroolsConstants.CAMPAIGN_TYPE3);
		map.putAll(baseMap);
		// 会员活动ID
		String campaignId = (String) map.get("campaignId");
		// 新增组合规则
		if (CherryChecker.isNullOrEmpty(campaignId)) {
			// 会员活动代号
			String campaignCode = binOLCM03_BL.getTicketNumber(
					organizationInfoId, brandInfoId, campaignSetBy, "AS");
			map.put("campaignCode", campaignCode);
			// 是否匹配默认规则
			map.put("strategy", 1);
			// 活动状态
			map.put("saveStatus", "1");
			// 积分规则类型： 组合规则
			map.put("pointRuleType", "3");
			// 工作流ID
			map.put("workFlowId", -1);
			// 动作ID
			map.put("actionId", -1);
			// 插入会员活动表并返回会员活动ID
			int campId = binoljnman06_Service.insertConfCampaign(map);
			Map<String, Object> addSubMap = new HashMap<String, Object>();
			addSubMap.putAll(baseMap);
			// 子活动代号
			String subCampaignCode = binOLCM03_BL.getTicketNumber(
					organizationInfoId, brandInfoId, String.valueOf(campaignSetBy), "AT");
			addSubMap.put("subCampaignCode", subCampaignCode);
			addSubMap.put("campaignId", campId);
			// 插入会员子活动表并返回会员子活动ID
			int subCampId = binoljnman06_Service.insertConfCampaignRule(addSubMap);
			// 子活动ID
			addSubMap.put("campaignRuleId", subCampId);
			// 规则描述
			addSubMap.put("ruleContent", map.get("campaignName"));
			// 插入规则表 并返回规则内容ID
			int ruledptId = binoljnman06_Service.insertConfRuleContent(addSubMap);
			Map<String, Object> combRuleParams = new HashMap<String, Object>();
			combRuleParams.put("CAMPAIGN_ID", campId);
			combRuleParams.put("SUBCAMPID", subCampId);
			combRuleParams.put("SUBCAMPCODE", subCampaignCode);
			combRuleParams.put("RULEDPT_ID", ruledptId);
			map.put("combRuleParams", combRuleParams);
			// 取得组合规则积分计算模板
			String combRuleFile = getCombRuleFile(map);
			if (CherryChecker.isNullOrEmpty(combRuleFile)) {
				throw new CherryException("ECP00006");
			}
			map.put("campaignId", campId);
			map.put("campUpdateTime", sysDate);
			map.put("campModifyCount", 0);
			map.put("ruleFileContent", combRuleFile);
			// 更新规则文件
			int result = binoljnman06_Service.updateConfRuleFile(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
		} else {
			// 更新会员活动表
			int result = binoljnman06_Service.updateConfCampaign(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
			map.put("ruleContent", map.get("campaignName"));
			// 更新规则内容
			result = binoljnman06_Service.updateConfRuleContent(map);
			// 更新失败
			if (0 == result) {
				throw new CherryException("ECM00038");
			}
			int campGrpId = -1;
			// 取得规则配置列表
			List<Map<String, Object>> ruleConfList = binoljnman06_Service.getRuleConfList(map);
			if (null != ruleConfList && !ruleConfList.isEmpty()) {
				List<String> combRules = new ArrayList<String>();
				// 组内规则
				List<Map<String, Object>> geneRuleList = (List<Map<String, Object>>) map.get("geneRules");
				if (null != geneRuleList) {
					for (Map<String, Object> geneRule : geneRuleList) {
						combRules.add(String.valueOf(geneRule.get("campaignId")));
					}
				}
				// 匹配方式
				String matchType = (String) map.get("matchType");
				// 执行策略
				String combType = (String) map.get("combType");
				for (Map<String, Object> ruleConfMap : ruleConfList) {
					// 优先级信息
					String priorityInfo = (String) ruleConfMap.get("priorityRuleDetail");
					if (!CherryChecker.isNullOrEmpty(priorityInfo)) {
						List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priorityInfo);
						if (null != priorityList) {
							boolean upFlag = false;
							for (Map<String, Object> priority : priorityList) {
								// 规则ID
								String campId = String.valueOf(priority.get("campaignId"));
								// 该组合规则在优先级列表中
								if (campaignId.equals(campId)) {
									// 组内包含的规则
									priority.put("combRules", combRules);
									// 匹配方式
									priority.put("matchType", matchType);
									// 执行策略
									priority.put("combType", combType);
									upFlag = true;
									break;
								}
							}
							if (upFlag) {
								// 启用中的配置
								if ("1".equals(ruleConfMap.get("validFlag"))) {
									campGrpId = Integer.parseInt(ruleConfMap.get("campaignGrpId").toString());
								}
								// 更新后的优先级内容
								ruleConfMap.put("priorityRuleDetail", JSONUtil.serialize(priorityList));
								ruleConfMap.putAll(baseMap);
								// 更新配置表的优先级信息
								result = binoljnman06_Service.updateConfPriority(ruleConfMap);
								// 更新失败
								if (0 == result) {
									throw new CherryException("ECM00038");
								}
							}
						}
					}
				}
			}
			if (-1 != campGrpId) {
				// 刷新启用中的配置
				knowledgeEngine.refreshGrpRule(campGrpId);
			}
		}
	}
	
	/**
	 * 保存规则匹配顺序
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	@Override
	public void tran_savePriority(Map<String, Object> map) throws Exception {
		// 插表时的共通字段
		Map<String, Object> baseMap = getBaseMap();
		Map<String, Object> priorityMap = new HashMap<String, Object>();
		// 规则ID
		String campaignId = (String) map.get("campaignId");
		priorityMap.put("campaignId", campaignId);
		List<String> keys = new ArrayList<String>();
		keys.add(campaignId);
		priorityMap.put("keys", keys);
		// 执行策略
		if (null != map.get("execType")) {
			priorityMap.put("execType", String.valueOf(map.get("execType")));
		}
		priorityMap.put("afterMatch", "1");
		// 配置内容
		String grpRuleDetail = (String) map.get("grpRuleDetail");
		if (!CherryChecker.isNullOrEmpty(grpRuleDetail, true)) {
			Map<String, Object> detailInfo = (Map<String, Object>) JSONUtil.deserialize(grpRuleDetail);
			if (null != detailInfo && !detailInfo.isEmpty()) {
				// 积分上限
	    		Map<String, Object> pointLimit = (Map<String, Object>) detailInfo.get("limitInfo");
	    		if (null != pointLimit && !pointLimit.isEmpty()) {
	    			priorityMap.put("pointLimit", pointLimit);
	    		}
			}
		}
		// 规则内容
		String priority = (String) map.get("priority");
		if (CherryChecker.isNullOrEmpty(priority, true)) {
			List<Map<String, Object>> priorityList = new ArrayList<Map<String, Object>>();
			priorityList.add(priorityMap);
			priority = JSONUtil.serialize(priorityList);
		} else {
			List<Map<String, Object>> priorityList = (List<Map<String, Object>>) JSONUtil.deserialize(priority);
			if(null == priorityList){
				priorityList = new ArrayList<Map<String, Object>>();
			}
			for (int i = 0; i < priorityList.size(); i++) {
				Map<String, Object> priorityInfo = priorityList.get(i);
				if (campaignId.equals(priorityInfo.get("campaignId"))) {
					priorityList.remove(i);
					break;
				}
			}
			// 匹配顺序
			String prioritySel = (String) map.get("prioritySel");
			// 最先匹配
			if ("0".equals(prioritySel)) {
				// 插入list的第一个
				priorityList.add(0, priorityMap);
			} else {
				// 优先级最低，放在最后
				priorityList.add(priorityMap);
				Map<String,Object> defaultMap = null;
				if (priorityList.size() > 1) {
					for (int i = priorityList.size() - 1; i >=0; i--) {
						Map<String, Object> priorityInfo = priorityList.get(i);
						if ("1".equals(priorityInfo.get("defaultFlag"))) {
							defaultMap = priorityInfo;
							priorityList.remove(i);
							break;
						}
					}
				}
				// 将默认规则放置最后
				if (null != defaultMap) {
					priorityList.add(defaultMap);
				}
			}
			priority = JSONUtil.serialize(priorityList);
		}
		// 新的优先级顺序
		map.put("priorityRuleDetail", priority);
		map.putAll(baseMap);
		// 更新配置表的优先级信息
		int result = binoljnman06_Service.updateConfPriority(map);
		// 更新失败
		if (0 == result) {
			throw new CherryException("ECM00038");
		}
		if ("1".equals(map.get("groupValidFlag"))) {
			// 更新规则的配置ID
			binoljnman06_Service.updateCampaignGrpId(map);
			// 刷新该组规则
			knowledgeEngine.refreshGrpRule(Integer.parseInt(map.get("campaignGrpId").toString()));
		}
	}
	
	/**
	 * 取得基础信息
	 * 
	 * @return Map
	 * 			基础信息
	 * @throws Exception 
	 */
	private Map<String, Object> getBaseMap(){
		// 插表时的共通字段
		Map<String, Object> baseMap = new HashMap<String, Object>();
		// 系统时间
		String sysDate = binoljnman06_Service.getSYSDate();
		// 作成日时
		baseMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 更新日时
		baseMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 作成程序名
		baseMap.put(CherryConstants.CREATEPGM, "BINOLJNMAN06");
		// 更新程序名
		baseMap.put(CherryConstants.UPDATEPGM, "BINOLJNMAN06");
		// 作成者
		baseMap.put(CherryConstants.CREATEDBY, "BINOLJNMAN06");
		// 更新者
		baseMap.put(CherryConstants.UPDATEDBY, "BINOLJNMAN06");
		return baseMap;
	}

	/**
	 * 取得组合规则内容
	 * 
	 * @param combNo
	 * 			组合规则编号
	 * @param combRuleList
	 * 			组合规则列表
	 * @return Map
	 * 			组合规则内容
	 * @throws Exception 
	 */
	private Map<String, Object> getCombRuleInfo(String combNo, List<Map<String, Object>> combRuleList) throws Exception {
		if (null != combRuleList) {
			for (Map<String, Object> combRule : combRuleList) {
				if (combNo.equals(combRule.get("COMBNO"))) {
					// 会员活动ID
					return	combRule;
				}
			}
		}
		return null;	
	}
	
	/**
	 * 取得规则配置积分计算模板
	 * 
	 * @param map
	 * @return String
	 * 			积分计算模板
	 * @throws Exception 
	 */
	private String getConfRuleFile(Map<String, Object> map) throws Exception {
		// 模板查询条件集合
		Map<String, Object> tempSearch = new HashMap<String, Object>();
		// 所属品牌
		tempSearch.put("brandInfoId", map.get("brandInfoId"));
		// 所属组织
		tempSearch.put("organizationInfoId", map.get("organizationInfoId"));
		// 会员活动类型
		String campaignType = (String) map.get("campaignType");
		tempSearch.put("campaignType", campaignType);
		// 模板区分:文件头
		tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_0);
		// 模板类型:活动组
		tempSearch.put("templateType", CherryConstants.TEMPLATE_TYPE_1);
		// 取得文件头的规则模板
		String headerTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
		if (CherryChecker.isNullOrEmpty(headerTemplate)) {
			throw new CherryException("ECP00005");
		}
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 包名称
		String packageName = "PKG_GRP_" + String.valueOf(map.get("campaignGrpId"));
		headerMap.put("PACKAGE_NAME", packageName);
		String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
		// 规则主体
		StringBuffer buffer = new StringBuffer();
		buffer.append(headerRule).append("\n\n");
		// 集合标识符
		tempSearch.put("groupCode", CampUtil.GROUPCODE_DEFAULTRULE);
		// 模板区分:文件主体
		tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_1);
		// 取得规则模板内容
		String ruleTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
		if (CherryChecker.isNullOrEmpty(ruleTemplate)) {
			throw new CherryException("ECP00005");
		}
		// 扩展参数集合
		Map<String, Object> extParams = new HashMap<String, Object>();
		extParams.put("CAMPAIGN_TYPE", campaignType);
		// 生成规则
		String rule = RuleToolUtil.getRule(extParams, ruleTemplate);
		if (CherryChecker.isNullOrEmpty(rule)) {
			throw new CherryException("ECP00006");
		}
		buffer.append(rule).append("\n\n");
		return buffer.toString();
	}
	
	/**
	 * 取得组合规则积分计算模板
	 * 
	 * @param map
	 * @return String
	 * 			积分计算模板
	 * @throws Exception 
	 */
	private String getCombRuleFile(Map<String, Object> map) throws Exception {
		Map<String, Object> combRuleParams = (Map<String, Object>) map.get("combRuleParams");
		if (null == combRuleParams) {
			return null;
		}
		// 规则主体
		StringBuffer buffer = new StringBuffer();
		// 模板查询条件集合
		Map<String, Object> tempSearch = new HashMap<String, Object>();
		// 所属品牌
		tempSearch.put("brandInfoId", map.get("brandInfoId"));
		// 所属组织
		tempSearch.put("organizationInfoId", map.get("organizationInfoId"));
		// 会员活动类型
		tempSearch.put("campaignType", map.get("campaignType"));
		// 模板区分:文件头
		tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_0);
		// 模板类型:会员活动
		tempSearch.put("templateType", CherryConstants.TEMPLATE_TYPE_0);
		// 取得文件头的规则模板
		String headerTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
		if (CherryChecker.isNullOrEmpty(headerTemplate)) {
			throw new CherryException("ECP00005");
		}
		Map<String, Object> headerMap = new HashMap<String, Object>();
		String campRuleId = String.valueOf(combRuleParams.get("CAMPAIGN_ID"));
		// 包名称
		String packageName = "PKG_" + campRuleId;
		headerMap.put("PACKAGE_NAME", packageName);
		String headerRule = RuleToolUtil.getRule(headerMap, headerTemplate);
		if (CherryChecker.isNullOrEmpty(headerRule)) {
			throw new CherryException("ECP00006");
		}
		buffer.append(headerRule).append("\n\n");
		// 集合标识符
		tempSearch.put("groupCode", CampUtil.GROUPCODE_COMBRULE);
		// 模板区分:文件主体
		tempSearch.put("templateKbn", CherryConstants.TEMPLATEKBN_1);
		// 取得规则模板内容
		String ruleTemplate = binolcpcom02_Service.getRuleTempContent(tempSearch);
		if (CherryChecker.isNullOrEmpty(ruleTemplate)) {
			throw new CherryException("ECP00005");
		}
		String ruleName = "Camp_" + String.valueOf(combRuleParams.get("SUBCAMPID"));
		combRuleParams.put("RULE_NAME", ruleName);
		// 生成规则
		String rule = RuleToolUtil.getRule(combRuleParams, ruleTemplate);
		if (CherryChecker.isNullOrEmpty(rule)) {
			throw new CherryException("ECP00006");
		}
		buffer.append(rule).append("\n\n");
		return buffer.toString();
	}
	

	/**
	 * 取得规则配置列表
	 * 
	 * @param map
	 * @return List
	 * 			规则配置列表
	 */
	public List<Map<String, Object>> getRuleConfList (Map<String, Object> map) {
		return binoljnman06_Service.getRuleConfList(map);
	}
}
