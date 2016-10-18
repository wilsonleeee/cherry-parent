/*
 * @(#)BINOLCTRPT04_Form.java     1.0 2013/09/26
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
 * @author ZhangLe
 * @version 1.0 2013.09.26
 */
public class BINOLCTRPT04_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 组织ID */
	private String organizationInfoId;
	
	/** 批次号 */
	private String batchId;
	
	/** 会员卡号 */
	private String memCode;
	
	/** 手机号码 */
	private String mobilePhone;
	
	/** 沟通类型*/
	private String commType;
	
	/** 发送时间开始 */
	private String sendBeginDate;
	
	/** 发送时间截止 */
	private String sendEndDate;
	
	/** 显示类型 */
	private String showType;
	
	/** 异常信息类型 */
	private String errorType;
	
	/** 导出类型 */
	private String exportFormat;
	
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

	public String getCommType() {
		return commType;
	}

	public void setCommType(String commType) {
		this.commType = commType;
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

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
