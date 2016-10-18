/*
 * @(#)BINOLMOWAT05_Form.java     1.0 2011/8/1
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 考勤信息查询Form
 * 
 * @author niushunjie
 * @version 1.0 2011.8.1
 */
public class BINOLMOWAT05_Form extends BINOLCM13_Form{
    /** 品牌ID */
    private String brandInfoId;
    
    /** U盘序列号 */
    private String udiskSN;
    
    /** 员工姓名 */
    private String employeeName;
    
    /** 员工ID*/
    private String employeeId;
    
    /** 岗位类别 */
    private String positionCategoryId;
    
    /** 考勤日期开始 */
    private String startAttendanceDate;
    
    /** 考勤日期结束 */
    private String endAttendanceDate;
    
    /**主页面一览明细导出标识*/
    private String flag;
    
    /**是否包含测试柜台*/
    private String testFlag;
    
    /**是否包含停用部门*/
    private String validFlag;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getUdiskSN() {
        return udiskSN;
    }

    public void setUdiskSN(String udiskSN) {
        this.udiskSN = udiskSN;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPositionCategoryId() {
        return positionCategoryId;
    }

    public void setPositionCategoryId(String positionCategoryId) {
        this.positionCategoryId = positionCategoryId;
    }

    public String getStartAttendanceDate() {
        return startAttendanceDate;
    }

    public void setStartAttendanceDate(String startAttendanceDate) {
        this.startAttendanceDate = startAttendanceDate;
    }

    public String getEndAttendanceDate() {
        return endAttendanceDate;
    }

    public void setEndAttendanceDate(String endAttendanceDate) {
        this.endAttendanceDate = endAttendanceDate;
    }

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTestFlag() {
		return testFlag;
	}

	public void setTestFlag(String testFlag) {
		this.testFlag = testFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
