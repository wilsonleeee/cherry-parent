/*
 * @(#)BINOLMBLEL01_Service.java     1.0 2011/07/20
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

package com.cherry.mb.lel.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 会员等级维护Service
 * 
 * @author lipc
 * @version 1.0 2011/07/20
 */
public class BINOLMBLEL01_Service extends BaseService {
	
	/**
	 * 取得会员等级List
	 * 
	 * @param brandInfoId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLevelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getLevelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员等级详细List
	 * 
	 * @param brandInfoId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLelDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getLelDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 添加会员等级表
	 * 
	 * @param map
	 * @return
	 */
	public int addLevel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.addLevel");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新会员等级表
	 * 
	 * @param map
	 * @return
	 */
	public int updLevel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.updLevel");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 删除会员等级表
	 * 
	 * @param map
	 * @return
	 */
	public int delLevel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.delLevel");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 添加会员等级详细表
	 * 
	 * @param map
	 * @return
	 */
	public void addLevelDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.addLevelDetail");
		baseServiceImpl.save(map);
	}
	/**
	 * 删除会员等级详细表
	 * 
	 * @param map
	 * @return
	 */
	public int delLevelDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.delLevelDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得会员等级ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getLevelId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getLevelId");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新会员等级表(会员有效期)
	 * 
	 * @param map
	 * @return
	 */
	public int updMemberDate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.updMemberDate");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员默认等级标志
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getDefaultFlag(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getDefaultFlag");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	

	/**
	 * 取得会员等级个数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getMemberCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getMemberCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	

	/**
	 * 取得升降级规则个数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getUpLevelRuleCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getUpLevelRuleCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得默认等级
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getDefaultLevel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBLEL01.getDefaultLevel");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
}
