/*
 * @(#)BINOLLGTOP03_Service.java     1.0 2011/02/22
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
package com.cherry.lg.top.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 用户信息查询
 * @author zj
 *
 */

public class BINOLLGTOP03_Service extends BaseService {
	
	/**
	 * 更新用户信息
	 * 
	 * @param map
	 * @return int
	 */
	public int updateUser(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLLGTOP03.updateUser");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得用户密码
	 * 
	 * @param map
	 * @return 用户密码
	 */
	public String getUserPassWord(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLLGTOP03.getUserPassWord");
		return (String)baseServiceImpl.get(map);
		 
	}
}
