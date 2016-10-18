/*
 * @(#)BINOLCTCOM05_Form.java     1.0 2013/06/06
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
package com.cherry.ct.common.form;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 沟通信息测试Form
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.06
 */
public class BINOLCTCOM05_Form extends DataTable_BaseForm{
	/** 沟通信息类型 */
	private String messageType;
	
	/** 沟通信息内容 */
	private String contents;
	
	/** 信息接收号码列表 */
	private String resCodeList;
	/** 短信通道 */
	private String smsChannel;
	
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getResCodeList() {
		return resCodeList;
	}
	
	public void setResCodeList(String resCodeList) {
		this.resCodeList = resCodeList;
	}

	public String getSmsChannel() {
		return smsChannel;
	}

	public void setSmsChannel(String smsChannel) {
		this.smsChannel = smsChannel;
	}
	
}
