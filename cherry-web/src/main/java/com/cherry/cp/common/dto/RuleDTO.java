/*	
 * @(#)RuleDTO.java     1.0 2011/11/01		
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
 * 规则 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class RuleDTO extends BaseDTO{
	
	/** 规则ID */
	private Integer ruleId;
	
	/** 会员子活动ID */
	private Integer campaignRuleId;
	
	/** 规则内容 */
	private String ruleContent;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getCampaignRuleId() {
		return campaignRuleId;
	}

	public void setCampaignRuleId(Integer campaignRuleId) {
		this.campaignRuleId = campaignRuleId;
	}

	public String getRuleContent() {
		return ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
}