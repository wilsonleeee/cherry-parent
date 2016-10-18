/*
 * @(#)BINOLBSDEP04_Service.java     1.0 2010/10/27
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

package com.cherry.bs.dep.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 添加部门画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP04_Service extends BaseService {
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewNodeId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getNewNodeId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加部门
	 * 
	 * @param map 添加内容
	 * @return 部门ID
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int addOrganization(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addOrganization");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加地址
	 * 
	 * @param map 添加内容
	 * @return 地址ID
	 */
	public int addAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addAddress");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加下属机构地址
	 * 
	 * @param map 添加内容
	 */
	public int addSubordinateAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addSubordinateAddress");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加部门联系人
	 * 
	 * @param map 添加内容
	 */
	public void addContactInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addContactInfo");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 验证同一组织中是否存在同样的部门ID
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getOrganizationIdCheck(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getOrganizationIdCheck");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加仓库
	 * 
	 * @param map 添加内容
	 * 
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int addDepotInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addDepotInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 
	 * 更新柜台仓库名称
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateDepotInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.updateDepotInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 添加部门仓库关系
	 * 
	 * @param map 添加内容
	 * 
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public void addInventoryInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.addInventoryInfo");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 取得仓库信息
	 * 
	 * @param map 查询条件
	 */
	public Map<String, Object> getDepotInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getDepotInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得仓库信息List
	 * 
	 * @param map 查询条件
	 */
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getDepotInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 判断仓库编码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getDepotCountByCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getDepotCountByCode");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询上级部门信息
	 * 
	 * @param map 查询条件
	 * @return 上级部门信息
	 */
	public Map<String, Object> getHigherDepart(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP04.getHigherDepart");
		return (Map)baseServiceImpl.get(parameterMap);
	}

}
