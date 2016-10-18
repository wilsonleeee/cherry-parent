/*
 * @(#)BINOLPTRPS33_IF.java     1.0 2014/9/24
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
package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 电商订单一览Interface
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
public interface BINOLPTRPS33_IF extends ICherryInterface{
    /**
     * 获取电商订单记录总数
     * @param map
     * @return
     */
    public int getESOrderMainCount(Map<String,Object> map);
    
    /**
     * 获取电商订单记录LIST
     * @param map
     * @return
     */
    public List<Map<String,Object>> getESOrderMainList(Map<String,Object> map);

    /**
     * 获取产品的总数量和总金额
     * 
     * */
    public Map<String,Object> getSumInfo(Map<String,Object> map);
    
    /**
     * Excel导出
     * 
     * @param map
     * @return 返回导出信息
     * @throws CherryException
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 导出CSV处理
     * 
     * @param map
     * @return 导出文件地址
     * @throws Exception
     */
    public String exportCSV(Map<String, Object> map) throws Exception;
    
    /**
     * 取得导出文件名（国际化）
     * 
     * @param map
     * @return
     * @throws Exception
     */
    public String getExportName(Map<String, Object> map) throws Exception;
    
    /**
     * 获取电商订单记录明细总数
     * @param map
     * @return
     */
    public int getExportDetailCount(Map<String, Object> map);
}