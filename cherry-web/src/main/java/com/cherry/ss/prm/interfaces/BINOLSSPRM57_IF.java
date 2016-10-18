/*
 * @(#)BINOLSSPRM57_IF.java     1.0 2012/04/13
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
 * 促销品收货一览Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.04.13
 */
public interface BINOLSSPRM57_IF extends ICherryInterface{
    /**
     * 取得发货单总数
     * @param map
     * @return
     */
    public int searchDeliverCount(Map<String, Object> map);
    
    /**
     * 取得发货单List
     * @param map
     * @return
     */
    public List<Map<String,Object>> searchDeliverList(Map<String, Object> map);
    
    /**
     * 取得促销品总数量和总金额
     * @param map
     * @return
     */
    public Map<String,Object> getSumInfo(Map<String, Object> map);
}
