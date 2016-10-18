/*
 * @(#)BINOLSTJCS11_Form.java     1.0 2014/06/20
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
package com.cherry.st.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * BB柜台维护Form
 * 
 * @author zhangle
 * @version 1.0 2014.06.20
 */
public class BINOLSTJCS11_Form extends DataTable_BaseForm {

	private String BBCounterInfoId;
	private String organizationInfoId;
	private String brandInfoId;
	private String organizationId;
	private String batchCode;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String comments;
	private String validFlag;
	private String departCode;
	private String departName;
	private String[] allBBCounterInfoId;

	public String getBBCounterInfoId() {
		return BBCounterInfoId;
	}

	public void setBBCounterInfoId(String bBCounterInfoId) {
		BBCounterInfoId = bBCounterInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String[] getAllBBCounterInfoId() {
		return allBBCounterInfoId;
	}

	public void setAllBBCounterInfoId(String[] allBBCounterInfoId) {
		this.allBBCounterInfoId = allBBCounterInfoId;
	}

}
