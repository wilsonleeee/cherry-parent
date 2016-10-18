/*  
 * @(#)BINOLMOMAN06_Form.java    1.0 2011-7-28     
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

package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOMAN06_Form extends DataTable_BaseForm {

	//U盘SN号
	private String udiskSn;
	//U盘ID号
	private String udiskInfoId;
	//品牌ID
	private String brandInfoId;
	//员工名称
	private String employeeName;
	//员工Code
	private String employeeCode;
	//权限级别
	private String ownerRight;
	//由U盘ID拼成的JSON格式的String
	private String udiskIdStr;

	public String getUdiskSn() {
		return udiskSn;
	}

	public void setUdiskSn(String udiskSn) {
		this.udiskSn = udiskSn;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getOwnerRight() {
		return ownerRight;
	}

	public void setOwnerRight(String ownerRight) {
		this.ownerRight = ownerRight;
	}

	public String getUdiskInfoId() {
		return udiskInfoId;
	}

	public void setUdiskInfoId(String udiskInfoId) {
		this.udiskInfoId = udiskInfoId;
	}

	public String getUdiskIdStr() {
		return udiskIdStr;
	}

	public void setUdiskIdStr(String udiskIdStr) {
		this.udiskIdStr = udiskIdStr;
	}
	
	
}
