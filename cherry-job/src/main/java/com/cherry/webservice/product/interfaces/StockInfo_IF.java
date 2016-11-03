/*  
 * @(#)StockInfo_IF.java     1.0 2014/12/11      
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

package com.cherry.webservice.product.interfaces;

import java.util.Map;

/**
 * 
 * 库存业务Interfaces
 * 
 * @author niushunjie
 * @version 1.0 2014.12.11
 */
public interface StockInfo_IF {
    
    /**
     * 获取实时库存List
     * @param paramMap
     * @return
     */
    public Map<String, Object> getRealtimeStock(Map<String, Object> paramMap);
}