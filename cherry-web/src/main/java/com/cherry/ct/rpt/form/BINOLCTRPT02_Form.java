/*
 * @(#)BINOLCTRPT02_Form.java     1.0 2013/08/06
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
 * 事件触发明细Form
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT02_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 沟通方式 */
	private String commType;
	
	/** 事件名称 */
	private String eventType;
	
	/** 导出格式*/
	private String exportFormat;

	/**开始时间*/
	private String startTime;
	
	/**结束时间*/
	private String endTime;
	
	/**手机号码*/
	private String mobilePhone;
	
	/**导出CSV编码*/
	private String charset;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCommType() {
		return commType;
	}

	public void setCommType(String commType) {
		this.commType = commType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
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

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
