/*	
 * @(#)RuleEngine.java     1.0 2011/08/18	
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
package com.cherry.dr.cmbussiness.rule;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatelessKnowledgeSession;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.DroolsFileDTO;
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM03_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleIF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.mq.mes.common.CherryMQException;

/**
 * 会员活动共通 Rule
 * 
 * @author hub
 * @version 1.0 2011.08.18
 */
public class RuleEngine implements RuleEngine_IF{
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	@Resource
	private BINBEDRCOM03_IF binbedrcom03IF;

	/**
	 * 会员活动batch处理
	 * 
	 * @param map
	 * 			参数集合
	 * @return CampBaseDTO
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public CampBaseDTO executeRuleBatch(CampBaseDTO campBaseDTO, CampRuleIF campRuleIF) throws Exception {
		if (null != campRuleIF) {
			// 创建规则文件
			DroolsFileDTO droolsFileDTO = campRuleIF.createDroolsFile(campBaseDTO);
			StatelessKnowledgeSession ksession = knowledgeEngine.getKsession(droolsFileDTO);
			if (null == ksession) {
				// 会员信息无记录
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00004, new String[] { droolsFileDTO.getDroolsFileName() });
				throw new CherryMQException(errMsg);
			}
			ksession.setGlobal("campRuleIF", campRuleIF);
			ksession.execute(campBaseDTO);
		} else {
			// 组织代码
			String orgCode = campBaseDTO.getOrgCode();
			// 品牌代码
			String brandCode = campBaseDTO.getBrandCode();
			// 规则类型
			String ruleType = campBaseDTO.getRuleType();
			// 取得组规则库
			Map<String, Object> groupRule = knowledgeEngine.getGroupRule(orgCode, brandCode, campBaseDTO.getGrpRuleKey());
			if (null == groupRule || groupRule.isEmpty()) {
				// 升降级
				if (DroolsConstants.CAMPAIGN_TYPE2.equals(ruleType)) {
					return campBaseDTO;
				}
				// 获取不到规则库
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00010, new String[] { ruleType });
				throw new CherryMQException(errMsg);
			}
			for(Map.Entry<String,Object> gr: groupRule.entrySet()){
				Map<String, Object> groupInfo = (Map<String, Object>) gr.getValue();
				// 规则库
				KnowledgeBase kbase = (KnowledgeBase) groupInfo.get("kbase");
				if (null == kbase || kbase.getKnowledgePackages().size() == 0) {
					// 获取不到规则库
					String errMsg = DroolsMessageUtil.getMessage(
							DroolsMessageUtil.EDR00010, new String[] { ruleType });
					throw new CherryMQException(errMsg);
				}
				// 规则条件
				List<RuleFilterDTO> allFilterList = (List<RuleFilterDTO>) groupInfo.get("allFilterList");
				// 优先级列表
				List<Map<String, Object>> priorityList = (List<Map<String, Object>>) groupInfo.get("priorityList");
				if (null != priorityList && !priorityList.isEmpty()) {
					Map<String, Object> firstMap = priorityList.get(0);
					// 第一次需要执行的规则
					List<String> ruleKeys = (List<String>) firstMap.get("keys");
					campBaseDTO.setRuleKeys(ruleKeys);
					campBaseDTO.setProIndex(0);
				}
				StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
				ksession.setGlobal("binbedrcom03IF", binbedrcom03IF);
				ksession.setGlobal("priorityList", priorityList);
				ksession.setGlobal("allFilters", allFilterList);
				ksession.execute(campBaseDTO);
				// 清空匹配记录
				campBaseDTO.clearResults();
			}
		}
		return campBaseDTO;
	}
	
	/**
	 * 会员活动batch处理(多个实体)
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * @return campRuleIF
	 * 			会员活动规则共通IF
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public List<CampBaseDTO> executeRuleMulti(List<CampBaseDTO> campBaseList,
			CampRuleIF campRuleIF) throws Exception {
		if (null == campBaseList || campBaseList.isEmpty()) {
			throw new CherryMQException("No rule fact exception!");
		}
		CampBaseDTO campBaseDTO = campBaseList.get(0);
		if (null != campRuleIF) {
			// 创建规则文件
			DroolsFileDTO droolsFileDTO = campRuleIF.createDroolsFile(campBaseDTO);
			StatelessKnowledgeSession ksession = knowledgeEngine.getKsession(droolsFileDTO);
			if (null == ksession) {
				// 会员信息无记录
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00004, new String[] { droolsFileDTO.getDroolsFileName() });
				throw new CherryMQException(errMsg);
			}
			ksession.setGlobal("campRuleIF", campRuleIF);
			ksession.execute(campBaseDTO);
		} else {
			// 组织代码
			String orgCode = campBaseDTO.getOrgCode();
			// 品牌代码
			String brandCode = campBaseDTO.getBrandCode();
			// 规则类型
			String ruleType = campBaseDTO.getRuleType();
			// 取得组规则库
			Map<String, Object> groupRule = null;
			if (DroolsConstants.CAMPAIGN_TYPE9999.equals(ruleType)) {
				groupRule = knowledgeEngine.getGroupRule(orgCode, brandCode, DroolsConstants.CAMPAIGN_TYPE2 + "_" + campBaseDTO.getMemberClubId());
			} else {
				groupRule = knowledgeEngine.getGroupRule(orgCode, brandCode, campBaseDTO.getGrpRuleKey());
			}
			if (null == groupRule || groupRule.isEmpty()) {
				if (DroolsConstants.CAMPAIGN_TYPE9999.equals(ruleType)) {
					return campBaseList;
				}
				// 获取不到规则库
				// 获取不到规则库
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00010, new String[] { ruleType });
				throw new CherryMQException(errMsg);
			}
			int i = 0;
			for(Map.Entry<String,Object> gr: groupRule.entrySet()){
				Map<String, Object> groupInfo = (Map<String, Object>) gr.getValue();
				// 规则库
				KnowledgeBase kbase = (KnowledgeBase) groupInfo.get("kbase");
				if (null == kbase || kbase.getKnowledgePackages().size() == 0) {
					continue;
				}
				if (i > 0) {
					for (CampBaseDTO campBase : campBaseList) {
						// 清空匹配记录
						campBase.clearResults();
					}
				}
				// 规则条件
				List<RuleFilterDTO> allFilterList = (List<RuleFilterDTO>) groupInfo.get("allFilterList");
				StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
				ksession.setGlobal("binbedrcom03IF", binbedrcom03IF);
				if (!DroolsConstants.CAMPAIGN_TYPE9999.equals(ruleType) &&
						!DroolsConstants.CAMPAIGN_TYPE8888.equals(ruleType)) {
					// 优先级列表
					List<Map<String, Object>> priorityList = (List<Map<String, Object>>) groupInfo.get("priorityList");
					if (null != priorityList && !priorityList.isEmpty()) {
						Map<String, Object> firstMap = priorityList.get(0);
						// 第一次需要执行的规则
						List<String> ruleKeys = (List<String>) firstMap.get("keys");
						campBaseDTO.setRuleKeys(ruleKeys);
						campBaseDTO.setProIndex(0);
					}
					ksession.setGlobal("priorityList", priorityList);
				}
				ksession.setGlobal("allFilters", allFilterList);
				ksession.execute(campBaseList);
				i++;
			}
		}
		return campBaseList;
	}
	
	/**
	 * 取得组规则库
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @return Map
	 * 			组规则库
	 * 
	 */
	@Override
	public Map<String, Object> getGroupRule(String orgCode, String brandCode, String campaignType) {
		return knowledgeEngine.getGroupRule(orgCode, brandCode, campaignType);
	}
}
