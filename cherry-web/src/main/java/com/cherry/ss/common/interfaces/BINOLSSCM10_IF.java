/*  
 * @(#)BINOLSTCM18_IF.java    1.0 2013.09.02
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
package com.cherry.ss.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 促销品入库单弹出table共通interfaces
 * 
 * @author niushunjie
 * @version 1.0 2013.09.02
 */
public interface BINOLSSCM10_IF extends ICherryInterface{

    /**
     * 取得入库单总数
     * @param map
     * @return
     */
    public int getInDepotCount(Map<String, Object> map);
    
    /**
     * 取得入库单List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInDepotList(Map<String, Object> map);
    
    /**
     * 取得入库单详细List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getInDepotDetailList(Map<String, Object> map);
}