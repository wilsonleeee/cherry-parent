/*
 * @(#)BINOLSSPRM05_Form.java     1.0 2010/11/23
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
 * 促销品分类查询实体类
 * 促销品分类信息DTO
 * 
 */
public class BINOLSSPRM05_Form extends DataTable_BaseForm {
	
	/** 有效区分 + 促销产品分类ID + 更新日期 + 更新次数  */
	private String[] prmTypeInfo;
	
	/**所属品牌ID*/
    private String brandInfoId;
    
    /**分类名称*/
    private String categoryName;
    
    /**分类代码*/
    private String categoryCode;
	
    /**有效区分*/
	private String validFlag;
	
	/**促销品操作区分*/
	private String optFlag;

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getOptFlag() {
		return optFlag;
	}

	public void setPrmTypeInfo(String[] prmTypeInfo) {
		this.prmTypeInfo = prmTypeInfo;
	}

	public String[] getPrmTypeInfo() {
		return prmTypeInfo;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

}
