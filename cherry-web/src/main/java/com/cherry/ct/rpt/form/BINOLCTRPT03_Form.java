/*
 * @(#)BINOLCTRPT03_Form.java     1.0 2013/08/06
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
package com.cherry.ct.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 沟通明细查询Form
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT03_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 组织ID */
	private String organizationInfoId;
	
	/** 批次号 */
	private String batchId;
	
	/** 活动编号 */
	private String communicationCode;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 手机号码 */
	private String mobilePhone;
	
	/** 验证号 */
	private String couponCode;
	
	/** 柜台名称 */
	private String counterName;
	
	/** 发送时间开始 */
	private String sendBeginDate;
	
	/** 发送时间截止 */
	private String sendEndDate;
	
	/** 显示类型 */
	private String showType;
	
	/** 导出格式*/
	private String exportFormat;
	
	/** 信息编号 */
	private String messageCode;
	
	/** 沟通方式 */
	private String commType;
	
	/** 执行方式 */
	private String runType;
	
	/** 计划名称*/
	private String planName;
	
	/** 计划编号*/
	private String planCode;
	
	/**导出CSV编码*/
	private String charset;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getCommunicationCode() {
		return communicationCode;
	}

	public void setCommunicationCode(String communicationCode) {
		this.communicationCode = communicationCode;
	}

	public String getMemCode() {
		return memCode;
	}

	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getSendBeginDate() {
		return sendBeginDate;
	}

	public void setSendBeginDate(String sendBeginDate) {
		this.sendBeginDate = sendBeginDate;
	}

	public String getSendEndDate() {
		return sendEndDate;
	}

	public void setSendEndDate(String sendEndDate) {
		this.sendEndDate = sendEndDate;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}
	
	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getCommType() {
		return commType;
	}

	public void setCommType(String commType) {
		this.commType = commType;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
