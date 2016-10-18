/*
 * @(#)BINOLMOWAT04_IF.java     1.0 2011/5/26
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
 * 异常盘点次数监控Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.5.26
 */
public interface BINOLMOWAT04_IF extends ICherryInterface{
    
    /**
     * 取得异常盘点次数柜台总数
     * 
     * @param map
     * @return 返回异常盘点次数柜台总数
     */
    public int searchCounterInfoCount(Map<String, Object> map);
    
    /**
     * 取得异常盘点次数柜台List
     * 
     * @param map
     * @return 返回异常盘点次数柜台List
     */
    public List<Map<String, Object>> searchCounterInfoList(Map<String, Object> map);
    
    /**
     * 导出异常盘点次数柜台Excel
     * 
     * @param map
     * @return 返回异常盘点次数柜台List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 取得异常盘差总数
     * 
     * @param map
     * @return 返回异常盘差总数
     */
    public int searchAbnormalGainQuantityCount(Map<String, Object> map);
    
    /**
     * 取得异常盘差List
     * 
     * @param map
     * @return 返回异常盘差List
     */
    public List<Map<String, Object>> searchAbnormalGainQuantityList(Map<String, Object> map);
}
