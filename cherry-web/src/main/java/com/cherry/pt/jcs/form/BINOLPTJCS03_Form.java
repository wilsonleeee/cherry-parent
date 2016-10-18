/*
 * @(#)BINOLPTJCS03_Form.java     1.0 2011/03/21
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
package com.cherry.pt.jcs.form;

import java.util.List;

import com.cherry.cm.dto.ExtendPropertyDto;
import com.cherry.cm.form.BaseForm;

/**
 * 
 * 产品添加Form
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.03.21
 */
public class BINOLPTJCS03_Form extends BaseForm{
	
	/** 分类ID */
	private int propId;

	/** 分类属性值ID */
	private int propValId;
	
    /** 分类终端区分 */
    private String teminalFlag;
	
	/** 产品全称 */
	private String path;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 产品全称 */
	private String nameTotal;
	
	/** 产品简称 */
	private String nameShort;
	 
	/** 产品别名 */
	private String nameAlias;
	
	/** 产品英文名 */
	private String nameForeign;
	
	/** 产品英文简称 */
	private String nameShortForeign;
	
	/** 产品样式 */
	private String styleCode;
	
	/** 产品使用方式 */
	private String operationStyle;
	
	/** 产品单位代码 */
	private String moduleCode;	
	
	/** 销售单位 */
	private String saleUnit;
	
	/** 经销商供货状态 */
	private String discontReseller;
	
	/** 柜台供货状态 */
	private String discontCounter;																						
			
	/** 可否补货 */
	private String isReplenish;	
	
	/** 可否作为BOM的组成 */
	private String isBOM;
	
	/** 产品类型 */
	private String mode;
	
	/** 是否明星产品 */
	private String starProduct;
	
	/** BOM信息 */
	private String bomInfo;
	
	/** 可否断货 */
	private String lackFlag;	
			
	/** 保质期 */
	private String shelfLife;
	
	/** 建议使用天数 */
	private String recNumDay;
	
	/** 厂商编码 */
	private String unitCode;
	
	/** 产品条码 */
	private String barCode;

	/** 产品标准成本 */
	private String standardCost;
	
	/** 采购价格 */
	private String orderPrice;
	
	/** 产品状态 */
	private String status;
	
	/** 产品开始销售日期 */
	private String sellStartDate;
	
	/** 产品结束销售日期 */
	private String sellEndDate;		
	
	/** 标准价格信息 */
	private String priceInfo;
	
	/** 产品分类信息 */
	private String cateInfo;
	
	/** 图片路径 */
	private String[] imagePath;
	
	/** 规格 */
	private String spec;
	
	/** 更新时间 */
	private String minSalePrice;
	
	/** 更新时间 */
	private String maxSalePrice;
	
	/** 扩展属性List */
	private List<ExtendPropertyDto> extendPropertyList;
	
	/** 可否用于积分兑换 */
	private String isExchanged;
	
	/** 产品销售方式 */
	private String saleStyle;
	
	/** 兑换关系 */
	private String exchangeRelation;
	
	/** 是否管理库存 */
	private String isStock;
	
	/** 子品牌 */
	private String originalBrand;
	
	/** 品类 */
	private String itemType;
	
	/** 色系 */
	private String color;
	
	
	/** 利潤 */
	private String profit;
	

	/** 产品等级 */
	private String grade;
	
	/** 安全库存数量 */
	private String secQty;
	
	/** 陈列数 */
	private String displayQty;
	
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getPropValId() {
		return propValId;
	}

	public void setPropValId(int propValId) {
		this.propValId = propValId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}
	
	public String getStarProduct() {
		return starProduct;
	}

	public void setStarProduct(String starProduct) {
		this.starProduct = starProduct;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public void setStandardCost(String standardCost) {
		this.standardCost = standardCost;
	}

	public String getStandardCost() {
		return standardCost;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setSellStartDate(String sellStartDate) {
		this.sellStartDate = sellStartDate;
	}

	public String getSellStartDate() {
		return sellStartDate;
	}

	public void setSellEndDate(String sellEndDate) {
		this.sellEndDate = sellEndDate;
	}

	public String getSellEndDate() {
		return sellEndDate;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	public String getNameShort() {
		return nameShort;
	}

	public void setNameAlias(String nameAlias) {
		this.nameAlias = nameAlias;
	}

	public String getNameAlias() {
		return nameAlias;
	}

	public void setNameForeign(String nameForeign) {
		this.nameForeign = nameForeign;
	}

	public String getNameForeign() {
		return nameForeign;
	}

	public void setNameShortForeign(String nameShortForeign) {
		this.nameShortForeign = nameShortForeign;
	}

	public String getNameShortForeign() {
		return nameShortForeign;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	public String getStyleCode() {
		return styleCode;
	}

	public void setOperationStyle(String operationStyle) {
		this.operationStyle = operationStyle;
	}

	public String getOperationStyle() {
		return operationStyle;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setIsReplenish(String isReplenish) {
		this.isReplenish = isReplenish;
	}

	public String getIsReplenish() {
		return isReplenish;
	}

	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}

	public String getShelfLife() {
		return shelfLife;
	}
	
	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}

	public String[] getImagePath() {
		return imagePath;
	}

	public void setImagePath(String[] imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getDiscontReseller() {
		return discontReseller;
	}

	public void setDiscontReseller(String discontReseller) {
		this.discontReseller = discontReseller;
	}

	public String getDiscontCounter() {
		return discontCounter;
	}

	public void setDiscontCounter(String discontCounter) {
		this.discontCounter = discontCounter;
	}

	public String getRecNumDay() {
		return recNumDay;
	}

	public void setRecNumDay(String recNumDay) {
		this.recNumDay = recNumDay;
	}

	public String getIsBOM() {
		return isBOM;
	}

	public void setIsBOM(String isBOM) {
		this.isBOM = isBOM;
	}

	public String getLackFlag() {
		return lackFlag;
	}

	public void setLackFlag(String lackFlag) {
		this.lackFlag = lackFlag;
	}

	public List<ExtendPropertyDto> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<ExtendPropertyDto> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public String getCateInfo() {
		return cateInfo;
	}

	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}

	public String getTeminalFlag() {
		return teminalFlag;
	}

	public void setTeminalFlag(String teminalFlag) {
		this.teminalFlag = teminalFlag;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getBomInfo() {
		return bomInfo;
	}

	public void setBomInfo(String bomInfo) {
		this.bomInfo = bomInfo;
	}
	
	public String getIsExchanged() {
		return isExchanged;
	}

	public void setIsExchanged(String isExchanged) {
		this.isExchanged = isExchanged;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getMinSalePrice() {
		return minSalePrice;
	}

	public void setMinSalePrice(String minSalePrice) {
		this.minSalePrice = minSalePrice;
	}

	public String getMaxSalePrice() {
		return maxSalePrice;
	}

	public void setMaxSalePrice(String maxSalePrice) {
		this.maxSalePrice = maxSalePrice;
	}
	
	public String getSaleStyle() {
		return saleStyle;
	}

	public void setSaleStyle(String saleStyle) {
		this.saleStyle = saleStyle;
	}

	public String getExchangeRelation() {
		return exchangeRelation;
	}

	public void setExchangeRelation(String exchangeRelation) {
		this.exchangeRelation = exchangeRelation;
	}
	
	public String getIsStock() {
		return isStock;
	}

	public void setIsStock(String isStock) {
		this.isStock = isStock;
	}
	
	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSecQty() {
		return secQty;
	}

	public void setSecQty(String secQty) {
		this.secQty = secQty;
	}

	public String getDisplayQty() {
		return displayQty;
	}

	public void setDisplayQty(String displayQty) {
		this.displayQty = displayQty;
	}

	
	
}
