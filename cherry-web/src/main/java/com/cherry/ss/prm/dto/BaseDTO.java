package com.cherry.ss.prm.dto;

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
	
	/** 所属组织ID */
	private int organizationInfoId;
	
	/** 所属品牌ID */
	private int brandInfoId;

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

	public int getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(int organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
}
