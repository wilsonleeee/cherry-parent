/*		
 * @(#)MemInitDataDTO.java     1.0 2011/08/24		
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
 * 会员初始数据采集信息明细数据行映射DTO
 * 
 * @author WangCT
 * 
 */
public class MemInitDataDTO {
	
	/** 柜台号 */
	private String countercode;
	
	/** Ba卡号 */
	private String bacode;
	
	/** 会员唯一号 */
	private String memberID;
	
	/** 会员卡号 */
	private String memberCode;
	
	/** 会员当前等级代码 */
	private String member_level;
	
	/** 当前拥有的化妆次数 */
	private String curBtimes;
	
	/** 会员当前有效积分 */
	private String curPoints;
	
	/** 会员累计销售金额（仅体验会员采集） */
	private String totalAmounts;
	
	/** 会员初始入会时间 */
	private String joinDate;
	
	/** 采集时间 */
	private String acquiTime;

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

	public String getMember_level() {
		return member_level;
	}

	public void setMember_level(String memberLevel) {
		member_level = memberLevel;
	}

	public String getCurBtimes() {
		return curBtimes;
	}

	public void setCurBtimes(String curBtimes) {
		this.curBtimes = curBtimes;
	}

	public String getCurPoints() {
		return curPoints;
	}

	public void setCurPoints(String curPoints) {
		this.curPoints = curPoints;
	}

	public String getTotalAmounts() {
		return totalAmounts;
	}

	public void setTotalAmounts(String totalAmounts) {
		this.totalAmounts = totalAmounts;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getAcquiTime() {
		return acquiTime;
	}

	public void setAcquiTime(String acquiTime) {
		this.acquiTime = acquiTime;
	}
	
}
