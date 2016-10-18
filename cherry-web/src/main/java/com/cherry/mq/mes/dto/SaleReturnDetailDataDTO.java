/*      
 * @(#)SaleReturnDetailDataDTO.java     1.0 2012/05/25      
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
 * 销售/退货单(老后台发送到新后台)，新销售退货的消息体的TYPE=0007
 * 
 * @author niushunjie
 * 
 */
public class SaleReturnDetailDataDTO {
    /** 单据号 */
    private String TradeNoIF;

    /** 修改回数 */
    private String ModifyCounts;

    /**明细类型  产品销售为N，促销为P，支付方式为Y*/
    private String DetailType;
    
    /**支付方式代码*/
    private String PayTypeCode;
    
    /**支付方式ID*/
    private String PayTypeID;
    
    /**支付方式名称*/
    private String PayTypeName;
    
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

    /** 单个商品出入库理由 */
    private String Reason;

    /** 单品折扣 */
    private String Discount;

    /**会员卡号*/
    private String MemberCodeDetail;
    
    /**促销活动主码*/
    private String ActivityMainCode;
    
    /**促销代码*/
    private String ActivityCode;
    
    /**礼品领用预约单号 */
    private String orderID;
    
    /**礼品领用时的Coupon号 */
    private String CouponCode;
    
    /**礼品领用是否管理库存 */
    private String IsStock;
    
    /**获知活动方式 */
    private String InformType;
    
    /**唯一码*/
    private String UniqueCode;
    
    /** 销售/退货原因 */
    private String SaleReason;
    
    /** 产品厂商ID */
    private String ProductId;
    
    /** 吊牌价 */
    private String TagPrice;

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

    public String getDetailType() {
        return DetailType;
    }

    public void setDetailType(String detailType) {
        DetailType = detailType;
    }

    public String getPayTypeCode() {
        return PayTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        PayTypeCode = payTypeCode;
    }

    public String getPayTypeID() {
        return PayTypeID;
    }

    public void setPayTypeID(String payTypeID) {
        PayTypeID = payTypeID;
    }

    public String getPayTypeName() {
        return PayTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        PayTypeName = payTypeName;
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

    public String getMemberCodeDetail() {
        return MemberCodeDetail;
    }

    public void setMemberCodeDetail(String memberCodeDetail) {
        MemberCodeDetail = memberCodeDetail;
    }

    public String getActivityMainCode() {
        return ActivityMainCode;
    }

    public void setActivityMainCode(String activityMainCode) {
        ActivityMainCode = activityMainCode;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    public String getInformType() {
        return InformType;
    }

    public void setInformType(String informType) {
        InformType = informType;
    }

    public String getUniqueCode() {
        return UniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        UniqueCode = uniqueCode;
    }

	public String getSaleReason() {
		return SaleReason;
	}

	public void setSaleReason(String saleReason) {
		SaleReason = saleReason;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getTagPrice() {
		return TagPrice;
	}

	public void setTagPrice(String tagPrice) {
		TagPrice = tagPrice;
	}
}