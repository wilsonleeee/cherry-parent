/*		
 * @(#)MainDataDTO.java     1.0 2010/12/01		
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
 * 消息主数据映射DTO(基类)
 * @author huzude
 *
 */
public class MainDataDTO {
	/** 品牌代码，即品牌简称 */
	private String BrandCode;
	
	/** 消息体 */
	private String MessageBody;
	
	/** 机器号  */
	private String MachineCode;
	
	/** 业务类型 */
	private String TradeType;

	public String getBrandCode() {
		return BrandCode;
	}

	public void setBrandCode(String brandCode) {
		BrandCode = brandCode;
	}

	public String getMessageBody() {
		return MessageBody;
	}

	public void setMessageBody(String messageBody) {
		MessageBody = messageBody;
	}

	public String getMachineCode() {
		return MachineCode;
	}

	public void setMachineCode(String machineCode) {
		MachineCode = machineCode;
	}

	public String getTradeType() {
		return TradeType;
	}

	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}
	
}
