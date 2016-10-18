/*  
 * @(#)BINOLCM26_IF.java    1.0 2011-11-09     
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


import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;
import com.googlecode.jsonplugin.JSONException;

/**
 * 工作流修改审核人IF
 * 
 * @author niushunjie
 * @version 1.0.0 2011.12.09
 */
public interface BINOLCM26_IF extends ICherryInterface {
    /**
     * 根据身份类型取得身份信息
     * 
     * @param map 查询条件
     */
    public List<Map<String, Object>> getCodeByType(Map<String, Object> map);
    
    /**
     * 更新审核人
     * @param map
     * @return
     */
    public int tran_updateAuditor(Map<String, Object> map);
    
    /**
     * 取当前操作信息
     * @param map
     * @return
     */
    public Map<String,Object> getCurrentOperateInfo(Map<String, Object> map) throws JSONException;
}
