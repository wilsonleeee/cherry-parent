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

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品业务操作流水共通Service
 * @author niushunjie
 *
 */
public class BINOLCM22_Service extends BaseService {
    /**
     * 将操作日志插入流水表。
     * @param pramMap
     * @return
     */
    public int insertInventoryOpLog(Map<String, Object> pramMap){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(pramMap);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM22.insertInventoryOpLog");
        return baseServiceImpl.saveBackId(parameterMap);
    }
}
