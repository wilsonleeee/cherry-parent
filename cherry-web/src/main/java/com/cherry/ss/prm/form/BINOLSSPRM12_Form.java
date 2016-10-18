/*
 * @(#)BINOLSSPRM12_Form.java     1.0 2010/11/29
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
 * 促销品类别详细实体类
 * 促销品类别信息DTO
 * 
 */
public class BINOLSSPRM12_Form extends DataTable_BaseForm {
	
	private String prmCategoryId;//促销品类别ID
	
	private String itemClassCode; // 促销品类别

	public void setPrmCategoryId(String prmCategoryId) {
		this.prmCategoryId = prmCategoryId;
	}

	public String getPrmCategoryId() {
		return prmCategoryId;
	}

	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public String getItemClassCode() {
		return itemClassCode;
	}
	

}
