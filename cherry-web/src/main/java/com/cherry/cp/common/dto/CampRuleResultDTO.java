/*	
 * @(#)CamRuleCondiDTO.java     1.0 2012/03/30	
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

import com.cherry.cm.cmbussiness.dto.BaseDTO;

/**
 * 会员活动规则结果明细DTO
 * 
 * @author hub
 * @version 1.0 2012.03.30
 */
public class CampRuleResultDTO extends BaseDTO{
	
	/** 会员子活动ID */
	private int campaignRuleId;
	
	/** 产品厂商ID */
	private int productVendorId;
	
	/** 条码 */
	private String barCode;
	
	/** 厂商编码 */
	private String unitCode;
	
	/** 销售类型 */
	private String saleType;
	
	/** 价格 */
	private float price;
	
	/** 数量 */
	private int quantity;
	
	/** 奖励方式 */
	private String rewardType;
	
	private String groupType;
	
	private String logicOpt;
	
	private int groupNo;
	
	private String deliveryType;

	public int getCampaignRuleId() {
		return campaignRuleId;
	}

	public void setCampaignRuleId(int campaignRuleId) {
		this.campaignRuleId = campaignRuleId;
	}

	public int getProductVendorId() {
		return productVendorId;
	}

	public void setProductVendorId(int productVendorId) {
		this.productVendorId = productVendorId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public String getLogicOpt() {
		return logicOpt;
	}

	public void setLogicOpt(String logicOpt) {
		this.logicOpt = logicOpt;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
}
