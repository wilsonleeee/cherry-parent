/*	
 * @(#)BINOLSSPRM57_BL.java     1.0 2012/04/13		
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

import com.cherry.ss.prm.interfaces.BINOLSSPRM57_IF;
import com.cherry.ss.prm.service.BINOLSSPRM57_Service;

/**
 * 促销品收货一览BL
 * 
 * @author niushunjie
 * @version 1.0 2012.04.13
 * 
 */
public class BINOLSSPRM57_BL implements BINOLSSPRM57_IF{
    @Resource
    private BINOLSSPRM57_Service binOLSSPRM57_Service;

    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    @Override
    public int searchDeliverCount(Map<String, Object> map) {
        // 取得发货单总数
        return binOLSSPRM57_Service.getDeliverCount(map);
    }
    
    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    @Override
    public List<Map<String,Object>> searchDeliverList(Map<String, Object> map) {
        // 取得发货单List
        return binOLSSPRM57_Service.getDeliverList(map);
    }
    
    /**
     * 取得促销品总数量和总金额
     * @param map
     * @return
     */
    @Override
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return binOLSSPRM57_Service.getSumInfo(map);
    }
}
