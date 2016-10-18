/*
 * @(#)BINOLMOWAT01_Form.java     1.0 2011/4/27
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

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 
 * 终端实时监控Form
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
public class BINOLMOWAT01_Form extends DataTable_BaseForm{
    /** 品牌ID */
    private String brandInfoId;
    
    /**品牌Code*/
    private String brandCode;
    
    /** 最后联络时差（分） */
    private String dateDiff;

    /**搜索类型(按分钟或按天)*/
    private String searchType;
    
    /**未联网天数*/
    private String nuberOfDays;
    
    /** 节日 */
    private String holidays;
    
    /**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /** 机器编号 */
    private String machineCode;
    
    /**柜台编号或名称*/
    private String counterCodeName;
    
    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setDateDiff(String dateDiff) {
        this.dateDiff = dateDiff;
    }

    public String getDateDiff() {
        return dateDiff;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getNuberOfDays() {
        return nuberOfDays;
    }

    public void setNuberOfDays(String nuberOfDays) {
        this.nuberOfDays = nuberOfDays;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getCounterCodeName() {
        return counterCodeName;
    }

    public void setCounterCodeName(String counterCodeName) {
        this.counterCodeName = counterCodeName;
    }
}
