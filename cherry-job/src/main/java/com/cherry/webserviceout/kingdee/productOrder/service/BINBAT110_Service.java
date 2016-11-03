/*	
 * @(#)BINBAT110_Service.java     1.0 @2015-6-02
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
package com.cherry.webserviceout.kingdee.productOrder.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * Kingdee接口：产品订单推送Service
 *
 * @author jijw
 *
 * @version  2015-6-02
 */
public class BINBAT110_Service extends BaseService {
	
	/**
	 * 更新产品订单的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updPrtOrderBySyncNew(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT110.updPrtOrderBySyncNew");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品订单的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	@Deprecated
	public int updPrtOrderBySync(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT110.updPrtOrderBySync");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 根据同步状态取得产品订单数据集合
	 * 
	 * @param map
	 * @return
	 */
	public List getOrderNoIFOfPrtOrderListBySync(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT110.getOrderNoIFOfPrtOrderListBySync");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得新后台产品订单信息
	 * 
	 * @param map
	 * @return
	 */
	public List getPrtOrderList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT110.getPrtOrderList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得推送失败的新后台产品订单信息
	 * 
	 * @param map
	 * @return
	 */
	@Deprecated
	public List getFaildProductOrderList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT110.getFaildProductOrderList");
		return baseServiceImpl.getList(map);
	}

}
