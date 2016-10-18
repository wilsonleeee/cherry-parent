/*	
 * @(#)BINOLCPCOM02_Form.java     1.0 2011/7/18		
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
package com.cherry.cp.common.form;

import com.cherry.cm.form.DataTable_BaseForm;


/**
 * 会员活动共通 Form
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM03_Form  extends DataTable_BaseForm{
	
	private String referDate;
	
	public String getReferDate() {
		return referDate;
	}
	
	public void setReferDate(String referDate) {
		this.referDate = referDate;
	}
}
