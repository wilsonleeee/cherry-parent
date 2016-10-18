/*	
 * @(#)TemplateInit.java     1.0 2011/7/18		
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
package com.cherry.cp.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.dto.CampBasePropDTO;
import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.cp.common.dto.CampRuleResultDTO;
import com.cherry.cp.common.dto.CampaignRuleDTO;
import com.cherry.cp.common.interfaces.TemplateInit_IF;
import com.cherry.cp.common.service.BINOLCPCOM02_Service;

/**
 * 会员活动基础模板初始化处理
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class TemplateInit implements TemplateInit_IF{
	
	protected static final Logger logger = LoggerFactory.getLogger(TemplateInit.class);
	@Resource
	protected BINOLCPCOM02_Service binolcpcom02_Service;
	
	@Resource
	protected BINOLCM31_IF binOLCM31_BL;
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	@Override
	public void invokeMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		Method[] mdArr = this.getClass().getMethods();
		for (Method method : mdArr) {
			if (method.getName().equals(mdName)) {
				method.invoke(this, paramMap, tempMap);
				break;
			}
		}
	}

	/**
	 * 运行保存方法
	 * 
	 * @param mdName
	 *            指定的方法名
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param campaignRule
	 *            会员子活动DTO
	 * @throws Exception 
	 * 
	 */
	@Override
	public void invokeSaveMd(String mdName, Map<String, Object> paramMap,
			Map<String, Object> tempMap, CampaignRuleDTO campaignRule)
			throws Exception {
		Method[] mdArr = this.getClass().getMethods();
		for (Method method : mdArr) {
			if (method.getName().equals(mdName)) {
				method.invoke(this, paramMap, tempMap, campaignRule);
				break;
			}
		}
	}
	
	/**
	 * 保存活动条件
	 * 
	 * @param ruleConditonList
	 *            会员活动规则条件明细
	 * @param paramMap
	 *            参数集合
	 * @param campaignRule
	 *            会员子活动DTO
	 * @throws Exception 
	 * 
	 */
	public void saveRuleConditons(List<CampRuleConditionDTO> ruleConditonList, Map<String, Object> paramMap, CampaignRuleDTO campaignRule) throws Exception{
		// 会员活动基础属性 List
		List<CampBasePropDTO> campBasePropList = (List<CampBasePropDTO>) paramMap.get("campBasePropList");
		if (null != ruleConditonList) {
			BaseDTO baseDto = (BaseDTO) paramMap.get("BaseDTO");
			for (CampRuleConditionDTO ruleCondition : ruleConditonList) {
				// 属性条件 
				String propertyName = ruleCondition.getPropertyName(); 
				if (CherryChecker.isNullOrEmpty(propertyName)) {
					throw new CherryException("ECP00016");
				}
				CampBasePropDTO campBaseProp = null;
				if (null != campBasePropList) {
					for (CampBasePropDTO campBasePropDTO : campBasePropList) {
						if (propertyName.equals(campBasePropDTO.getPropertyName())) {
							campBaseProp = campBasePropDTO;
							break;
						}
					}
				}
				if (null == campBaseProp) {
					campBaseProp = new CampBasePropDTO();
					campBaseProp.setPropertyName(propertyName);
					// 条件名称
					campBaseProp.setCondition(CampUtil.BASEPROPS.getCondition(propertyName));
					// 属性字段类型
					campBaseProp.setFieldType(CampUtil.BASEPROPS.getFieldType(propertyName));
					ConvertUtil.convertDTO(campBaseProp, baseDto, false);
					// 插入会员活动基础属性表
					binolcpcom02_Service.insertCampaignBaseProp(campBaseProp);
					if (null == campBasePropList) {
						campBasePropList = new ArrayList<CampBasePropDTO>();
					}
					campBasePropList.add(campBaseProp);
				}
				// 基础属性ID
				ruleCondition.setCampBasePropId(campBaseProp.getCampBasePropId());
			}
		}
		if (null == campaignRule) {
			// 会员活动规则条件明细 List
			List<CampRuleConditionDTO> campRuleCondList = (List<CampRuleConditionDTO>) paramMap.get("campRuleCondList");
			campRuleCondList.addAll(ruleConditonList);
		} else {
			campaignRule.getCampRuleCondList().addAll(ruleConditonList);
		}
	}
	
	/**
	 * 保存会员活动规则结果
	 * 
	 * @param ruleConditonList
	 *            会员活动规则结果明细
	 * @param paramMap
	 *            参数集合
	 * @param campaignRule
	 *            会员子活动DTO
	 * @throws Exception 
	 * 
	 */
	public void saveRuleResults(List<CampRuleResultDTO> ruleResultList, Map<String, Object> paramMap, CampaignRuleDTO campaignRule) throws Exception{
		if (null == campaignRule) {
			// 会员活动规则条件明细 List
			List<CampRuleResultDTO> campRuleResultList = (List<CampRuleResultDTO>) paramMap.get("campRuleResultList");
			campRuleResultList.addAll(ruleResultList);
		} else {
			campaignRule.getCampRuleResultList().addAll(ruleResultList);
		}
	}
}
