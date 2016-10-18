/*  
 * @(#)BINOLMOWAT06_Form.java    1.0 2012-9-18     
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
package com.cherry.mo.wat.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * MQ消息错误日志查询Form
 * 
 * @author WangCT
 * @version 1.0 2012-9-18
 */
public class BINOLMOWAT06_Form extends DataTable_BaseForm {

	/** 开始时间 */
	private String putTimeStart;
	
	/** 结束时间 */
	private String putTimeEnd;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 单据号 */
	private String tradeNoIF;
	
	/** 错误类型 */
	private String errType;
	
	/** 错误信息 */
	private String errInfo;
	
	/** 消息体 */
	private String messageBody;
	
	/** MongoDB日志ID */
	private String[] id;

	public String getPutTimeStart() {
		return putTimeStart;
	}

	public void setPutTimeStart(String putTimeStart) {
		this.putTimeStart = putTimeStart;
	}

	public String getPutTimeEnd() {
		return putTimeEnd;
	}

	public void setPutTimeEnd(String putTimeEnd) {
		this.putTimeEnd = putTimeEnd;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeNoIF() {
		return tradeNoIF;
	}

	public void setTradeNoIF(String tradeNoIF) {
		this.tradeNoIF = tradeNoIF;
	}

	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String[] getId() {
		return id;
	}

	public void setId(String[] id) {
		this.id = id;
	}
}
