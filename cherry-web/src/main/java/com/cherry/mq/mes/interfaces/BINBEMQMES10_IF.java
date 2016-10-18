/*	
 * @(#)BINBEMQMES10_IF.java     1.0 2012/12/6		
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
package com.cherry.mq.mes.interfaces;

import java.util.Map;

import com.cherry.mq.mes.common.CherryMQException;

/**
 * 合并库存业务 IF
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public interface BINBEMQMES10_IF {
    /**
     * 合并库存
     * @param map
     * @throws Exception
     */
    public void analyzeStockHB (Map<String, Object> map) throws Exception;
    
    /**
     * 设置操作程序名称
     * @param map
     */
    public void setInsertInfoMapKey (Map<String,Object> map);
    
    /**
     * 插入消息信息(MongoDB)
     * @param map
     * @throws CherryMQException
     */
    public void addMongoMsgInfo(Map<String,Object> map) throws CherryMQException;
}