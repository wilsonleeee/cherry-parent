/*	
 * @(#)LevelBTimesDetailDTO.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.dto.mq;

/**
 * 等级和化妆次数MQ明细业务 DTO
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class LevelBTimesDtlDTO {
	
	/** 会员卡号 */
	private String memberCode;
	
	/** 变更前等级 */
	private String memberlevelOld;
	
	/** 变更前化妆次数 */
	private String btimesOld;
	
	/** 变更后等级 */
	private String memberlevelNew;
	
	/** 变更后化妆次数 */
	private String curBtimesNew;
	
	/** 化妆次数差分 */
	private String diffBtimes;
	
	/** 业务类型 */
	private String bizType;
	
	/** 关联单据时间 */
	private String relevantTicketDate;
	
	/** 关联单号 */
	private String relevantNo;
	
	/** 变动渠道 */
	private String channel;
	
	/** 变化原因 */
	private String reason;
	
	/** 重算次数 */
	private String reCalcCount;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 员工编号 */
	private String employeeCode;

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getMemberlevelOld() {
		return memberlevelOld;
	}

	public void setMemberlevelOld(String memberlevelOld) {
		this.memberlevelOld = memberlevelOld;
	}

	public String getBtimesOld() {
		return btimesOld;
	}

	public void setBtimesOld(String btimesOld) {
		this.btimesOld = btimesOld;
	}

	public String getMemberlevelNew() {
		return memberlevelNew;
	}

	public void setMemberlevelNew(String memberlevelNew) {
		this.memberlevelNew = memberlevelNew;
	}

	public String getCurBtimesNew() {
		return curBtimesNew;
	}

	public void setCurBtimesNew(String curBtimesNew) {
		this.curBtimesNew = curBtimesNew;
	}

	public String getDiffBtimes() {
		return diffBtimes;
	}

	public void setDiffBtimes(String diffBtimes) {
		this.diffBtimes = diffBtimes;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getRelevantTicketDate() {
		return relevantTicketDate;
	}

	public void setRelevantTicketDate(String relevantTicketDate) {
		this.relevantTicketDate = relevantTicketDate;
	}

	public String getRelevantNo() {
		return relevantNo;
	}

	public void setRelevantNo(String relevantNo) {
		this.relevantNo = relevantNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReCalcCount() {
		return reCalcCount;
	}

	public void setReCalcCount(String reCalcCount) {
		this.reCalcCount = reCalcCount;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
}
