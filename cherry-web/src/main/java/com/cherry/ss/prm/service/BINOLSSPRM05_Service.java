/*
 * @(#)BINOLSSPRM05_Service.java     1.0 2010/10/27
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
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品分类查询
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM05_Service extends BaseService {

	
	/**
	 * 取得促销产品分类总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrmTypeCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM05.getPrmTypeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品分类信息List
	 * 
	 * @param map
	 * @return
	 */
	public List getPrmTypeList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM05.getPrmTypeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 伦理删除促销产品分类信息
	 * 
	 * @param map
	 * @return
	 */
	public int operatePrmType (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM05.invalidPrmType");
		return baseServiceImpl.update(map);
	}
	
}
