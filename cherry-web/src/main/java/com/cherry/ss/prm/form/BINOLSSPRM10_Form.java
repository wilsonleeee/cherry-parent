/*
 * @(#)BINOLSSPRM10_Form.java     1.0 2011/02/18
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
public class BINOLSSPRM10_Form extends DataTable_BaseForm {
    
	/** 上级类别节点位置 */
	private String path;
	
	/** 促销品类别ID */
	private String prmCategoryId;
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 类别中文名 */
	private String itemClassNameCN;
	
	/** 类别英文名 */
    private String itemClassNameEN;
    
    /** 类别代码*/
    private String itemClassCode;
    
    /** 类别特征码 */
    private String curClassCode;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPrmCategoryId(String prmCategoryId) {
		this.prmCategoryId = prmCategoryId;
	}

	public String getPrmCategoryId() {
		return prmCategoryId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
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

}
