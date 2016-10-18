/*
 * @(#)BINOLPTJCS43_Form.java     1.0 2015/10/13
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
package com.cherry.pt.jcs.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 产品关联维护Form
 * 
 * @author Hujh
 * @version 1.0 2015.10.13
 */
public class BINOLPTJCS43_Form extends DataTable_BaseForm{
	
	private List<Map<String, Object>> prtList = new ArrayList<Map<String, Object>>();
	
	private List<Map<String, Object>> detailPrtList = new ArrayList<Map<String, Object>>();
		
	/**分组ID**/
	private String groupId;
	/** 产品厂商ID */
	private String prtVendorId;
	/** 产品名称 */
	private String nameTotal;
	/** 厂商编码 */
	private String unitCode;
	/** 产品条码 */
	private String barCode;
	/** 产品类型 */
	private String mode;
	/**有效区分*/
	private String validFlag;
	
	public String getNameTotal() {
		return nameTotal;
	}
	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getPrtVendorId() {
		return prtVendorId;
	}
	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}
	public List<Map<String, Object>> getPrtList() {
		return prtList;
	}
	public void setPrtList(List<Map<String, Object>> prtList) {
		this.prtList = prtList;
	}
	public List<Map<String, Object>> getDetailPrtList() {
		return detailPrtList;
	}
	public void setDetailPrtList(List<Map<String, Object>> detailPrtList) {
		this.detailPrtList = detailPrtList;
	}
	
}