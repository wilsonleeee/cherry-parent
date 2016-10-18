/*	
 * @(#)BINOLCM26_Service     1.0 2011/12/09		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 工作流修改审核人Service
 * 
 * @author niushunjie
 * @version 1.0.0 2011.12.09
 */
public class BINOLCM26_Service extends BaseService {
    /**
     * 查询用户List
     * 
     * @param map 查询条件
     * @return 用户List
     */
    public List<Map<String, Object>> getUserInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM26.getUserInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询岗位List
     * 
     * @param map 查询条件
     * @return 岗位List
     */
    public List<Map<String, Object>> getPosInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM26.getPosInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询部门List
     * 
     * @param map 查询条件
     * @return 部门List
     */
    public List<Map<String, Object>> getOrgInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM26.getOrgInfoList");
        return baseServiceImpl.getList(parameterMap);
    }
}
