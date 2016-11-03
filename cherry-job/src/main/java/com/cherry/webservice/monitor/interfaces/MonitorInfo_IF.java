/*  
 * @(#)MonitorInfo_IF.java     1.0 2015/01/20      
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

package com.cherry.webservice.monitor.interfaces;

import java.util.Map;

/**
 * 
 * 监控业务 Interfaces
 * 
 * @author niushunjie
 * @version 1.0 2015.01.20
 */
public interface MonitorInfo_IF {
    
    /**
     * 查询配置项
     * @param paramMap
     * @return
     */
    public Map<String, Object> getTerminalConfig(Map<String, Object> paramMap);
}