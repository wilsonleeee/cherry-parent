/*	
 * @(#)RuleBodyDTO.java     1.0 2011/11/01		
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

import java.util.HashMap;
import java.util.Map;

import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;

/**
 * 规则内容 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class RuleBodyDTO extends RuleConditionDTO{
	
	/** 规则名称 */
	private String ruleName;
	
	/** 规则优先级 */
	private int ruleSalience;
	
	/** agenda-group名 */
	private String agendaGroupName;
	
	/** 规则开始日期 */
	private String ruleFromDate;
	
	/** 规则结束日期 */
	private String ruleToDate;
	
	/** 规则描述ID */
	private Integer ruleId;
	
	/** 扩展参数 */
	private Map<String, Object> extParams;
	
	/** 页面模板内容 */
	private Map<String, Object> pageTemp;
	
	/** 集合标识符 */
	private String groupCode;
	
	/** 共通条件标识 */
	private boolean isCommCondition;
	
	/** 是否需要优先级控制规则执行 */
	private boolean isPriority;
	
	/** 规则过滤器 DTO */
	private RuleFilterDTO ruleFilter;
	
	public RuleBodyDTO() {
		this.isPriority = true;
	}
	public boolean isCommCondition() {
		return isCommCondition;
	}

	public void setCommCondition(boolean isCommCondition) {
		this.isCommCondition = isCommCondition;
	}

	public boolean isPriority() {
		return isPriority;
	}

	public void setPriority(boolean isPriority) {
		this.isPriority = isPriority;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public int getRuleSalience() {
		return ruleSalience;
	}

	public void setRuleSalience(int ruleSalience) {
		this.ruleSalience = ruleSalience;
	}

	public String getAgendaGroupName() {
		return agendaGroupName;
	}

	public void setAgendaGroupName(String agendaGroupName) {
		this.agendaGroupName = agendaGroupName;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Map<String, Object> getExtParams() {
		if (null == this.extParams) {
			this.extParams = new HashMap<String, Object>();
		}
		return extParams;
	}

	public void setExtParams(Map<String, Object> extParams) {
		this.extParams = extParams;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getRuleFromDate() {
		return ruleFromDate;
	}
	
	public void setRuleFromDate(String ruleFromDate) {
		this.ruleFromDate = ruleFromDate;
	}

	public String getRuleToDate() {
		return ruleToDate;
	}

	public void setRuleToDate(String ruleToDate) {
		this.ruleToDate = ruleToDate;
	}

	public Map<String, Object> getPageTemp() {
		return pageTemp;
	}

	public void setPageTemp(Map<String, Object> pageTemp) {
		this.pageTemp = pageTemp;
	}
	
	public RuleFilterDTO getRuleFilterVal() {
		return ruleFilter;
	}
	
	public RuleFilterDTO getRuleFilter() {
		if (null == this.ruleFilter) {
			this.ruleFilter = new RuleFilterDTO();
		}
		return ruleFilter;
	}
	
	public void setRuleFilter(RuleFilterDTO ruleFilter) {
		this.ruleFilter = ruleFilter;
	}
}
