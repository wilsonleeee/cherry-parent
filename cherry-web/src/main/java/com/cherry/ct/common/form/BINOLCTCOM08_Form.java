package com.cherry.ct.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLCTCOM08_Form extends DataTable_BaseForm{
	/** 模板名称  */
	private String templateName;
	
	/** 模板用途（比如：入会致谢、生日祝福、预约成功提示等）  */
	private String templateUse;
	
	/** 模板类型 （比如：短信、邮件、彩信等） */
	private String messageType;
	
	/** 适用客户类型（比如：会员、员工等）  */
	private String customerType;
	
	/** 模板状态  */
	private String status;

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

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
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
