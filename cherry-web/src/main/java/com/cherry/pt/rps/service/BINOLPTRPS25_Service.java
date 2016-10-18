/*
 * @(#)BINOLPTRPS25_Service.java     1.0.0 2011/10/17
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
 * 
 * 产品在途库存查询Service
 * 
 * 
 * 
 * @author lipc
 * @version 1.0.0 2011.10.17
 */
public class BINOLPTRPS25_Service extends BaseService{
	/**
	 * 在途库存记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrtDeliverCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getPrtDeliverCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 在途库存记录LIST
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtDeliverList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getPrtDeliverList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 在途库存汇总信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
    /**
     * 取得产品在途库存导出总数
     * 
     * @param map
     * @return 产品在途库存导出总数
     */
    public int getDeliverExportCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getDeliverExportCount");
        return baseServiceImpl.getSum(map);
    }
    
    /**
     * 取得产品在途库存明细导出List
     * 
     * @param map
     * @return 产品在途库存导出List
     */
    public List getDeliverExportList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS25.getDeliverExportList");
        return baseServiceImpl.getList(map);
    } 
}
