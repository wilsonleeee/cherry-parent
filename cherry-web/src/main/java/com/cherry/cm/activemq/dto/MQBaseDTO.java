/*	
 * @(#)MQBaseDTO.java     1.0 2011/12/14	
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
package com.cherry.cm.activemq.dto;

/**
 * 共通MQ消息体 DTO
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public class MQBaseDTO {
	
	/** 品牌代码 */
	private String brandCode;
	
	/** 单据号 */
	private String tradeNoIF;
	
	/** 修改回数 */
	private String modifyCounts;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 子类型 */
	private String subType;

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getModifyCounts() {
		return modifyCounts;
	}

	public void setModifyCounts(String modifyCounts) {
		this.modifyCounts = modifyCounts;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public String convertNullVal(String val) {
		return null == val? "" : val;
	}
}
