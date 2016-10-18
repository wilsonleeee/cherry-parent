/*
 * @(#)BINOLPTRPS34_IF.java     1.0 2014/9/24
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

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 电商订单详细Interface
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
public interface BINOLPTRPS34_IF extends ICherryInterface{
    /**
     * 获取电商订单主表信息
     * @param map
     * @return
     */
    public Map<String,Object> getESOrderMain(Map<String,Object> map);
    
    /**
     * 获取电商订单明细表信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> geESOrderDetail(Map<String,Object> map);
    
    /**
     * 获取支付方式详细信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPayTypeDetail(Map<String,Object> map);
}