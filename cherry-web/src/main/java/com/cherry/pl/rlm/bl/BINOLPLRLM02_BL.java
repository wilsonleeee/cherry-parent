/*
 * @(#)BINOLPLRLM02_BL.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.rlm.service.BINOLPLRLM99_Service;

/**
 * 添加角色BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM02_BL {
	
	/** 角色管理Service */
	@Resource
	private BINOLPLRLM99_Service binOLPLRLM99_Service;
	
	/**
	 * 添加角色
	 * 
	 * @param Map
	 * @return 返回一个自增长id
	 */
	public int tran_addRole(Map<String, Object> map) {
		
		// 添加角色
		return binOLPLRLM99_Service.addRole(map);
	}
	
	/**
	 * 查询指定角色是否存在
	 * 
	 * @param Map
	 * @return 返回角色数 
	 */
	public Map<String, Object> getRoleByRoleName(Map<String, Object> map) {
		
		// 查询指定角色是否存在
		return binOLPLRLM99_Service.getRoleByRoleName(map);
	}

}
