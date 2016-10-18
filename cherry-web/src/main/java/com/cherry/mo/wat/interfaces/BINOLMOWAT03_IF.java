/*
 * @(#)BINOLMOWAT03_IF.java     1.0 2011/6/24
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
 * 会员异常数据监控Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.6.24
 */
public interface BINOLMOWAT03_IF extends ICherryInterface{
    /**
     * 取得会员异常数据总数
     * 
     * @param map
     * @return 返回会员异常数据总数
     */
    public int searchMemberInfoCount(Map<String, Object> map);
    
    /**
     * 取得会员异常数据List
     * 
     * @param map
     * @return 返回会员异常数据List
     */
    public List<Map<String, Object>> searchMemberInfoList(Map<String, Object> map);
    
    /**
     * 导出会员异常数据Excel
     * 
     * @param map
     * @return 返回会员异常数据List
     * @throws Exception 
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
