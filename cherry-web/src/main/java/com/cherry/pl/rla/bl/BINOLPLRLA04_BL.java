/*
 * @(#)BINOLPLRLA04_BL.java     1.0 2010/10/27
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

package com.cherry.pl.rla.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.rla.service.BINOLPLRLA04_Service;

/**
 * 用户角色分配BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLA04_BL {
	
	/** 用户角色分配Service */
	@Resource
	private BINOLPLRLA04_Service binOLPLRLA04_Service;
	
	/**
	 * 取得用户总数
	 * 
	 * @param Map
	 * @return 返回用户总数
	 */
	public int getUserInfoCount(Map<String, Object> map) {
		
		// 取得用户总数
		return binOLPLRLA04_Service.getUserInfoCount(map);
	}
	
	/**
	 * 取得用户信息List
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getUserInfoList(Map<String, Object> map) {
		
		// 取得用户信息List
		return binOLPLRLA04_Service.getUserInfoList(map);
	}

}
