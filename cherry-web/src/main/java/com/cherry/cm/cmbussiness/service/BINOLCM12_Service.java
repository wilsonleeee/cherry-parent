/*	
 * @(#)BINOLCM12_Service.java     1.0 2011/03/22		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.dto.ExtendPropertyDto;
import com.cherry.cm.service.BaseService;
import com.cherry.pt.common.ProductConstants;

/**
 * 扩展属性共通Service
 * 
 * @author lipc
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLCM12_Service extends BaseService {

	/**
	 * 取得扩展属性List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getExtPropertyList(
			Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM12.getExtPropertyList");

		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得扩展属性选项值List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getItemList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM12.getItemList");

		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得产品扩展属性及属性值List
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getExtValList(Map<String, Object> paramMap) {

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM12.getExtValList");

		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 插入产品扩展属性值表
	 * 
	 * @param paramMap
	 * @return
	 */
	public void insertPrtExtValue(Map<String, Object> paramMap) {
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM12.insertPrtExtValue");
		// 扩展属性List
		List<ExtendPropertyDto> list = (List<ExtendPropertyDto>) paramMap
				.get("extendPropertyList");
		if(null !=list){
			for(ExtendPropertyDto dto: list){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("extendPropertyId", dto.getExtendPropertyId());
				map.put("propertyValue", dto.getPropertyValue());
				map.putAll(paramMap);
				baseServiceImpl.save(map);
	
			}
		}
	}
	/**
	 * 删除产品扩展属性值表
	 * 
	 * @param paramMap
	 * @return
	 */
	public void delPrtExtValue(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM12.delPrtExtValue");
		map.put(ProductConstants.PRODUCTID, paramMap.get(ProductConstants.PRODUCTID));
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 查询扩展属性个数
	 * 
	 * @param map
	 * @return
	 */
	public int getExtProCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM12.getExtProCount");
		return baseServiceImpl.getSum(map);
	}
}
