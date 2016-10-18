/*
 * @(#)BINOLPTRPS02_Service.java     1.0 2010/11/03
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 产品发货单
 * @author weisc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS02_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getDeliverCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTRPS02.getDeliverCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List getDeliverList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
						"BINOLPTRPS02.getDeliverList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS02.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
}
