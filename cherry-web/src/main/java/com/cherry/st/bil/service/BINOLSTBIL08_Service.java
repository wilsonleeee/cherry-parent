/*
 * @(#)BINOLSTBIL06_Service.java     1.0 2011/10/20
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
package com.cherry.st.bil.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 移库单明细
 * @author zhhuyi
 * @version 1.0 2011.10.20
 */
public class BINOLSTBIL08_Service extends BaseService{
    
    /**
     * 删除【产品移库单据明细表】
     * @param map
     * @return
     */
    public int deleteProductShiftDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL08.deleteProductShiftDetail");
        return baseServiceImpl.update(map);
    }

    /**
     * 删除【产品移库单据主表】
     * @param map
     * @return
     */
	public void deleteProductShift(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL08.deleteProductShift");
         baseServiceImpl.update(map);
		
	}
}
