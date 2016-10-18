package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO04_IF;
import com.cherry.mo.cio.service.BINOLMOCIO04_Service;
import com.cherry.mo.cio.service.BINOLMOCIO05_Service;
import com.cherry.mo.common.MonitorConstants;

/*  
 * @(#)BINOLMOCIO04_BL.java    1.0 2012-3-9     
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
public class BINOLMOCIO04_BL implements BINOLMOCIO04_IF {

	@Resource
	private BINOLMOCIO05_Service binOLMOCIO05_Service;
	
	@Resource
	private BINOLMOCIO04_Service binOLMOCIO04_Service;
	
	@Override
	public List<List> getPaperQuestion(Map<String, Object> map) {
		
		List<Map<String, Object>> questionList = binOLMOCIO05_Service
		.getPaperQuestion(map);

		int paperId = Integer.parseInt((String)map.get("paperId"));
		List<Map<String,Object>> validTime = binOLMOCIO04_Service.getValidTime(paperId);
		
		List<List> groupList = new ArrayList<List>();
		
		if(null != validTime && questionList.size() > 0){
			int groupNum = validTime.size();
			for(int j = 0 ; j < groupNum ; j++){
				List<Map<String, Object>> queList = new ArrayList<Map<String, Object>>();
				int displayOrder = 1;
				for (int i = 0; i < questionList.size(); i++) {
					Map<String, Object> map1 = questionList.get(i);
					String startTime1 = ConvertUtil.getString(validTime.get(j).get("startTime"));
					String startTime2 = ConvertUtil.getString(map1.get("startTime"));
					
					String endTime1 = ConvertUtil.getString(validTime.get(j).get("endTime"));
					String endTime2 = ConvertUtil.getString(map1.get("endTime"));
					
					if(startTime1.equals(startTime2) && endTime1.equals(endTime2)){
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("questionType", map1.get("questionType"));
						map2.put("isRequired", map1.get("isRequired"));
						map2.put("questionItem", map1.get("questionItem"));
						map2.put("displayOrder", displayOrder);
						map2.put("point", map1.get("point"));
						map2.put("paperId", map1.get("paperId"));
						map2.putAll(validTime.get(j));
						// 分别处理选择题和非选择题
						if (MonitorConstants.QUESTIONTYPE_APFILL.equals(map1
								.get("questionType"))
								|| MonitorConstants.QUESTIONTYPE_ESSAY.equals(map1
										.get("questionType"))) {
							queList.add(map2);
						} else {
							int choiceNum = 0;
							for (Map.Entry<String, Object> en : map1.entrySet()) {
								String key = en.getKey();
								if (null != en.getValue() && key.indexOf("option") > -1) {
									map2.put(key, en.getValue());
									// 将该选项对应的分值也放进map中
									String[] keyArr = key.split("");
									String optKey = "point" + keyArr[keyArr.length - 1];
									map2.put(optKey, map1.get(optKey));
									choiceNum ++;
								}
							}
							map2.put("choiceNum", choiceNum);
							queList.add(map2);
						}
						questionList.remove(i);
						i--;
						displayOrder ++;
					}else{
						continue;
					}
					
				}
				groupList.add(queList);
			}
		}
		
		return groupList;
	}
}
