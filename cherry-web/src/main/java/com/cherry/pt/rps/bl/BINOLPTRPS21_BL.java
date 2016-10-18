/*
 * @(#)BINOLPTRPS21_BL.java     1.0 2010/11/05
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

import com.cherry.pt.rps.interfaces.BINOLPTRPS21_IF;
import com.cherry.pt.rps.service.BINOLPTRPS21_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 盘点单明细BL
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS21_BL extends SsBaseBussinessLogic implements BINOLPTRPS21_IF{
	
	@Resource
	private BINOLPTRPS21_Service binolptrps21_Service;
	
	/**
	 * 取得盘点单信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> searchTakingInfo(Map<String, Object> map) {
		// 取得盘点单信息
		return binolptrps21_Service.getTakingInfo(map);
	}
	
	/**
	 * 取得盘点单明细List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchTakingDetailList(Map<String, Object> map) {
		// 取得盘点单明细List
		return binolptrps21_Service.getTakingDetailList(map);
	}
}
