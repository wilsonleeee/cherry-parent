/*	
 * @(#)BINOTDEF02_Service.java     1.0 @2013-7-11		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 标准订单数据导出Service
 *
 * @author jijw
 *
 * @version  2013-7-11
 */
@SuppressWarnings("unchecked")
public class BINOTDEF02_Service extends BaseService {
	
	/**
	 * 根据同步状态取得订单单据集合
	 * 
	 * @param map
	 * @return
	 */
	public List getPrtOrderListBySynch(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF02.getPrtOrderListBySynch");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据新后台billCode查询订单接口表的单据号List
	 * @param map
	 * @return
	 */
	public List getPrtOrderListByOrderNoForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF02.getPrtOrderListByOrderNoForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新订单数据的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtOrderBySync(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF02.updPrtOrderBySync");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得新后台同步状态为"同步处理中"[syncFlag=2]订单数据（主数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPOListBySynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF02.getPOListBySynchFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]订单数据（主数据）插入订单数据主接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertI_WITPOS_Orders(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTDEF02.insertI_WITPOS_Orders");
	}
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]订单数据（主数据）插入订单数据主接口日志表
	 * 
	 * @param list 插入内容
	 */
	public void insertI_WITPOS_Orders_Log(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTDEF02.insertI_WITPOS_Orders_Log");
	}
	
	/**
	 * 取得新后台同步状态为"同步处理中"[syncFlag=2]订单数据（明细数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPODetailListBySynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTDEF02.getPODetailListBySynchFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]订单数据插入订单数据明细接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertI_WITPOS_OrdersDetail(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTDEF02.insertI_WITPOS_OrdersDetail");
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]订单数据插入订单数据明细接口日志表
	 * 
	 * @param list 插入内容
	 */
	public void insertI_WITPOS_OrdersDetail_Log(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTDEF02.insertI_WITPOS_OrdersDetail_Log");
	}

}
