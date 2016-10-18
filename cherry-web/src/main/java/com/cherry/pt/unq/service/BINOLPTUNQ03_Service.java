/*  
 * @(#)BINOLPTUNQ03_Service     1.0 2016-06-17     
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
 * 唯一码维护Service
 * 
 * @author zw
 * @version 1.0 2016.06.17
 */
public class BINOLPTUNQ03_Service  extends BaseService implements Serializable{
	

	private static final long serialVersionUID = -24757369115502962L;
	
	/** 
	 * 查询唯一码基础信息
	 * @param map
	 * @return Map
	 * 
	 */
	@SuppressWarnings("unchecked")
    public Map<String, Object>  getUnqInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ03.getUnqInfo");
		return (Map)baseServiceImpl.get(paramMap);
     }
	
	/** 
	 * 查询产品产商基础信息
	 * @param map
	 * @return Map
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUnitCodeInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ03.getUnitCodeInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/** 
	 * 唯一码信息维护
	 * @param map
	 * @return int
	 * 
	 */
	public int updateUnqCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ03.updateUnqCode");
		return baseServiceImpl.update(paramMap);
		
	}


}
