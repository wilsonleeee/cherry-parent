/*
 * @(#)BINOLSSPRM04_Form.java     1.0 2010/11/23
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
 * 促销品详细实体类
 * 促销品信息DTO
 * 
 */
public class BINOLSSPRM04_Form extends DataTable_BaseForm {
	
	private String promotionProId;//促销品ID
	
	private String nameTotal; // 促销品全称
	
	private String brandInfoId; // 品牌id
	
	// 促销品全称
	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	//促销品ID
	public void setPromotionProId(String promotionProId) {
		this.promotionProId = promotionProId;
	}

	public String getPromotionProId() {
		return promotionProId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	
}
