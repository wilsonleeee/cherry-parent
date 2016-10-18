/*  
 * @(#)BINOLCM02_Action.java     1.0 2013/01/16      
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 沟通模块共通Form
 * @author ZhangGS
 *
 */
public class BINOLCM32_Form extends DataTable_BaseForm{
	/** 品牌信息ID */
	private String brandInfoId;
	
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
	
	/** 弹出框 沟通模板信息 **/
	private List<Map<String, Object>> popMsgTemplateList;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
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

	public List<Map<String, Object>> getPopMsgTemplateList() {
		return popMsgTemplateList;
	}

	public void setPopMsgTemplateList(List<Map<String, Object>> popMsgTemplateList) {
		this.popMsgTemplateList = popMsgTemplateList;
	}
	
	
}
