/*		
 * @(#)BINOLSSPRM50_Service.java     1.0 2010/11/16		
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
public class BINOLSSPRM50_Service {
	@Resource
	private BaseServiceImpl baseServeceImpl;
	
	/**
	 * 根据部门代码，取得部门的名称等信息
	 * @param map
	 * @return
	 */
	public List getDepartInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM50.getDepartInfo");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得促销品的信息
	 * @param map
	 * @return
	 */
	public List getPromotionInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM50.getPromotionInfo");
		return baseServeceImpl.getList(map);
	}

}
