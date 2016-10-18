/*
 * @(#)BINOLMOWAT09_Form.java     1.0 2014/12/17
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
 * BA考勤信息查询Form
 * 
 * @author niushunjie
 * @version 1.0 2014.12.17
 */
public class BINOLMOWAT09_Form extends BINOLCM13_Form{
    /** 品牌ID */
    private String brandInfoId;
    
    /** BA编号 */
    private String baCode;
    
    /** BA姓名*/
    private String baName;
    
    /** 考勤日期开始 */
    private String startAttendanceDate;
    
    /** 考勤日期结束 */
    private String endAttendanceDate;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBaCode() {
        return baCode;
    }

    public void setBaCode(String baCode) {
        this.baCode = baCode;
    }

    public String getBaName() {
        return baName;
    }

    public void setBaName(String baName) {
        this.baName = baName;
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
}