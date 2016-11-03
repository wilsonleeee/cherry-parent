/*
 * @(#)BatchExceptionDTO.java     1.0 2010/11/12
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
package com.cherry.cm.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *共通Batch 异常 DTO
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.12
 */
public class BatchExceptionDTO {

	/** batch程序名 */
	private Class<?> batchName;

	/** 资源文件定义的code */
	private String errorCode;

	/** 参数 */
	private List<String> errorParamsList;

	/** 等级 */
	private int errorLevel;

	/** 异常 */
	private Exception exception;

	public BatchExceptionDTO() {
		this.errorParamsList = new ArrayList<String>();
	}

	public BatchExceptionDTO(Class<?> batchName, String errorCode,
			List<String> errorParamsList, int errorLevel, Exception e) {
		this.setBatchName(batchName);
		this.setErrorCode(errorCode);
		this.setErrorParamsList(errorParamsList);
		this.setErrorLevel(errorLevel);
		this.setException(e);
	}
	
	public Class<?> getBatchName() {
		return batchName;
	}

	public void setBatchName(Class<?> batchName) {
		this.batchName = batchName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<String> getErrorParamsList() {
		return errorParamsList;
	}

	public void setErrorParamsList(List<String> errorParamsList) {
		this.errorParamsList = errorParamsList;
	}
	
	public void addErrorParam(String errorParam) {
		if (this.errorParamsList != null) {
			this.errorParamsList.add(errorParam);
		}
	}
	
	public int getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public void clear() {
		this.batchName = null;
		this.errorCode = null;
		this.errorLevel = 0;
		this.exception = null;
		if (errorParamsList != null) {
			errorParamsList.clear();
		}
	}
}
