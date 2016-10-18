/*  
 * @(#)BINOLCM26_Form.java     1.0.0 2011/12/09   
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
package com.cherry.cm.cmbussiness.form;

/**
 * 工作流修改审核人Form
 * 
 * @author niushunjie
 * @version 1.0.0 2011.12.09
 */
public class BINOLCM26_Form {

	//工作流ID
	private long workFlowId;

	//单据号
	private String billId;
	
	//审核者身份类型
	private String auditorType;
	
	//审核者
    private String auditorID;
    
    //品牌ID
    private String brandInfoId;
    
    //业务类型
    private String bussinessType;
    
    //当前操作
    private String currentOperateName;
    
    //岗位权限（关注/管辖）标志
    private String privilegeFlag;
    
    //规则类型
    private String ruleType;

    public long getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(long workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAuditorType() {
        return auditorType;
    }

    public void setAuditorType(String auditorType) {
        this.auditorType = auditorType;
    }

    public String getAuditorID() {
        return auditorID;
    }

    public void setAuditorID(String auditorID) {
        this.auditorID = auditorID;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(String bussinessType) {
        this.bussinessType = bussinessType;
    }

    public String getCurrentOperateName() {
        return currentOperateName;
    }

    public void setCurrentOperateName(String currentOperateName) {
        this.currentOperateName = currentOperateName;
    }

    public String getPrivilegeFlag() {
        return privilegeFlag;
    }

    public void setPrivilegeFlag(String privilegeFlag) {
        this.privilegeFlag = privilegeFlag;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

}
