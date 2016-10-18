/**		
 * @(#)DetailDataDTO.java     1.0 2011/05/23		
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
 * 会员/BA/BAS主数据行映射DTO
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class MemMainDataDTO extends MainDataDTO{
	/** 子类型 */
	private String SubType;
	/** 数据来源，pos机上传的用POS2,IPOS3上传的用POS3 */
	private String Sourse;
	/** 单据号 */
    private String TradeNoIF;
    /** 柜台号 */
    private String TradeCounterCode;
    /** BA卡号 */
    private String TradeBAcode;
	/** 明细消息数据类型  */
	private List DetailDataDTOList;

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

	public List getDetailDataDTOList() {
		return DetailDataDTOList;
	}

	public void setDetailDataDTOList(List detailDataDTOList) {
		DetailDataDTOList = detailDataDTOList;
	}

	public String getTradeNoIF() {
		return TradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		TradeNoIF = tradeNoIF;
	}

	public String getTradeCounterCode() {
		return TradeCounterCode;
	}

	public void setTradeCounterCode(String tradeCounterCode) {
		TradeCounterCode = tradeCounterCode;
	}

	public String getTradeBAcode() {
		return TradeBAcode;
	}

	public void setTradeBAcode(String tradeBAcode) {
		TradeBAcode = tradeBAcode;
	}


	
	
	
}
