/*	
 * @(#)BINOLSSPRM52_Service.java     1.0 2010/11/29		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 发货记录详细service
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM52_Service extends BaseService {
	/**
	 * 修改【促销产品发货业务单据表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverMainLogic(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM52.deleteDeliverMainLogic");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除【促销产品发货业务单据表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetailLogic(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM52.deleteDeliverDetailLogic");
		return baseServiceImpl.update(map);
	}
	/**
	 * 删除【促销产品发货业务单据表】
	 * @param map
	 * @return
	 */
	public int deleteDeliverDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM52.deleteDeliverDetail");
		return baseServiceImpl.update(map);
	}

	/**
	 * 插入【促销产品发货业务单据明细表】
	 * @param list
	 */
	public void insertAllocationDetail(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSSPRM52.insertAllocationDetail");
	}
}
