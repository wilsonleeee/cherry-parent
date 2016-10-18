/*
 * @(#)BINOLPLSCF06_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.cherry.pl.scf.service.BINOLPLSCF14_Service;


/**
 * code值管理一览BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF14_BL {
	
	/** code值管理一览Service */
	@Resource
	private BINOLPLSCF14_Service binolplscf14Service;

	/**
	 * 取得所有组织List
	 * 
	 * @param Map
	 *			查询条件
	 * @return List
	 *			所有的组织信息
	 */
	public List getOrgInfoList() {
		return binolplscf14Service.getOrgInfoList();
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			品牌brandCodeList
	 */
	public List<Map<String, Object>> getBrandCodeList(Map<String, Object> map) {
		// 取得品牌CodeList
		return binolplscf14Service.getBrandCodeList(map);
	}
	
	/**
	 * 处理文件
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			品牌CodeList
	 */
	public void tran_handleFile(Map<String, Object> fileMap) {
		List<Map<String, Object>> insertList = (List<Map<String, Object>>) fileMap.get("insertList");
		List<Map<String, Object>> updateList = (List<Map<String, Object>>) fileMap.get("updateList");
		if(null != insertList && !insertList.isEmpty()){
			// 批量插入文件信息
			binolplscf14Service.insertFile(insertList);
		}
		if(null != updateList && !updateList.isEmpty()){
			// 批量更新文件信息
			binolplscf14Service.updateFile(updateList);
		}
	}
	
	/**
	 * 取得当前文件在数据中信息
	 * 
	 * @param map 
	 * 			查询条件
	 * @return List
	 * 			品牌brandCodeList
	 */
	public Map<String, Object> getFileCount(Map<String, Object> map) {
		// 取得品牌CodeList
		return binolplscf14Service.getFileCount(map);
	}
	
	
}
