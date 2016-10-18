/*
 * @(#)BINOLSTIOS12_Service.java     1.0 2015-10-9
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
package com.cherry.st.ios.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLSTIOS12_Service 
 * @Description: TODO(大仓入库Service) 
 * @author menghao
 * @version v1.0.0 2015-10-9 
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSTIOS12_Service extends BaseService{
    
	public String getDepart(Map<String,Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS12.getDepart");
        return  (String) baseServiceImpl.get(parameterMap);
    }
}
