/*
 * @(#)BINOLPTRPS34_BL.java     1.0 2014/9/24
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.rps.interfaces.BINOLPTRPS34_IF;
import com.cherry.pt.rps.service.BINOLPTRPS34_Service;

/**
 * 
 * 电商订单详细BL
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS34_BL implements BINOLPTRPS34_IF{

    @Resource(name="binOLPTRPS34_Service")
    private BINOLPTRPS34_Service binOLPTRPS34_Service;
    
    @Override
    public Map<String, Object> getESOrderMain(Map<String, Object> map) {
        return binOLPTRPS34_Service.getESOrderMain(map);
    }

    @Override
    public List<Map<String, Object>> geESOrderDetail(Map<String, Object> map) {
        return binOLPTRPS34_Service.geESOrderDetail(map);
    }

    @Override
    public List<Map<String, Object>> getPayTypeDetail(Map<String, Object> map) {
        return binOLPTRPS34_Service.getPayTypeDetail(map);
    }
}