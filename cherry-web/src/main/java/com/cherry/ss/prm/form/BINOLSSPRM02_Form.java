/*
 * @(#)BINOLSSPRM02_Form.java     1.0 2010/11/19
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
package com.cherry.ss.prm.form;

/**
 * 
 * 促销品添加Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
public class BINOLSSPRM02_Form {
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 促销产品类别 */
	private String promPrtCateId;
	
	/**促销产品条码*/
    private String barCode;
	
	/** 大分类 */
	private String primaryCateCode;
	
	/** 中分类 */
	private String secondCateCode;
	
	/** 小分类 */
	private String smallCateCode;
	
	/** 促销品全称 */
	private String nameTotal;
	
	/** 促销品简称 */
	private String nameShort;
	 
	/** 促销品别名 */
	private String nameAlias;
	
	/** 促销品英文名 */
	private String nameForeign;
	
	/** 促销品英文简称 */
	private String nameShortForeign;
	
	/** 促销品样式 */
	private String styleCode;
	
	/** 促销品使用方式 */
	private String operationStyle;
	
	/** 促销品容量 */
	private String volume;
	
	/** 促销品容量单位 */
	private String volumeUnit;
	
	/** 促销品重量 */
	private String weight;
	
	/** 促销品重量单位 */
	private String weightUnit;
	
	/** 促销品单位代码 */
	private String moduleCode;	
	
	/** 销售单位 */
	private String saleUnit;
	
	/** 经销商供货状态 */
	private String discontReseller;
	
	/** 柜台供货状态 */
	private String discontCounter;																						
			
	/** 可否补货 */
	private String isReplenish;									
			
	/** 保质期 */
	private String shelfLife;
	
	/** 厂商编码 */
	private String unitCode;

	/** 促销品标准成本 */
	private String standardCost;
	
	/** 促销品状态 */
	private String status;
	
	/** 促销品开始销售日期 */
	private String sellStartDate;
	
	/** 促销品结束销售日期 */
	private String sellEndDate;		
	
	/** 标准价格信息 */
	private String priceInfo;
	
	/** 图片路径 */
	private String[] promImagePath;
	
	/** 促销品厂商信息 */
	private String manuFactInfo;
	
	/** 部门价格信息 */
	private String departPriceInfo;
	
	/** 促销品类型 */
	private String promCate;
	
	/** 是否库存管理 */
	private String isStock;
	
	/** 可兑换积分 */
	private String exPoint;
	
	/** 可否用于积分兑换 */
	private String isExchanged;
	
	/** 是否下发到POS **/
	private String isPosIss;
	
	/**促销品（促销品类型=促销礼品   启用改属性）*/
	private String mode;
	
	public String getIsPosIss() {
		return isPosIss;
	}

	public void setIsPosIss(String isPosIss) {
		this.isPosIss = isPosIss;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getPromPrtCateId() {
		return promPrtCateId;
	}

	public void setPromPrtCateId(String promPrtCateId) {
		this.promPrtCateId = promPrtCateId;
	}
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getPrimaryCateCode() {
		return primaryCateCode;
	}

	public void setPrimaryCateCode(String primaryCateCode) {
		this.primaryCateCode = primaryCateCode;
	}

	public String getSecondCateCode() {
		return secondCateCode;
	}

	public void setSecondCateCode(String secondCateCode) {
		this.secondCateCode = secondCateCode;
	}

	public String getSmallCateCode() {
		return smallCateCode;
	}

	public void setSmallCateCode(String smallCateCode) {
		this.smallCateCode = smallCateCode;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}
	
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setStandardCost(String standardCost) {
		this.standardCost = standardCost;
	}

	public String getStandardCost() {
		return standardCost;
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
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
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

	public String[] getPromImagePath() {
		return promImagePath;
	}

	public void setPromImagePath(String[] promImagePath) {
		this.promImagePath = promImagePath;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
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

	public String getManuFactInfo() {
		return manuFactInfo;
	}

	public void setManuFactInfo(String manuFactInfo) {
		this.manuFactInfo = manuFactInfo;
	}

	public String getDepartPriceInfo() {
		return departPriceInfo;
	}

	public void setDepartPriceInfo(String departPriceInfo) {
		this.departPriceInfo = departPriceInfo;
	}

	public String getPromCate() {
		return promCate;
	}

	public void setPromCate(String promCate) {
		this.promCate = promCate;
	}

	public String getIsStock() {
		return isStock;
	}

	public void setIsStock(String isStock) {
		this.isStock = isStock;
	}

	public String getExPoint() {
		return exPoint;
	}

	public void setExPoint(String exPoint) {
		this.exPoint = exPoint;
	}
	
	public String getIsExchanged() {
		return isExchanged;
	}

	public void setIsExchanged(String isExchanged) {
		this.isExchanged = isExchanged;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}
