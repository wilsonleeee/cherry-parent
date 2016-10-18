/**		
 * @(#)RivalSaleMainDataDTO.java     1.0 2011/07/06		
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
 * 竞争对手日销售主数据行映射DTO
 * 
 * @author zhhuyi
 * 
 */
public class RivalSaleMainDataDTO extends MainDataDTO {
	/**子类型*/
	private String SubType;
	
	/**数据来源，pos机上传的用POS2,IPOS3上传的用POS3*/
	private String Sourse;
	
	/**柜台号*/
	private String CounterCode;

	/**商场名称*/
	private String MarketName;
	
	/**销售日期*/
	private String SaleDate;
	
	/**上传日期*/
	private String UploadDate;
	
	/**上传时间*/
	private String UploadTime;
	
	/** 单据号 */
    private String TradeNoIF;
    
	/**消息明细数据DTO*/
	private List<RivalSaleDetailDataDTO> DetailDataDTOList;
    
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

	public String getCounterCode() {
		return CounterCode;
	}

	public void setCounterCode(String counterCode) {
		CounterCode = counterCode;
	}

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String marketName) {
		MarketName = marketName;
	}

	public String getSaleDate() {
		return SaleDate;
	}

	public void setSaleDate(String saleDate) {
		SaleDate = saleDate;
	}

	public String getUploadDate() {
		return UploadDate;
	}

	public void setUploadDate(String uploadDate) {
		UploadDate = uploadDate;
	}
	
	public String getUploadTime() {
		return UploadTime;
	}

	public void setUploadTime(String uploadTime) {
		UploadTime = uploadTime;
	}
	
	public List<RivalSaleDetailDataDTO> getDetailDataDTOList() {
		return DetailDataDTOList;
	}

	public void setDetailDataDTOList(List<RivalSaleDetailDataDTO> detailDataDTOList) {
		DetailDataDTOList = detailDataDTOList;
	}

	public String getTradeNoIF() {
		return TradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		TradeNoIF = tradeNoIF;
	}
}
