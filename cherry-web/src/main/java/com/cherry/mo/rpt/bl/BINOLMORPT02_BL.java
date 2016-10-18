/*  
 * @(#)BINOLMORPT02_BL.java     1.0 2011.10.24
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
package com.cherry.mo.rpt.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.rpt.interfaces.BINOLMORPT02_IF;
import com.cherry.mo.rpt.service.BINOLMORPT02_Service;

/**
 * 
 * 考核答卷信息BL
 * 
 * @author WangCT
 * @version 1.0 2011.10.24
 */
public class BINOLMORPT02_BL implements BINOLMORPT02_IF {
	
	@Resource
	private BINOLMORPT02_Service binOLMORPT02_Service;

	/**
     * 取得考核答卷信息
     * 
     * @param map 查询条件
     * @return 考核答卷信息
     */
	@Override
	public Map<String, Object> getCheckAnswer(Map<String, Object> map) {
		
		// 根据答卷ID获取考核答卷信息
		Map<String, Object> checkAnswerMap = binOLMORPT02_Service.getCheckAnswer(map);
		if(checkAnswerMap != null && !checkAnswerMap.isEmpty()) {
			// 取得评定级别
			String pointLevelName = binOLMORPT02_Service.getPointLevelName(checkAnswerMap);
			checkAnswerMap.put("pointLevel", pointLevelName);
			// 根据考核问卷ID和答卷ID获取问题和答案
			List<Map<String, Object>> checkQuestionList = binOLMORPT02_Service.getCheckQuestionList(checkAnswerMap);
			if(checkQuestionList != null && !checkQuestionList.isEmpty()) {
				for(int i = 0; i < checkQuestionList.size(); i++) {
					Map<String, Object> checkQuestionMap = checkQuestionList.get(i);
					if (MonitorConstants.CHECKPAPER_QUESTIONTYPE_SINCHOICE.equals(checkQuestionMap.get("questionType"))) {
						List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
						String answer = (String)checkQuestionMap.get("answer");
						for(int j = 65; j <= 84; j++) {
							char ca = (char)j;
							String value = (String)checkQuestionMap.get("option"+ca);
							if(value != null && !"".equals(value)) {
								Map<String, Object> answerMap = new HashMap<String, Object>();
								answerMap.put("answer", value);
								answerMap.put("point", checkQuestionMap.get("point"+ca));
								if(answer != null && !"".equals(answer)) {
									BigDecimal point = (BigDecimal)checkQuestionMap.get("point"+ca);
									BigDecimal de = new BigDecimal(answer);
									if(de.compareTo(point) == 0) {
										answerMap.put("checkStatus", 1);
									} else {
										answerMap.put("checkStatus", 0);
									}
								} else {
									answerMap.put("checkStatus", 0);
								}
								answerList.add(answerMap);
							}
						}
						checkQuestionMap.put("answerList", answerList);
					}
				}
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				List<String[]> keyList = new ArrayList<String[]>();
				String[] key = {"checkQuestionGroupId", "groupName"};
				keyList.add(key);
				ConvertUtil.convertList2DeepList(checkQuestionList,list,keyList,0);
				checkAnswerMap.put("checkQuestionList", list);
			}
			
		}
		return checkAnswerMap;
	}

}
