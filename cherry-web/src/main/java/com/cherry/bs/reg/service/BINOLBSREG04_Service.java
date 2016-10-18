/*
 * @(#)BINOLBSREG04_Service.java     1.0 2011/11/23
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
package com.cherry.bs.reg.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 区域添加画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public class BINOLBSREG04_Service extends BaseService {
	
	/**
	 * 添加区域
	 * 
	 * @param map 添加内容
	 */
	public void addRegion(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.addRegion");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 取得新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewRegNodeId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.getNewRegNodeId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	/**
	 * 
	 * 区域结构节点移动
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateRegionNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.updateRegionNode");
		return baseServiceImpl.update(parameterMap);
	}
	/**
	 * 取得品牌下的新节点
	 * 
	 * @param map 查询条件
	 * @return 新节点
	 */
	public String getNewRegNodeIdInBrand(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.getNewRegNodeIdInBrand");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据区域ID取得上级区域信息
	 * 
	 * @param map 查询条件
	 * @return 上级区域信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getHigherRegionInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.getHigherRegionInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据区域名称取得类似该区域名称的其他区域信息
	 * 
	 * @param map 查询条件
	 * @return 区域信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLikeRegionInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.getLikeRegionInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 验证区域代码是否唯一
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getRegionCodeCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSREG04.getRegionCodeCount");
		return (Integer)baseServiceImpl.get(parameterMap);
	}

}
