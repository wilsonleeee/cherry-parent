/*
 * @(#)BINOLPLUPM02_Service.java     1.0 2010/12/29
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
package com.cherry.pl.upm.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 用户添加Service
 * 
 */
public class BINOLPLUPM02_Service extends BaseService {

	/**
	 * 插入用户信息表并且返回用户ID
	 * 
	 * @param map
	 * @return int
	 */
	public void insertUser(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM02.insertUser");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得密码安全配置信息
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getPassWordConfig (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM02.getPassWordConfig");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 验证是否存在同样的登入账号
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getLoginNameCheck(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPLUPM02.getLoginNameCheck");
		return (String)baseServiceImpl.get(map);
	}
}
