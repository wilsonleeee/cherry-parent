package com.cherry.mo.buy.form;

import com.cherry.cm.form.DataTable_BaseForm;

/*  
 * @(#)BINOLMOBUY01_Form.java    1.0 2012-5-28     
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
public class BINOLMOBUY01_Form extends DataTable_BaseForm {

	/** 品牌ID */
    private String brandInfoId;
    
    /** U盘序列号 */
    private String udiskSN;
    
    /** 员工姓名 */
    private String employeeName;
    
    /** 岗位类别 */
    private String positionCategoryId;
    
    /** 考勤日期开始 */
    private String startAttendanceDate;
    
    /** 考勤日期结束 */
    private String endAttendanceDate;
    
    /** 考勤统计  城市ID*/
    private String cityId;
    
    /** 考勤统计  关键时间段开始时间*/
    private String[] importTimeStartArr;
    
    /** 考勤统计  关键时间段结束时间*/
    private String[] importTimeEndArr;
    
    /** 考勤统计  大区部门ID*/
    private String reginDepartID;
    
    /** 考勤统计  选中的渠道ID*/
    private String[] checkedChannelArr; 
    
    /** 考勤信息统计  选中渠道区分*/
    private String channelFlag;
    
    /** 员工有效区分*/
    private String empValidFlag;

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
    
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String[] getImportTimeStartArr() {
		return importTimeStartArr;
	}

	public void setImportTimeStartArr(String[] importTimeStartArr) {
		this.importTimeStartArr = importTimeStartArr;
	}

	public String[] getImportTimeEndArr() {
		return importTimeEndArr;
	}

	public void setImportTimeEndArr(String[] importTimeEndArr) {
		this.importTimeEndArr = importTimeEndArr;
	}

	public String getReginDepartID() {
		return reginDepartID;
	}

	public void setReginDepartID(String reginDepartID) {
		this.reginDepartID = reginDepartID;
	}

	public String[] getCheckedChannelArr() {
		return checkedChannelArr;
	}

	public void setCheckedChannelArr(String[] checkedChannelArr) {
		this.checkedChannelArr = checkedChannelArr;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	
	public String getEmpValidFlag() {
		return empValidFlag;
	}

	public void setEmpValidFlag(String empValidFlag) {
		this.empValidFlag = empValidFlag;
	}
}
