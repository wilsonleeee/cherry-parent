/*      
 * @(#)BINOLSSCM10_BL.java     1.0 2013/09/02      
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
package com.cherry.ss.common.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.common.interfaces.BINOLSSCM10_IF;
import com.cherry.ss.common.service.BINOLSSCM10_Service;

/**
 * 促销品入库单弹出table共通BL
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public class BINOLSSCM10_BL implements BINOLSSCM10_IF{
    @Resource(name="binOLSSCM10_Service")
    private BINOLSSCM10_Service binOLSSCM10_Service;
    
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return
     */
    public int getInDepotCount(Map<String, Object> map){
        return binOLSSCM10_Service.getInDepotCount(map);
    }
    
    /**
     * 取得入库单List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInDepotList(Map<String, Object> map){
        return binOLSSCM10_Service.getInDepotList(map);
    }
    
    /**
     * 取得入库单详细List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInDepotDetailList(Map<String, Object> map){
        return binOLSSCM10_Service.getInDepotDetailList(map);
    }
}