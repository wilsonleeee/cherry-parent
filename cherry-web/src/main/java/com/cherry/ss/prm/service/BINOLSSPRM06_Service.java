/*
 * @(#)BINOLSSPRM06_Service.java     1.0 2010/12/01
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
 * 促销品大中分类添加Service
 * 
 * 
 */
public class BINOLSSPRM06_Service extends BaseService {

	/**
	 * 插入促销产品分类
	 * 
	 * @param map
	 * @return int
	 */
	public void insertPrmType(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM06.insertPrmType");
		baseServiceImpl.save(map);
	}
	
	/**
     * 更新促销产品分类
     * 
     * @param map
     * @return int
     */
    public int updatePrmType(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM06.updatePrmType");
        return baseServiceImpl.update(map);
    }
}
