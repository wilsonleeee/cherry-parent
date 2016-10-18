/*
 * @(#)FileStoreDTO.java     1.0 2011/11/21
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
package com.cherry.cm.core;

/**
 * 文件储存 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.21
 */
public class FileStoreDTO {
	
	/** 文件储存表ID */
	private int fileStoreId;
	
	/** 组织Code */
	private String orgCode;
	
	/** 品牌代码 */
	private String brandCode;
	
	/** 文件类别 */
	private String fileCategory;
	
	/** 文件名称 */
	private String fileName;
	
	/** 文件代号 */
	private String fileCode;
	
	/** 文件内容 */
	private String fileContent;
	
	/** 文件类型 */
	private String fileType;
	
	/** 详细描述 */
	private String descriptionDtl;
	
	/** 修改回数 */
	private int modifyCount;
	
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
	
	public int getFileStoreId() {
		return fileStoreId;
	}

	public void setFileStoreId(int fileStoreId) {
		this.fileStoreId = fileStoreId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDescriptionDtl() {
		return descriptionDtl;
	}

	public void setDescriptionDtl(String descriptionDtl) {
		this.descriptionDtl = descriptionDtl;
	}

	public int getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(int modifyCount) {
		this.modifyCount = modifyCount;
	}

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
