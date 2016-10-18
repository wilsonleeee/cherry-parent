/*
 * @(#)BINOLSSPRM01_Form.java     1.0 2010/11/23
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
 * 促销品查询实体类
 * 促销品信息DTO
 * 
 */
public class BINOLSSPRM01_Form extends DataTable_BaseForm {
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 有效区分 + 促销产品ID + 更新日期 + 更新次数  */
	private String[] promotionProInfo;
	
	private String nameTotal; // 促销品全称

	private String unitCode; // 厂商编码

	private String barCode; // 促销品条码
	
	private String sellStartDate; //促销品开始销售日期
	
	private String sellEndDate; //促销品结束销售日期
	
	private String validFlag; //有效区分
	
	private String optFlag; // 促销品操作区分1：开启 0：停用 
	
	private String promCate; // 促销品类型
	
	private String isPosIss;// 是否下发到POS
	
	
	private String mode;//促销品（促销品类型=促销礼品   启用改属性）

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

	// 促销品全称
	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}
	
	// 厂商编码
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	//有效区分
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	// 促销品操作区分1：开启 0：停用 
	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getOptFlag() {
		return optFlag;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCode() {
		return barCode;
	}

	/** 有效区分+促销产品ID + 更新日期 + 更新次数  */
	public void setPromotionProInfo(String[] promotionProInfo) {
		this.promotionProInfo = promotionProInfo;
	}

	public String[] getPromotionProInfo() {
		return promotionProInfo;
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

	public String getPromCate() {
		return promCate;
	}

	public void setPromCate(String promCate) {
		this.promCate = promCate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}
