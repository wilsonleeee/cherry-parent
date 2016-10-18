/*
 * @(#)BINOLPLRLM01_BL.java     1.0 2010/10/27
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.rlm.service.BINOLPLRLM99_Service;

/**
 * 角色一览BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM01_BL {
	
	/** 角色管理Service */
	@Resource
	private BINOLPLRLM99_Service binOLPLRLM99_Service;
	
	/**
	 * 取得角色信息总数
	 * 
	 * @param map
	 * @return 件数
	 */
	public int getRoleInfoCount(Map<String, Object> map) {
		
		// 取得角色信息总数
		return binOLPLRLM99_Service.getRoleInfoCount(map);
	}

	
	/**
	 * 取得角色信息List
	 * 
	 * @param map
	 * @return 角色信息List
	 */
	public List<Map<String, Object>> getRoleInfoList(Map<String, Object> map) {
		
		// 取得角色信息List
		return binOLPLRLM99_Service.getRoleInfoList(map);
	}

}
