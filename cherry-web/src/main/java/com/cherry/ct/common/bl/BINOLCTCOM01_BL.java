/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ct.common.interfaces.BINOLCTCOM01_IF;
import com.cherry.ct.common.service.BINOLCTCOM01_Service;

/**
 * 沟通计划管理BL
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM01_BL implements BINOLCTCOM01_IF{
	
	@Resource
	private BINOLCTCOM01_Service binOLCTCOM01_Service;
	
	/**
	 * 根据活动编号取得沟通计划编号
	 * 
	 * @param Map
	 *			查询条件
	 * @return String
	 *			沟通对象集合名称
	 */
	@Override
	public Map<String, Object> getPlanInfo(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLCTCOM01_Service.getPlanInfo(map);
	}
}
