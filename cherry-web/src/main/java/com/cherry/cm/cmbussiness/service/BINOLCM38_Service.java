/*
 * @(#)BINOLCM38_Service.java     1.0 2013/06/19
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 沟通第三方接口共通Service
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.19
 */
public class BINOLCM38_Service extends BaseService{
	/**
	 * 获取接口配置信息
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getIFConfigInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM38.getIFConfigInfo");
        return baseServiceImpl.getList(paramMap);
    }
}
