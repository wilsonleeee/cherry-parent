/*
 * @(#)BINOLPLPLT02_BL.java     1.0 2010/10/27
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.plt.service.BINOLPLPLT99_Service;

/**
 * 添加权限类型BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLPLT02_BL {
	
	/** 权限类型管理Service */
	@Resource
	private BINOLPLPLT99_Service binOLPLPLT99_Service;
	
	/**
	 * 添加权限类型
	 * 
	 * @param map 添加内容
	 */
	@SuppressWarnings("unchecked")
	public void tran_addPlt(Map<String, Object> map) {
		
		// 取得部门类型List
		List departType = (List)map.get("departType");
		// 取得岗位类别List
		List positionCategoryId = (List)map.get("positionCategoryId");
		// 取得业务类型List
		List businessType = (List)map.get("businessType");
		// 取得操作类型List
		List operationType = (List)map.get("operationType");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(departType != null && !departType.isEmpty()) {
			for(int x = 0; x < departType.size(); x++) {
				for(int i = 0; i < businessType.size(); i++) {
					for(int j = 0; j < operationType.size(); j++) {
						Map<String, Object> newMap = new HashMap<String, Object>();
						newMap.putAll(map);
						newMap.put("departType", departType.get(x));
						newMap.put("businessType", businessType.get(i));
						newMap.put("operationType", operationType.get(j));
						list.add(newMap);
					}
				}
			}
		} else if(positionCategoryId != null && !positionCategoryId.isEmpty()) {
			for(int x = 0; x < positionCategoryId.size(); x++) {
				for(int i = 0; i < businessType.size(); i++) {
					for(int j = 0; j < operationType.size(); j++) {
						Map<String, Object> newMap = new HashMap<String, Object>();
						newMap.putAll(map);
						newMap.put("positionCategoryId", positionCategoryId.get(x));
						newMap.put("businessType", businessType.get(i));
						newMap.put("operationType", operationType.get(j));
						list.add(newMap);
					}
				}
			}
		}
		// 添加权限类型
		binOLPLPLT99_Service.addPlt(list);
	}
	
	/**
	 * 查询权限类型是否存在
	 * 
	 * @param map 查询条件
	 * @return 权限类型数
	 */
	public int getPltCount(Map<String, Object> map) {
		
		// 查询权限类型是否存在
		return binOLPLPLT99_Service.getPltCount(map);
	}

}
