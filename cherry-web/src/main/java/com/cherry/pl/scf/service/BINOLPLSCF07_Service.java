/*
 * @(#)BINOLPLSCF07_Service.java     1.0 2010/10/27
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

package com.cherry.pl.scf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * code值管理详细Service
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF07_Service extends BaseService {

	/**
	 * 取得code值管理详细
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCodeMDetail(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF07.getCodeManagerDetail");
		// code值管理详细信息
		Map<String, Object> codeManagerInfo = (Map<String, Object>)baseConfServiceImpl.get(map);
		
		return codeManagerInfo;
	}
	
	/**
	 * 查询code表信息List
	 * 
	 * @param map 查询条件
	 * @return List
	 */
	public List<Map<String, Object>> getCoderList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF07.getCoderList");
		return baseConfServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 判断某组织和品牌下的是否已经存在某个codeType
	 * 
	 * */
	public List<Map<String,Object>> isExist(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLSCF07.isExist");
		return baseConfServiceImpl.getList(map);
	}
}
