/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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
 * @author ZhangGS
 * @version 1.0 2012.11.06
 */
public class BINOLCTTPL02_Form{
	/** 模板编号 */
	private String templateCode;
	
	/** 模板名称 */
	private String templateName;
	
	/** 模板用途编号 */
	private String templateUse;
	
	/** 模板类型 */
	private String templateType;
	
	/** 适用客户类型 */
	private String customerType;
	
	/** 模板内容 */
	private String msgContents;
	
	/** 显示类型 */
	private String showType;
	
	/** 默认签名 */
	private String signature;

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getTemplateUse() {
		return templateUse;
	}

	public void setTemplateUse(String templateUse) {
		this.templateUse = templateUse;
	}
	
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getMsgContents() {
		return msgContents;
	}

	public void setMsgContents(String msgContents) {
		this.msgContents = msgContents;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
