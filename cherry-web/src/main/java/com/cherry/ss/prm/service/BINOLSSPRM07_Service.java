/*
 * @(#)BINOLSSPRM07_Service.java     1.0 2010/10/29
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

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品分类编辑
 * @author Administrator
 *
 */

public class BINOLSSPRM07_Service extends BaseService {
	
	/**
	 * 更新促销产品分类信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmTypeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM07.updatePrmTypeInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新促销品分类大分类名称
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmTypePrimary(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM07.updatePrmTypePrimary");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新促销品分类中分类名称
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmTypeSecondry(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM07.updatePrmTypeSecondry");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 更新促销品分类小分类名称
	 * 
	 * @param map
	 * @return int
	 */
	public int updatePrmTypeSmall(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM07.updatePrmTypeSmall");
		return baseServiceImpl.update(map);
		 
	}
}
