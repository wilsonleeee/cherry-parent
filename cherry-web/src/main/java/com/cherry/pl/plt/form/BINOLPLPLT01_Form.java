/*
 * @(#)BINOLPLPLT01_Form.java     1.0 2010/10/27
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

package com.cherry.pl.plt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 权限类型一览Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT01_Form extends DataTable_BaseForm {
	
	/** 部门类型 */
	private String departType;
	
	/** 岗位类别 */
	private String positionCategoryId;
	
	/** 业务类型 */
	private String businessType;

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
