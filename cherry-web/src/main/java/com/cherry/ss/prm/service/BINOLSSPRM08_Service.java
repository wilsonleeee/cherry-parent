/*
 * @(#)BINOLSSPRM08_Service.java     1.0 2010/11/29
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
package com.cherry.ss.prm.service;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 促销品分类Service
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM08_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得促销品类别信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getPrmTypeInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM08.getPrmTypeInfo");
		return (Map) baseServiceImpl.get(map);
	}
}
