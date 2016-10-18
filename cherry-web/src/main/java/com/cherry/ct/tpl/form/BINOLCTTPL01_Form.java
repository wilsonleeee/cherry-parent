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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 沟通模板一览Form
 * 
 * @author ZhangGS
 * @version 1.0 2012.11.06
 */
public class BINOLCTTPL01_Form  extends DataTable_BaseForm{
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 品牌信息ID */
	private String templateCode;
	
	/** 模板名称  */
	private String templateName;
	
	/** 模板用途（比如：入会致谢、生日祝福、预约成功提示等）  */
	private String templateUse;
	
	/** 模板类型 （比如：短信、邮件、彩信等） */
	private String templateType;
	
	/** 适用客户类型（比如：会员、员工等）  */
	private String customerType;
	
	/** 模板状态  */
	private String status;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandid) {
		this.brandInfoId = brandid;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
