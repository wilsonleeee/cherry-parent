/*	
 * @(#)BINOLSSPRM61_BL.java     1.0 2012/09/27		
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.interfaces.BINOLSSPRM61_IF;
import com.cherry.ss.prm.service.BINOLSSPRM61_Service;

/**
 * 
 * 促销品移库一览BL
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM61_BL implements BINOLSSPRM61_IF{

    @Resource(name="binOLSSPRM61_Service")
    private BINOLSSPRM61_Service binOLSSPRM61_Service;
    
    @Override
    public int searchShiftCount(Map<String, Object> map) {
        return binOLSSPRM61_Service.getShiftCount(map);
    }

    @Override
    public List<Map<String, Object>> searchShiftList(Map<String, Object> map) {
        return binOLSSPRM61_Service.getShiftList(map);
    }

    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSSPRM61_Service.getSumInfo(map);
    }
}