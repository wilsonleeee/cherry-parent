/*
 * @(#)BINOLCTTPL03_Form.java     1.0 2013/10/08
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
package com.cherry.ct.tpl.form;

/**
 * 沟通模板管理Form
 * 
 * @author ZhangLe
 * @version 1.0 2013.10.08
 */
public class BINOLCTTPL03_Form {

	/** 品牌信息ID */
	private String brandInfoId;
	/** 参数Id */
	private String associateId;
	/** 模板用途编号 */
	private String templateUse;
	/** 参数状态 */
	private String validFlag;
	/** 参数名称 */
	private String variableName;
	/** 参数代码 */
	private String variableCode;
	/** 备注*/
	private String comments;
	/** 基础变量*/
	private String basicVariable;
	/** 运算符*/
	private String operatorChar;
	/**运算值*/
	private String computedValue;
	/**变量类型*/
	private String type;
	/**查询方式 （1:查询基础参数 2：按功能查询）*/
	private String queryType;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getTemplateUse() {
		return templateUse;
	}

	public void setTemplateUse(String templateUse) {
		this.templateUse = templateUse;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableCode() {
		return variableCode;
	}

	public void setVariableCode(String variableCode) {
		this.variableCode = variableCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBasicVariable() {
		return basicVariable;
	}

	public void setBasicVariable(String basicVariable) {
		this.basicVariable = basicVariable;
	}

	public String getOperatorChar() {
		return operatorChar;
	}

	public void setOperatorChar(String operatorChar) {
		this.operatorChar = operatorChar;
	}

	public String getComputedValue() {
		return computedValue;
	}

	public void setComputedValue(String computedValue) {
		this.computedValue = computedValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
}
