/*
 * @(#)BINOLSTIOS11_IF.java     1.0 2015/02/04
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
package com.cherry.st.ios.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 退库申请（Excel导入）Interface
 * 
 * @author niushunjie
 * @version 1.0 2015.02.04
 */
public interface BINOLSTIOS11_IF extends ICherryInterface {
    /**
     * 获取退库申请单（Excel导入）批次总数
     * @param map
     * @return
     */
    public int getImportBatchCount(Map<String, Object> map);

    /**
     * 获取退库申请单（Excel导入）批次信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getImportBatchList(Map<String, Object> map);

    /**
     * 解析导入的Excel数据
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> ResolveExcel(Map<String, Object> map) throws Exception;

    /**
     * 退库申请导入处理
     * @param importDataMap
     * @param map
     * @return
     * @throws Exception
     */
    public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap, Map<String, Object> sessionMap) throws Exception;

    /**
     * 插入导入批次
     * @param map
     * @return
     */
    public int insertImportBatch(Map<String, Object> map);
    
    /**
     * 获取退库申请单（Excel导入）总数
     * @param map
     * @return
     */
    public int getBillExcelCount(Map<String, Object> map);
    
    /**
     * 获取退库申请单（Excel导入）信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBillExcelList(Map<String, Object> map);
    
    /**
     * 获取退库申请单产品明细（Excel导入）
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBillExcelDetailList(Map<String, Object> map);
    
    /**
     * 通过ID获取单条退库申请单（Excel导入）主信息
     * @param map
     * @return
     */
    public Map<String, Object> getBillExcelInfo(Map<String, Object> map);
    
    /**
     * 获得导出Excel
     * @param map
     * @return
     * @throws Exception
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}