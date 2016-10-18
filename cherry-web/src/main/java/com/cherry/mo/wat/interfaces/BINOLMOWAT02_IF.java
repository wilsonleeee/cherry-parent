/*
 * @(#)BINOLMOWAT02_IF.java     1.0 2011/5/11
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
package com.cherry.mo.wat.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 销售异常数据监控Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.5.11
 */
public interface BINOLMOWAT02_IF extends ICherryInterface{
    /**
     * 取得销售异常柜台总数
     * 
     * @param map
     * @return 返回销售异常柜台总数
     */
    public int searchCounterInfoCount(Map<String, Object> map);
    
    /**
     * 取得销售异常柜台List
     * 
     * @param map
     * @return 返回销售异常柜台List
     */
    public List<Map<String, Object>> searchCounterInfoList(Map<String, Object> map);
    
    /**
     * 导出销售异常柜台Excel
     * 
     * @param map
     * @return 返回销售异常柜台List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
