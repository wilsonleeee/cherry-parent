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
/**
 * 
 * 电商产品对应关系一览
 * @author lzs
 * @version 1.0 2015.12.18
 * 
 **/
package com.cherry.st.jcs.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 电商产品对应关系一览service
 * @author lzs
 * @version 1.0 2015.12.18
 */
public class BINOLSTJCS12_Service extends BaseService{
	
	/**
	 * 获取电商产品对应关系信息count
	 * @param map
	 * @return
	 */
	public int getProductRelationCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS12.getProductRelationCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取电商产品对应关系一览List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductRelationList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS12.getProductRelationList");
		return baseServiceImpl.getList(map);
	}
}
