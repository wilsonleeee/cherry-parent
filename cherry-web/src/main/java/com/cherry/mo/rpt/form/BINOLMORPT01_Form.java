/*  
 * @(#)BINOLMORPT01_Form.java     1.0 2011.10.21  
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
package com.cherry.mo.rpt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 考核答卷一览Form
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT01_Form extends DataTable_BaseForm {
	
	/** 问卷名称 **/
	private String paperName;
	
	/** 考核开始时间 **/
	private String checkDateStart;
	
	/** 考核结束时间 **/
	private String checkDateEnd;
	
	/** 柜台 **/
	private String departName;
	
	/** 回答人员 **/
	private String employeeName;
	
	/** 考核问卷ID **/
	private String checkPaperId;

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public String getCheckDateStart() {
		return checkDateStart;
	}

	public void setCheckDateStart(String checkDateStart) {
		this.checkDateStart = checkDateStart;
	}

	public String getCheckDateEnd() {
		return checkDateEnd;
	}

	public void setCheckDateEnd(String checkDateEnd) {
		this.checkDateEnd = checkDateEnd;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCheckPaperId() {
		return checkPaperId;
	}

	public void setCheckPaperId(String checkPaperId) {
		this.checkPaperId = checkPaperId;
	}

}
