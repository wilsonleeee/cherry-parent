/*	
 * @(#)BINCPMEACT05_Service.java     1.0 2016/08/01		
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
package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 会员等级调整单据调整service
 * 
 * @author GeHequn
 */
public class BINCPMEACT05_Service extends BaseService {

	/**
	 * 取得会员等级改变表中的所有会员ID
	 * @return
	 */
	public List<Integer> getChangeMemIdList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.getChangeMemIdList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除会员等级调整表中对应的会员
	 * @param memberId
	 */
	public int delMember(int memberId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberId", memberId);
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.delMember");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除订单号对应明细表中的内容
	 * @param orderId
	 * @return
	 */
	public int delOrderInfo(int orderId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.delOrderInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入新明细
	 * @param newOrders
	 */
	public void insertOrder(List<Map<String,Object>> newOrders){
		baseServiceImpl.saveAll(newOrders, "BINCPMEACT05.insertOrder");
	}
	
	/**
	 * 更新主单版本
	 * @param map
	 * @return
	 */
	public int updateOrder(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.updateOrder");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员所有生日礼活动
	 * @param memberId
	 * @return
	 */
	public List<Map<String,Object>> getMemAllCamp(int memberId,String optYear){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberId", memberId);
		map.put("optYear", optYear);
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.getMemAllCamp");
		return baseServiceImpl.getList(map);	
	}
	
	/**
	 * 取得主题活动下的所有子活动
	 * @param campCode
	 * @return
	 */
	public List<Map<String,Object>> getSubCampList(String campCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campCode", campCode);
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.getSubCampList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取子活动的所有生日礼礼品
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrtList(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.getPrtList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得导入会员，搜索结果数量
	 * @param map
	 * @return
	 */
	public Integer getCampMemCount(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.getCampMemCount");
		return baseServiceImpl.getSum(map);
	}

	public void addHisMember(int memberId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT05.addHisMember");
		map.put("memberId", memberId);
		baseServiceImpl.save(map);
	}
	
}
