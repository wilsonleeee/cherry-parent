/*		
 * @(#)BINOLSSPRM31_Form.java     1.0 2010/11/04		
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
package com.cherry.ss.prm.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 库存查询form
 * 
 * @author lipc
 * @version 1.0 2010.11.04
 */
public class BINOLSSPRM31_Form extends BINOLCM13_Form{
	
	/** 促销产品厂商编码 */
	private String unitCode;
	
	/** 促销产品名称 */
	private String nameTotal;
	
	/** 促销产品条码 */
	private String barCode;
	
	/** 促销产品状态 */
	private String validFlag;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;
	
	/**促销产品厂商ID*/
    private String prmVendorId;

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
