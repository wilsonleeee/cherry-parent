/*
 * @(#)GadgetCommonService.java     1.0 2012/12/06
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
package com.cherry.shindig.gadgets.service.common;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 小工具共通Service
 * 
 * @author WangCT
 * @version 1.0 2012/12/06
 */
public class GadgetCommonService extends BaseService {
	
	/**
	 * 取得小工具参数
	 * 
	 * @param map 查询条件
	 * @return 小工具参数
	 */
	public Map<String, Object> getGadgetParam(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "GadgetCommon.getGadgetParam");
		return (Map)baseServiceImpl.get(parameterMap);
	}

}
