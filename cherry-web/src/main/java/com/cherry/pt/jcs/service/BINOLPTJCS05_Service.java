/*	
 * @(#)BINOLPTJCS05_Service.java     1.0 2011/5/18		
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
package com.cherry.pt.jcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 产品批量导入Service
 * 
 * @author lipc
 * @version 1.0 2011.5.18
 * 
 */
public class BINOLPTJCS05_Service extends BaseService {
	
	/**
	 * 更新产品信息
	 * 
	 * @param map
	 * @return
	 * 
	 * */
	public int updProduct(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.updProduct");
		return baseServiceImpl.update(map);
	}

	/**
	 * 删除产品价格信息
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void delProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.delProductPrice");
		baseServiceImpl.remove(map);
	}

	/**
	 * 更新产品厂商表
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public void updProductVendor(Map<String, Object> map,String validflag) {
		Map<String, Object>	paramMap = new HashMap<String, Object>(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.updProductVendor");
		paramMap.put(CherryConstants.VALID_FLAG, validflag);
		baseServiceImpl.update(paramMap);
	}

	/**
	 * 取得产品条码list
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBarCodeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.getBarCodeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductVendorInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.getProductVendorInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询当前业务日期生效的产品价格记录
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.getProductPrice");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询当前业务日期生效的产品价格记录
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPreProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.getPreProductPrice");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品价格
	 * @param map
	 * @param validflag
	 */
	public void upProPrices(Map<String, Object> map) {
		Map<String, Object>	paramMap = new HashMap<String, Object>(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.upProPrices");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void delPrtCategory(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.delPrtCategory");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 查询分类属性名称在分类预设值表中已经存在
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List getExistPvCN(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS05.getExistPvCN");
//		return CherryUtil.obj2int(baseServiceImpl.get(map));
		return baseServiceImpl.getList(map);
	}
}
