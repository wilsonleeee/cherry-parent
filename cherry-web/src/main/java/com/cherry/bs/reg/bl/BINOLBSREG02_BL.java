/*
 * @(#)BINOLBSREG02_BL.java     1.0 2011/11/23
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
package com.cherry.bs.reg.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.reg.interfaces.BINOLBSREG02_IF;
import com.cherry.bs.reg.service.BINOLBSREG02_Service;

/**
 * 区域详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG02_BL implements BINOLBSREG02_IF {
	
	/** 区域详细画面Service */
	@Resource
	private BINOLBSREG02_Service binOLBSREG02_Service;

	/**
     * 取得区域详细信息
     * 
     * @param map 查询条件
     * @return 区域详细信息
     */
	@Override
	public Map<String, Object> getRegionInfo(Map<String, Object> map) {
		
		// 取得区域详细信息
		return binOLBSREG02_Service.getRegionInfo(map);
	}
	/**
	 * 取得省份或直辖市List
	 * 
	 * @param map 查询条件
	 * @return 省份或直辖市List
	 */
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map) {
		
		// 取得省份或直辖市List
		return binOLBSREG02_Service.getProvinceList(map);
	}
}
