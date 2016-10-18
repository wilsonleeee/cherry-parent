/*
 * @(#)BINOLBSDEP11_Form.java     1.0 2011.2.10
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

package com.cherry.bs.dep.form;

/**
 * 品牌添加画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP11_Form {
	
	/** 品牌代码 */
	private String brandCode;
	
	/** 品牌名称中文 */
	private String brandNameChinese;
	
	/** 品牌名称中文简称 */
	private String brandNameShort;
	
	/** 品牌名称外文 */
	private String brandNameForeign;
	
	/** 品牌名称外文简称 */
	private String brandNameForeignShort;
	
	/** 成立日期 */
	private String foundationDate;

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandNameChinese() {
		return brandNameChinese;
	}

	public void setBrandNameChinese(String brandNameChinese) {
		this.brandNameChinese = brandNameChinese;
	}

	public String getBrandNameShort() {
		return brandNameShort;
	}

	public void setBrandNameShort(String brandNameShort) {
		this.brandNameShort = brandNameShort;
	}

	public String getBrandNameForeign() {
		return brandNameForeign;
	}

	public void setBrandNameForeign(String brandNameForeign) {
		this.brandNameForeign = brandNameForeign;
	}

	public String getBrandNameForeignShort() {
		return brandNameForeignShort;
	}

	public void setBrandNameForeignShort(String brandNameForeignShort) {
		this.brandNameForeignShort = brandNameForeignShort;
	}

	public String getFoundationDate() {
		return foundationDate;
	}

	public void setFoundationDate(String foundationDate) {
		this.foundationDate = foundationDate;
	}

}
