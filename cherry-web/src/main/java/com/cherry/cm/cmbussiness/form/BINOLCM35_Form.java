/*	
 * @(#)BINOLCM34_Form.java     1.0 2012/01/18		
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 查询会员和会员事件弹出画面共通Form
 * 
 * @author WangCT
 * @version 1.0 2012/01/18
 */
public class BINOLCM35_Form extends DataTable_BaseForm {
	
	/** 入会时间上限 **/
	private String joinDateStart;
	
	/** 入会时间下限 **/
	private String joinDateEnd;
	
	/** 购买时间上限 **/
	private String saleDateStart;
	
	/** 购买时间下限 **/
	private String saleDateEnd;

	public String getJoinDateStart() {
		return joinDateStart;
	}

	public void setJoinDateStart(String joinDateStart) {
		this.joinDateStart = joinDateStart;
	}

	public String getJoinDateEnd() {
		return joinDateEnd;
	}

	public void setJoinDateEnd(String joinDateEnd) {
		this.joinDateEnd = joinDateEnd;
	}

	public String getSaleDateStart() {
		return saleDateStart;
	}

	public void setSaleDateStart(String saleDateStart) {
		this.saleDateStart = saleDateStart;
	}

	public String getSaleDateEnd() {
		return saleDateEnd;
	}

	public void setSaleDateEnd(String saleDateEnd) {
		this.saleDateEnd = saleDateEnd;
	}

}
