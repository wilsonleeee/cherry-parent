/*
 * @(#)BINOLPLPLT03_BL.java     1.0 2010/10/27
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.pl.plt.service.BINOLPLPLT99_Service;

/**
 * 更新权限类型BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT03_BL {
	
	/** 权限类型管理Service */
	@Resource
	private BINOLPLPLT99_Service binOLPLPLT99_Service;
	
	/**
	 * 查询权限类型信息
	 * 
	 * @param map 查询条件
	 * @return 权限类型信息
	 */
	public Map<String, Object> getPrivilegeTypeInfo(Map<String, Object> map) {
		
		// 查询权限类型信息
		return binOLPLPLT99_Service.getPrivilegeTypeInfo(map);
	}
	
	/**
	 * 更新权限类型
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public void tran_updatePlt(Map<String, Object> map) throws Exception {
		
		// 更新权限类型
		int result = binOLPLPLT99_Service.updatePlt(map);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
	}
	
	/**
	 * 查询权限类型是否存在
	 * 
	 * @param map 查询条件
	 * @return 权限类型数
	 */
	public String getPltCountVal(Map<String, Object> map) {
		
		// 查询权限类型是否存在
		return binOLPLPLT99_Service.getPltCountVal(map);
	}

}
