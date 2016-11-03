/*	
 * @(#)BINOTYIN11_Service.java     1.0 @2014-01-26
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
package com.cherry.ot.yin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 颖通接口：颖通BA信息导出Service
 *
 * @author jijw
 *
 * @version  2014-01-26
 */
@SuppressWarnings("unchecked")
public class BINOTYIN12_Service extends BaseService {
	
	/**
	 * 
	 * 清空颖通接口BA信息表记录
	 * CPS_IMPORT_BaInfo
	 * @param map
	 */
	public void truncateCpsImpBaInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN12.truncateCpsImpBaInfo");
		tpifServiceImpl.update(parameterMap);
	}
	
	/**
	 * 从新后台取得BA信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getBAInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN12.getBAInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入颖通BA信息接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertCpsImportBaInfo(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTYIN12.insertCpsImportBaInfo");
	}
	
}
