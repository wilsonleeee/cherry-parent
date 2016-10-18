/*	
 * @(#)BINOLPTRPS06_BL.java     1.0 2010/11/25		
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

import com.cherry.pt.rps.interfaces.BINOLPTRPS06_IF;
import com.cherry.pt.rps.service.BINOLPTRPS06_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 产品调入单查询BL
 * 
 * @author weissc
 * @version 1.0 2010.11.25
 * 
 */
public class BINOLPTRPS06_BL extends SsBaseBussinessLogic implements BINOLPTRPS06_IF{

	@Resource
	private BINOLPTRPS06_Service binolptrps06Service;

	/**
	 * 取得调拨记录总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getAllocationCount(Map<String, Object> map) {
		return binolptrps06Service.getAllocationCount(map);
	}

	/**
	 * 取得调拨记录LIST
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		return binolptrps06Service.getAllocationList(map);
	}

	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolptrps06Service.getSumInfo(map);
	}
}
