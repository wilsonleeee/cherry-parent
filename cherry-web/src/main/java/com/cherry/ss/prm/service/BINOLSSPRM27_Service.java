/*
 * @(#)BINOLSSPRM27_Service.java     1.0 2010/11/03
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
 * 发货单查询Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.03
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM27_Service {
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
				"BINOLSSPRM27.getDeliverCount");
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
						"BINOLSSPRM27.getDeliverList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 伦理删除促销产品收发货业务单据表
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int invalidPromDeliver(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLSSPRM27.invalidPromotionDeliver");
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
				"BINOLSSPRM27.invalidPromotionDeliverDetail");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得某一产品的总数量和总金额
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSSPRM27.getSumInfo");
	}
}
