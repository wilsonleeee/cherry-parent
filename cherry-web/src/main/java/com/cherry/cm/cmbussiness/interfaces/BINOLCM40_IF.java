/*  
 * @(#)BINOLCM40_IF.java    1.0 2013-12-25     
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
 * Batch工作流步骤查看 共通 IF
 * 
 * @author niushunjie
 * @version 1.0.0 2013.12.25
 */
public interface BINOLCM40_IF extends ICherryInterface {
    
    /**
     * 取得工作流步骤
     * @param map
     * @return 工作流
     */
    public Map<String,Object> getView(Map<String,Object> map);
    
    
    /**
     * 取得工作流文件信息
     * 
     * @param map 查询条件
     * @return 工作流文件内容
     */
    public Map<String, Object> getWorkFlowContent(Map<String, Object> map);
}