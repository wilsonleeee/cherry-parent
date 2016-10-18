/*
 * @(#)BINOLMBMBM13_Service.java     1.0 2013/04/11
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
 * 会员资料修改履历明细画面Service
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM13_Service extends BaseService {
	
	/**
	 * 查询会员资料修改履历
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历
	 * @throws Exception 
	 */
	public Map<String, Object> getMemInfoRecordInfo(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getMemInfoRecordInfo");
		//会员信息Map
	    Map<String, Object> memberInfoMap =(Map)baseServiceImpl.get(paramMap);
	    if(memberInfoMap!=null){
	    	// 会员【备注1】字段解密
	  		if (!CherryChecker.isNullOrEmpty(memberInfoMap.get("remark"), true)) {
	  			String brandCode = ConvertUtil.getString(paramMap.get("brandCode"));
	  			String memo1 = ConvertUtil.getString(memberInfoMap.get("remark"));
	  			memberInfoMap.put("remark", CherrySecret.decryptData(brandCode,memo1));
	  		}
	    }
		return memberInfoMap;
	}
	
	/**
	 * 查询会员资料修改履历明细
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历明细
	 */
	public List<Map<String, Object>> getMemInfoRecordDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getMemInfoRecordDetail");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据区域ID查询区域名称
	 * 
	 * @param map 查询条件
	 * @return 区域名称
	 */
	public String getRegionName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getRegionName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据员工ID查询员工名称
	 * 
	 * @param map 查询条件
	 * @return 员工名称
	 */
	public String getEmployeeName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getEmployeeName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据部门ID查询部门名称
	 * 
	 * @param map 查询条件
	 * @return 部门名称
	 */
	public String getDepartName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getDepartName");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据会员ID查询会员姓名
	 * 
	 * @param map 查询条件
	 * @return 会员姓名
	 */
	public String getMemName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM13.getMemName");
		return (String)baseServiceImpl.get(paramMap);
	}

}
