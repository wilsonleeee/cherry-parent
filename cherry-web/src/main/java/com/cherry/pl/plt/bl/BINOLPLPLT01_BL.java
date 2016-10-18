/*
 * @(#)BINOLPLPLT01_BL.java     1.0 2010/10/27
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

package com.cherry.pl.plt.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.plt.service.BINOLPLPLT99_Service;

/**
 * 权限类型一览BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT01_BL {
	
	/** 权限类型管理Service */
	@Resource
	private BINOLPLPLT99_Service binOLPLPLT99_Service;
	
	/**
	 * 查询权限类型总数
	 * 
	 * @param map 查询条件
	 * @return 权限类型总数
	 */
	public int getPrivilegeTypeCount(Map<String, Object> map) {
		
		// 查询权限类型总数
		return binOLPLPLT99_Service.getPrivilegeTypeCount(map);
	}

	
	/**
	 * 查询权限类型List
	 * 
	 * @param map 查询条件
	 * @return 权限类型List
	 */
	public List<Map<String, Object>> getPrivilegeTypeList(Map<String, Object> map) {
		
		// 查询权限类型List
		return binOLPLPLT99_Service.getPrivilegeTypeList(map);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		
		// 取得岗位类别信息List
		return binOLPLPLT99_Service.getPositionCategoryList(map);
	}

}
