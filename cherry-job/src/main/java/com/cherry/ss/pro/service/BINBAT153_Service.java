/*	
 * @(#)BINBAT153_Service.java     1.0 @2016-7-10		
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
package com.cherry.ss.pro.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 标准接口 ：产品入出库批次成本导出 Service
 *
 * @author zw
 *
 * @version  2016-7-10
 */
@SuppressWarnings("unchecked")
public class BINBAT153_Service extends BaseService {

	/**
	 * 更新入出库批次主表的同步状态(从null改为1)
	 * @param map
	 * @return
	 * 
	 * */
	public int updProBatBySynNull(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT153.updProBatBySynNull");
		return baseServiceImpl.update(map);
	}

	/**
	 * 取得需要导入入出库批次数据（同步状态为同步中和同步异常的且业务类型不为LG和AR的）
	 * @param map
	 * @return
	 * 
	 * */
	public List<Map<String, Object>> getProBatList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT153.getProBatList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 	插入产品入出库批次数据
	 * @param list
	 * @return
	 * 
	 * */
	public void insertProBat(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list,"BINBAT153.insertProBat");
		
	}

	/**
	 * 	修改产品入出库批次数据同步状态为2
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFProBat(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT153.updIFProBat");
		return baseServiceImpl.update(map);

	}


	/**
	 * 取得需要导入入出库批次明细数据（在所取得主数据之内）
	 * @param map
	 * @return
	 * 
	 * */
	public List<Map<String, Object>> getBatDetailListNew(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT153.getBatDetailListNew");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 	插入产品入出库批次明细数据
	 * @param list
	 * @return
	 * 
	 * */
	public void insertProBatDetailNew(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT153.insertProBatDetailNew");
	}

}

