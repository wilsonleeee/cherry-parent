/*	
 * @(#)BINOLPLCOM02_Service.java     1.0 2011/12/20		
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
package com.cherry.pl.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 工作流向导共通 Service
 * 
 * @author niushunjie
 * @version 1.0 2011.12.20
 */
public class BINOLPLCOM02_Service extends BaseService{

    public List<Map<String,Object>> getWorkFlowID(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLCOM02.getWorkFlowID");
        return baseServiceImpl.getList(parameterMap);
    }
}
