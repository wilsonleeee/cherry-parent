/*
 * @(#)BINOLCM08_Service.java     1.0 2010/12/08
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
 * 标准区域共通 Service
 * 
 * @author hub
 * @version 1.0 2010.12.08
 */
@SuppressWarnings("unchecked")
public class BINOLCM08_Service extends BaseService{
	/**
	 * 取得省及其所属区域List
	 * 
	 * @param map
	 *            (所属品牌,语言)
	 * @return
	 */
	public List getProvinceList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM08.getStandProvinceList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得regionId下属区域List
	 * 
	 * @param map
	 *            (区域ID，语言)
	 * @return
	 */
	public List getChildRegionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM08.getChildStandRegionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得所有下属区域List
	 * 
	 * @param map
	 *            (区域ID，语言)
	 * @return
	 */
	public List getAllChildStandRegionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM08.getAllChildStandRegionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据区域code或者区域名称以及区域类型去匹配区域
	 * 	code是完全匹配，名称是模糊匹配
	 * 
	 * */
	public List getRegionInfoList(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLCM08.getRegionInfoList");
	}
	
	/**
	 * 取得所有省份、城市、区县数据List
	 *
	 */
	public List<Map<String, Object>> getAllRegionList(){
		Map<String, Object> map = new HashMap<String, Object>();
		return baseServiceImpl.getList(map,"BINOLCM08.getAllRegionList");
	}
}
