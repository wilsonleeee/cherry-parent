/*
 * @(#)BINOLMOMAN03_IF.java     1.0 2011/3/21
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
package com.cherry.mo.man.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 绑定机器Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.3.21
 */
public interface BINOLMOMAN03_IF extends ICherryInterface{
    /**
     * 绑定柜台
     * @param map
     * @throws CherryException
     */
    public int tran_bindCounter(Map<String, Object> map) throws CherryException;
    
    /**
     * 取得柜台总数
     * @param map
     * @return 返回柜台总数
     */
    public int getCounterInfoCount(Map<String, Object> map);
    
    /**
     * 取得柜台List
     * @param map 查询条件
     * @return 柜台List
     */
    public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map);
}
