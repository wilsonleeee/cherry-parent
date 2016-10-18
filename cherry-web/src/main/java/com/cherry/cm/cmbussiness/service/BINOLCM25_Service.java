/*	
 * @(#)BINOLCM22_Service     1.0 2011/10/08		
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
 * 单据详细流程显示Service
 * @author zhhuyi
 *
 */
public class BINOLCM25_Service extends BaseService {
    /**
     * 查询流水表
     * @param pramMap
     * @return
     */

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selInventoryOpLog(Map<String, Object> pramMap){
        pramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM25.selInventoryOpLog");
        return baseServiceImpl.getList(pramMap);
    }
	
	 /**
     * 取得用户名
     * @param pramMap
     * @return
     */
	public String getUserName(Map<String, Object> pramMap){
        pramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM25.getUserName");
        return (String) baseServiceImpl.get(pramMap);
    }
	
	/**
     * 取得岗位名
     * @param pramMap
     * @return
     */
	public String getCategoryName(Map<String, Object> pramMap){
        pramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM25.getCategoryName");
        return (String) baseServiceImpl.get(pramMap);
    } 
	
	/**
     * 取得部门名
     * @param pramMap
     * @return
     */
	public String getDepartName(Map<String, Object> pramMap){
        pramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM25.getDepartName");
        return (String) baseServiceImpl.get(pramMap);
    }
}
