/*	
 * @(#)BINOLSSPRM55_Service.java     1.0 2010/11/25		
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
 * 调拨记录查询service
 * 
 * @author lipc
 * @version 1.0 2010.11.25
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM55_Service extends BaseService {

	/**
	 * 取得调拨记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getAllocationCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM55.getAllocationCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得调拨记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM55.getAllocationList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 取得某一产品的总数量和总金额
     * 
     */
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSSPRM55.getSumInfo");
    }
}
