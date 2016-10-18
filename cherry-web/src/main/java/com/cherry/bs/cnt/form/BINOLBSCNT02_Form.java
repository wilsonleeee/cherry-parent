/*	
 * @(#)BINOLBSCNT02_Form.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 	柜台详细画面Form
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT02_Form extends DataTable_BaseForm {
	
	/** 柜台ID */
	private String counterInfoId;
	
	/** 部门ID */
	private String organizationId;
	
	/** 柜台号 */
	private String counterCode;
	
	/** 柜台密码*/
	private String passWord;
	
	/** 柜台协同区分*/
	private String counterSynergyFlag;
	
	/** 是否维护柜台密码*/
	private boolean maintainPassWord;
	
	/** 是否支持柜台协同维护*/
	private boolean maintainCoutSynergy;

	public String getCounterInfoId() {
		return counterInfoId;
	}

	public void setCounterInfoId(String counterInfoId) {
		this.counterInfoId = counterInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public boolean isMaintainPassWord() {
		return maintainPassWord;
	}

	public void setMaintainPassWord(boolean maintainPassWord) {
		this.maintainPassWord = maintainPassWord;
	}

	public boolean isMaintainCoutSynergy() {
		return maintainCoutSynergy;
	}

	public void setMaintainCoutSynergy(boolean maintainCoutSynergy) {
		this.maintainCoutSynergy = maintainCoutSynergy;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getCounterSynergyFlag() {
		return counterSynergyFlag;
	}

	public void setCounterSynergyFlag(String counterSynergyFlag) {
		this.counterSynergyFlag = counterSynergyFlag;
	}

	
}
