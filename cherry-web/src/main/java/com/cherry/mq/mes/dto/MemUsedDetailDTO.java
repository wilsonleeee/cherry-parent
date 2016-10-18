/*		
 * @(#)MemUsedDetailDTO.java     1.0 2011/08/24		
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
package com.cherry.mq.mes.dto;

/**
 * 会员使用化妆次数、积分明细数据行映射DTO
 * 
 * @author WangCT
 * 
 */
public class MemUsedDetailDTO {
	
	/** 柜台号 */
	private String countercode;
	
	/** Ba卡号 */
	private String bacode;
	
	/** 会员唯一号 */
	private String memberID;
	
	/** 会员卡号 */
	private String memberCode;
	
	/** 使用次数 */
	private String usedTimes;
	
	/** 业务时间 */
	private String businessTime;

	public String getCountercode() {
		return countercode;
	}

	public void setCountercode(String countercode) {
		this.countercode = countercode;
	}

	public String getBacode() {
		return bacode;
	}

	public void setBacode(String bacode) {
		this.bacode = bacode;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getUsedTimes() {
		return usedTimes;
	}

	public void setUsedTimes(String usedTimes) {
		this.usedTimes = usedTimes;
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}

}
