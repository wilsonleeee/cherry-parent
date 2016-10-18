/*	
 * @(#)BINOLPTRPS12_BL.java     1.0 2010/03/16		
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.interfaces.BINOLPTRPS12_IF;
import com.cherry.pt.rps.service.BINOLPTRPS12_Service;

/**
 * 库存记录详细BL
 * 
 * @author lipc
 * @version 1.0 2011.03.16
 * 
 */
public class BINOLPTRPS12_BL implements BINOLPTRPS12_IF{

	@Resource
	private BINOLPTRPS12_Service binolptrps12Service;

	@Override
	public Map<String, Object> getProProduct(Map<String, Object> map) {
		Map<String, Object> prtMap = binolptrps12Service.getProProduct(map);
		String productId = ConvertUtil.getString(map.get(ProductConstants.PRODUCTID));
		if(!"".equals(productId)){
			List<String> barCode = binolptrps12Service.getBarCodeList(map);
			prtMap.put("list", barCode);
		}
		return prtMap;
	}

	@Override
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map) {

		return binolptrps12Service.getProStockDetails(map);
	}

	@Override
	public List<Map<String, Object>> getdetailed(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolptrps12Service.getdetailed(map);
	}

	
}
