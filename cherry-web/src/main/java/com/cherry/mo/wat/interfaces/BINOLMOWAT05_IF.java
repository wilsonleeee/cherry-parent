/*
 * @(#)BINOLMOWAT05_IF.java     1.0 2011/8/1
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
 * 考勤信息查询Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.8.1
 */
public interface BINOLMOWAT05_IF extends ICherryInterface{
    /**
     * 取得考勤信息总数
     * 
     * @param map
     * @return 考勤信息总数
     */
    public int getAttendanceInfoCount(Map<String, Object> map);
    
    /**
     * 取得考勤信息List
     * 
     * @param map
     * @return 考勤信息List
     */
    public List<Map<String, Object>> getAttendanceInfoList(Map<String, Object> map);
    
    /**
     * 取得考勤统计查询总数
     * 
     * @param map 查询条件
     * @return 返回考勤统计信息总数
     */
    public int getAttendanceCountNum(Map<String, Object> map);
    
    /**
     *  取得考勤统计信息List
     * 
     * @param map 查询条件
     * @return 考勤统计信息List
     */
    public List<Map<String, Object>> getAttendanceCountList(Map<String, Object> map);
    
    /**
     * 取得岗位类别信息List
     * 
     * @param map
     * @return 岗位类别信息List
     */
    public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map);
    
    /**
     * 导出考勤信息Excel
     * 
     * @param map
     * @return 返回考勤信息List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * 导出考勤统计信息Excel
     * 
     * @param map
     * @return 返回考勤统计信息List
     * @throws Exception 
     */
    public byte[] exportCountExcel(Map<String, Object> map) throws Exception;

    /**
     * 取得指定员工的考勤明细的主信息 MAP
     * @param map
     * @return
     */
	public Map<String, Object> getEmployeeInfoById(Map<String, Object> map);
}
