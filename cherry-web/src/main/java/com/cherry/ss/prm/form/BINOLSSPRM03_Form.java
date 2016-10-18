/*
 * @(#)BINOLSSPRM03_Form.java     1.0 2010/11/23
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 促销品详细实体类
 * 促销品信息DTO
 * 
 */
public class BINOLSSPRM03_Form extends DataTable_BaseForm {
	
	/**促销品ID*/
	private String promotionProId;
	
	/**促销品更新时间*/
	private String prmUpdateTime;	
	
	/**促销品更新次数*/
    private String prmModifyCount;
    
    /**促销产品条码*/
    private String barCodeAdd;
    
    /** 品牌信息ID */
	private String brandInfoId;
    
    /** 促销产品类别ID */
	private String promPrtCateId;
	
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
	
	/** 更新前的厂商编码 */
	private String oldUnitCode;
	
	/** 厂商编码 */
	private String unitCode;

	/** 促销品标准成本 */
	private String standardCost;
	
	/** 可否成为BOM的组成 */
	private String isBOMComp;
	
	/** 促销品状态 */
	private String status;
	
	/** 促销品开始销售日期 */
	private String sellStartDate;
	
	/** 促销品结束销售日期 */
	private String sellEndDate;
	
	/** 开始销售日期显示区分 */
	private String sellStartDateKbn;
	
	/** 结束销售日期显示区分 */
	private String sellEndDateKbn;
	
	/** 标准价格信息 */
	private String priceInfo;
	
	/** 促销品厂商信息 */
	private String manuFactInfo;
	
	/** 部门价格信息 */
	private String departPriceInfo;

	/**促销品厂商ID*/
	private String prmVendorId;
	
	/**部门机构促销品价格ID*/
	private String prmPriceDepartId;
	
	/** 图片路径*/
	private String[] promImagePath;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 更新前的有效区分 */
	private String prevValidFlag;
	
	/** 促销品类型 */
	private String promCate;
	
	/** 旧促销品类型 */
	private String oldPromCate;
	
	/** 是否库存管理 */
	private String isStock;
	
	/** 可兑换积分 */
	private String exPoint;
	
	/** 可编辑标志*/
	private String editFlag;
	
	/** 可编辑标志*/
	private String sendEditFlag;
	
	/** 已停用促销品 */
	private String validPrmFlag;
	
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

	
	public String getPrmUpdateTime() {
		return prmUpdateTime;
	}

	public void setPrmUpdateTime(String prmUpdateTime) {
		this.prmUpdateTime = prmUpdateTime;
	}

	public String getPrmModifyCount() {
		return prmModifyCount;
	}

	public void setPrmModifyCount(String prmModifyCount) {
		this.prmModifyCount = prmModifyCount;
	}
	

	public String getBarCodeAdd() {
		return barCodeAdd;
	}

	public void setBarCodeAdd(String barCodeAdd) {
		this.barCodeAdd = barCodeAdd;
	}

	public void setPromPrtCateId(String promPrtCateId) {
		this.promPrtCateId = promPrtCateId;
	}

	public String getPromPrtCateId() {
		return promPrtCateId;
	}

	public void setPrimaryCateCode(String primaryCateCode) {
		this.primaryCateCode = primaryCateCode;
	}

	public String getPrimaryCateCode() {
		return primaryCateCode;
	}

	public void setSecondCateCode(String secondCateCode) {
		this.secondCateCode = secondCateCode;
	}

	public String getSecondCateCode() {
		return secondCateCode;
	}

	public void setSmallCateCode(String smallCateCode) {
		this.smallCateCode = smallCateCode;
	}

	public String getSmallCateCode() {
		return smallCateCode;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
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

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getWeightUnit() {
		return weightUnit;
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

	public void setDiscontReseller(String discontReseller) {
		this.discontReseller = discontReseller;
	}

	public String getDiscontReseller() {
		return discontReseller;
	}

	public void setDiscontCounter(String discontCounter) {
		this.discontCounter = discontCounter;
	}

	public String getDiscontCounter() {
		return discontCounter;
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
	
	public String getOldUnitCode() {
		return oldUnitCode;
	}

	public void setOldUnitCode(String oldUnitCode) {
		this.oldUnitCode = oldUnitCode;
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

	public void setIsBOMComp(String isBOMComp) {
		this.isBOMComp = isBOMComp;
	}

	public String getIsBOMComp() {
		return isBOMComp;
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
		
	public String getSellStartDateKbn() {
		return sellStartDateKbn;
	}

	public void setSellStartDateKbn(String sellStartDateKbn) {
		this.sellStartDateKbn = sellStartDateKbn;
	}

	public String getSellEndDateKbn() {
		return sellEndDateKbn;
	}

	public void setSellEndDateKbn(String sellEndDateKbn) {
		this.sellEndDateKbn = sellEndDateKbn;
	}
	
	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}

	public void setManuFactInfo(String manuFactInfo) {
		this.manuFactInfo = manuFactInfo;
	}

	public String getManuFactInfo() {
		return manuFactInfo;
	}

	public void setDepartPriceInfo(String departPriceInfo) {
		this.departPriceInfo = departPriceInfo;
	}

	public String getDepartPriceInfo() {
		return departPriceInfo;
	}

	public void setPrmPriceDepartId(String prmPriceDepartId) {
		this.prmPriceDepartId = prmPriceDepartId;
	}

	public String getPrmPriceDepartId() {
		return prmPriceDepartId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setPromImagePath(String[] promImagePath) {
		this.promImagePath = promImagePath;
	}

	public String[] getPromImagePath() {
		return promImagePath;
	}
	
	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public void setPromotionProId(String promotionProId) {
		this.promotionProId = promotionProId;
	}

	public String getPromotionProId() {
		return promotionProId;
	}

	public String getPrevValidFlag() {
		return prevValidFlag;
	}

	public void setPrevValidFlag(String prevValidFlag) {
		this.prevValidFlag = prevValidFlag;
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

	public String getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}

	public String getValidPrmFlag() {
		return validPrmFlag;
	}

	public void setValidPrmFlag(String validPrmFlag) {
		this.validPrmFlag = validPrmFlag;
	}

	public String getOldPromCate() {
		return oldPromCate;
	}

	public void setOldPromCate(String oldPromCate) {
		this.oldPromCate = oldPromCate;
	}

	public String getSendEditFlag() {
		return sendEditFlag;
	}

	public void setSendEditFlag(String sendEditFlag) {
		this.sendEditFlag = sendEditFlag;
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
