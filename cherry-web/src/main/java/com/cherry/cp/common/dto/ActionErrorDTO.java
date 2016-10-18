/*	
 * @(#)ActionErrorDTO.java     1.0 2011/11/01		
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
package com.cherry.cp.common.dto;

/**
 * 错误信息共通 DTO
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class ActionErrorDTO {
	
	/** 错误级别 */
	private int errorLevel;
	
	/** 字段名称 */
	private String fieldName;
	
	/** 错误信息代码 */
	private String errMsgCode;
	
	/** 错误信息参数 */
	private String[] errMsgParams;
	
	public ActionErrorDTO(int errorLevel, String fieldName, String errMsgCode, String[] errMsgParams) {
		this.errorLevel = errorLevel;
		this.fieldName = fieldName;
		this.errMsgCode = errMsgCode;
		this.errMsgParams = errMsgParams;
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrMsgCode() {
		return errMsgCode;
	}

	public void setErrMsgCode(String errMsgCode) {
		this.errMsgCode = errMsgCode;
	}

	public String[] getErrMsgParams() {
		return errMsgParams;
	}

	public void setErrMsgParams(String[] errMsgParams) {
		this.errMsgParams = errMsgParams;
	}
}
