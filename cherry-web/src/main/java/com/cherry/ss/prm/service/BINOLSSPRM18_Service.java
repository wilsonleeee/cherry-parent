/*
 * @(#)BINOLSSPRM18_Service.java     1.0 2010/11/03
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
 * 我的发货单
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM18_Service {
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
				"BINOLSSPRM18.getDeliverCount");
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
						"BINOLSSPRM18.getDeliverList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 取得某一产品的总数量和总金额
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSSPRM18.getSumInfo");
    }
}
