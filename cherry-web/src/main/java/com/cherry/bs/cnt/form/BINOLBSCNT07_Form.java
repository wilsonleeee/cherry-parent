package com.cherry.bs.cnt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/*
 * @(#)BINOLBSCNT07_Form.java    1.0 2012-7-1
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
public class BINOLBSCNT07_Form  extends DataTable_BaseForm {


	/**柜台积分计划记录ID **/
	private String counterPointPlanId;

	/** 组织ID**/
	private String organizationInfoId;

	/**所属品牌ID **/
	private String brandInfoId;

	/** 柜台对应的组织结构ID**/
	private String organizationId;

	/** 计划开始日期**/
	private String startDate;

	/** 计划结束日期**/
	private String endDate;

	/** 经销商当前积分额度**/
	private String currentPointLimit;

	/** 备注**/
	private String comment;

	/**有效区分 **/
	private String validFlag;


	public String getCounterPointPlanId() {
		return counterPointPlanId;
	}

	public void setCounterPointPlanId(String counterPointPlanId) {
		this.counterPointPlanId = counterPointPlanId;
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

	public String getCurrentPointLimit() {
		return currentPointLimit;
	}

	public void setCurrentPointLimit(String currentPointLimit) {
		this.currentPointLimit = currentPointLimit;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
