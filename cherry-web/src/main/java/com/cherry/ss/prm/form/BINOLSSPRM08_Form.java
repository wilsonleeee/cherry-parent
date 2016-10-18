/*
 * @(#)BINOLSSPRM08_Form.java     1.0 2010/11/29
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

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 促销品大中分类详细实体类
 * 促销品大中分类信息DTO
 * 
 */
public class BINOLSSPRM08_Form extends DataTable_BaseForm {
	
	private String prmTypeId;//促销品大中分类ID

	public void setPrmTypeId(String prmTypeId) {
		this.prmTypeId = prmTypeId;
	}

	public String getPrmTypeId() {
		return prmTypeId;
	}


}
