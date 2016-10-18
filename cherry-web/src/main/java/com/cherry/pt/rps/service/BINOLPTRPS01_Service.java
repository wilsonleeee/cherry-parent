/*
 * @(#)BINOLPTRPS01_Service.java     1.0 2011/3/11
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 盘点查询Service
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS01_Service extends BaseService{

	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getTakingCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS01.getTakingCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS01.getTakingList");
		return baseServiceImpl.getList(map);
	}
	
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS01.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
}
