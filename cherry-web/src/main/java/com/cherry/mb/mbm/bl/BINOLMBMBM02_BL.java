/*
 * @(#)BINOLMBMBM02_BL.java     1.0 2011.10.25
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CodeTable;
import com.cherry.mb.mbm.service.BINOLMBMBM02_Service;
import com.cherry.mo.common.MonitorConstants;

/**
 * 会员详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.10.25
 */
public class BINOLMBMBM02_BL {
	
	/** 会员详细画面Service */
	@Resource
	private BINOLMBMBM02_Service binOLMBMBM02_Service;
	
	@Resource
	private CodeTable codeTable;
	
	/**
	 * 取得会员信息
	 * 
	 * @param map 检索条件
	 * @return 会员信息
	 * @throws Exception 
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception {
		
		if(map.get("memberInfoId") == null) {
			// 根据会员卡号查询会员ID
			map.put("memberInfoId", binOLMBMBM02_Service.getMemberInfoId(map));
		}
		// 查询会员基本信息
		Map<String, Object> memberInfoMap = binOLMBMBM02_Service.getMemberInfo(map);
		if(memberInfoMap != null && !memberInfoMap.isEmpty()) {
			// 会员生日
			String birthYear = (String)memberInfoMap.get("birthYear");
			String birthDay = (String)memberInfoMap.get("birthDay");
			if(birthYear != null && !"".equals(birthYear) 
					&& birthDay != null && !"".equals(birthDay)) {
				memberInfoMap.put("birth", birthYear + "-" + birthDay.substring(0,2) + "-" + birthDay.substring(2,4));
			}
			// 最佳联络时间
			String connectTime = (String)memberInfoMap.get("connectTime");
			if(connectTime != null && !"".equals(connectTime)) {
				String[] connectTimes = connectTime.split(",");
				String connectTimeValue = "";
				for(int i = 0; i < connectTimes.length; i++) {
					if(i == 0) {
						connectTimeValue = codeTable.getVal("1237", connectTimes[i]);
					} else {
						connectTimeValue += "、" + codeTable.getVal("1237", connectTimes[i]);
					}
				}
				memberInfoMap.put("connectTimeValue", connectTimeValue);
			}
			// 存在推荐会员的场合取得推荐会员的卡号
			Object referrerId = memberInfoMap.get("referrerId");
			if(referrerId != null && !"".equals(referrerId.toString())) {
				// 根据会员ID查询会员卡号
				String referrer = binOLMBMBM02_Service.getMemCode(memberInfoMap);
				if(referrer != null && !"".equals(referrer)) {
					memberInfoMap.put("referrer", referrer);
				}
			}
			// 查询会员持卡信息
			List<Map<String, Object>> memCardInfoList = binOLMBMBM02_Service.getMemCardInfoList(memberInfoMap);
			if(memCardInfoList != null && !memCardInfoList.isEmpty()) {
				memberInfoMap.put("memCardInfoList", memCardInfoList);
			}
			// 查询会员地址信息
			List<Map<String, Object>> memberAddressList = binOLMBMBM02_Service.getMemberAddress(memberInfoMap);
			if(memberAddressList != null && !memberAddressList.isEmpty()) {
				memberInfoMap.put("memberAddressInfo", memberAddressList.get(0));
			}
			// 查询会员问卷信息
			List<Map<String, Object>> memPaperList = binOLMBMBM02_Service.getMemPaperList(map);
			if(memPaperList != null && !memPaperList.isEmpty()) {
				for(int i = 0; i < memPaperList.size(); i++) {
					Map<String, Object> memPaperMap = memPaperList.get(i);
					String answer = (String)memPaperMap.get("answer");
					String questionType = (String)memPaperMap.get("questionType");
					if(answer != null && !"".equals(answer)) {
						if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(questionType)) {
							StringBuffer answerTemp = new StringBuffer();
							int x = 0;
							for(int j = 65; j <= 84; j++) {
								char ca = (char)j;
								String value = (String)memPaperMap.get("option"+ca);
								if(value != null && !"".equals(value)) {
									if(answer.length() > x && "1".equals(answer.substring(x,x+1))) {
										answerTemp.append(value);
										answerTemp.append("、");
									}
									x++;
								}
							}
							answer = answerTemp.toString();
							if(!"".equals(answer)) {
								answer = answer.substring(0,answer.length()-1);
							}
							memPaperMap.put("answer", answer);
						}
					}
				}
				memberInfoMap.put("memPagerList", memPaperList);
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
		Map<String, Object> resultMap = binOLMBMBM02_Service.getMemClubInfo(map);
		if (null != resultMap && !resultMap.isEmpty()) {
			// 存在推荐会员的场合取得推荐会员的卡号
			Object referrerId = resultMap.get("referrerIdClub");
			if(referrerId != null && !"".equals(referrerId.toString())) {
				resultMap.put("referrerId", referrerId);
				// 根据会员ID查询会员卡号
				String referrer = binOLMBMBM02_Service.getMemCode(resultMap);
				if(referrer != null && !"".equals(referrer)) {
					resultMap.put("referrerClub", referrer);
				}
			}
		}
		return resultMap;
	}
	
	public String getAgeByYear(String year) {
		String age = null;
		if(year != null && !"".equals(year)) {
			try {
				String sysDate = binOLMBMBM02_Service.getDateYMD();
				age = String.valueOf(Integer.parseInt(sysDate.substring(0,4)) - Integer.parseInt(year) + 1);
			} catch (Exception e) {
				return age;
			}
		}
		return age;
	}

}
