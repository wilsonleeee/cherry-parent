/*
 * @(#)BINOLMBMBM02_Service.java     1.0 2011.10.25
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 会员详细画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.10.25
 */
public class BINOLMBMBM02_Service extends BaseService {
	
	/**
	 * 查询会员基本信息
	 * 
	 * @param map 检索条件
	 * @return 会员基本信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemberInfo");
		//会员信息Map
	    Map<String, Object> memberInfoMap =(Map)baseServiceImpl.get(parameterMap);
	    if(memberInfoMap != null){
	    	String brandCode = ConvertUtil.getString(parameterMap.get("brandCode"));
	    	// 会员【备注1】字段解密
	  		if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("memo1"), true)) {
	  			String memo1 = ConvertUtil.getString(memberInfoMap.get("memo1"));
	  			memberInfoMap.put("memo1", CherrySecret.decryptData(brandCode,memo1));
	  		}
	  		// 会员【身份证】字段解密
	  		if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("identityCard"), true)) {
	  			String identityCard = ConvertUtil.getString(memberInfoMap.get("identityCard"));
	  			memberInfoMap.put("identityCard", CherrySecret.decryptData(brandCode,identityCard));
	  		}
	  		// 会员【电话】字段解密
			if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("telephone"), true)) {
				String telephone = ConvertUtil.getString(memberInfoMap.get("telephone"));
				memberInfoMap.put("telephone", CherrySecret.decryptData(brandCode,telephone));
			}
			// 会员【手机号】字段解密
			if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("mobilePhone"), true)) {
				String mobilePhone = ConvertUtil.getString(memberInfoMap.get("mobilePhone"));
				memberInfoMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
			}
			// 会员【电子邮箱】字段解密
			if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("email"), true)) {
				String email = ConvertUtil.getString(memberInfoMap.get("email"));
				memberInfoMap.put("email", CherrySecret.decryptData(brandCode,email));
			}
	    }	    
		return memberInfoMap;
	}
	
	/**
	 * 查询会员俱乐部信息
	 * 
	 * @param map 检索条件
	 * @return 会员俱乐部信息
	 */
	public Map<String, Object> getMemClubInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemClubInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询会员持卡信息
	 * 
	 * @param map 检索条件
	 * @return 会员持卡信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemCardInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemCardInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员地址信息
	 * 
	 * @param map 检索条件
	 * @return 会员地址信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemberAddress");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询会员问卷信息
	 * 
	 * @param map 检索条件
	 * @return 会员问卷信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemPaperList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemPaperList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据会员卡号查询会员ID
	 * 
	 * @param map 检索条件
	 * @return 会员ID
	 */
	public String getMemberInfoId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemberInfoId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据会员ID查询会员卡号
	 * 
	 * @param map 检索条件
	 * @return 会员卡号
	 */
	public String getMemCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMemCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 查询会员俱乐部扩展信息
	 * 
	 * @param map 检索条件
	 * @return 会员俱乐部扩展信息
	 */
	public Map<String, Object> getClubExtInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.getMB02ClubExtInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 更新会员俱乐部扩展信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubExtInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.updateMB02ClubExtInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入会员俱乐部扩展信息
	 * 
	 * @param map 新增条件
	 */
	public void addClubExtInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM02.addMB02ClubExtInfo");
		baseServiceImpl.save(map);
	}
}
