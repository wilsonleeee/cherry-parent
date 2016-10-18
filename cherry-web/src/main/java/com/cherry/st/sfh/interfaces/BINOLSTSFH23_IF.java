/*  
 * @(#)BINOLSTSFH22_IF.java     1.0 2012/11/13      
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
package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.sfh.form.BINOLSTSFH23_Form;

/**
 * 
 * 订货查询（浓妆淡抹）Interface
 * 
 * @author zw
 * @version 1.0 2016.09.07
 */
public interface BINOLSTSFH23_IF extends ICherryInterface{

    /**
     * 获取符合查询条件的订单总数
     * @param map
     * @return
     */
	public int getOrderCount(Map<String, Object> map);

    /**
     * 获取符合查询条件的订单主单数据
     * @param map
     * @return
     */
	public List<Map<String, Object>> getOrderInfoList(Map<String, Object> map);

	
	/**
	 * 订单Excel导出
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;

	/**
	 * 订单删除主单数据
	 * @param map
	 * @return
	 */
	public int deleteOrder(Map<String, Object> map);
	/**
	 * 订单删除明细数据
	 * @param map
	 * @return
	 */
	public int deleteOrderDetail(Map<String, Object> map);


	/**
	 * 根据订单号获取订单详细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderInfoByOrder(Map<String, Object> map);
	/**
	 * 根据订单汇总信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
}
