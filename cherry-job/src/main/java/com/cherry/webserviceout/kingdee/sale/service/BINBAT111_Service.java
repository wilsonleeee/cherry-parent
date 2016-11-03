/*	
 * @(#)BINBAT111_Service.java     1.0 @2015-6-16
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
package com.cherry.webserviceout.kingdee.sale.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * Kingdee接口：销售单据推送Service
 *
 * @author jijw
 *
 * @version  2015-6-16
 */
public class BINBAT111_Service extends BaseService {
	
	/**
	 * 更新销售单据的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public int updSaleBillBySyncNew(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT111.updSaleBillBySyncNew");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 根据同步状态取得销售单据数据集合
	 * 
	 * @param map
	 * @return
	 */
	public List getOrderNoIFOfSaleBillListBySync(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT111.getOrderNoIFOfSaleBillListBySync");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得新后台销售单据信息
	 * 
	 * @param map
	 * @return
	 */
	public List getSaleRecordList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT111.getSaleRecordList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得推送失败的新后台销售单据信息
	 * 
	 * @param map
	 * @return
	 */
	@Deprecated
	public List getFaildProductOrderList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT111.getFaildProductOrderList");
		return baseServiceImpl.getList(map);
	}

}
