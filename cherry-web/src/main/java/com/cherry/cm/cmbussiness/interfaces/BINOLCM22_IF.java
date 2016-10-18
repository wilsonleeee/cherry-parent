/*  
 * @(#)BINOLCM22_IF.java    1.0 2011-10-08     
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
package com.cherry.cm.cmbussiness.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 产品业务操作流水共通
 * @author niushunjie
 *
 */
public interface BINOLCM22_IF extends ICherryInterface {
    /**
     * 将操作日志插入流水表。
     * @param pramMap
     * @return
     */
    public int insertInventoryOpLog(Map<String, Object> pramMap);
    
    /**
     * 将操作日志插入流水表。
     * @param pramMap
     * @return
     */
    public int insertInventoryOpLog(Map<String, Object> mainData,Map<String,Object> paramMap);
}
