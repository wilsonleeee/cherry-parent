/*	
 * @(#)DroolsFileDTO.java     1.0 2011/05/12	
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
 * 规则文件 DTO
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class DroolsFileDTO {
	
	/** 规则文件名称 */
	private String droolsFileName;
	
	/** 规则内容 */
	private String ruleContent;

	public String getDroolsFileName() {
		return droolsFileName;
	}

	public void setDroolsFileName(String droolsFileName) {
		this.droolsFileName = droolsFileName;
	}

	public String getRuleContent() {
		return ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
}
