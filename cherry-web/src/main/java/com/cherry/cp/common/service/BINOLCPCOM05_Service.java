/*	
 * @(#)BINOLCPCOM05_Service.java     1.0 2013/4/18		
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
package com.cherry.cp.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cp.common.CampConstants;

/**
 * 活动预约表状态操作 Service
 * 
 * @author lipc
 * @version 1.0 2013.4.18
 */
public class BINOLCPCOM05_Service extends BaseService {
	
	/**
	 * 根据活动档次mainCode取得主题活动信息
	 * @return
	 */
	public Map<String,Object> getCampInfo(String subCampCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.SUBCAMP_CODE, subCampCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM05.getCampInfo");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得活动信息
	 * @return
	 */
	public Map<String,Object> getSubCampInfo(String subCampCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.SUBCAMP_CODE, subCampCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM05.getSubCampInfo");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得活动对象信息
	 * @return
	 */
	public Integer getMebCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM05.getMebCount");
        return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得活动对象LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMebList(Map<String, Object> map){
		return baseServiceImpl.getList(map, "BINOLCPCOM05.getMebList");
	}
	
	/**
	 * 取得活动单据历史
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampHistory(Map<String, Object> map){
		return baseServiceImpl.getList(map, "BINOLCPCOM05.getCampHistory");
	}
	
	/**
	 * 取得需要发送积分扣减MQ会员
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMqMemList(Map<String, Object> map){
		return baseServiceImpl.getList(map, "BINOLCPCOM05.getMqMemList");
	}
	
	/**
	 * 取得活动单据List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(Map<String, Object> map){
		return baseServiceImpl.getList(map, "BINOLCPCOM05.getOrderList");
	}
	
	/**
	 * 备份主单据
	 * @param list
	 */
	public void addCampHistory(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCPCOM05.addCampHistory");
	}
	
	/**
	 * 插入会员活动预约表
	 * @param list
	 */
	public void addCampOrder(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCPCOM05.addCampOrder");
	}
	
	/**
	 * 插入会员活动预约表
	 * @param list
	 */
	public int addCampOrder(Map<String, Object> map){	
		return baseServiceImpl.saveBackId(map, "BINOLCPCOM05.addCampOrderBackId");
	}
	/**
	 * 返回预约主单据ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCampOrderIdList(Map<String, Object> map){
		return baseServiceImpl.getList(map, "BINOLCPCOM05.getCampOrderIdList");
	}
	
	/**
	 * 更新会员活动预约信息下发状态【临时状态-->未下发】
	 * @param list
	 */
	public void updCampOrderSendFlag(Map<String, Object> map){
		baseServiceImpl.update(map, "BINOLCPCOM05.updCampOrderSendFlag");
	}
	
	/**
	 * 插入活动预约明细表
	 * @param list
	 */
	public void addCampOrdDetail(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCPCOM05.addCampOrdDetail");
	}
	
	/**
	 * 更新会员活动预约表
	 * @param list
	 */
	public void updCampOrder(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINOLCPCOM05.updCampOrder");
	}
	/**
	 * 更新会员活动预约表
	 * @param list
	 */
	public void updCampOrder(Map<String, Object> order){
		baseServiceImpl.update(order, "BINOLCPCOM05.updCampOrder");
	}
	
	/**
	 * 更新会员活动预约表MQFLAG
	 * @param list
	 */
	public void updCampOrderMQ(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINOLCPCOM05.updCampOrderMQ");
	}
	
	/**
	 * 更新活动单据主表为已下发
	 * @param list
	 */
	public void updCampOrderSendFlag1(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINOLCPCOM05.updCampOrderSendFlag1");
	}

	public int getOrderCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM05.getOrderCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员活动LIST信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getActResultList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM05.getActResultList");
		return baseServiceImpl.getList(map);
	}
}
