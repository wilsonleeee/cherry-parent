/*  
 * @(#)ProductInspection_IF.java     1.0 2015/01/13      
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
 * 查货宝业务 Interfaces
 * 
 * @author niushunjie
 * @version 1.0 2015.01.13
 */
public interface ProductInspection_IF {
    
    /**
     * 货品查询及记录
     * @param paramMap
     * @return
     */
    public Map<String, Object> tran_productInspection(Map<String, Object> paramMap);
    
    /**
     * 货品列表
     * @param paramMap
     * @return
     */
    public Map<String, Object> getProductInspectionList(Map<String, Object> paramMap);
    
    /**
     * 工作日报
     * @param paramMap
     * @return
     */
    public Map<String, Object> getProductInspectionReport(Map<String, Object> paramMap);
}