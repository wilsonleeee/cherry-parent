/*	
 * @(#)ConditionsDTO.java     1.0 2012/02/13		
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
package com.cherry.cp.common.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;

/**
 * 规则过滤条件 DTO
 * 
 * @author hub
 * @version 1.0 2012.02.13
 */
public class ConditionsDTO {
	
	/** 规则条件List */
	private List<RuleConditionDTO> ruleCondList;
	
	/** 规则条件 */
	private Map<String, Object> ruleConditons;
	
	/** 规则条件转换后List */
	private List<RuleConditionDTO> ruleRelatList;
	
	/** 规则过滤器 DTO */
	private RuleFilterDTO ruleFilter;
	
	public ConditionsDTO() {
		this.ruleCondList = new ArrayList<RuleConditionDTO>();
		this.ruleRelatList = new ArrayList<RuleConditionDTO>();
	}

	public void clearConditions() {
		this.ruleCondList.clear();
	}
	
	public List<RuleConditionDTO> getRuleCondList() {
		return ruleCondList;
	}

	public void setRuleCondList(List<RuleConditionDTO> ruleCondList) {
		this.ruleCondList = ruleCondList;
	}

	public RuleFilterDTO getRuleFilter() {
		if (null == ruleFilter) {
			ruleFilter = new RuleFilterDTO();
		}
		return ruleFilter;
	}

	public void setRuleFilter(RuleFilterDTO ruleFilter) {
		this.ruleFilter = ruleFilter;
	}

	public List<RuleConditionDTO> getRuleRelatList() {
		return ruleRelatList;
	}

	public void setRuleRelatList(List<RuleConditionDTO> ruleRelatList) {
		this.ruleRelatList = ruleRelatList;
	}

	public Map<String, Object> getRuleConditons() {
		return ruleConditons;
	}

	public void setRuleConditons(Map<String, Object> ruleConditons) {
		this.ruleConditons = ruleConditons;
	}
	
}
