/*
 * @(#)BINOLPTRPS01_BL.java     1.0 2011/3/11
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pt.rps.interfaces.BINOLPTRPS01_IF;
import com.cherry.pt.rps.service.BINOLPTRPS01_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 盘点查询BL
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS01_BL extends SsBaseBussinessLogic implements BINOLPTRPS01_IF{
    
	@Resource
	private BINOLPTRPS01_Service binOLPTRPS01_Service;
	
	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchTakingCount(Map<String, Object> map) {
		// 取得盘点单总数
		return binOLPTRPS01_Service.getTakingCount(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchTakingList(Map<String, Object> map) {
		// 取得盘点单List
		return binOLPTRPS01_Service.getTakingList(map);
	}

	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLPTRPS01_Service.getSumInfo(map);
    }
}
