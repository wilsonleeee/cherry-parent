/*
 * @(#)BINOLMBMBM01_BL.java     1.0 2011/03/22
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
package com.cherry.mb.mbm.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM01_Service;
import com.cherry.mb.mbm.service.BINOLMBMBM02_Service;
import com.cherry.mo.common.MonitorConstants;

/**
 * 会员一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BINOLMBMBM01_BL {
	
	/** 会员一览画面Service */
	@Resource
	private BINOLMBMBM01_Service binOLMBMBM01_Service;
	
	/** 会员详细画面Service */
	@Resource
	private BINOLMBMBM02_Service binOLMBMBM02_Service;
	
	/**
	 * 会员搜索条件设定
	 * 
	 * @param map 会员搜索条件
	 */
	public void setCondition(Map<String, Object> map) {
		
		// 年龄转化为生日年处理
		String sysDate = binOLMBMBM01_Service.getDateYMD();
		String ageStart = (String)map.get("ageStart");
		if(ageStart != null && !"".equals(ageStart)) {
			map.put("birthYearEnd", CherryUtil.getYearByAge(ageStart, sysDate));
		}
		String ageEnd = (String)map.get("ageEnd");
		if(ageEnd != null && !"".equals(ageEnd)) {
			map.put("birthYearStart", CherryUtil.getYearByAge(ageEnd, sysDate));
		}
		// 生日格式化处理
		String birthDayMonth = (String)map.get("birthDayMonth");
		if(birthDayMonth != null && !"".equals(birthDayMonth)) {
			if(Integer.parseInt(birthDayMonth) < 10) {
				birthDayMonth = "0" + birthDayMonth;
				map.put("birthDayMonth", birthDayMonth);
			}
		}
		String birthDayDate = (String)map.get("birthDayDate");
		if(birthDayDate != null && !"".equals(birthDayDate)) {
			if(Integer.parseInt(birthDayDate) < 10) {
				birthDayDate = "0" + birthDayDate;
				map.put("birthDayDate", birthDayDate);
			}
		}
		// 日期格式化处理
		String joinDateStart = (String)map.get("joinDateStart");
		String joinDateEnd = (String)map.get("joinDateEnd");
		Date startDate = DateUtil.coverString2Date(joinDateStart);
		Date endDate = DateUtil.coverString2Date(joinDateEnd);
		if(startDate != null) {
			joinDateStart = DateUtil.date2String(startDate, "yyyy-MM-dd");
		} else {
			joinDateStart = "";
		}
		if(endDate != null) {
			joinDateEnd = DateUtil.date2String(endDate, "yyyy-MM-dd");
		} else {
			joinDateEnd = "";
		}
		map.put("joinDateStart", joinDateStart);
		map.put("joinDateEnd", joinDateEnd);
		
		// 设置推荐会员ID
		String referrerMemCode = (String)map.get("referrerMemCode");
		if(referrerMemCode != null && !"".equals(referrerMemCode)) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
			paramMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			paramMap.put("memCode", referrerMemCode);
			// 根据会员卡号查询会员ID
			String referrerId = binOLMBMBM02_Service.getMemberInfoId(paramMap);
			if(referrerId != null && !"".equals(referrerId)) {
				map.put("referrerId", referrerId);
			}
		}
		
		// 会员扩展条件去除空处理
		List propertyInfoList = (List)map.get("propertyInfoList");
		boolean extendProFlg = false;
		if(propertyInfoList != null && !propertyInfoList.isEmpty()) {
			for(int i = 0; i < propertyInfoList.size(); i++) {
				Map propertyInfoMap = (Map)propertyInfoList.get(i);
				List propertyValue = (List)propertyInfoMap.get("propertyValues");
				if(propertyValue != null && !propertyValue.isEmpty() && !"".equals(propertyValue.get(0))) {
					extendProFlg = true;
				} else {
					propertyInfoMap.remove("propertyValues");
				}
			}
		}
		if(!extendProFlg) {
			map.remove("propertyInfoList");
		}
	}

	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberInfoCount(Map<String, Object> map) {
		
		// 取得会员总数
		return binOLMBMBM01_Service.getMemberInfoCount(map);
	}
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map 检索条件
	 * @return 会员信息List
	 */
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		
		// 取得会员信息List
		return binOLMBMBM01_Service.getMemberInfoList(map);
	}
	
	/**
	 * 取得会员扩展信息List
	 * 
	 * @param map 检索条件
	 * @return 会员扩展信息List
	 */
	public List<Map<String, Object>> getExtendProperty(Map<String, Object> map) {
		
		// 查询会员问卷信息
		List<Map<String, Object>> extendPropertyList = binOLMBMBM01_Service.getMemPaperList(map);
		// 会员扩展信息存在的场合
		if(extendPropertyList != null && !extendPropertyList.isEmpty()) {
			for(int i = 0; i < extendPropertyList.size(); i++) {
				Map<String, Object> checkQuestionMap = extendPropertyList.get(i);
				if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(checkQuestionMap.get("questionType"))
						|| MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(checkQuestionMap.get("questionType"))) {
					List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
					for(int j = 65; j <= 84; j++) {
						char ca = (char)j;
						String value = (String)checkQuestionMap.get("option"+ca);
						if(value != null && !"".equals(value)) {
							Map<String, Object> answerMap = new HashMap<String, Object>();
							answerMap.put("answer", value);
							answerList.add(answerMap);
						}
					}
					checkQuestionMap.put("answerList", answerList);
				}
			}
//			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//			List<String[]> keyList = new ArrayList<String[]>();
//			String[] key1 = {"paperId","paperName"};
//			keyList.add(key1);
//			ConvertUtil.convertList2DeepList(extendPropertyList,list,keyList,0);
			return extendPropertyList;
		}
		return null;
	}
}
