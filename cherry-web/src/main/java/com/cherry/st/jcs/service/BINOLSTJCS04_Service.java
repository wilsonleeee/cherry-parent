/*  
 * @(#)BINOLSTJCS04_Service.java    1.0 2011-8-23     
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

package com.cherry.st.jcs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTJCS04_Service extends BaseService {

	/**
	 * 取得部门仓库关系List
	 * 
	 * 
	 * */
	public List<Map<String, Object>> getInventoryList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS04.getInventoryList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得柜台部门关系总数
	 * 
	 * 
	 * */
	public int getInventoryCount(Map<String, Object> map) {
		map
				.put(CherryConstants.IBATIS_SQL_ID,
						"BINOLSTJCS04.getInventoryCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得非柜台实体仓库List
	 * 
	 * 
	 * */
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS04.getDepotInfoList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得非柜台部门List
	 * 
	 * */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS04.getDepartList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 往仓库部门关系表中写数据
	 * 
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void insertToInventoryInfo(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLSTJCS04.insertToInventoryInfo");
	}

	/**
	 * 判断仓库和部门对应关系是否存在
	 * 
	 * 
	 * */
	public List<Map<String, Object>> isDepartExist(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS04.isDepartExist");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 根据部门仓库关系ID删除数据
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void deleteInvDepRelation(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINOLSTJCS04.deleteInvDepRelation");
	}

	/**
	 * 根据部门仓库关系ID更新数据
	 * 
	 * 
	 * */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void updateInventoryInfo(Map<String, Object> map) {
		baseServiceImpl.update(map, "BINOLSTJCS04.updateInventoryInfo");
	}
}
