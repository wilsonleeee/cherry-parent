/*	
 * @(#)BINOLPLGAD01_Service.java     1.0 @2012-12-5		
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


/**
 *
 * 小工具管理Service
 *
 * @author jijw
 *
 * @version  2012-12-5
 */
@SuppressWarnings("unchecked")
public class BINOLPLGAD01_Service extends BaseService {
	
	/**
	 * 查询小工具信息总数
	 * @param map
	 * @return
	 */
	public int getGadgetInfoCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLGAD01.getGadgetInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询小工具信息List
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getGadgetInfoList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLGAD01.getGadgetInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据ID取得小工具信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getGadgetInfoById(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLGAD01.getGadgetInfoById");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新小工具信息
	 * @param map
	 * @return
	 * 
	 * */
	public int updGadgetInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLGAD01.updGadgetInfo");
		return baseServiceImpl.update(map);
	}
}
