/*
 * @(#)BINOLSSPRM11_Form.java     1.0 2010/11/29
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
public class BINOLSSPRM11_Form extends DataTable_BaseForm {
	
	/**促销品类别ID*/
	private String prmCategoryId;
	
	/** 促销品类别位置 */
	private String categoryPath;
	
	/** 原上级促销品类别节点位置 */
	private String higherCategoryPath;
	
	/** 新上级促销品类别节点位置 */
	private String path;
	
	/**促销品类别更新时间*/
	private String modifyTime;	
	
	/**促销品类别更新次数*/
    private String modifyCount;
    
    /**促销品类别中文名*/
    private String itemClassNameCN;
    
    /**促销品类别英文名*/
    private String itemClassNameEN;
    
    /**促销品类别码*/
    private String itemClassCode;
    
    /**促销品类别特征码*/
    private String curClassCode;
    
    /**促销品品牌ID*/
    private String brandInfoId;
    
    /** 迁移源 */
	private String fromPage;
    
    /**促销品类别ID*/
	public void setPrmCategoryId(String prmCategoryId) {
		this.prmCategoryId = prmCategoryId;
	}

	public String getPrmCategoryId() {
		return prmCategoryId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setHigherCategoryPath(String higherCategoryPath) {
		this.higherCategoryPath = higherCategoryPath;
	}

	public String getHigherCategoryPath() {
		return higherCategoryPath;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
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

	public void setItemClassNameCN(String itemClassNameCN) {
		this.itemClassNameCN = itemClassNameCN;
	}

	public String getItemClassNameCN() {
		return itemClassNameCN;
	}

	public void setItemClassNameEN(String itemClassNameEN) {
		this.itemClassNameEN = itemClassNameEN;
	}

	public String getItemClassNameEN() {
		return itemClassNameEN;
	}

	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public String getItemClassCode() {
		return itemClassCode;
	}

	public void setCurClassCode(String curClassCode) {
		this.curClassCode = curClassCode;
	}

	public String getCurClassCode() {
		return curClassCode;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

}
