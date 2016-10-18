/*	
 * @(#)RuleFileDTO.java     1.0 2011/11/01		
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

import com.cherry.cm.cmbussiness.dto.BaseDTO;

/**
 * 规则文件 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class RuleFileDTO extends BaseDTO{
	
	/** 包名 */
	private Integer ruleFileBodyId;
	
	/** 主体类型 */
	private String bodyType;
	
	/** 主体关联ID */
	private Integer bodyRelationId;
	
	/** 主体模板ID */
	private Integer bodyTemplateId;
	
	/** 规则体详细 */
	private String ruleDetail;
	
	/** 规则开始日期 */
	private String ruleFromDate;
	
	/** 规则结束日期 */
	private String ruleToDate;
	
	/** 规则优先级 */
	private int ruleSalience;

	public Integer getRuleFileBodyId() {
		return ruleFileBodyId;
	}

	public void setRuleFileBodyId(Integer ruleFileBodyId) {
		this.ruleFileBodyId = ruleFileBodyId;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}

	public Integer getBodyRelationId() {
		return bodyRelationId;
	}

	public void setBodyRelationId(Integer bodyRelationId) {
		this.bodyRelationId = bodyRelationId;
	}

	public Integer getBodyTemplateId() {
		return bodyTemplateId;
	}

	public void setBodyTemplateId(Integer bodyTemplateId) {
		this.bodyTemplateId = bodyTemplateId;
	}

	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
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

	public int getRuleSalience() {
		return ruleSalience;
	}

	public void setRuleSalience(int ruleSalience) {
		this.ruleSalience = ruleSalience;
	}
}
