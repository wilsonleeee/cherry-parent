/*
 * @(#)BINOLMOWAT09_IF.java     1.0 2014/12/17
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
 * BA考勤信息查询Interface
 * 
 * @author niushunjie
 * @version 1.0 2014.12.17
 */
public interface BINOLMOWAT09_IF extends ICherryInterface{
    /**
     * 取得BA考勤信息总数
     * 
     * @param map
     * @return BA考勤信息总数
     */
    public int getAttendanceInfoCount(Map<String, Object> map);
    
    /**
     * 取得BA考勤信息List
     * 
     * @param map
     * @return BA考勤信息List
     */
    public List<Map<String, Object>> getAttendanceInfoList(Map<String, Object> map);
    
    /**
     * 导出BA考勤信息Excel
     * 
     * @param map
     * @return 返回BA考勤信息List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}