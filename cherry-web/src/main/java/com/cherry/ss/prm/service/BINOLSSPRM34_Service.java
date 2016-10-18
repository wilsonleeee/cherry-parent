/*
 * @(#)BINOLSSPRM34_Service.java     1.0 2010/11/24
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

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 发货单编辑Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.24
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM34_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return 发货单信息
	 */
	public Map getDeliverInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.getDeliverInfoEdit");
		return (Map) baseServiceImpl.get(paramMap);
	}

	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return 发货单明细List
	 */
	public List getDeliverDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.getDeliverDetailEditList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 更新促销产品收发货业务单据表
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updatePromDeliver(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.updatePromotionDeliver");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新入库区分
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updateStockInFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.updateStockInFlag");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 更新促销产品收发货业务单据明细表
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updatePromDeliverDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.updatePromotionDeliverDetail");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 伦理删除促销产品收发货业务单据明细表
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int invalidPromDeliverDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.invalidPromotionDeliverDetail");
		return baseServiceImpl.update(paramMap);
	}

	/**
	 * 插入促销产品收发货业务单据明细表
	 * 
	 * @param map
	 */
	public void insertPromDeliverDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM34.insertPromotionDeliverDetail");
		baseServiceImpl.save(paramMap);
	}
}
