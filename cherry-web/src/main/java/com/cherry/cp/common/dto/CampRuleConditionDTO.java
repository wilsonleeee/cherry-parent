/*	
 * @(#)CamRuleCondiDTO.java     1.0 2011/04/26	
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
 * 会员活动规则条件明细DTO
 * 
 * @author hub
 * @version 1.0 2010.04.26
 */
public class CampRuleConditionDTO extends BaseDTO{
	
	/** 会员子活动ID */
	private int campaignRuleId;
	
	/** 基础属性ID */
	private int campBasePropId;
	
	/** 属性条件 */
	private String propertyName;
	
	/** 属性值From */
	private String basePropValue1;
	
	/** 属性值To */
	private String basePropValue2;
	
	/** 基础属性值区分 */
	private String propFlag;
	
	/** 条件分组标记 */
	private int conditionGrpId;
	
	/** 活动地点选择区分 */
	private String actLocationType;
	
	public CampRuleConditionDTO() {
		this.conditionGrpId = 1;
	}
	public int getCampaignRuleId() {
		return campaignRuleId;
	}

	public void setCampaignRuleId(int campaignRuleId) {
		this.campaignRuleId = campaignRuleId;
	}
	
	public int getCampBasePropId() {
		return campBasePropId;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setCampBasePropId(int campBasePropId) {
		this.campBasePropId = campBasePropId;
	}

	public String getBasePropValue1() {
		return basePropValue1;
	}

	public void setBasePropValue1(String basePropValue1) {
		this.basePropValue1 = basePropValue1;
	}

	public String getBasePropValue2() {
		return basePropValue2;
	}

	public void setBasePropValue2(String basePropValue2) {
		this.basePropValue2 = basePropValue2;
	}
	
	public int getConditionGrpId() {
		return conditionGrpId;
	}
	public void setConditionGrpId(int conditionGrpId) {
		this.conditionGrpId = conditionGrpId;
	}
	public String getPropFlag() {
		return propFlag;
	}

	public void setPropFlag(String propFlag) {
		this.propFlag = propFlag;
	}

	public String getActLocationType() {
		return actLocationType;
	}

	public void setActLocationType(String actLocationType) {
		this.actLocationType = actLocationType;
	}
}
