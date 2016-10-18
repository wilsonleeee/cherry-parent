/*
 * @(#)BINOLMBPTM04_Action.java     1.0 2013/06/03
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
package com.cherry.mb.ptm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 积分Excel导入Service
 * 
 * @author LUOHONG
 * @version 1.0 2013/06/03
 */
public class BINOLMBPTM04_Service extends BaseService {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	/**
	 *积分导入List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM04.getPointList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 积分导入结果Count
	 * 
	 * @param map
	 * @return
	 */
	public int getPointCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM04.getPointCount");
		return baseServiceImpl.getSum(map);
	}
	/**
	 *积分导入List
	 * 
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPointDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM04.getPointDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	/**
	 * 积分导入结果Count
	 * 
	 * @param map
	 * @return
	 */
	public int getPointDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBPTM04.getPointDetailCount");
		return baseServiceImpl.getSum(map);
	}
	 /**
     * 取得导入结果数量
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLMBPTM04.getSumInfo");
    }
}
