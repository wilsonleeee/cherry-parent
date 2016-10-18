/*
 * @(#)BINOLSTBIL14_Service.java     1.0 2012/7/24
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
package com.cherry.st.bil.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 退库申请单明细一览Service
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
public class BINOLSTBIL14_Service extends BaseService{
    /**
     * 删除【产品退库申请单据明细表】
     * @param map
     * @return
     */
    public int deleteProReturnReqDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL14.deleteProReturnReqDetail");
        return baseServiceImpl.update(map);
    }
}
