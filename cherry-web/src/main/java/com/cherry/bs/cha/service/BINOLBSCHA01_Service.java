/*
 * @(#)BINOLBSCHA01_Service.java     1.0 2010/11/03
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
package com.cherry.bs.cha.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 *渠道一览
 * 
 * @author weisc
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLBSCHA01_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得渠道总数
	 * 
	 * @param map
	 * @return
	 */
	public int getChannelCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA01.getChannelCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得渠道List
	 * 
	 * @param map
	 * @return
	 */
	public List getChannelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA01.getChannelList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 渠道停用
     * 
     * @param map
     * @return
     */
    public int disableChannel(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA01.disableChannel");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 渠道启用
     * 
     * @param map
     * @return
     */
    public int enableChannel(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCHA01.enableChannel");
        return baseServiceImpl.update(map);
    }
}