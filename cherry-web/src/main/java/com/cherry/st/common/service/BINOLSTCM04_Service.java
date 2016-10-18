
/*  
 * @(#)BINOLSTCM04_Service.java    1.0 2011-8-30     
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

public class BINOLSTCM04_Service extends BaseService {

	/**
	 * 往产品移库主表中插入数据并返回记录ID
	 * 
	 * 
	 * */
	public int insertToProductShift(Map<String,Object> map){
		return (Integer)baseServiceImpl.saveBackId(map, "BINOLSTCM04.insertToProductShift");
	}
	
	/**
	 * 往移库明细表中插入数据
	 * 
	 * 
	 * */
	public void insertToProductShiftDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTCM04.insertToProductShiftDetail");
	}
	
	/**
	 * 更新移库单主表
	 * @param map
	 * @return
	 */
	public int updateProductShiftMain(Map<String,Object> map){		
		return baseServiceImpl.update(map, "BINOLSTCM04.updateProductShiftMain");
	}
	
	/**
	 * 取得移库单概要信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getProductShiftMainData(Map<String,Object> map){		
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSTCM04.getProductShiftMainData");
	}
	/**
	 * 取得移库单明细信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getProductShiftDetailData(Map<String,Object> map){		
		return baseServiceImpl.getList(map, "BINOLSTCM04.getProductShiftDetailData");
	}
}
