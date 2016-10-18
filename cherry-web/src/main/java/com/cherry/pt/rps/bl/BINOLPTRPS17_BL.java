/*	
 * @(#)BINOLPTRPS17_BL.java     1.0 2010/11/29		
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

import com.cherry.pt.rps.interfaces.BINOLPTRPS17_IF;
import com.cherry.pt.rps.service.BINOLPTRPS17_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 产品调入详细查询BL
 * 
 * @author weisc
 * @version 1.0 2011.4.3
 * 
 */
public class BINOLPTRPS17_BL extends SsBaseBussinessLogic implements BINOLPTRPS17_IF{

	@Resource
	private BINOLPTRPS17_Service binolptrps017Service;

	/**
	 * 取得调拨单详细
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getAllocationInfo(Map<String, Object> map) {
		return binolptrps017Service.getAllocationInfo(map);
	}

	/**
	 * 取得调拨单详细信息LIST
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		return binolptrps017Service.getAllocationList(map);
	}
}
