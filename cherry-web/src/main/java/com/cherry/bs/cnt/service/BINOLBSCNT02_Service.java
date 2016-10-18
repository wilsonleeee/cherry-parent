/*	
 * @(#)BINOLBSCNT02_Service.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	柜台详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT02_Service extends BaseService {
	
	/**
	 * 取得柜台详细信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getCounterInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得管辖或者关注指定柜台的人的信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getEmployeeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得上级部门信息
	 * 
	 * @param map
	 * @return 上级部门信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getHigherDepart(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getHigherDepart");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得柜台事件信息总数
	 * 
	 * @param map
	 * @return
	 */
	public int getCounterEventCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getCounterEventCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得柜台事件信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterEventList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getCounterEventList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台方案信息总数
	 * @author ZhaoChaoFan
	 * @param map
	 * @return
	 */
	public int getCounterSolutionCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getCounterSolutionCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得柜台方案信息List
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterSolutionList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT02.getCounterSolutionList");
		return baseServiceImpl.getList(map);
	}
	

}
