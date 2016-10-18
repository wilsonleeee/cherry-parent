/*	
 * @(#)BINOLCM11_Service.java     1.0 2011/01/25		
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
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品分类共通Service
 * @author zj
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM11_Service extends BaseService{
	
	/**
	 * 取得大分类名称
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			大分类中英文名称
	 */
	public Map getPrimaryCategoryName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 大分类代码
		paramMap.put("primaryCategoryCode", map.get("primaryCategoryCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM11.getPrimaryCategoryName");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得中分类名称Map
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			中分类中英文名称
	 */
	public Map getSecondryCategoryName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 中分类代码
		paramMap.put("secondryCategoryCode", map.get("secondryCategoryCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM11.getSecondryCategoryName");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得小分类名称Map
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			小分类中英文名称
	 */
	public Map getSmallCategoryName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 小分类代码
		paramMap.put("smallCategoryCode", map.get("smallCategoryCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM11.getSmallCategoryName");
		return (Map)baseServiceImpl.get(paramMap);
	}
}
