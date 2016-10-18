/*	
 * @(#)BINOLCM18_Service     1.0 2011/08/30		
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
package com.cherry.cm.cmbussiness.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 实体仓库共通Service
 * @author WangCT
 *
 */
public class BINOLCM20_Service extends BaseService {
	

	/**
	 * 取得指定组织下的所有实体仓库信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProductStockNofrozen(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getProductStockNofrozen");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	public Map<String, Object> getProductStock(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getProductStock");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得指定组织下的所有实体仓库的库存情况，剔除冻结库存
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDepartProductStockNofrozen(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getDepartProductStockNofrozen");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得指定组织下的所有实体仓库的库存情况
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDepartProductStock(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getDepartProductStock");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
     * 根据批次号取得产品批次ID
     * @param map
     * @return
     */
    public Map<String, Object> getProductBatchID(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getProductBatchID");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
	
	/**
	 * 按批次取得产品库存。
	 * @param map
	 * @return
	 */
    public Map<String, Object> getProductStockBatch(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM20.getProductStockBatch");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
}
