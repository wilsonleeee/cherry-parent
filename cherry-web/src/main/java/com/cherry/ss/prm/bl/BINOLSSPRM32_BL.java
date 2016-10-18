/*	
 * @(#)BINOLSSPRM32_BL.java     1.0 2010/11/16		
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM32_Service;

/**
 * 库存详细BL
 * 
 * @author lipc
 * @version 1.0 2010.11.16
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM32_BL {

	@Resource
	private BINOLSSPRM32_Service binolssprm32Service;

	/**
	 * 取得促销品信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProProduct(Map<String, Object> map) {
		
		return (Map<String, Object>)binolssprm32Service.getProProduct(map);
	}
	/**
	 * 取得产品库存详细
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map) {
		
		return binolssprm32Service.getProStockDetails(map);
	}
	
	public List<Map<String, Object>> getdetailed(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolssprm32Service.getdetailed(map);
	}
}
