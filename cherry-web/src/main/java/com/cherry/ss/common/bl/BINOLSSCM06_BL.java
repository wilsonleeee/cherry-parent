/*      
 * @(#)BINOLSSCM06_BL.java     1.0 2011/11/21        
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

import com.cherry.ss.common.service.BINOLSSCM06_Service;

/**
 * 促销产品发货单弹出table共通BL
 * @author niushunjie
 *
 */
public class BINOLSSCM06_BL {
    @Resource
    private BINOLSSCM06_Service binOLSSCM06_Service;
    
    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    public int getDeliverCount(Map<String, Object> map){
        return binOLSSCM06_Service.getDeliverCount(map);
    }
    
    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map){
        return binOLSSCM06_Service.getDeliverList(map);
    }
    
    /**
     * 取得发货单详细List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getDeliverDetailList(Map<String, Object> map){
        return binOLSSCM06_Service.getDeliverDetailList(map);
    }
}
