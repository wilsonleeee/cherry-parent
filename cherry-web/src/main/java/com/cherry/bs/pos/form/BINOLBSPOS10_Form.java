/*
 * @(#)BINOLBSPOS10_Form.java     1.0 2011/11/04
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
 * 启用停用岗位画面Form
 * 
 * @author WangCT
 * @version 1.0 2011/11/04
 */
public class BINOLBSPOS10_Form {
	
	/** 岗位类别ID */
	private String[] positionCategoryId;
	
	/** 有效区分 */
	private String validFlag;

	public String[] getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String[] positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
