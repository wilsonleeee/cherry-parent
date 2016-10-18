/*	
 * @(#)BINOLCPCOM04_Form.java     1.0 2011/7/18		
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
package com.cherry.cp.common.form;

import com.cherry.cp.common.dto.CampaignRuleDTO;

/**
 * 规则测试共通 Form
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM04_Form {
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 会员活动类型 */
	private String campaignType;
	
	/** 会员子活动 */
	private CampaignRuleDTO campaignRule;
	
	/** 模板名称 */
	private String templateName;
	
	/** 令牌 */
	private String csrftoken;
	
	/** 加载区分 */
	private String loadKbn;
	
	/** 会员活动组ID */
	private String campaignGrpId;
	
	/** 活动信息 */
	private String camTemps;
	
	/** 积分类型 */
	private String templateType;
	
	/** 规则详细 */
	private String ruleDetail;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}
	
	public CampaignRuleDTO getCampaignRule() {
		return campaignRule;
	}

	public void setCampaignRule(CampaignRuleDTO campaignRule) {
		this.campaignRule = campaignRule;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	public String getLoadKbn() {
		return loadKbn;
	}

	public void setLoadKbn(String loadKbn) {
		this.loadKbn = loadKbn;
	}

	public String getCampaignGrpId() {
		return campaignGrpId;
	}

	public void setCampaignGrpId(String campaignGrpId) {
		this.campaignGrpId = campaignGrpId;
	}

	public String getCamTemps() {
		return camTemps;
	}

	public void setCamTemps(String camTemps) {
		this.camTemps = camTemps;
	}

	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
}
