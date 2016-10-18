/*	
 * @(#)BINOLJNCOM02_Form.java     1.0 2011/4/18		
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
package com.cherry.jn.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 会员活动组一览 Form
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM02_Form extends DataTable_BaseForm {
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 活动组名称  */
	private String groupName;
	
	/** 活动类型  */
	private String campaignType;
	
	/** 有效区分  */
	private String validFlag;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
