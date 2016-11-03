/*
 * @(#)BINBEIFCON01_Service.java     1.0 2011/02/18
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
package com.cherry.ia.cont.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 
 *BATCH控制SERVICE
 * 
 * 
 * @author zhangjie
 * @version 1.0 2011.02.18
 */
public class BINBEIFCON01_Service extends BaseService {

	/**
	 * 查询Product所在记录job_date字段值
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public Object getJobDate(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.getJobDate");
		return ifServiceImpl.get(map);
	}

	/**
	 * 更新job时序表job_date
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void updateJobDate(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateJobDate");
		ifServiceImpl.update(map);
	}

	/**
	 * 查询flag字段值
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public Object getFlag(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.getFlag");
		return ifServiceImpl.get(map);
	}

	/**
	 * 更新job时序表flag为2，modified时间为当前时间
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void updateFlmo(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateFlmo");
		ifServiceImpl.update(map);		
	}

	/**
	 *  更新柜台，省，市job时序表job_date
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public Object updateCouJobDate() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateCouJobDate");
		return ifServiceImpl.update(map);
	}

	/**
	 * 查询柜台，省，市flag字段值
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public List getCouFlag() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.getCouFlag");
		return ifServiceImpl.getList(map);
	}

	/**
	 * 更新柜台，省，市job时序表flag为2，modified时间为当前时间
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void updateCouFlmo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateCouFlmo");
		ifServiceImpl.update(map);		
	}

	/**
	 * 设置成功标志
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void updateSucFlag(Map<String, String> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateSucFlag");
		ifServiceImpl.update(map);				
	}

	/**
	 * 设置柜台，省，市成功标志
	 * 
	 * @param Map
	 * 
	 * 
	 * @return Object
	 * 
	 */
	public void updateCouSucFlag() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFCON01.updateCouSucFlag");
		ifServiceImpl.update(map);						
	}

}
