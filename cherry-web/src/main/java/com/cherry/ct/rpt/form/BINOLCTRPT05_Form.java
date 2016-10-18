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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;
/**
 * 沟通绩效分析Form
 * 
 * @author ZhangLe
 * @version 1.0 2013.09.26
 */
public class BINOLCTRPT05_Form extends BINOLCM13_Form{
	/**信息发送批次*/
	private String batchId;
	/**开始日期*/
	private String startTime;
	/**结束日期*/
	private String endTime;
	/**沟通信息发送时间*/
	private String sendTime;
	/**开始天数*/
	private String startDays;
	/**结束天数*/
	private String endDays;
	/**时间类型*/
	private String queryType;
	/**部门ID**/
	private String organizationId;
	/**导出格式(1：沟通统计效果Excel导出；2：沟通统计效果CSV导出；3：参与明细Excel导出；4：参与明细CSV导出)*/
	private String exportFormat;
	/**导出CSV编码*/
	private String charset;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
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
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getStartDays() {
		return startDays;
	}
	public void setStartDays(String startDays) {
		this.startDays = startDays;
	}
	public String getEndDays() {
		return endDays;
	}
	public void setEndDays(String endDays) {
		this.endDays = endDays;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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
