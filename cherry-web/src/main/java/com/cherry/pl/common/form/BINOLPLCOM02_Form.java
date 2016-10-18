/*	
 * @(#)BINOLPLCOM02_Form.java     1.0 2011/12/20		
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
package com.cherry.pl.common.form;

import java.util.List;
import java.util.Map;

/**
 * 工作流向导共通 Form
 * 
 * @author niushunjie
 * @version 1.0 2011.12.20
 */
public class BINOLPLCOM02_Form {   
    /** 品牌信息ID */
    private String brandInfoId;
    
    /** 当前导航条所在步骤 */
    private String onStep;

    /**工作流参数*/
    private String workflowParam;
    
    /**工作流文件名称*/
    private String flowType;
    
    /**工作流ID*/
    private String workFlowId;
    
    /**动作Id*/
    private String actionId;
    
    /**向导当前步骤*/
    private int stepId;
    
    /**业务工作流当前步骤*/
    private int[] ruleOnStep;
    
    /**业务工作流文件名*/
    private String ruleOnFlowCode;
    
    /**第三方标志，只有当1时为第三方*/
    private String thirdPartyFlag;
    
    /**隐藏第三方标志*/
    private String hideThirdParty;
    
    /**允许编辑标志 true false*/
    private String canEditFlag;
    
    /**隐藏允许编辑标志 true false*/
    private String hideCanEditFlag;
    
    /**允许判断优先级标志 true false*/
    private String preferredFlag;
    
    /**隐藏允许判断优先级标志 true false*/
    private String hidePreferredFlag;
    
    /**允许自动审核标志 true false*/
    private String autoAuditFlag;
    
    /**隐藏允许自动审核标志 true false*/
    private String hideAutoAuditFlag;
    
    /**规则类型 1无规则 2简单模式 3复杂模式(审核)*/
    private String ruleType;
    
    /**json格式的规则*/
    private String ruleParams;
    
    /**当前步骤可执行actionId*/
    private int[] ruleOnAction;
    
    /**发起者身份类型*/
    private String[] roleTypeCreater;
    
    /**发起者*/
    private String[] roleValueCreater;
    
    /**审核者身份类型*/
    private String[] roleTypeAuditor;
    
    /**审核者*/
    private String[] roleValueAuditor;
    
    /**执行者身份类型*/
    private String[] roleTypeActor;
    
    /**执行者*/
    private String[] roleValueActor;
    
    /**接收者身份类型*/
    private String[] roleTypeReceiver;
    
    /**接收者*/
    private String[] roleValueReceiver;
    
    /**确认者身份类型*/
    private String[] roleTypeConfirmation;
    
    /**确认者*/
    private String[] roleValueConfirmation;
    
    /**确认者与接收者的权限范围*/
    private String[] rolePrivilegeRecFlag;
    
    /**权限范围*/
    private String[] rolePrivilegeFlag;
    
    /**导航条*/
    private List<Map<String,Object>> topStepsList;
    
    /**按钮区*/
    private List<Map<String,Object>> buttonList;
    
    /**规则设置区*/
    private Map<String,Object> ruleMap;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getOnStep() {
        return onStep;
    }

    public void setOnStep(String onStep) {
        this.onStep = onStep;
    }

    public String getWorkflowParam() {
        return workflowParam;
    }

    public void setWorkflowParam(String workflowParam) {
        this.workflowParam = workflowParam;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public int[] getRuleOnStep() {
        return ruleOnStep;
    }

    public void setRuleOnStep(int[] ruleOnStep) {
        this.ruleOnStep = ruleOnStep;
    }

    public String getRuleOnFlowCode() {
        return ruleOnFlowCode;
    }

    public void setRuleOnFlowCode(String ruleOnFlowCode) {
        this.ruleOnFlowCode = ruleOnFlowCode;
    }

    public String getThirdPartyFlag() {
        return thirdPartyFlag;
    }

    public void setThirdPartyFlag(String thirdPartyFlag) {
        this.thirdPartyFlag = thirdPartyFlag;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public int[] getRuleOnAction() {
        return ruleOnAction;
    }

    public void setRuleOnAction(int[] ruleOnAction) {
        this.ruleOnAction = ruleOnAction;
    }

    public String[] getRoleTypeCreater() {
        return roleTypeCreater;
    }

    public void setRoleTypeCreater(String[] roleTypeCreater) {
        this.roleTypeCreater = roleTypeCreater;
    }

    public String[] getRoleValueCreater() {
        return roleValueCreater;
    }

    public void setRoleValueCreater(String[] roleValueCreater) {
        this.roleValueCreater = roleValueCreater;
    }

    public String[] getRoleTypeAuditor() {
        return roleTypeAuditor;
    }

    public void setRoleTypeAuditor(String[] roleTypeAuditor) {
        this.roleTypeAuditor = roleTypeAuditor;
    }

    public String[] getRoleValueAuditor() {
        return roleValueAuditor;
    }

    public void setRoleValueAuditor(String[] roleValueAuditor) {
        this.roleValueAuditor = roleValueAuditor;
    }

    public String[] getRoleTypeActor() {
        return roleTypeActor;
    }

    public void setRoleTypeActor(String[] roleTypeActor) {
        this.roleTypeActor = roleTypeActor;
    }

    public String[] getRoleValueActor() {
        return roleValueActor;
    }

    public void setRoleValueActor(String[] roleValueActor) {
        this.roleValueActor = roleValueActor;
    }

    public List<Map<String, Object>> getTopStepsList() {
        return topStepsList;
    }

    public void setTopStepsList(List<Map<String, Object>> topStepsList) {
        this.topStepsList = topStepsList;
    }

    public List<Map<String, Object>> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<Map<String, Object>> buttonList) {
        this.buttonList = buttonList;
    }

    public Map<String, Object> getRuleMap() {
        return ruleMap;
    }

    public void setRuleMap(Map<String, Object> ruleMap) {
        this.ruleMap = ruleMap;
    }

    public String getRuleParams() {
        return ruleParams;
    }

    public void setRuleParams(String ruleParams) {
        this.ruleParams = ruleParams;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getHideThirdParty() {
        return hideThirdParty;
    }

    public void setHideThirdParty(String hideThirdParty) {
        this.hideThirdParty = hideThirdParty;
    }

    public String[] getRolePrivilegeFlag() {
        return rolePrivilegeFlag;
    }

    public void setRolePrivilegeFlag(String[] rolePrivilegeFlag) {
        this.rolePrivilegeFlag = rolePrivilegeFlag;
    }

    public String getCanEditFlag() {
        return canEditFlag;
    }

    public void setCanEditFlag(String canEditFlag) {
        this.canEditFlag = canEditFlag;
    }

    public String getHideCanEditFlag() {
        return hideCanEditFlag;
    }

    public void setHideCanEditFlag(String hideCanEditFlag) {
        this.hideCanEditFlag = hideCanEditFlag;
    }

    public String getPreferredFlag() {
        return preferredFlag;
    }

    public void setPreferredFlag(String preferredFlag) {
        this.preferredFlag = preferredFlag;
    }

    public String getHidePreferredFlag() {
        return hidePreferredFlag;
    }

    public void setHidePreferredFlag(String hidePreferredFlag) {
        this.hidePreferredFlag = hidePreferredFlag;
    }

    public String getAutoAuditFlag() {
        return autoAuditFlag;
    }

    public void setAutoAuditFlag(String autoAuditFlag) {
        this.autoAuditFlag = autoAuditFlag;
    }

    public String getHideAutoAuditFlag() {
        return hideAutoAuditFlag;
    }

    public void setHideAutoAuditFlag(String hideAutoAuditFlag) {
        this.hideAutoAuditFlag = hideAutoAuditFlag;
    }

	public String[] getRoleTypeReceiver() {
		return roleTypeReceiver;
	}

	public void setRoleTypeReceiver(String[] roleTypeReceiver) {
		this.roleTypeReceiver = roleTypeReceiver;
	}

	public String[] getRoleValueReceiver() {
		return roleValueReceiver;
	}

	public void setRoleValueReceiver(String[] roleValueReceiver) {
		this.roleValueReceiver = roleValueReceiver;
	}

	public String[] getRoleTypeConfirmation() {
		return roleTypeConfirmation;
	}

	public void setRoleTypeConfirmation(String[] roleTypeConfirmation) {
		this.roleTypeConfirmation = roleTypeConfirmation;
	}

	public String[] getRoleValueConfirmation() {
		return roleValueConfirmation;
	}

	public void setRoleValueConfirmation(String[] roleValueConfirmation) {
		this.roleValueConfirmation = roleValueConfirmation;
	}

	public String[] getRolePrivilegeRecFlag() {
		return rolePrivilegeRecFlag;
	}

	public void setRolePrivilegeRecFlag(String[] rolePrivilegeRecFlag) {
		this.rolePrivilegeRecFlag = rolePrivilegeRecFlag;
	}
    
}
