/*	
 * @(#)BINOLSSPRM41_Service.java     1.0 2010/10/29		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 办事处）柜台退库记录查询service
 * 
 * @author lipc
 * @version 1.0 2010.10.29
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM41_Service extends BaseService{
	
	/**
	 * 取得退库记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM41.getProCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得退库记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProReturnList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM41.getProReturnList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 出入库记录汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM41.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
}
