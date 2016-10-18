/*
 * @(#)BINOLCM40_Service.java     1.0 2013/12/25
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

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * Batch工作流步骤查看 共通 Service
 * 
 * @author niushunjie
 * @version 1.0 2013/12/25
 */
public class BINOLCM40_Service extends BaseService {
	
    /**
     * 取得未完成的工作流文件内容信息（配置数据库）
     * 
     * @param map 查询条件
     * @return 工作流文件
     */
    public Map<String, Object> getWorkFlowContent(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM40.getWorkFlowContent");
        return (Map<String, Object>) baseConfServiceImpl.get(map);
    }
    
    /**
     * 取得最后一次未完成工作流信息
     * 
     * @param map 查询条件
     * @return 工作流信息
     */
    public Map<String, Object> getWorkFlowInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM40.getWorkFlowInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
}