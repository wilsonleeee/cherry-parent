/*		
 * @(#)DetailDataDTO.java     1.0 2010/12/01		
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
 * mq明细数据行映射DTO
 * 
 * @author huzude
 * 
 */
public class SalDetailDataDTO {
	/** 单据号 */
	private String TradeNoIF;

	/** 修改回数 */
	private String ModifyCounts;

	/** BA卡号 */
	private String BAcode;

	/** 入出库区分； */
	private String StockType;

	/** 商品条码 */
	private String Barcode;

	/** 厂商编码 */
	private String Unitcode;

	/** 仓库类型 */
	private String InventoryTypeCode;

	/** 数量 */
	private String Quantity;

	/** 入出库前该商品柜台账面数量（针对盘点） */
	private String QuantityBefore;

	/** 价格 */
	private String Price;
	
	/** 参考价格 */
	private String ReferencePrice;

	/** 单个商品出入库理由 */
	private String Reason;

	// MQ接口对应 1.10版 Start //

	/** 单品折扣 */
	private String Discount;

	// MQ接口对应 1.10版 End //

	// MQ接口对应 1.20版 Start //

	/** 建议订货数量：目前只有订货业务有用 */
	private String SuggestedQuantity;

	// MQ接口对应 1.20版 End //
	
	/**礼品领用 礼品领用时的Coupon号 */
	private String CouponCode;
	
	/**礼品领用 是否管理库存*/
	private String IsStock;
	
	/**库存业务 明细类型*/
	private String DetailType;
	
	/**库存业务 产品厂商ID*/
	private String ProductId;

	public String getTradeNoIF() {
		return TradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		TradeNoIF = tradeNoIF;
	}

	public String getModifyCounts() {
		return ModifyCounts;
	}

	public void setModifyCounts(String modifyCounts) {
		ModifyCounts = modifyCounts;
	}

	public String getBAcode() {
		return BAcode;
	}

	public void setBAcode(String bAcode) {
		BAcode = bAcode;
	}

	public String getStockType() {
		return StockType;
	}

	public void setStockType(String stockType) {
		StockType = stockType;
	}

	public String getBarcode() {
		return Barcode;
	}

	public void setBarcode(String barcode) {
		Barcode = barcode;
	}

	public String getUnitcode() {
		return Unitcode;
	}

	public void setUnitcode(String unitcode) {
		Unitcode = unitcode;
	}

	public String getInventoryTypeCode() {
		return InventoryTypeCode;
	}

	public void setInventoryTypeCode(String inventoryTypeCode) {
		InventoryTypeCode = inventoryTypeCode;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getQuantityBefore() {
		return QuantityBefore;
	}

	public void setQuantityBefore(String quantityBefore) {
		QuantityBefore = quantityBefore;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getIsStock() {
        return IsStock;
    }

    public void setIsStock(String isStock) {
        IsStock = isStock;
    }

	public String getReferencePrice() {
		return ReferencePrice;
	}

	public void setReferencePrice(String referencePrice) {
		ReferencePrice = referencePrice;
	}

	public String getDetailType() {
		return DetailType;
	}

	public void setDetailType(String detailType) {
		DetailType = detailType;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getSuggestedQuantity() {
		return SuggestedQuantity;
	}

	public void setSuggestedQuantity(String suggestedQuantity) {
		SuggestedQuantity = suggestedQuantity;
	}

}
