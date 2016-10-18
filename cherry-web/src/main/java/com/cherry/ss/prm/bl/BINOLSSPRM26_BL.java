/*
 * @(#)BINOLSSPRM26_BL.java     1.0 2010/11/05
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM26_Service;

/**
 * 
 * 盘点单明细BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM26_BL {
	
	@Resource
	private BINOLSSPRM26_Service binolssprm26_Service;
	
	/**
	 * 取得盘点单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchTakingInfo(Map<String, Object> map) {
		// 取得盘点单信息
		return binolssprm26_Service.getTakingInfo(map);
	}
	
	/**
	 * 取得盘点单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List searchTakingDetailList(Map<String, Object> map) {
		// 取得盘点单明细List
		return binolssprm26_Service.getTakingDetailList(map);
	}
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binolssprm26_Service.getSumInfo(map);
    }
}
