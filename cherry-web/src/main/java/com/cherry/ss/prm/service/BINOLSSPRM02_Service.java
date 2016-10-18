/*
 * @(#)BINOLSSPRM02_Service.java     1.0 2010/11/19
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
package com.cherry.ss.prm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品添加Service
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
public class BINOLSSPRM02_Service extends BaseService {

	/**
	 * 插入促销产品表并返回促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPromotionProduct(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM02.insertPromotionProduct");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromProductVendor(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM02.insertPromProductVendor");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入促销产品价格表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromotionPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM02.insertPromotionPrice");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入部门机构产品价格表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrmPriceDepart(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM02.insertPrmPriceDepart");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入促销产品信息扩展表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromotionProductExt(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM02.insertPromotionProductExt");
		baseServiceImpl.save(map);
	}
}
