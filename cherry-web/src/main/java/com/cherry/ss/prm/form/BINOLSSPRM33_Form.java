/*
 * @(#)BINOLSSPRM33_Form.java     1.0 2010/11/11
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
 * 
 * 在途库存查询Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.11
 */
public class BINOLSSPRM33_Form extends BINOLCM13_Form{
	
	/** 收发货单号 */
	private String deliverRecNo;
	
	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;

	/**促销产品厂商ID*/
    private String prmVendorId;

	/**收货部门*/
    private String inOrganizationId;
	
	public String getDeliverRecNo() {
		return deliverRecNo;
	}

	public void setDeliverRecNo(String deliverRecNo) {
		this.deliverRecNo = deliverRecNo;
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
    
    public String getInOrganizationId() {
		return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}
}
