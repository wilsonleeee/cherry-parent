/*
 * @(#)BINOLMBMBM11_Service.java     1.0 2013/03/05
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
 * 会员添加画面Service
 * 
 * @author WangCT
 * @version 1.0 2013/03/05
 */
public class BINOLMBMBM11_Service extends BaseService {
	
	/**
	 * 添加会员信息
	 * 
	 * @param map 添加内容
	 * @return 会员ID
	 */
	public int addMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addMemberInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 添加会员持卡信息
	 * 
	 * @param map 添加内容
	 */
	public void addMemCardInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addMemCardInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 添加地址信息
	 * 
	 * @param map 添加内容
	 * @return 地址ID
	 */
	public int addAddressInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addAddressInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 添加会员地址
	 * 
	 * @param map 添加内容
	 */
	public void addMemberAddress(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addMemberAddress");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 添加答卷
	 * 
	 * @param map 添加内容
	 * @return 答卷ID
	 */
	public int addPaperAnswer(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addPaperAnswer");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 添加答卷明细
	 * 
	 * @param map 添加内容
	 */
	public void addPaperAnswerDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLMBMBM11.addPaperAnswerDetail");
	}
	
	/**
	 * 会员卡号唯一验证
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 */
	public String getMemberInfoId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemberInfoId");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取会员绑定标志
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 */
	public Map<String, Object> getMemberPhoneAndBindFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemberPhoneAndBindFlag");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询区域名称
	 * 
	 * @param map 查询条件
	 * @return 区域名称
	 */
	public String getRegionName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getRegionName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询等级默认标志
	 * 
	 * @param map 查询条件
	 * @return 等级默认标志
	 */
	public String getMemLevelDefaultFlag(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemLevelDefaultFlag");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 通过会员卡号查询会员信息
	 * 
	 * @param map 查询条件
	 * @return 会员信息
	 */
	public Map<String, Object> getMemberInfoByMemCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemberInfoByMemCode");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新会员基本信息
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 */
	public int updMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.updMemberInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新会员卡信息
	 * 
	 * @param map 更新条件和内容
	 * @return 会员信息
	 */
	public int updMemCardInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.updMemCardInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 添加会员扩展信息
	 * 
	 * @param map 添加内容
	 */
	public void addMemberExtInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addMemberExtInfo");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 添加会员扩展信息肤质回访方式收入
	 *
	 * @param map 添加内容
	 */
	public void addMemberExtInfoMain(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addMemberExtInfoMain");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 会员手机号唯一验证
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 */
	public List<String> getMemMobile(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemMobile");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询发卡BA信息Id
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Object getBaId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getBaId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台信息Id
	 * 
	 * @param map 查询条件
	 * @return 会员ID
	 * 
	 */
	public Object getCounterId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getCounterId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 通过柜台查询所属俱乐部信息
	 * 
	 * @param map 查询条件
	 * @return 俱乐部信息
	 */
	public Map<String, Object> getClubInfoByCounter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getClubInfoByCounter");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	 
    /**
	 * 查询会员俱乐部个数 
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员俱乐部个数 
	 * 
	 */
	public int getMemClubCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.getMemClubCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 
	 * 插入会员俱乐部扩展信息
	 * 
	 * @param map 更新条件
	 */
	public void addClubLevelInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM11.addClubLevelInfo");
		baseServiceImpl.save(paramMap);
	}
}
