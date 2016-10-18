/*
 * @(#)BINOLMOMAN01_Form.java     1.0 2011/3/15
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
/**
 * 
 * 机器查询Form
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
public class BINOLMOMAN01_Form extends DataTable_BaseForm{
    /** 机器ID */
    private String machineInfoId;
    
    /** 组织ID */
    private String organizationInfoId;
    
    /** 所属品牌ID */
    private String brandInfoId;
    
    /** 机器编号 */
    private String machineCode;
    
    /** MAC地址*/
    private String mobileMacAddress;
    
    /** 机器版本 */
    private String machineType;
    
    /** 硬件版本 */
    private String hardWareVersion;
    
    /** 软件版本 */
    private String softWareVersion;
    
    /** 机器状态 */
    private String machineStatus;
    
    /** 机器启用的时间 */
    private String startTime;

    /** 绑定状态 */
    private String bindStatus;
    
    /** 有效区分 */
    private String validFlag;
    
    /** 柜台名称*/
    private String CounterNameIF;

    /** 责任人 */
    private String employeeName;
    
    /** 容量 */
    private String capacity;
    
    /** 老机器号 */
    private String machineCodeOld;
    
    /** 通讯卡号 */
    private String phoneCode;
    
    /** 新机器号数组 */
    private String[] machineCodeArr;
    
    /** 老机器号数组*/
    private String[] machineCodeOldArr;
    
    /** 机器类型数组*/
    private String[] machineTypeArr;
    
    /** 手机卡号数组 */
    private String[] phoneCodeArr;
    
    /**柜台编号或名称*/
    private String counterCodeName;
    
    public String getMachineInfoId() {
        return machineInfoId;
    }

    public void setMachineInfoId(String machineInfoId) {
        this.machineInfoId = machineInfoId;
    }
    
    public String getOrganizationInfoId() {
        return organizationInfoId;
    }

    public void setOrganizationInfoId(String organizationInfoId) {
        this.organizationInfoId = organizationInfoId;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getHardWareVersion() {
        return hardWareVersion;
    }

    public void setHardWareVersion(String hardWareVersion) {
        this.hardWareVersion = hardWareVersion;
    }

    public String getSoftWareVersion() {
        return softWareVersion;
    }

    public void setSoftWareVersion(String softWareVersion) {
        this.softWareVersion = softWareVersion;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getCounterNameIF() {
        return CounterNameIF;
    }

    public void setCounterNameIF(String counterNameIF) {
        CounterNameIF = counterNameIF;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getMachineCodeOld() {
        return machineCodeOld;
    }

    public void setMachineCodeOld(String machineCodeOld) {
        this.machineCodeOld = machineCodeOld;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public void setMachineCodeArr(String[] machineCodeArr) {
        this.machineCodeArr = machineCodeArr;
    }

    public String[] getMachineCodeArr() {
        return machineCodeArr;
    }

    public void setMachineCodeOldArr(String[] machineCodeOldArr) {
        this.machineCodeOldArr = machineCodeOldArr;
    }

    public String[] getMachineCodeOldArr() {
        return machineCodeOldArr;
    }

    public String getCounterCodeName() {
        return counterCodeName;
    }

    public void setCounterCodeName(String counterCodeName) {
        this.counterCodeName = counterCodeName;
    }

	public String[] getMachineTypeArr() {
		return machineTypeArr;
	}

	public void setMachineTypeArr(String[] machineTypeArr) {
		this.machineTypeArr = machineTypeArr;
	}

	public String[] getPhoneCodeArr() {
		return phoneCodeArr;
	}

	public void setPhoneCodeArr(String[] phoneCodeArr) {
		this.phoneCodeArr = phoneCodeArr;
	}

	public String getMobileMacAddress() {
		return mobileMacAddress;
	}

	public void setMobileMacAddress(String mobileMacAddress) {
		this.mobileMacAddress = mobileMacAddress;
	}
    
}
