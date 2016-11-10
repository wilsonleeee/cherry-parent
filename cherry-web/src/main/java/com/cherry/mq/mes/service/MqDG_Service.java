/*  
 * @(#)MqPP_Service.java     1.0 2016-7-26    
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
package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: MqDG_Service 
 * @Description: TODO(提货单Service) 
 * @author menghao
 * @version v1.0.0 2016-7-26 
 *
 */
public class MqDG_Service extends BaseService{
	
	/**
	 * 根据单号取得预付单信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrePayBillByNo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.getPrePayBillByNo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据单号取得提货单信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPickupBillByNo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.getPickupBillByNo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据提货主单ID取得提货单明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPickupBillDetailByID(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.getPickupBillDetailByID");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入提货单主表信息
	 * @param map
	 * @return
	 */
	public int insertPickupBillMain(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.insertPickupBillMain");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入提货明细表数据
	 * @param list
	 */
	public void insertPickupBillDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "MqDG.insertPickupBillDetail");
	}
	
	/**
	 * 更新原始预定单主信息
	 * @param map
	 * @return
	 */
	public int updateOriginalNSBillInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.updateOriginalNSBillInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 批量更新原始预定单的明细信息
	 * @param map
	 */
	public void updateOriginalNSBillDetailInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.updateOriginalNSBillDetailInfo");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新电商订单表相关状态信息
	 * @param map
	 */
	public void updateESOrderMainInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqDG.updateESOrderMainInfo");
		baseServiceImpl.update(paramMap);
	}
}
