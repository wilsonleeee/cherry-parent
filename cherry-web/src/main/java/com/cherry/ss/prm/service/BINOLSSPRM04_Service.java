/*
 * @(#)BINOLSSPRM04_Service.java     1.0 2010/10/27
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品详细Service
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM04_Service extends BaseService {

	/**
	 * 取得促销品基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getPrmInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品ID
		paramMap.put("promotionProId", map.get("promotionProId"));

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM04.getPrmInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}

	/**
	 * 取得促销品销售价格信息
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmSalePriceList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品ID
		paramMap.put("promotionProId", map.get("promotionProId"));
		// 价格区分
		paramMap.put("priceKbn", map.get("priceKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM04.getPrmSalePriceList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得部门机构促销产品价格信息
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmPriceDepartList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品ID
		paramMap.put("promotionProId", map.get("promotionProId"));
		// 价格区分
		paramMap.put("priceKbn", map.get("priceKbn"));
		paramMap.put("businessType", "1");
		paramMap.put("operationType", "1");
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM04.getPrmPriceDepartList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得促销品厂商信息
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmFacList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品ID
		paramMap.put("promotionProId", map.get("promotionProId"));
		// 是否只获取有效的促销品厂商信息
		paramMap.put("validFlagKbn", map.get("validFlagKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM04.getPrmFacList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得促销品扩展信息
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmExtList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品ID
		paramMap.put("promotionProId", map.get("promotionProId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM04.getPrmExtList");
		return baseServiceImpl.getList(paramMap);
	}

}
