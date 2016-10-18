/*
 * @(#)BINOLCTPLN02_Action.java     1.0 2013/07/10
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
package com.cherry.ct.pln.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 沟通触发事件一览Form
 * 
 * @author ZhangGS
 * @version 1.0 2013.07.10
 */
public class BINOLCTPLN02_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 模板类型 */
	private String eventType;
	
	/** 事件设置信息 */
	private String eventSetInfo;
	
	/** 事件延时设置频率信息 */
	private String frequencyCode;
	
	/** 事件允许发送信息的开始时间 */
	private String sendBeginTime;
	
	/** 事件允许发送信息的截止时间 */
	private String sendEndTime;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventSetInfo() {
		return eventSetInfo;
	}

	public void setEventSetInfo(String eventSetInfo) {
		this.eventSetInfo = eventSetInfo;
	}

	public String getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getSendBeginTime() {
		return sendBeginTime;
	}

	public void setSendBeginTime(String sendBeginTime) {
		this.sendBeginTime = sendBeginTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
}
