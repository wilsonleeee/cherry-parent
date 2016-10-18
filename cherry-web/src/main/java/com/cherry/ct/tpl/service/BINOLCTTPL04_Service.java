/*
 * @(#)BINOLCTTPL04_Service.java     1.0 2013/11/19
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 沟通模板非法字符设置Service
 * 
 * @author ZhangLe
 * @version 1.0 2013.11.19
 */
public class BINOLCTTPL04_Service extends BaseService {

	/**
	 * 获取非法字符Count
	 * @param map
	 * @return
	 */
	public int getIllegalCharCount(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL04.getIllegalCharCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取非法字符List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getIllegalCharList(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL04.getIllegalCharList");
		return baseServiceImpl.getList(map);
		
	}
	
	/**
	 * 添加非法字符
	 * @param map
	 * @return
	 */
	public int addIllegalChar(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL04.addIllegalChar");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新非法字符
	 * @param map
	 * @return
	 */
	public int updateIllegalChar(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL04.updateIllegalChar");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 获取非法字符Map
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getIllegalCharMap(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL04.getIllegalCharMap");
		return (Map<String, Object>) baseServiceImpl.get(map);
		
	}
}
