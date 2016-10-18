/*	
 * @(#)BINOLSSPRM62_Service.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 促销品移库详细Service
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM62_Service extends BaseService {
    /**
     * 删除【促销品移库单据明细表】
     * @param map
     * @return
     */
    public int deletePromotionShiftDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM62.deletePromotionShiftDetail");
        return baseServiceImpl.update(map);
    }
}