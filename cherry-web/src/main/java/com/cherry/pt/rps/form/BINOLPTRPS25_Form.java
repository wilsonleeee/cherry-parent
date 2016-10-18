/*		
 * @(#)BINOLPTRPS25_Form.java     1.0.0 2011/10/17		
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 产品在途库存查询form
 * 
 * @author lipc
 * @version 1.0.0 2011.10.17
 * 
 */
public class BINOLPTRPS25_Form extends BINOLCM13_Form{

	/** 单据号 */
	private String deliverNoIF;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品名称 */
	private String nameTotal;
	
	/** 收货部门 */
	private String inOrganizationId;

	public String getDeliverNoIF() {
		return deliverNoIF;
	}

	public void setDeliverNoIF(String deliverNoIF) {
		this.deliverNoIF = deliverNoIF;
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

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
	public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

	
}
