/*  
 * @(#)BINOLSTCM05_Service.java    1.0 2011-8-30     
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
package com.cherry.st.common.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTCM05_Service extends BaseService{
	/**
	 * 
	 * 将报损信息写入报损单据主表并返回记录ID
	 * 
	 * */
	public int insertOutboundFreeAll(Map<String,Object> map){
		return (Integer)baseServiceImpl.saveBackId(map, "BINOLSTCM05.insertOutboundFreeAll");
	}
	
	/**
	 *将报损信息写入报损单据明细表
	 * 
	 * 
	 * */
	public void insertOutboundFreeDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTCM05.insertOutboundFreeDetail");
	}
	
	/**
	 *  修改报损单据主表数据。
	 * @param map
	 * @return
	 */
	public int updateOutboundFreeMain(Map<String,Object> map){		
		return baseServiceImpl.update(map, "BINOLSTCM05.updateOutboundFreeMain");
	}
	
	/**
	 * 给定报损单ID，取得概要信息。
	 * @param map
	 * @return
	 */
	public Map<String,Object> getOutboundFreeMainData(Map<String,Object> map){		
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSTCM05.getOutboundFreeMainData");
	}
	/**
	 * 给报损单ID，取得明细信息。
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getOutboundFreeDetailData(Map<String,Object> map){		
		return baseServiceImpl.getList(map, "BINOLSTCM05.getOutboundFreeDetailData");
	}
}
