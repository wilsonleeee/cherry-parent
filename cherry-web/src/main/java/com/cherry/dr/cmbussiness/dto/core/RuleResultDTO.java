/*	
 * @(#)RuleResultDTO.java     1.0 2011/11/30	
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
package com.cherry.dr.cmbussiness.dto.core;

/**
 * 规则执行结果 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.30
 */
public class RuleResultDTO {
	
	/** 活动ID */
	private String campaignId;
	
	/** 子活动代号 */
	private String subCampCode;
	
	/** 规则描述ID */
	private String ruleDptId;
	
	/** 规则执行的结果 */
	private Object ruleDTO;
	
	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	
	public String getSubCampCode() {
		return subCampCode;
	}

	public void setSubCampCode(String subCampCode) {
		this.subCampCode = subCampCode;
	}
	
	public String getRuleDptId() {
		return ruleDptId;
	}

	public void setRuleDptId(String ruleDptId) {
		this.ruleDptId = ruleDptId;
	}

	public Object getRuleDTO() {
		return ruleDTO;
	}

	public void setRuleDTO(Object ruleDTO) {
		this.ruleDTO = ruleDTO;
	}
}
