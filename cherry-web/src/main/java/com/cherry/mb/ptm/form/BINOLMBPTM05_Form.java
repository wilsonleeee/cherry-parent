/*
 * @(#)BINOLMBPTM05_Action.java     1.0 2013/05/23
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
package com.cherry.mb.ptm.form;

/**
 * 积分Excel导入Form
 * 
 * @author LUOHONG
 * @version 1.0 2013/05/23
 */
public class BINOLMBPTM05_Form {
	
	/**积分类型*/
	private String pointType ;
	
	/**导入方式*/
	private String importType ;
	
	/**导入原因*/
	private String reason ;
	
	/** 品牌ID */
	private int brandInfoId;
	
	private String importName;
	
	/**会员俱乐部ID*/
	private String memberClubId;

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getMemberClubId() {
		return memberClubId;
	}

	public void setMemberClubId(String memberClubId) {
		this.memberClubId = memberClubId;
	}
}
