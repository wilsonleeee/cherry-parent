/*
 * @(#)BINOLBSREG05_Service.java     1.0 2011/11/23
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
package com.cherry.bs.reg.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域启用停用画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG05_Service extends BaseService {
	
	/**
	 * 启用停用区域处理
	 * 
	 * @param map 查询条件
	 */
	public int updateRegValidFlag(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG05.updateRegValidFlag");
		return baseServiceImpl.update(parameterMap);
	}

}
