/*  
 * @(#)BINOLCM07_Form.java     1.0 2011/05/31      
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
 * 促销产品弹出table共通Form
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM07_Form extends DataTable_BaseForm{
	/** 部门id*/
	private String organizationID;
	
	/** 柜台List */
	private List<Map<String, Object>> counterInfoList;
	
	/** 柜台ID */
	private String counterInfoId;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 厂商List */
	private List factoryList;
	
	/** 生产厂商ID */
	private String[] manuFactId;
	
	/** 部门ID(办事处或柜台) */
	private String[] departInfoId;
	
	/** 部门List(办事处或柜台) */
	private List departInfoList;

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public List<Map<String, Object>> getCounterInfoList() {
		return counterInfoList;
	}

	public void setCounterInfoList(List<Map<String, Object>> counterInfoList) {
		this.counterInfoList = counterInfoList;
	}

	public String getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(String counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List getFactoryList() {
		return factoryList;
	}

	public void setFactoryList(List factoryList) {
		this.factoryList = factoryList;
	}

	public String[] getManuFactId() {
		return manuFactId;
	}

	public void setManuFactId(String[] manuFactId) {
		this.manuFactId = manuFactId;
	}

	public String[] getDepartInfoId() {
		return departInfoId;
	}

	public void setDepartInfoId(String[] departInfoId) {
		this.departInfoId = departInfoId;
	}

	public List getDepartInfoList() {
		return departInfoList;
	}

	public void setDepartInfoList(List departInfoList) {
		this.departInfoList = departInfoList;
	}
}
