/*	
 * @(#)BINBEKDCPI01_Service.java     1.0 @2015-5-13
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
package com.cherry.webserviceout.kingdee.cntPrtInv.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * Kingdee接口：柜台产品库存更新Service
 *
 * @author jijw
 *
 * @version  2015-5-13
 */
public class BINBEKDCPI01_Service extends BaseService {
	
	/**
	 * 取得新后台柜台产品库存信息
	 * 
	 * @param map
	 * @return
	 */
	public List getProductStockList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDCPI01.getProductStockList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得失败的新后台柜台产品库存信息
	 * 
	 * @param map
	 * @return
	 */
	public List getFaildProductStockList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDCPI01.getFaildProductStockList");
		return baseServiceImpl.getList(map);
	}

}
