/*
 * @(#)BINOLBSRES01_Service.java     1.0 2014/10/29
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
package com.cherry.bs.wem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


/**
 * 设置返点分成
 * 
 * @author zhangbo
 * @version 1.0 2015/08/13
 */
public class BINOLBSWEM03_Service extends BaseService{

	public void save(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLBSWEM03.saveRebateDivide");
	}

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDivideConfList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM03.getDivideList");
		return baseServiceImpl.getList(map);
	}
}