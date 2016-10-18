/*
 * @(#)BINOLPLSCF03_Form.java     1.0 2010/10/27
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
 * 添加审核审批配置信息Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF03_Form {
	
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

}
