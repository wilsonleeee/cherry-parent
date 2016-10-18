/*
 * @(#)BINOLSSPRM04_BL.java     1.0 2010/11/23
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

import com.cherry.ss.prm.service.BINOLSSPRM04_Service;

/**
 * 促销产品详细 BL
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM04_BL {
	
	@Resource
	private BINOLSSPRM04_Service binolssprm04_Service;
	
	/**
	 * 取得促销品基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchPrmInfo (Map <String, Object> map){
		return binolssprm04_Service.getPrmInfo(map);
		
	}
	
	/**
	 * 取得促销品销售价格信息
	 * 
	 * @param map
	 * @return
	 */
	public List searchPrmSalePriceList (Map <String, Object> map){
		return binolssprm04_Service.getPrmSalePriceList(map);
		
	}
	
	/**
	 * 取得部门机构促销产品价格List
	 * 
	 * @param map
	 * @return
	 */
	public List searchPrmPriceDepartList (Map <String, Object> map){
		return binolssprm04_Service.getPrmPriceDepartList(map);
		
	}
	
	/**
	 * 取得促销品厂商信息List
	 * 
	 * @param map
	 * @return
	 */
	public List searchPrmFacList (Map <String, Object> map){
		return binolssprm04_Service.getPrmFacList(map);
	}
	
	/**
	 * 取得促销品扩展信息List
	 * 
	 * @param map
	 * @return
	 */
	public List searchPrmExtList(Map <String, Object> map){
		return binolssprm04_Service.getPrmExtList(map);
		
	}
}
