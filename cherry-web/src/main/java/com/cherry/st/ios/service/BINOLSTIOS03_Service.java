/*
 * @(#)BINOLSTIOS03_Service.java     1.0 2011/04/11
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
package com.cherry.st.ios.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTIOS03_Service extends BaseService{
	/**
	 * 根据产品条码以及厂商编码获取产品厂商ID以及对应的库存
	 * 
	 * 
	 * */
	
	public Map<String,Object> getPrtVenIdAndStock(Map<String,Object> map){
		return (Map<String, Object>) baseServiceImpl.get(map,"BINOLSTIOS03.getPrtVenIdAndStock");
	}
	
	public List<Map<String,Object>> getOrganIdByDepotInfoID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS03.getOrganIdByDepotInfoID");
        return baseServiceImpl.getList(map);
    }
	public String getDepart(Map<String,Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.getDepart");
        return  (String) baseServiceImpl.get(parameterMap);
    }
}
