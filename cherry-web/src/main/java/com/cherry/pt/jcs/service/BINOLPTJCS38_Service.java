/*
 * @(#)BINOLPTJCS38_Service.java     1.0 2015/01/19
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
package com.cherry.pt.jcs.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 *产品功能开启时间维护一览（service）
 * 
 * @author jijw
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS38_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得渠道总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrtFunCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS38.getPrtFunCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得方案List
	 * 
	 * @param map
	 * @return
	 */
	public List getPrtFunList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS38.getPrtFunList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 产品功能开启时间 停用/启用
     * 
     * @param map
     * @return
     */
    public int disOrEnablePrtFun(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS38.disOrEnablePrtFun");
        return baseServiceImpl.update(map);
    }
    
}