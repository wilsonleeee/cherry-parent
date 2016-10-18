/*  
 * @(#)BINOLMOCIO01_IF.java     1.0 2011/06/09      
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
package com.cherry.mo.cio.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 柜台消息管理Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public interface BINOLMOCIO23_IF extends ICherryInterface {
    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getDepartmentMessageCount(Map<String, Object> map);
    
    /**
     * 取得柜台消息List
     * 
     * @param map
     * @return 柜台消息List
     */
    public List<Map<String, Object>> getDepartmentMessageList(Map<String, Object> map);
    
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    public Map<String, Object> getDepartmentMessage(Map<String, Object> map);
    
}
