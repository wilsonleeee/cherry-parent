/*
 * @(#)BINOLWSMNG06_Service.java     1.0 2014/10/20
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
 * 自由盘点Service
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
public class BINOLWSMNG06_Service extends BaseService {
    
	/**
	 *取得审核中单据 List
	 * 
	 * @param map 查询条件
	 * @return 发货单信息List
	 */
	public List<Map<String, Object>> getAuditBill(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG06.getAuditBill");
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
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWSMNG06.getAllCntPrtStockList");
        return baseServiceImpl.getList(paramMap);
    }
	
}