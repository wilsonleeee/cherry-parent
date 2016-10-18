/*
 * @(#)BINOLMOMAN03_Form.java     1.0 2011/3/21
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;
/**
 * 
 * 绑定机器Form
 * 
 * @author niushunjie
 * @version 1.0 2011.3.21
 */
public class BINOLMOMAN03_Form extends DataTable_BaseForm{
    /** 机器ID */
    private String machineInfoId;
   
    /** 品牌ID */
    private String brandInfoId;
    
    /** 柜台ID*/
    private String counterInfoId;

    /** 柜台List */
    private List<Map<String, Object>> counterInfoList;
    
    /** 新机器编号 */
    private String machineCode;
    
    /** 老机器编号 */
    private String machineCodeOld;
    
    /** 柜台编号*/
    private String counterCode;
    
    public String getMachineInfoId() {
        return machineInfoId;
    }

    public void setMachineInfoId(String machineInfoId) {
        this.machineInfoId = machineInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public String getCounterInfoId() {
        return counterInfoId;
    }

    public void setCounterInfoId(String counterInfoId) {
        this.counterInfoId = counterInfoId;
    }

    public void setCounterInfoList(List<Map<String, Object>> counterInfoList) {
        this.counterInfoList = counterInfoList;
    }

    public List<Map<String, Object>> getCounterInfoList() {
        return counterInfoList;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineCodeOld() {
        return machineCodeOld;
    }

    public void setMachineCodeOld(String machineCodeOld) {
        this.machineCodeOld = machineCodeOld;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getCounterCode() {
        return counterCode;
    }

}
