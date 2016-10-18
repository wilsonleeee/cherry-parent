/*
 * @(#)BINOLMBMBM22_BL.java     1.0 2013.08.13
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

import com.cherry.mb.mbm.service.BINOLMBMBM22_Service;
import com.cherry.mo.common.MonitorConstants;

/**
 * 会员答卷画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.08.13
 */
public class BINOLMBMBM22_BL {
	
	/** 会员答卷画面Service */
	@Resource
	private BINOLMBMBM22_Service binOLMBMBM22_Service;
	
	/**
	 * 取得会员答卷信息总数
	 * 
	 * @param map 检索条件
	 * @return 会员答卷信息总数
	 */
	public int getMemAnswerCount(Map<String, Object> map) {
		
		// 取得会员答卷信息总数
		return binOLMBMBM22_Service.getMemAnswerCount(map);
	}
	
	/**
	 * 取得会员答卷信息List
	 * 
	 * @param map 检索条件
	 * @return 会员答卷信息List
	 */
	public List<Map<String, Object>> getMemAnswerList(Map<String, Object> map) {

		// 取得会员答卷信息List
		return binOLMBMBM22_Service.getMemAnswerList(map);
	}
	
	/**
	 * 根据问卷ID和答卷ID获取问题和答案
	 * 
	 * @param map 检索条件
	 * @return 问题和答案信息List
	 */
	public List<Map<String, Object>> getMemQuestionList(Map<String, Object> map) {

		// 根据问卷ID和答卷ID获取问题和答案
		List<Map<String, Object>> memQuestionList = binOLMBMBM22_Service.getMemQuestionList(map);
		if(memQuestionList != null && !memQuestionList.isEmpty()) {
			for(int i = 0; i < memQuestionList.size(); i++) {
				Map<String, Object> memQuestionMap = memQuestionList.get(i);
				String answer = (String)memQuestionMap.get("answer");
				String questionType = (String)memQuestionMap.get("questionType");
				if(answer != null && !"".equals(answer)) {
					if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(questionType)) {
						StringBuffer answerTemp = new StringBuffer();
						int x = 0;
						for(int j = 65; j <= 84; j++) {
							char ca = (char)j;
							String value = (String)memQuestionMap.get("option"+ca);
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
						memQuestionMap.put("answer", answer);
					}
				}
			}
		}
		return memQuestionList;
	}

}
