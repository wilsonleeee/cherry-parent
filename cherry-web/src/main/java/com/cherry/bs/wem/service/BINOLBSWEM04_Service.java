/*
 * @(#)BINOLBSRES01_Service.java     1.0 2014/10/29
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
package com.cherry.bs.wem.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;


/**
 * 微商一览
 * 
 * @author Hujh
 * @version 1.0 2015/08/18
 */
public class BINOLBSWEM04_Service extends BaseService{

	
	public int getWechatMerchantCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM04.getWechatMerchantCount");
		return baseServiceImpl.getSum(map);
	}

	public List<Map<String, Object>> getWechatMerchantList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM04.getWechatMerchantList");
		return baseServiceImpl.getList(map);		
	}

	/**
	 * 获取微店预留号总数
	 * @param map
	 * @return
	 */
	public int getReservedCodeCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM04.getReservedCodeCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 获取微店预留号List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReservedCodeList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM04.getReservedCodeList");
		return baseServiceImpl.getList(map);	
	}

	/**
	 * 更新预留号可用或不可用状态
	 * @param map
	 */
	public void setReservedCodeInvalid(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM04.setReservedCodeInvalid");
		baseServiceImpl.update(map);
	}

	/**
	 * 获取微商帐户信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAgentAccountInfoList(Map<String, Object> map) {
		return baseServiceImpl.getList(map, "BINOLBSWEM04.getAgentAccountInfoList");
	}

	/**
	 * 判断手机号是否在申请表中
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMobExistsInAgentApply(Map<String, Object> map) {
		return (Map<String, Object>) baseServiceImpl.get(map, "BINOLBSWEM04.getMobExistsInAgentApply");
	}

}