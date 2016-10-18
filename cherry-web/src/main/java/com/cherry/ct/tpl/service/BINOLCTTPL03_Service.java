/*
 * @(#)BINOLCTTPL03_Service.java     1.0 2013/10/08
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
package com.cherry.ct.tpl.service;

import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 沟通模板一览Service
 * 
 * @author ZhangLe
 * @version 1.0 2013.10.08
 */
public class BINOLCTTPL03_Service extends BaseService {

	/**
	 * 更新模板变量
	 * @param map
	 * @return
	 */
	public int updateVariable(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL03.updateVariable");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 停用或启用模版变量
	 * @param map
	 * @return
	 */
	public int disOrEnable(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL03.disOrEnableVariable");
		return baseServiceImpl.update(map);
	}
}
