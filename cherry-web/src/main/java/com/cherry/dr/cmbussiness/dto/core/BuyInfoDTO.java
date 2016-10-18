/*	
 * @(#)BuyInfoDTO.java     1.0 2012/03/16	
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
package com.cherry.dr.cmbussiness.dto.core;

import java.util.List;
import java.util.Map;

/**
 * 购买信息 DTO
 * 
 * @author hub
 * @version 1.0 2012.03.16
 */
public class BuyInfoDTO {
	
	
	
	/** 单次购买产品 */
	private List<Map<String, Object>> products;

	public List<Map<String, Object>> getProducts() {
		return products;
	}

	public void setProducts(List<Map<String, Object>> products) {
		this.products = products;
	}
}
