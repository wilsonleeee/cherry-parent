/*
 * @(#)LoginService.java     1.0 2010/10/12
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
package com.webconsole.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseConfServiceImpl;
import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

@SuppressWarnings("unchecked")
public class LoginService {

	@Resource
	private BaseServiceImpl baseServiceImpl;	
	@Resource
	protected BaseConfServiceImpl baseConfServiceImpl;
	/**
	 * 取得数据源
	 * @param loginName
	 * @return
	 */
	public List getDBByName (String loginName) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("InputName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getDBByName");
		return baseConfServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得指定登录名的相关信息
	 * @param loginName
	 * @return
	 */
	public List checkAccount (String loginName,String psd) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("InputName",loginName);
		parameterMap.put("InputPsd",psd);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.checkAccount");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得指定登录名的相关信息
	 * @param loginName
	 * @return
	 */
	public List<Map<String, Object>> getUserInfoByName (String loginName) {
		HashMap parameterMap  = new HashMap();
		parameterMap.put("InputName",loginName);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "login.getUserInfoByName");
		return baseServiceImpl.getList(parameterMap);
	}

}
