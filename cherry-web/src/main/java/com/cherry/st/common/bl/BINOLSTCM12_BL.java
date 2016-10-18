/*      
 * @(#)BINOLSTCM12_BL.java     1.0 2012/06/26      
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
package com.cherry.st.common.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.st.common.service.BINOLSTCM12_Service;

/**
 * 产品发货单弹出table共通BL
 * @author LuoHong
 *
 */
public class BINOLSTCM12_BL {
    @Resource
    private  BINOLSTCM12_Service binOLSTCM12_Service;
    
    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    public int getDeliverCount(Map<String, Object> map){
        return binOLSTCM12_Service.getDeliverCount(map);
    }
    
    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getDeliverList(Map<String, Object> map){
        return binOLSTCM12_Service.getDeliverList(map);
    }
    
    /**
     * 取得发货单详细List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getDeliverDetailList(Map<String, Object> map){
        return binOLSTCM12_Service.getDeliverDetailList(map);
    }
}
