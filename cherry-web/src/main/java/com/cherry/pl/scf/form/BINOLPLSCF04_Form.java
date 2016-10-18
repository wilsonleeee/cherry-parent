/*
 * @(#)BINOLPLSCF04_Form.java     1.0 2010/10/27
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

package com.cherry.pl.scf.form;

/**
 * 更新审核审批配置信息Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF04_Form {
	
	/** 审核审批配置信息ID */
	private String auditPrivilegeId;
	
	/** 品牌ID */
	private String brandInfoId;
	
	/** 业务类型代码 */
	private String bussinessTypeCode;
	
	/** 发起者身份类型 */
	private String initiatorType;
	
	/** 发起者 */
	private String initiatorID;
	
	/** 审核者身份类型 */
	private String auditorType;
	
	/** 审核者 */
	private String auditorID;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;

	public String getAuditPrivilegeId() {
		return auditPrivilegeId;
	}

	public void setAuditPrivilegeId(String auditPrivilegeId) {
		this.auditPrivilegeId = auditPrivilegeId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBussinessTypeCode() {
		return bussinessTypeCode;
	}

	public void setBussinessTypeCode(String bussinessTypeCode) {
		this.bussinessTypeCode = bussinessTypeCode;
	}

	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}

	public String getInitiatorID() {
		return initiatorID;
	}

	public void setInitiatorID(String initiatorID) {
		this.initiatorID = initiatorID;
	}

	public String getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}

	public String getAuditorID() {
		return auditorID;
	}

	public void setAuditorID(String auditorID) {
		this.auditorID = auditorID;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

}
