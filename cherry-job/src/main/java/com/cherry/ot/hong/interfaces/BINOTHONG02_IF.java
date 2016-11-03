/*
 * @(#)BINOTHONG01_IF.java     1.0 2014/09/04
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
package com.cherry.ot.hong.interfaces;

import java.util.Map;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 宏巍电商订单获取 IF
 * 
 * 
 * @author jjw
 * @version 1.0 2015.12.07
 */
public interface BINOTHONG02_IF extends ICherryInterface{
    /**
     * 宏巍电商订单获取 Batch处理
     * @param map
     * @return
     * @throws Exception 
     */
    public int tran_batchOTHONG(Map<String, Object> map) throws CherryBatchException, Exception;

}