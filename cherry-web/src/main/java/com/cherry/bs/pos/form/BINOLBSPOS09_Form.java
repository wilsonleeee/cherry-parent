/*
 * @(#)BINOLBSPOS04_Form.java     1.0 2010/10/27
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

package com.cherry.bs.pos.form;

/**
 * 添加岗位类别画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS09_Form {
	
	/** 类别代码 */
	private String categoryCode;
	
	/** 类别名称 */
	private String categoryName;
	
	/** 类别外文名称 */
	private String categoryNameForeign;
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	/** 岗位级别 */
	private String posGrade;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryNameForeign() {
		return categoryNameForeign;
	}

	public void setCategoryNameForeign(String categoryNameForeign) {
		this.categoryNameForeign = categoryNameForeign;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getPosGrade() {
		return posGrade;
	}

	public void setPosGrade(String posGrade) {
		this.posGrade = posGrade;
	}

}
