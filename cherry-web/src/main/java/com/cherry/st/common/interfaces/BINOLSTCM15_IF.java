/*  
 * @(#)BINOLSTCM15_IF.java    1.0 2012.09.10
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
package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 产品发货接收方库存弹出table共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.09.10
 */
public interface BINOLSTCM15_IF {
    /**
     * 取得产品发货接收方库存总数
     * 
     * @param map
     * @return
     */
    public int getStockCount(Map<String, Object> map);
    
    /**
     * 取得产品发货接收方库存List
     * 
     * @param map
     * @return
     */
    public List<Map<String,Object>> getStockList(Map<String, Object> map);
    
    /**
     * 查询是否显示订货方库存按钮
     * 
     * @param map
     * @return
     */
    public String getShowRecStockFlag(Map<String, Object> map);
}
