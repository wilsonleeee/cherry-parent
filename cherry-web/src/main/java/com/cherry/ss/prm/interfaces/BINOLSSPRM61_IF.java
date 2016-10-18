/*
 * @(#)BINOLSSPRM61_IF.java     1.0 2012/09/27
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 促销品移库一览IF
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public interface BINOLSSPRM61_IF extends ICherryInterface{
    /**
     * 取得移库单总数
     * @param map
     * @return
     */
    public int searchShiftCount(Map<String, Object> map);
    
    /**
     * 取得移库单List
     * @param map
     * @return
     */
    public List<Map<String, Object>> searchShiftList(Map<String, Object> map);
    
    /**
     * 取得汇总信息
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
}
