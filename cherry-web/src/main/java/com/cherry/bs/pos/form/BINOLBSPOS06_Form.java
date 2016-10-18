/*
 * @(#)BINOLBSPOS06_Form.java     1.0 2010/10/27
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 岗位类别一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS06_Form extends DataTable_BaseForm {
	
	/** 类别代码 */
	private String categoryCode;
	
	/** 类别名称 */
	private String categoryName;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 所属品牌ID */
	private String brandInfoId;
	
	private List<Map<String, Object>> positionCategoryList;

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

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public List<Map<String, Object>> getPositionCategoryList() {
		return positionCategoryList;
	}

	public void setPositionCategoryList(
			List<Map<String, Object>> positionCategoryList) {
		this.positionCategoryList = positionCategoryList;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
}
