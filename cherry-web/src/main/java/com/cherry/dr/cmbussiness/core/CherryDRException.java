/*		
 * @(#)CherryMQException.java     1.0 2011/09/15		
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
package com.cherry.dr.cmbussiness.core;


/**
 * 规则执行异常
 * 
 * @author hub
 * @version 1.0 2011.09.15
 */
public class CherryDRException extends Exception{
	
	private static final long serialVersionUID = -3577336284754324443L;
	
	/** 错误信息 */
	private String errMsg;
	
	/** 错误类型 */
	private int errType;
		
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public int getErrType() {
		return errType;
	}
	public void setErrType(int errType) {
		this.errType = errType;
	}
	
	public CherryDRException (String errMsg) {
		super(errMsg);
		this.errMsg = errMsg;
	}
	
	public CherryDRException (String errMsg, int errType) {
		super(errMsg);
		this.errMsg = errMsg;
		this.errType = errType;
	}
}
