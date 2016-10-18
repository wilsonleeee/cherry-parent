/*	
 * @(#)BINOLRPVALUE_Service     1.0 2010/11/08		
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

package com.cherry.rp.query.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * BI报表查询条件的表示值共通Service
 * @author WangCT
 *
 */
public class BINOLRPVALUE_Service extends BaseService {
	
	/**
	 * 取得渠道信息List
	 * @param map
	 * @return 返回渠道信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChannelList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLRPVALUE.getChannelList");
		return baseServiceImpl.getList(map);
	}

}
