/*		
 * @(#)BINOLPTRPS27_Form.java     1.0.0 2013/08/08		
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 进销存统计查询form
 * 
 * @author zhangle	
 * @version 1.0.0 2013.08.08
 * 
 */
public class BINOLPTRPS27_Form extends BINOLCM13_Form{
	private String startTime;
	private String endTime;
	private String schedulesId;
	private String runTime;
	private String parameterType;
	private String parameterData[];
	private String validFlag;
	private String dtParameter;
	private String dpParameter;
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
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getSchedulesId() {
		return schedulesId;
	}
	public void setSchedulesId(String schedulesId) {
		this.schedulesId = schedulesId;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String[] getParameterData() {
		return parameterData;
	}
	public void setParameterData(String[] parameterData) {
		this.parameterData = parameterData;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getDtParameter() {
		return dtParameter;
	}
	public void setDtParameter(String dtParameter) {
		this.dtParameter = dtParameter;
	}
	public String getDpParameter() {
		return dpParameter;
	}
	public void setDpParameter(String dpParameter) {
		this.dpParameter = dpParameter;
	}


	
}
