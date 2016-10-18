/*		
 * @(#)BINOLPTRPS11_Form.java     1.0 2011/03/15		
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 库存查询form
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 */
public class BINOLPTRPS38_Form extends BINOLCM13_Form{
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;
	
	/** 产品有效状态 */
	private String validFlag;
	
	/** 子品牌 */
	private String originalBrand;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品ID */
	private String productId;
	
	/** 库存统计方式 */
	private String type;
	
	/** 字符编码 **/
	private String charset;
	
	/** 逻辑仓库ID **/
	private String lgcInventoryId;
	
	/** 大分类ID **/
	private String catePropValId;
	
	/** 大分类名称 **/
	private String bigClassName;
	
	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	
	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getLgcInventoryId() {
		return lgcInventoryId;
	}

	public void setLgcInventoryId(String lgcInventoryId) {
		this.lgcInventoryId = lgcInventoryId;
	}

	public String getCatePropValId() {
		return catePropValId;
	}

	public void setCatePropValId(String catePropValId) {
		this.catePropValId = catePropValId;
	}

	public String getBigClassName() {
		return bigClassName;
	}

	public void setBigClassName(String bigClassName) {
		this.bigClassName = bigClassName;
	}
}
