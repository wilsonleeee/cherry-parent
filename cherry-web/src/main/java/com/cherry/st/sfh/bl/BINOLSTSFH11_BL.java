/*	
 * @(#)BINOLSTSFH11_BL.java     1.0 2012/11/15		
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
package com.cherry.st.sfh.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.st.sfh.interfaces.BINOLSTSFH11_IF;
import com.cherry.st.sfh.service.BINOLSTSFH11_Service;

/**
 * 产品收货一览BL
 * 
 * @author niushunjie
 * @version 1.0 2012.11.15
 * 
 */
public class BINOLSTSFH11_BL implements BINOLSTSFH11_IF{
    @Resource(name="binOLSTSFH11_Service")
    private BINOLSTSFH11_Service binOLSTSFH11_Service;

    /**
     * 取得发货单总数
     * 
     * @param map
     * @return
     */
    @Override
    public int searchDeliverCount(Map<String, Object> map) {
        // 取得发货单总数
        return binOLSTSFH11_Service.getDeliverCount(map);
    }
    
    /**
     * 取得发货单List
     * 
     * @param map
     * @return
     */
    @Override
    public List<Map<String,Object>> searchDeliverList(Map<String, Object> map) {
        // 取得发货单List
        return binOLSTSFH11_Service.getDeliverList(map);
    }
    
    /**
     * 取得产品总数量和总金额
     * @param map
     * @return
     */
    @Override
    public Map<String,Object> getSumInfo(Map<String,Object> map){
        return binOLSTSFH11_Service.getSumInfo(map);
    }
}