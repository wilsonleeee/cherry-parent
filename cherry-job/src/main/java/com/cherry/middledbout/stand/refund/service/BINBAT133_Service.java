/*	
 * @(#)BINBAT133_Service.java     1.0 2015/12/22		
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

package com.cherry.middledbout.stand.refund.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品退库申请单导出(标准接口)Service
 * 
 * @author lzs
 * 
 * @version 2015-12-22
 * 
 */
public class BINBAT133_Service extends BaseService {
	/**
	 * 查询新后台退库申请单主单数据同步状态为【SynchFlag=2】的数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProReturnReqList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT133.getProReturnReqList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 查询新后台退库申请单明细数据同步状态为【SynchFlag=2】的数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProReturnReqDetailList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT133.getProReturnReqDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 状态更新
	 * 
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT133.updateSynchFlag");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得指定导出状态的订单号List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProReturnReqListByBillNoIF(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT133.getProReturnReqListByBillNoIF");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据新后台已存在的单号查询单号数据在标准接口表中是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getExistsProReturnReqList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT133.getExistsProReturnReqList");
		return tpifServiceImpl.getList(paramMap);
	}

	/**
	 * 批量插入退库申请单据主单数据至标准接口表
	 * 
	 * @param list
	 */
	public void insertIFOutStorage(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT133.insertIFOutStorage");
	}
	
	/**
	 * 批量插入退库申请单据明细数据至标准接口表
	 * 
	 * @param list
	 */
	public void insertIFOutStorageDetail(List<Map<String, Object>> list) {
		tpifServiceImpl.saveAll(list, "BINBAT133.insertIFOutStorageDetail");
	}

}
