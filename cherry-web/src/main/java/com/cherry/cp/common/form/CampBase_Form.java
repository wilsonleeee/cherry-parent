/*	
 * @(#)CampBase_Form.java     1.0 2011/7/18		
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

import com.cherry.cp.common.dto.CampaignDTO;

/**
 * 会员活动基础 Form
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class CampBase_Form {
	
	/** 活动基本信息 */
	private CampaignDTO campInfo;

	public CampaignDTO getCampInfo() {
		return campInfo;
	}

	public void setCampInfo(CampaignDTO campInfo) {
		this.campInfo = campInfo;
	}
}
