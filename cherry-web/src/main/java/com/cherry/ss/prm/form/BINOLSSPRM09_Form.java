/*
 * @(#)BINOLSSPRM09_Form.java     1.0 2010/11/23
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
 * 促销品查询实体类
 * 促销品信息DTO
 * 
 */
public class BINOLSSPRM09_Form extends DataTable_BaseForm {
	
	/** 有效区分 + 促销产品类别ID + 更新日期 + 更新次数  */
	private String[] prmCategoryInfo;
	
	private String brandInfoId; //所属品牌ID
	
	private String prmCategoryId; // 促销品类别ID
    
    private String itemClassName; //类别名称
    
    private String itemClassCode; //类别码
    
    private String curClassCode; //类别特征码
	
	private String validFlag; //有效区分
	
	private String optFlag; // 促销品操作区分1：开启 0：停用 
	
	private String path; // 上级类别节点位置 

	
	//有效区分
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	// 促销品操作区分1：开启 0：停用 
	public void setOptFlag(String optFlag) {
		this.optFlag = optFlag;
	}

	public String getOptFlag() {
		return optFlag;
	}

	//类别名称
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	//类别码
	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public String getItemClassCode() {
		return itemClassCode;
	}

	//类别特征码
	public void setCurClassCode(String curClassCode) {
		this.curClassCode = curClassCode;
	}

	public String getCurClassCode() {
		return curClassCode;
	}

	/** 有效区分 + 促销产品类别ID + 更新日期 + 更新次数  */
	public void setPrmCategoryInfo(String[] prmCategoryInfo) {
		this.prmCategoryInfo = prmCategoryInfo;
	}

	public String[] getPrmCategoryInfo() {
		return prmCategoryInfo;
	}

	//所属品牌ID
	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	// 促销品类别ID
	public void setPrmCategoryId(String prmCategoryId) {
		this.prmCategoryId = prmCategoryId;
	}

	public String getPrmCategoryId() {
		return prmCategoryId;
	}

	// 上级类别节点位置
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
