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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.MonitorConstants;
import com.cherry.mo.rpt.interfaces.BINOLMORPT04_IF;
import com.cherry.mo.rpt.service.BINOLMORPT04_Service;

/**
 * 
 * 答卷信息BL
 * 
 * @author WangCT
 * @version 1.0 2011.10.24
 */
public class BINOLMORPT04_BL implements BINOLMORPT04_IF {
	
	@Resource
	private BINOLMORPT04_Service binOLMORPT04_Service;

	/**
     * 取得答卷信息
     * 
     * @param map 查询条件
     * @return 答卷信息
     */
	@Override
	public Map<String, Object> getCheckAnswer(Map<String, Object> map) {
		
		// 根据答卷ID获取答卷信息
		Map<String, Object> checkAnswerMap = binOLMORPT04_Service.getCheckAnswer(map);
		double totalPoint=0.0;
		double maxPoint =ConvertUtil.getDouble(checkAnswerMap.get("maxPoint"));
		if (maxPoint==0){
			checkAnswerMap.put("isPoint", "0");
		}else{
			checkAnswerMap.put("isPoint", "1");
		}
		if(checkAnswerMap != null && !checkAnswerMap.isEmpty()) {
			// 根据问卷ID和答卷ID获取问题和答案
			List<Map<String, Object>> checkQuestionList = binOLMORPT04_Service.getCheckQuestionList(checkAnswerMap);
			if(checkQuestionList != null && !checkQuestionList.isEmpty()) {
				double orginalPoint=0;
				double point=0;
				String answer ="";
				String value = "";
				double pointTemp=0;
				

				for(int i = 0; i < checkQuestionList.size(); i++) {
					Map<String, Object> checkQuestionMap = checkQuestionList.get(i);
					orginalPoint=ConvertUtil.getDouble(checkQuestionMap.get("orginalPoint"));
					if (maxPoint==0){
						orginalPoint=0;
					}
					point=0;
					if (MonitorConstants.QUESTIONTYPE_SINCHOICE.equals(checkQuestionMap.get("questionType"))) {
						List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
						answer =  ConvertUtil.getString(checkQuestionMap.get("answer"));
						char ca =0;
						for(int j = 65; j <= 84; j++) {
							ca = (char)j;
							point=0;//重置，重新加分
							value = (String)checkQuestionMap.get("option"+ca);
							if(value != null && !"".equals(value)) {
								Map<String, Object> answerMap = new HashMap<String, Object>();
								answerMap.put("answer", value);
								answerMap.put("point", checkQuestionMap.get("point"+ca));
								if(answer != null && answer.equals(value)) {
									answerMap.put("checkStatus", 1);
									point+= ConvertUtil.getDouble(checkQuestionMap.get("point"+ca));
									if (orginalPoint==0){
										point=0;
									}
									checkQuestionMap.put("point", point);
									checkQuestionMap.put("answer", value);
								} else {									
									answerMap.put("checkStatus", 0);
									point+=0;
								}
								totalPoint+=point;
								answerList.add(answerMap);
//								answerMap.clear();
							}else{
								break;
							}
						}
						
//						for (int k=0;k<answer.length();k++){
//							if (answer.substring(k,k+1).equals("1")){
//								option=(char) (k+65);
//								break;
//							}
//						}
//						for(int j = 65; j <= 84; j++) {
//							char ca = (char)j;
//							point=0;//重置，重新加分
//							String value = (String)checkQuestionMap.get("option"+ca);
//							if(value != null && !"".equals(value)) {
//								Map<String, Object> answerMap = new HashMap<String, Object>();
//								answerMap.put("answer", value);		
//								if(option==ca){
////								if(answer != null && answer.equals(value)) {
//									answerMap.put("checkStatus", 1);
//									point+= ConvertUtil.getDouble(checkQuestionMap.get("point"+ca));
//									if (orginalPoint==0){
//										point=0;
//									}
//									checkQuestionMap.put("point", point);
//									checkQuestionMap.put("answer", value);
//								} else {
//									answerMap.put("checkStatus", 0);
//									point+=0;
//								}
//								totalPoint+=point;
//								answerList.add(answerMap);
//							}
//						}
						
						checkQuestionMap.put("answerList", answerList);
//						answerList.clear();
					} else if(MonitorConstants.QUESTIONTYPE_MULCHOICE.equals(checkQuestionMap.get("questionType"))) {
						List<Map<String, Object>> answerList = new ArrayList<Map<String,Object>>();
						answer = ConvertUtil.getString(checkQuestionMap.get("answer"));
						StringBuffer answerTemp = new StringBuffer();
						
						int x = 0;
						char ca =0;
						for(int j = 65; j <= 84; j++) {
							ca = (char)j;
							pointTemp=0;
							value = (String)checkQuestionMap.get("option"+ca);
							if(value != null && !"".equals(value)) {
								Map<String, Object> answerMap = new HashMap<String, Object>();
								answerMap.put("answer", value);
//								answerMap.put("point", checkQuestionMap.get("point"+ca));
								if(answer != null && answer.length() > x && "1".equals(answer.substring(x,x+1))) {
									answerTemp.append(value);
									answerTemp.append("、");
									answerMap.put("checkStatus", 1);
									pointTemp=ConvertUtil.getDouble(checkQuestionMap.get("point"+ca));
									if (orginalPoint==0){
										pointTemp=0;
									}
									point+= pointTemp;
								} else {
									answerMap.put("checkStatus", 0);
									point+=0;
								}
								totalPoint+=pointTemp;
								answerList.add(answerMap);
								x++;
//								answerMap.clear();
							}else{
								break;
							}
						}
						answer = answerTemp.toString();
						if(!"".equals(answer)) {
							answer = answer.substring(0,answer.length()-1);
						}
						checkQuestionMap.put("point", point);
						checkQuestionMap.put("answerList", answerList);
						checkQuestionMap.put("answer", answer);
//						answerList.clear();
					}
				}
				checkAnswerMap.put("checkQuestionList", checkQuestionList);
			}
			
		}
		checkAnswerMap.put("realTotalPoint", totalPoint);
		return checkAnswerMap;
	}

}
