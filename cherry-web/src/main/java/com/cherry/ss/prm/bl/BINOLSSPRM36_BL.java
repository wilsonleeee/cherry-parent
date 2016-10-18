/*	
 * @(#)BINOLSSPRM36_BL.java     1.0 2010/12/03		
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

import com.cherry.ss.prm.service.BINOLSSPRM36_Service;

/**
 * 出入库记录详细BL
 * 
 * @author lipc
 * @version 1.0 2010.12.03
 * 
 */
public class BINOLSSPRM36_BL {

	@Resource
	private BINOLSSPRM36_Service binolssprm36Service;

	/**
	 * 取得入出库单详细信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>>getProStockInOutInfo(Map<String, Object> map) {
		return binolssprm36Service.getProStockInOutInfo(map);
	}

	/**
	 * 取得入出库物品清单LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockInOutList(Map<String, Object> map) {
		return binolssprm36Service.getProStockInOutList(map);
	}
}
