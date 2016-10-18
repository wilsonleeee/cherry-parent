/*		
 * @(#)BINOLPTRPS18_Form.java     1.0 2010/11/29		
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


/**
 * 产品调出单详细form
 * 
 * @author weisc
 * @version 1.0 2011.4.3
 */
public class BINOLPTRPS18_Form{
	
	/** 调拨单Id */
	private String proAllocationId;
	
	/** 调拨类型(1：调出 2：调入) */
	private String allocationType;

	public void setProAllocationId(String proAllocationId) {
		this.proAllocationId = proAllocationId;
	}

	public String getProAllocationId() {
		return proAllocationId;
	}
	
	public String getAllocationType() {
		return allocationType;
	}

	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}
}
