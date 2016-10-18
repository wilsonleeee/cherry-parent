/*	
 * @(#)RuleTestDTO.java     1.0 2011/11/01		
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

/**
 * 规则测试 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class RuleTestDTO {
	
	/** 规则测试结果 */
	private int resultFlag;
	
	/** 规则测试结果描述 */
	private String resultDpt;
	
	/** 规则测试结果详细 */
	private String resultDetail;

	public int getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(int resultFlag) {
		this.resultFlag = resultFlag;
	}
	
	public String getResultDpt() {
		return resultDpt;
	}

	public void setResultDpt(String resultDpt) {
		this.resultDpt = resultDpt;
	}

	public String getResultDetail() {
		return resultDetail;
	}

	public void setResultDetail(String resultDetail) {
		this.resultDetail = resultDetail;
	}
	
}
