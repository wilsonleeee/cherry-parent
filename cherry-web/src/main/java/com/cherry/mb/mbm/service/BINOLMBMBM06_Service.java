/*
 * @(#)BINOLMBMBM06_Service.java     1.0 2012.11.27
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员资料修改画面Service
 * 
 * @author WangCT
 * @version 1.0 2012.11.27
 */
public class BINOLMBMBM06_Service extends BaseService {
	
	/**
	 * 更新会员基本信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemberInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		//批量更新模式
		String synMemMode = (String)map.get("synMemMode");
		if(synMemMode != null && "1".equals(synMemMode)){//批量更新模式
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.batchUpdMemInfo");
		}else{
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemberInfo");
		}
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 更新会员卡信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemCardInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemCardInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 会员老卡停用处理
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int delMemCardInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.delMemCardInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 启用会员卡信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemCardValidFlag(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemCardValidFlag");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 更新会员地址信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updAddressInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		//批量更新模式
		String synMemMode = (String)map.get("synMemMode");
		if(synMemMode != null && "1".equals(synMemMode)){//批量更新模式
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.batchUpdAddressInfo");
		}else{
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updAddressInfo");
		}
		
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除会员地址信息
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delAddressInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.delAddressInfo");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除会员地址关系
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delMemberAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.delMemberAddress");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 删除答卷信息
	 * 
	 * @param map 删除条件
	 */
	public void delPaperAnswer(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINOLMBMBM06.delPaperAnswer");
	}
	
	/**
	 * 删除答卷明细信息
	 * 
	 * @param map 删除条件
	 */
	public void delPaperAnswerDetail(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINOLMBMBM06.delPaperAnswerDetail");
	}
	
	/**
	 * 查询会员答卷ID
	 * 
	 * @param map 查询条件
	 * @return 会员答卷信息
	 */
	public String getPaperAnswerId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.getPaperAnswerId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新会员扩展信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemberExtInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		//批量更新模式
		String synMemMode = (String)map.get("synMemMode");
		if(synMemMode != null && "1".equals(synMemMode)){//批量更新模式
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.batchUpdMemberExtInfo");
		}else{
			parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemberExtInfo");
		}
		return baseServiceImpl.update(parameterMap);
	}

	/**
	 * 更新会员扩展信息
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemberExtInfoMain(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemberExtInfoMain");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 根据员工ID查询BA卡号
	 * 
	 * @param map 检索条件
	 * @return 会员卡号
	 */
	public String getEmployeeCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.getEmployeeCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据组织ID查询卡号
	 * 
	 * @param map 检索条件
	 * @return 会员卡号
	 */
	public String getOrganizationCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.getOrganizationCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据会员ID查询会员卡号
	 * 
	 * @param map 检索条件
	 * @return 会员卡号
	 */
	public String getMemberCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.getMemberCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新会员手机号
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updMemberMobile(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM06.updMemberMobile");
		return baseServiceImpl.update(parameterMap);
	}
}
