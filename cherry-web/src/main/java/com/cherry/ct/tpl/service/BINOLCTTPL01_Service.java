/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 沟通模板一览Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.11.12
 */
public class BINOLCTTPL01_Service extends BaseService{
	/**
     * 获取沟通模板List
     * 
     * @param map
     * @return
     * 		沟通模板List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCommTemplateList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL01.getTemplateList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取沟通模板数量
     * 
     * @param map
     * @return
     * 		沟通模板数量
     */
	public int getTemplateCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL01.getTemplateCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 停用沟通模板
	 * @param map
	 * @return
	 */
	public int disableTemplate (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL01.disableTemplate");
		return baseServiceImpl.update(map);
	}
}
