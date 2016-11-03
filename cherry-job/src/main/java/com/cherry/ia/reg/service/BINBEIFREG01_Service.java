/*
 * @(#)BINBEIFREG01_Service.java v1.0 2015-2-6
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
package com.cherry.ia.reg.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域信息下发Service
 * 
 * @author JiJW
 * @version 1.0 2015-2-6
 */
public class BINBEIFREG01_Service extends BaseService {
	
	/**
	 * 清空终端区域表数据
	 * 
	 * @param map
	 * @return
	 */
	public void truncateUDiskCounter(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFREG01.truncateUDiskCounter");
		witBaseServiceImpl.update(map);
	}
	
	/**
	 * 更新终端区域表对应的version
	 * 
	 * @param map
	 * @return
	 */
	public void updWP3PCSA_Ver(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFREG01.updWP3PCSA_Ver");
		ifServiceImpl.update(map);
	}
	
	/**
	 * 取得新新后台的区域信息List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFREG01.getRegionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入产品接口数据库
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addRegion(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFREG01.insRegion");
		witBaseServiceImpl.save(map);
	}

}
