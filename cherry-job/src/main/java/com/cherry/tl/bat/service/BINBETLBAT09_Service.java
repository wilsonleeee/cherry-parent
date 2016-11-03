/*
 * @(#)BINBETLBAT09_Service.java     1.0 2013/10/22
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
package com.cherry.tl.bat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 修改销售单据处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/10/22
 */
public class BINBETLBAT09_Service extends BaseService {
	
	/**
	 * 查询POS品牌的销售MQ记录List
	 * 
	 * @param map 查询条件
	 * @return 销售MQ记录List
	 */
	public List<Map<String, Object>> getWitposMQLogList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT09.getWitposMQLogList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 把POS品牌的销售MQ记录更新成待处理状态
	 * 
	 * @param map 查询条件
	 * @return 更新件数
	 */
	public int updateWitPosMQLogInit(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT09.updateWitPosMQLogInit");
		return witBaseServiceImpl.update(paramMap);
	}
	
	/**
	 * 删除POS品牌的销售MQ记录
	 * 
	 * @param map 删除条件
	 */
	public void deleteWitposMQLog(List<Map<String, Object>> list) {
		
		witBaseServiceImpl.deleteAll(list, "BINBETLBAT09.deleteWitposMQLog");
	}
	
	/**
	 * 更新POS品牌的销售MQ记录状态
	 * 
	 * @param map 更新条件
	 */
	public void updateWitposMQLog(List<Map<String, Object>> list) {
		
		witBaseServiceImpl.updateAll(list, "BINBETLBAT09.updateWitposMQLog");
	}

}
