/*	
 * @(#)BINOTYIN01_Service.java     1.0 @2013-3-11		
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
package com.cherry.ot.yin.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 颖通接口：颖通销售数据导出(销售+支付)Service
 *
 * @author jijw
 *
 * @version  2013-3-11
 */
@SuppressWarnings("unchecked")
public class BINOTYIN01_Service extends BaseService {
	
	/**
	 * 取得指定同步状态的销售数据总数
	 * 
	 * @param map
	 * @return
	 */
	public int getSRCountBySync(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.getSRCountBySync");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 根据同步状态取得销售单据集合
	 * 
	 * @param map
	 * @return
	 */
	public List getBillCodeOfSRListBySync(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.getBillCodeOfSRListBySync");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据新后台billCode查询颖通销售接口表的单据号List
	 * @param map
	 * @return
	 */
	public List getSRListByBillCodeForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.getSRListByBillCodeForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新销售数据的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updSaleRecordBySync(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.updSaleRecordBySync");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得新后台同步状态为"同步处理中"[syncFlag=2]销售数据（主数据、明细数据）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSRListBySynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.getSRListBySyncFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]销售数据（主数据、明细数据）插入颖通销售接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertCpsImportSales(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTYIN01.insertCpsImportSales");
	}
	
	/**
	 * 插入会员回访信息表
	 * @param map
	 */
	public void insertMemVisitInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN01.insertCpsImportMemberInfo");
		tpifServiceImpl.save(map);
	}
	
	/**
	 * 取得新后台同步状态为"同步处理中"[syncFlag=2]销售支付数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSalePayListBySynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN01.getSalePayListBySyncFlag");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把新后台同步状态为"同步处理中"[syncFlag=2]销售支付数据 插入颖通支付构成接口表
	 * 
	 * @param list 插入内容
	 */
	public void insertCpsImportPayment(List<Map<String,Object>> list) {
		tpifServiceImpl.saveAll(list, "BINOTYIN01.insertCpsImportPayment");
	}

}
