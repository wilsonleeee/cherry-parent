/*		
 * @(#)BINOLSSPRM51_Service.java     1.0 2010/11/16		
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
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
/**
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM51_Service {
	@Resource
	private BaseServiceImpl baseServeceImpl;
	
	/**
	 * 取得发货单概要信息
	 * @param map
	 * @return
	 */
	
	public List getDeliverDataList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM51.getDeliverDataList");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得发货单概要信息  工作流使用
	 * @param map
	 * @return
	 */
	public List getDeliverDataListJbpm(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM51.getDeliverDataListJbpm");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得发货单的明细数据
	 * @param map
	 * @return
	 */
	public List getDeliverDataDetailList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM51.getDeliverDataDetailList");
		return baseServeceImpl.getList(map);
	}
}
