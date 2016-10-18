/*  
 * @(#)BINOLPTUNQ02_Service     1.0 2016-06-06     
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

package com.cherry.pt.unq.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 单码查询Service
 * 
 * @author zw
 * @version 1.0 2016.06.06
 */
public class BINOLPTUNQ02_Service  extends BaseService implements Serializable{
	
	private static final long serialVersionUID = 5973024279742596483L;
	
	/** 
	 * 单码查询生成总数
	 * @param map
	 * @return int
	 * 
	 */
	public int getSingleCodeSearchCount(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ02.getSingleCodeSearchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/** 
	 * 单码查询一览列表
	 * @param map
	 * @return list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSingleCodeList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ02.getSingleCodeList");
		return baseServiceImpl.getList(paramMap);
	}


}
