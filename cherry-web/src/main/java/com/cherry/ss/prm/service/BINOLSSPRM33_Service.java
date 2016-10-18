/*
 * @(#)BINOLSSPRM33_Service.java     1.0 2010/11/11
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 在途库存查询Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.11
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM33_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getDeliverReceCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM33.getDeliverReceCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List getDeliverReceList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
						"BINOLSSPRM33.getDeliverReceList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM33.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
}
