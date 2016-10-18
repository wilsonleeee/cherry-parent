/*
 * @(#)BINOLSTJCS12_BL.java     1.0 2015/12/18
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
package com.cherry.st.jcs.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.st.jcs.interfaces.BINOLSTJCS12_IF;
import com.cherry.st.jcs.service.BINOLSTJCS12_Service;

/**
 * 电商产品对应关系一览BL
 * @author lzs	
 * @version 1.0 2015.12.18
 */
public class BINOLSTJCS12_BL implements BINOLSTJCS12_IF{
	
    @Resource(name="binOLSTJCS12_Service")
	private BINOLSTJCS12_Service binOLSTJCS12_service;
	@Override
	public int getProductRelationCount(Map<String, Object> map) {
		return binOLSTJCS12_service.getProductRelationCount(map);
	}

	@Override
	public List<Map<String, Object>> getProductRelationList (Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLSTJCS12_service.getProductRelationList(map);
	}
}

	

