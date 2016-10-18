/*	
 * @(#)BINOLPLSCF17_Form.java     1.0 2013/08/27	
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
package com.cherry.pl.scf.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 业务处理器管理画面Form
 * 
 * @author hub
 * @version 1.0 2013/08/27
 */
public class BINOLPLSCF17_Form extends DataTable_BaseForm{
	
	/** 品牌list */
	private List<Map<String, Object>> brandInfoList;
	
	/** 所属品牌代码 */
	private String brandCode;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 处理器描述 */
	private String handlerDept;
	
	/** 处理器名称 */
	private String handNameCN;
	
	/** 处理器ID */
	private String upInfo;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 业务处理器List */
	private List<Map<String, Object>> handlerList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getHandlerDept() {
		return handlerDept;
	}

	public void setHandlerDept(String handlerDept) {
		this.handlerDept = handlerDept;
	}

	public List<Map<String, Object>> getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(List<Map<String, Object>> handlerList) {
		this.handlerList = handlerList;
	}

	public String getHandNameCN() {
		return handNameCN;
	}

	public void setHandNameCN(String handNameCN) {
		this.handNameCN = handNameCN;
	}
	
	public String getUpInfo() {
		return upInfo;
	}

	public void setUpInfo(String upInfo) {
		this.upInfo = upInfo;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
