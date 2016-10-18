/*  
 * @(#)BINOLPTRPS39_IF.java     1.0 2015/07/08      
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
package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTRPS39_IF extends ICherryInterface{

	/**
	 * 获取催单数量
	 * @param map
	 * @return
	 */
	public int getReminderCount(Map<String, Object> map);

	/**
	 * 获取催单List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReminderList(Map<String, Object> map);

	/**
	 * Excel导出
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;

	/**
	 * 给BAS发送催单消息
	 * @param map 
	 * @throws Exception 
	 */
	public Map<String, Object> reminderToBAS(Map<String, Object> map) throws Exception;

	/**
	 * 取得发货单数量
	 * @param map
	 * @return
	 */
	public int getDeliverCount(Map<String, Object> map);

	/**
	 * 取得发货单List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDeliverList(Map<String, Object> map);

	/**
	 * 获取excel导出名
	 * @param map
	 * @return
	 */
	public String getExportName(Map<String, Object> map);

	/**
	 * 将发货单号写入催单表中
	 * @param reminderMap
	 * @param userInfo
	 */
	public void tran_updateReminder(Map<String, Object> reminderMap, UserInfo userInfo);

	public boolean verifyDeliverNo(Map<String, Object> map);

	public void tran_updateReminderExists(Map<String, Object> map);
	
	

}
