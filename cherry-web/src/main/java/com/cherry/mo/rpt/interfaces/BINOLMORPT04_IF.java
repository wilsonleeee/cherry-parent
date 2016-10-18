/*  
 * @(#)BINOLMORPT02_IF.java     1.0 2011.10.24
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
package com.cherry.mo.rpt.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 答卷信息IF
 * 
 * @author WangCT
 * @version 1.0 2011.10.24
 */
public interface BINOLMORPT04_IF extends ICherryInterface {
	
	/**
     * 取得答卷信息
     * 
     * @param map 查询条件
     * @return 答卷信息
     */
    public Map<String, Object> getCheckAnswer(Map<String, Object> map);

}
