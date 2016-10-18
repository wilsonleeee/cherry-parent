/**		
 * @(#)DetailDataDTO.java     1.0 2011/12/15		
 * 		
 * Copyright (c) 2011 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
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
 * 会员回访明细数据行映射DTO
 * 
 * @author zhanghuyi
 * 
 */
public class MemVisitDetailDataDTO {

	//柜台号
	private String countercode;
	//会员号
	private String memberCode;
	//营业员卡号
	private String BAcode;
	//回访开始时间
	private String visitBeginDate;
	//回访结束时间
	private String visitEndDate;
	//回访时间
	private String visitDate;
	//回访结果标识
	private String visitFlag;
	//会员回访代码
	private String visitCode;
	//会员回访类型代码
	private String visitTypeCode;
	//会员回访任务ID
	private String visitTaskID;

	public String getCountercode() {
		return countercode;
	}

	public void setCountercode(String countercode) {
		this.countercode = countercode;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getVisitBeginDate() {
		return visitBeginDate;
	}

	public void setVisitBeginDate(String visitBeginDate) {
		this.visitBeginDate = visitBeginDate;
	}

	public String getVisitEndDate() {
		return visitEndDate;
	}

	public void setVisitEndDate(String visitEndDate) {
		this.visitEndDate = visitEndDate;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitFlag() {
		return visitFlag;
	}

	public void setVisitFlag(String visitFlag) {
		this.visitFlag = visitFlag;
	}

	public String getVisitCode() {
		return visitCode;
	}

	public void setVisitCode(String visitCode) {
		this.visitCode = visitCode;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public String getVisitTaskID() {
		return visitTaskID;
	}

	public void setVisitTaskID(String visitTaskID) {
		this.visitTaskID = visitTaskID;
	}
}
