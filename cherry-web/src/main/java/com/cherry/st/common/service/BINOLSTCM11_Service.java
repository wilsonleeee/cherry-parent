
/*  
 * @(#)BINOLSTCM11_Service.java    1.0 2011-12-2     
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 产品收货
 * 
 * @author zhanggl
 * @date 2011-12-2
 * 
 * */

public class BINOLSTCM11_Service extends BaseService {

	/**
	 * 插入产品收货主表,返回自增ID
	 * 
	 * */
	public int insertProductReceiveMain(Map<String,Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM11.insertProductReceiveMain");
        return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 插入产品收货明细表
	 * 
	 * 
	 * */
	public void insertProductReceiveDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTCM11.insertProductReceiveDetail");
	}
	
	/**
	 * 根据收货单ID取得收货单主表信息
	 * 
	 * 
	 * */
	public Map<String,Object> getProductReceiveMainData(Map<String,Object> paramMap){
		return (Map<String, Object>) baseServiceImpl.get(paramMap,"BINOLSTCM11.getProductReceiveMainData");
	}
	
	/**
	 * 根据收货单ID取得收货单明细信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getProductReceiveDetailData(Map<String,Object> paramMap){
		return (List<Map<String,Object>>) baseServiceImpl.getList(paramMap,"BINOLSTCM11.getProductReceiveDetailData");
	}
	
    /**
     * 修改入收货单主表数据。
     * @param map
     * @return
     */
    public int updateProductReceiveMain(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM11.updateProductReceiveMain");
        return baseServiceImpl.update(parameterMap);
    }
}
