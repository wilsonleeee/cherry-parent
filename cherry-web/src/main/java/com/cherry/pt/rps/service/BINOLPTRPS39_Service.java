/*	
 * @(#)BINOLPTRPS11_Service.java     1.0 2011/03/15		
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品催单查询service
 * 
 * @author hujh
 * @version 1.0 2015.07.15
 * 
 */
public class BINOLPTRPS39_Service extends BaseService {

	/**
	 * 获取催单数量
	 * @param map
	 * @return
	 */
	public int getReminderCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getReminderCount");
		return (Integer) baseServiceImpl.get(map);
	}

	/**
	 * 获取催单List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReminderList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getReminderList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 查询Excel导出数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReminderListExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getReminderListExcel");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获得BAS信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBASInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getBASInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 更新催单次数
	 * @param map
	 */
	public void reminderToBAS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.updateReminderCount");
		baseServiceImpl.update(map);
	}

	/**
	 * 获取发货单数量
	 * @param map
	 * @return
	 */
	public int getDeliverCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getDeliverCount");
		return (Integer) baseServiceImpl.get(map);
	}

	/**
	 * 获取发货单List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getDeliverList");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getPrmDeliverList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getPrmDeliverList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得发货单Excel导出list
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverListExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getDeliverListExcel");
		return baseServiceImpl.getList(map);
	}
	
	public List<Map<String, Object>> getPrmDeliverListExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getPrmDeliverListExcel");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 将发货单号写入催单表中
	 * @param reminderMap billId reminderId
	 * @param userInfo
	 */
	public void updateReminder(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.updateReminder");
		baseServiceImpl.update(map);		
	}

	/**
	 * 取得产品发货单号
	 * @param tempMap
	 * @return
	 */
	public Map<String, Object> getProductDeliverNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getProductDeliverNo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 取得促销品发货单号
	 * @param tempMap
	 * @return
	 */
	public Map<String, Object> getPromotionDeliverNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getPromotionDeliverNo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 获取短信模板
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMsgTemplate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getMsgTemplate");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	public int verifyDeliverNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.verifyDeliverNo");
		return (Integer) baseServiceImpl.get(map);
	}

	public int verifyReminderNo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.verifyReminderNo");
		return (Integer) baseServiceImpl.get(map);
	}

	public List<Map<String, Object>> getMobilePhoneList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS39.getMobilePhoneList");
		return baseServiceImpl.getList(map);
	}

	
}
