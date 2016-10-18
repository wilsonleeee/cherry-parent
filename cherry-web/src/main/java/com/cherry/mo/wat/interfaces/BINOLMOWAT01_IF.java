/*
 * @(#)BINOLMOWAT01_IF.java     1.0 2011/4/27
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
 * 终端实时监控Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.4.27
 */
public interface BINOLMOWAT01_IF extends ICherryInterface{
    /**
     * 取得机器总数
     * 
     * @param map
     * @return 机器总数
     */
    public int searchMachineInfoCount(Map<String, Object> map);
    
    /**
     * 取得机器List
     * 
     * @param map
     * @return 机器List
     */
    public List<Map<String, Object>> searchMachineInfoList(Map<String, Object> map) throws Exception;
    
    /**
     * 导出机器Excel
     * 
     * @param map
     * @return 返回机器List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 汇总信息
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
}
