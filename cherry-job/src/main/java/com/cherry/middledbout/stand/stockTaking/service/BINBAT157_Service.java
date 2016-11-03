/*	
 * @(#)BINBAT124_Service.java     1.0 @2015-12-16	
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
package com.cherry.middledbout.stand.stockTaking.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 标准接口：产品盘点申请单据导出
 *
 * @author jijw
 *
 * @version  2013-3-21
 */
@SuppressWarnings("unchecked")
public class BINBAT157_Service extends BaseService {
	
	/**
	 * 根据同步状态取得产品盘点申请单据主表的单据号集合
	 * 
	 * @param map
	 * @return
	 */
	public List getSTNoIFOfPrtStockTakingRequestListBySync(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT157.getSTNoIFOfPrtStockTakingRequestListBySync");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据新后台StockTakingNoIF查询标准产品盘点申请单据接口表的单据号List
	 * @param map
	 * @return
	 */
	public List getPstrqListBySTNoIFForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT157.getPstrqListBySTNoIFForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新产品盘点申请单据主表的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updProductStockTakingRequestBySync(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT157.updProductStockTakingRequestBySync");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得产品盘点申请主表同步状态为"同步处理中"[syncFlag=2]盘点申请数据（主数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getProductStockRequestListBySyncFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT157.getProductStockRequestListBySyncFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品盘点申请主表同步状态为"同步处理中"[syncFlag=2]盘点申请数据（明细数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getProductStockRequestDetailListBySyncFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT157.getProductStockRequestDetailListBySyncFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]盘点申请数据（主数据）插入标准盘点申请单接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertIF_StockTaking(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT157.insertIF_StockTaking");
	}
	
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]盘点申请数据（明细数据）插入标准盘点申请单接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertIF_StockTakingDetail(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT157.insertIF_StockTakingDetail");
	}
	
		
	/**
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDateMap");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}

}
