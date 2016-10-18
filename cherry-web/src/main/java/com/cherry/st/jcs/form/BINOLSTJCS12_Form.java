/*
 * @(#)BINOLSTJCS12_Form.java     1.0 2015/12/18
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
package com.cherry.st.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 电商产品对应关系一览Form
 * 
 * @author lzs
 * @version 1.0 2015.12.18
 */
public class BINOLSTJCS12_Form extends DataTable_BaseForm {
	
	private String organizationInfoId;
	//品牌
	private String brandInfoId;
	//电商商品标题
	private String esProductTitleName;
	//Sku编码
	private String skuCode;
	//outCode
	private String outCode;
	//厂商编码
	private String unitCode;
	//产品条码
	private String barCode;
	//产品名称
	private String nameTotal;
	//Sku码和新后台对应的单据的时间（以最早的时间为准）
	private String getDate;
	//产品对应关系是否改变 
	private String isRelationChange;
	//数据有效性
	private String validFlag;
	
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getOrganizationInfoId() {
		return organizationInfoId;
	}
	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}
	public String getBrandInfoId() {
		return brandInfoId;
	}
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String getEsProductTitleName() {
		return esProductTitleName;
	}
	public void setEsProductTitleName(String esProductTitleName) {
		this.esProductTitleName = esProductTitleName;
	}
	public String getOutCode() {
		return outCode;
	}
	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getNameTotal() {
		return nameTotal;
	}
	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
	public String getGetDate() {
		return getDate;
	}
	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getIsRelationChange() {
		return isRelationChange;
	}
	public void setIsRelationChange(String isRelationChange) {
		this.isRelationChange = isRelationChange;
	}
	
}
