/*      
 * @(#)BINOLSTCM18_BL.java     1.0 2013/09/02      
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

import com.cherry.st.common.interfaces.BINOLSTCM17_IF;
import com.cherry.st.common.interfaces.BINOLSTCM18_IF;
import com.cherry.st.common.service.BINOLSTCM18_Service;

/**
 * 产品入库单弹出table共通BL
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public class BINOLSTCM18_BL implements BINOLSTCM18_IF{
    @Resource(name="binOLSTCM18_Service")
    private  BINOLSTCM18_Service binOLSTCM18_Service;
    
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return
     */
    public int getInDepotCount(Map<String, Object> map){
        return binOLSTCM18_Service.getInDepotCount(map);
    }
    
    /**
     * 取得入库单List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInDepotList(Map<String, Object> map){
        return binOLSTCM18_Service.getInDepotList(map);
    }
    
    /**
     * 取得入库单详细List
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInDepotDetailList(Map<String, Object> map){
        return binOLSTCM18_Service.getInDepotDetailList(map);
    }
}