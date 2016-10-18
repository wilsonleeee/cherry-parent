/**		
 * @(#)QueMainDataDTO.java     1.0 2011/06/07		
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

import java.util.List;

/**
 * 问卷主数据行映射DTO
 * 
 * @author huzude
 * 
 */
public class QueMainDataDTO extends MainDataDTO {
	// 类型，1 表示会员问卷，2表示商场问卷，0表示普通问卷
	private String SubType;
	// 数据来源，pos机上传的用POS2,IPOS3上传的用POS3
	private String Sourse;
	// 会员号
	private String MemberCode;
	// 会员的paperid/问卷ID
	private String PaperID;
	// 柜台号
	private String CounterCode;
	// Ba卡号
	private String BAcode;
	// U盘序列号
	private String UDiskSN;
	// CS姓名
	private String CSName;
	// BA姓名
	private String BAName;
	// 商场名称
	private String MarketName;
	// 辅导重点及建议
	private String Advice;
	// 是否需要改进（1为改进，0为不需）
	private String OrderImprove;
	// 最终改进期限（YYYYMMDD）
	private String OrderImproveLastDate;
	// 考核日期（YYYYMMDD）
	private String CheckDate;
	// 考核时间（HHMMSS）
	private String CheckTime;
	// 单据号 
    private String TradeNoIF;
	// 消息明细数据DTO
	private List<QueDetailDataDTO> DetailDataDTOList;
    
	public String getSubType() {
		return SubType;
	}

	public void setSubType(String subType) {
		SubType = subType;
	}

	public String getSourse() {
		return Sourse;
	}

	public void setSourse(String sourse) {
		Sourse = sourse;
	}

	public String getMemberCode() {
		return MemberCode;
	}

	public void setMemberCode(String memberCode) {
		MemberCode = memberCode;
	}

	public String getPaperID() {
		return PaperID;
	}

	public void setPaperID(String paperID) {
		PaperID = paperID;
	}

	public String getCounterCode() {
		return CounterCode;
	}

	public void setCounterCode(String counterCode) {
		CounterCode = counterCode;
	}

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getUDiskSN() {
		return UDiskSN;
	}

	public void setUDiskSN(String uDiskSN) {
		UDiskSN = uDiskSN;
	}

	public String getCSName() {
		return CSName;
	}

	public void setCSName(String cSName) {
		CSName = cSName;
	}

	public String getBAName() {
		return BAName;
	}

	public void setBAName(String bAName) {
		BAName = bAName;
	}

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String marketName) {
		MarketName = marketName;
	}

	public String getAdvice() {
		return Advice;
	}

	public void setAdvice(String advice) {
		Advice = advice;
	}

	public String getOrderImprove() {
		return OrderImprove;
	}

	public void setOrderImprove(String orderImprove) {
		OrderImprove = orderImprove;
	}

	public String getOrderImproveLastDate() {
		return OrderImproveLastDate;
	}

	public void setOrderImproveLastDate(String orderImproveLastDate) {
		OrderImproveLastDate = orderImproveLastDate;
	}

	public String getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

	public String getCheckTime() {
		return CheckTime;
	}

	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}

	public List<QueDetailDataDTO> getDetailDataDTOList() {
		return DetailDataDTOList;
	}

	public void setDetailDataDTOList(List<QueDetailDataDTO> detailDataDTOList) {
		DetailDataDTOList = detailDataDTOList;
	}

	public String getTradeNoIF() {
		return TradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		TradeNoIF = tradeNoIF;
	}
	
}
