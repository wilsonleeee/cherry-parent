/*	
 * @(#)BINOLPLGAD02_Service.java     1.0 2013.8.29		
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
package com.cherry.pl.gad.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 小工具布局配置Service
 *
 * @author WangCT
 * @version 1.0 2013.8.29
 */
public class BINOLPLGAD02_Service extends BaseService {
	
	/**
	 * 查询某个画面的所有小工具List
	 * 
	 * @param map 查询条件
	 * @return 小工具List
	 */
	public List<Map<String,Object>> getGadgetInfoList(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLGAD02.getGadgetInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新小工具布局配置
	 * 
	 * @param list 更新条件
	 */
	public void updGadgetInfo(List<Map<String,Object>> list){
		baseServiceImpl.updateAll(list, "BINOLPLGAD02.updGadgetInfo");
	}

}
