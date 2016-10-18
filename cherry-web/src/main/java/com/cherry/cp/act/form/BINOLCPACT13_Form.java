/*	
 * @(#)BINOLCPACT13_Form.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 活动产品库存导入
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT13_Form extends BINOLCM13_Form{
	
	/**所属品牌ID*/
	private String brandInfoId;

	/** 主题活动代码 */
	private String importType;
	
	/** 是否加权限标志 0：不加权限查询 1：加权限查询 */
	private String privilegeFlag;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

}
