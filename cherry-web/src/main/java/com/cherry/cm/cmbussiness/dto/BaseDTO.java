/*	
 * @(#)BaseDTO.java     1.0 2011/04/26	
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
package com.cherry.cm.cmbussiness.dto;


/**
 * 基础信息DTO
 * 
 * @author hub
 * @version 1.0 2010.04.26
 */
public class BaseDTO {
	
	/** 有效区分 */
	private String validFlag;
	
	/** 作成日时 */
	private String createTime;
	
	/** 作成者 */
	private String createdBy;
	
	/** 作成程序名 */
	private String createPGM;
	
	/** 更新日时 */
	private String updateTime;
	
	/** 更新者 */
	private String updatedBy;
	
	/** 更新程序名 */
	private String updatePGM;
	
	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatePGM() {
		return createPGM;
	}

	public void setCreatePGM(String createPGM) {
		this.createPGM = createPGM;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatePGM() {
		return updatePGM;
	}

	public void setUpdatePGM(String updatePGM) {
		this.updatePGM = updatePGM;
	}
}
