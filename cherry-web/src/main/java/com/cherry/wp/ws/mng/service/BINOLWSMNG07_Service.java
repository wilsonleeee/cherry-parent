/*
 * @(#)BINOLWSMNG07_Service.java     1.0 2015-10-29
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
package com.cherry.wp.ws.mng.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLWSMNG07_Service 
 * @Description: TODO(盘点申请Service) 
 * @author menghao
 * @version v1.0.0 2015-10-29 
 *
 */
public class BINOLWSMNG07_Service extends BaseService {
    
	/**
	 *取得审核中单据 List
	 * 
	 * @param map 查询条件
	 * @return 发货单信息List
	 */
	public List<Map<String, Object>> getAuditBill(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG07.getAuditBill");
		return baseServiceImpl.getList(paramMap);
	}
	
    /**
     * 柜台产品全盘List
     * 
     * @param map 查询条件
     * @return 发货单信息List
     */
    public List<Map<String, Object>> getAllCntPrtStockList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG07.getAllCntPrtStockList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 柜台产品指定盘点List
     * 
     * @param map 查询条件
     * @return 发货单信息List
     */
    public List<Map<String, Object>> getGivenCntPrtStockList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG07.getGivenCntPrtStockList");
        return baseServiceImpl.getList(paramMap);
    }
	
    public List<Map<String, Object>> getPrtFunList(Map<String, Object> map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG07.getPrtFunList");
    	return baseServiceImpl.getList(map);
    }
}