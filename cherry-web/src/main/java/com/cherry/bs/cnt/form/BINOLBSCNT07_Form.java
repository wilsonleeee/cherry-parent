package com.cherry.bs.cnt.form;

import com.cherry.cm.form.DataTable_BaseForm;

import java.util.Map;

/*
 * @(#)BINOLBSCNT07_Form.java    1.0 2016-11-24
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

	/**柜台编号Id**/
	private int counterInfoId;

	/**柜台编号 **/
	private String counterCode;

	/**柜台名称 **/
	private String counterName;


	/**积分开始范围 **/
	private String pointLimitBegin;


	/**积分结束范围 **/
	private String pointLimitEnd;

	/**是否积分计划 **/
	private String pointPlanStatus;


	/**柜台状态 **/
	private String counterStatus;

	/**积分开始日期 **/
	private String pointDateBegin;

	/**积分结束日期 **/
	private String pointDateEnd;

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

	/** 积分变更值**/
	private String pointChange;

	/** 备注**/
	private String comment;

	/**有效区分 **/
	private String validFlag;

	/** 当前日期 **/
	private String currentDate;

	public int getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(int counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String getPointChange() {
		return pointChange;
	}

	public void setPointChange(String pointChange) {
		this.pointChange = pointChange;
	}

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

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getPointLimitBegin() {
		return pointLimitBegin;
	}

	public void setPointLimitBegin(String pointLimitBegin) {
		this.pointLimitBegin = pointLimitBegin;
	}

	public String getPointLimitEnd() {
		return pointLimitEnd;
	}

	public void setPointLimitEnd(String pointLimitEnd) {
		this.pointLimitEnd = pointLimitEnd;
	}

	public String getPointPlanStatus() {
		return pointPlanStatus;
	}

	public void setPointPlanStatus(String pointPlanStatus) {
		this.pointPlanStatus = pointPlanStatus;
	}

	public String getCounterStatus() {
		return counterStatus;
	}

	public void setCounterStatus(String counterStatus) {
		this.counterStatus = counterStatus;
	}

	public String getPointDateBegin() {
		return pointDateBegin;
	}

	public void setPointDateBegin(String pointDateBegin) {
		this.pointDateBegin = pointDateBegin;
	}

	public String getPointDateEnd() {
		return pointDateEnd;
	}

	public void setPointDateEnd(String pointDateEnd) {
		this.pointDateEnd = pointDateEnd;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

}
