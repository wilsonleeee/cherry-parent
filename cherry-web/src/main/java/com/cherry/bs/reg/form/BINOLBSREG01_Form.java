/*
 * @(#)BINOLBSREG01_Form.java     1.0 2011/11/23
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
package com.cherry.bs.reg.form;

/**
 * 区域一览画面Form
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG01_Form {
	
	/** 区域ID */
	private String regionId;
	
	/** 定位条件 */
	private String locationPosition;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getLocationPosition() {
		return locationPosition;
	}

	public void setLocationPosition(String locationPosition) {
		this.locationPosition = locationPosition;
	}

}
