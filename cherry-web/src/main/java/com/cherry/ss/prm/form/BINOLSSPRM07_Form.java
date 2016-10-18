/*
 * @(#)BINOLSSPRM07_Form.java     1.0 2010/11/29
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
 * 促销品分类详细实体类
 * 促销品分类信息DTO
 * 
 */
public class BINOLSSPRM07_Form extends DataTable_BaseForm {
	
	/**促销品分类ID*/
	private String prmTypeId;
	
	/**所属品牌ID*/
	private String brandInfoId;
	
	/**促销品分类更新时间*/
	private String modifyTime;	
	
	/**促销品分类更新次数*/
    private String modifyCount;
    
	/**促销品大分类代码*/
	private String primaryCategoryCode;
	
	/**促销品大分类中文名称*/
    private String primaryCategoryNameCN;
    
    /**促销品大分类英文名称*/
    private String primaryCategoryNameEN;
    
    /**促销品中分类代码*/
    private String secondryCategoryCode;
    
    /**促销品中分类中文名称*/
    private String secondryCategoryNameCN;
    
    /**促销品中分类英文名称*/
    private String secondryCategoryNameEN;
    
    /**促销品小分类代码*/
    private String smallCategoryCode;
    
    /**促销品小分类中文名称*/
    private String smallCategoryNameCN;
    
    /**促销品小分类英文名称*/
    private String smallCategoryNameEN;

	public void setPrmTypeId(String prmTypeId) {
		this.prmTypeId = prmTypeId;
	}

	public String getPrmTypeId() {
		return prmTypeId;
	}
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public void setPrimaryCategoryCode(String primaryCategoryCode) {
		this.primaryCategoryCode = primaryCategoryCode;
	}

	public String getPrimaryCategoryCode() {
		return primaryCategoryCode;
	}

	public void setPrimaryCategoryNameCN(String primaryCategoryNameCN) {
		this.primaryCategoryNameCN = primaryCategoryNameCN;
	}

	public String getPrimaryCategoryNameCN() {
		return primaryCategoryNameCN;
	}

	public void setPrimaryCategoryNameEN(String primaryCategoryNameEN) {
		this.primaryCategoryNameEN = primaryCategoryNameEN;
	}

	public String getPrimaryCategoryNameEN() {
		return primaryCategoryNameEN;
	}

	public void setSecondryCategoryCode(String secondryCategoryCode) {
		this.secondryCategoryCode = secondryCategoryCode;
	}

	public String getSecondryCategoryCode() {
		return secondryCategoryCode;
	}

	public void setSecondryCategoryNameCN(String secondryCategoryNameCN) {
		this.secondryCategoryNameCN = secondryCategoryNameCN;
	}

	public String getSecondryCategoryNameCN() {
		return secondryCategoryNameCN;
	}

	public void setSecondryCategoryNameEN(String secondryCategoryNameEN) {
		this.secondryCategoryNameEN = secondryCategoryNameEN;
	}

	public String getSecondryCategoryNameEN() {
		return secondryCategoryNameEN;
	}

	public void setSmallCategoryCode(String smallCategoryCode) {
		this.smallCategoryCode = smallCategoryCode;
	}

	public String getSmallCategoryCode() {
		return smallCategoryCode;
	}

	public void setSmallCategoryNameCN(String smallCategoryNameCN) {
		this.smallCategoryNameCN = smallCategoryNameCN;
	}

	public String getSmallCategoryNameCN() {
		return smallCategoryNameCN;
	}

	public void setSmallCategoryNameEN(String smallCategoryNameEN) {
		this.smallCategoryNameEN = smallCategoryNameEN;
	}

	public String getSmallCategoryNameEN() {
		return smallCategoryNameEN;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getModifyCount() {
		return modifyCount;
	}


}
