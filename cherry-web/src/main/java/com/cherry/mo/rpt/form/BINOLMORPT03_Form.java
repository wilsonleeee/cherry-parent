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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 答卷一览Form
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public class BINOLMORPT03_Form extends BINOLCM13_Form {
	
	/** 问卷名称 **/
	private String paperName;
	
	/** 答卷开始时间 **/
	private String checkDateStart;
	
	/** 答卷结束时间 **/
	private String checkDateEnd;
	
	/** 柜台(已改为用部门) **/
//	private String departName;
	
	/** 回答人员(未使用此字段) **/
//	private String employeeName;
	
	/** 问卷ID **/
	private String paperId;
	
	/** 问卷类型 **/
	private String paperType;
	
	/** 是否显示所有答卷 */
	private String showMode;

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

//	public String getDepartName() {
//		return departName;
//	}
//
//	public void setDepartName(String departName) {
//		this.departName = departName;
//	}

//	public String getEmployeeName() {
//		return employeeName;
//	}
//
//	public void setEmployeeName(String employeeName) {
//		this.employeeName = employeeName;
//	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

}
