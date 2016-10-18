/*	
 * @(#)BINOLCPCOM06_Service.java     1.0 2013/8/14		
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
package com.cherry.cp.common.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cp.common.CampConstants;

/**
 * 活动信息查询 Service
 * 
 * @author lipc
 * @version 1.0 2013.8.14
 */
public class BINOLCPCOM06_Service extends BaseService {
	
	/**
	 * 根据活动campCode取得活动信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCampInfo(String campCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CampConstants.CAMP_CODE, campCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM06.getCampInfo");
        return (Map<String,Object>)baseServiceImpl.get(map);
	}
}
