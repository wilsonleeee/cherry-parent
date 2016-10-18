/*
 * @(#)BINOLBSPOS03_Service.java     1.0 2010/10/27
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

package com.cherry.bs.pos.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 更新岗位画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS03_Service extends BaseService {
	
	/**
	 * 
	 * 更新岗位信息
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updatePosition(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS03.updatePosition");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 岗位结构节点移动
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updatePositionNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS03.updatePositionNode");
		return baseServiceImpl.update(parameterMap);
	}

}
