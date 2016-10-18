/*
 * @(#)BINOLBSRES01_Service.java     1.0 2014/10/29
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
package com.cherry.bs.res.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;


/**
 * 经销商一览
 * @author hujh
 * @version 1.0 2014/11/11
 */
public class BINOLBSRES01_Service extends BaseService{
	
	/**
	 * 获取经销商总数
	 * @param map
	 * @return
	 */
	public int getResCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES01.getResCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取经销商List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES01.getResList");
		return baseServiceImpl.getList(map);
	}
	
	 /**
     * 经销商停用
     * 
     * @param map
     * @return
     */
    public int disableRes(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES01.disableRes");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 经销商启用
     * 
     * @param map
     * @return
     */
    public int enableRes(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSRES01.enableRes");
        return baseServiceImpl.update(map);
    }
	
}