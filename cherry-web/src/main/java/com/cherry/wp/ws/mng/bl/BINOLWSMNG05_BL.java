/*
 * @(#)BINOLWSMNG05_BL.java     1.0 2014/10/20
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
package com.cherry.wp.ws.mng.bl;

import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.wp.ws.mng.interfaces.BINOLWSMNG05_IF;
import com.cherry.wp.ws.mng.service.BINOLWSMNG05_Service;

/**
 * 
 * 调出BL
 * 
 * @author niushunjie
 * @version 1.0 2014.10.20
 */
public class BINOLWSMNG05_BL implements BINOLWSMNG05_IF {
	
	@Resource(name="binOLWSMNG05_Service")
	private BINOLWSMNG05_Service binOLWSMNG05_Service;

    @Override
    public int getProductAllocationCount(Map<String, Object> map) {
        return binOLWSMNG05_Service.getProductAllocationCount(map);
    }

    @Override
    public List<Map<String, Object>> getProductAllocationList(Map<String, Object> map) {
        return binOLWSMNG05_Service.getProductAllocationList(map);
    }
}