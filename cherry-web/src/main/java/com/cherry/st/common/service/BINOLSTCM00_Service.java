/*
 * @(#)BINOLSSPRM01_Service.java     1.0 2011/09/02
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
package com.cherry.st.common.service;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


@SuppressWarnings("unchecked")
public class BINOLSTCM00_Service extends BaseService {

    /**
     * 在发货单表中取得workFlowID。
     * @param map
     * @return
     */
    public Map<String,Object> getWorkFlowID(Map<String, Object> map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM00.getWorkFlowID");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
  
}
