/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.service;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 新建沟通计划Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM02_Service extends BaseService{
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSearchRecordList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM02.getSearchRecordList");
		return baseServiceImpl.getList(map);
	}
	
	public int getSearchRecordCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM02.getSearchRecordCount");
		return baseServiceImpl.getSum(map);
	}
	
	public List<Map<String,Object>> getSendType(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM02.getSendType");
		return baseServiceImpl.getList(map);
	}
}
