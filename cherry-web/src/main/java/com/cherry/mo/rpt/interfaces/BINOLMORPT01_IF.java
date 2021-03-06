/*  
 * @(#)BINOLMORPT01_IF.java     1.0 2011.10.21  
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 考核答卷一览IF
 * 
 * @author WangCT
 * @version 1.0 2011.10.21
 */
public interface BINOLMORPT01_IF extends ICherryInterface {
	
	/**
     * 取得考核答卷信息总数
     * 
     * @param map 查询条件
     * @return 考核答卷信息总数
     */
    public int getCheckAnswerCount(Map<String, Object> map);
    
    /**
     * 取得考核答卷信息List
     * 
     * @param map 查询条件
     * @return 考核答卷信息List
     */
    public List<Map<String, Object>> getCheckAnswerList(Map<String, Object> map);
    
    /**
     * 导出考核答卷信息Excel
     * 
     * @param map
     * @return 返回考核答卷信息List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
