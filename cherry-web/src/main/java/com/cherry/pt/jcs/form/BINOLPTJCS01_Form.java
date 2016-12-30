/*
 * @(#)BINOLPTJCS01_Form.java     1.0 2011/04/11
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
package com.cherry.pt.jcs.form;


import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品分类维护Form
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.04.11
 */
public class BINOLPTJCS01_Form extends DataTable_BaseForm {
	/** 所属品牌ID */
    private String brandInfoId;
    
    /** 分类ID */
    private int propId;
    
    /** 分类选项值ID */
    private int propValId;
    
    /** JSON */
    private String json;
    
    /** 终端显示是否可操作flag */
    private int optFlag;
    
    /** 顺序移动 */
    private String[] moveSeq;

	/**显示停用或者启用分类选项值的区分 1表示停用 0或者空表示启用  */
	private String showDisabled;

	/** 有效标识 0表示无效 1表示有效 */
	private String validFlag;
    
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String[] getMoveSeq() {
		return moveSeq;
	}

	public void setMoveSeq(String[] moveSeq) {
		this.moveSeq = moveSeq;
	}

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getPropValId() {
		return propValId;
	}

	public void setPropValId(int propValId) {
		this.propValId = propValId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getOptFlag() {
		return optFlag;
	}

	public void setOptFlag(int optFlag) {
		this.optFlag = optFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getShowDisabled() {
		return showDisabled;
	}

	public void setShowDisabled(String showDisabled) {
		this.showDisabled = showDisabled;
	}
}
