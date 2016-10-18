/*	
 * @(#)BINOLPTRPS10_BL.java     1.0 2011/03/10		
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

import com.cherry.pt.rps.interfaces.BINOLPTRPS10_IF;
import com.cherry.pt.rps.service.BINOLPTRPS10_Service;

/**
 * 出入库记录详细BL
 * 
 * @author lipc
 * @version 1.0 2011.03.10
 * 
 */
public class BINOLPTRPS10_BL implements BINOLPTRPS10_IF {

	@Resource
	private BINOLPTRPS10_Service binolptrps10Service;

	@Override
	public Map<String, Object> getProInOutInfo(Map<String, Object> map) {
		
		return binolptrps10Service.getProInOutInfo(map);
	}

	@Override
	public List<Map<String, Object>> getProInOutList(Map<String, Object> map) {

		return binolptrps10Service.getProInOutList(map);
	}
}
