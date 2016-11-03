/*	
 * @(#)BINOTDEF01_Service.java     1.0 @2013-7-5		
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
package com.cherry.ot.def.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 *
 * 标准销售数据导出Service
 *
 * @author jijw
 *
 * @version  2013-7-05
 */
@SuppressWarnings("unchecked")
public class BINOTDEF01_Service extends BaseService {
	
	/**
	 * 根据同步状态取得销售单据集合
	 * 
	 * @param map
	 * @return
	 */
	public List getBillCodeOfSRListBySync(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF01.getBillCodeOfSRListBySync");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据新后台billCode查询销售接口表的单据号List
	 * @param map
	 * @return
	 */
	public List getSRListByBillCodeForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF01.getSRListByBillCodeForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新销售数据的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updSaleRecordBySync(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF01.updSaleRecordBySync");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得新后台同步状态为"同步处理中"[syncFlag=2]销售数据（主数据、明细数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSRListBySynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF01.getSRListBySynchFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]销售数据（主数据、明细数据）插入销售数据接口表[Interfaces.BIN_Sale]
	 * 
	 * @param list 插入内容
	 */
	public void insertBinSales(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTDEF01.insertBinSales");
	}
	
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTDEF01.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}

}
