/*  
 * @(#)SaleInfo_IF.java     1.0 2014/08/01      
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

package com.cherry.webservice.sale.interfaces;

import java.util.Map;

/**
 * 
 * 销售业务Interfaces
 * 
 * @author niushunjie
 * @version 1.0 2014.08.01
 */
public interface SaleInfo_IF {
    
    /**
     * 校验接口数据，写入电商订单相关表，如单据状态是2/3/4，需要发送销售/积分兑换MQ。
     * 
     * @param paramMap
     * @return
     */
    public Map<String, Object> tran_changeESOrder(Map<String, Object> paramMap) throws Exception;
 
    /**
     * 更改电商订单单据状态
     * 
     * @param paramMap
     * @return
     */
    public Map<String, Object> tran_changeESBillState(Map<String, Object> paramMap) throws Exception;
    
    /**
     * 获取电商订单业务
     * 
     * @param paramMap
     * @return
     */
    public Map<String,Object> getDSOrderInfo(Map<String, Object> paramMap) throws Exception;
}