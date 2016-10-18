/*  
 * @(#)BINOLMOCIO03_BL.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.cio.interfaces.BINOLMOCIO03_IF;
import com.cherry.mo.cio.service.BINOLMOCIO03_Service;

public class BINOLMOCIO03_BL implements BINOLMOCIO03_IF {

	@Resource
	private BINOLMOCIO03_Service binOLMOCIO03_Service;

	/**
	 * 问卷保存
	 * 
	 * @param map
	 *            存放的是问卷信息，用于插入问卷主表中
	 * @param list
	 *            存在的是问卷的问题，用于插入问卷明细表中
	 * 
	 * 
	 * */
	@SuppressWarnings( { "unchecked" })
	@Override
	public void tran_savePaper(Map<String, Object> map,
			List<Map<String, Object>> list) throws CherryException {
		// 将map中的问卷信息插入到数据库中，并且返回paperId，供问卷下发和往问卷明细表中插入数据时使用
		try {
			this.setDateTime(map);
			int paperId = binOLMOCIO03_Service.savePaper(map);
			int listSize = list.size();
			List<Map<String, Object>> quesList = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapOfList = null;
			List<Map<String, Object>> listOfMap;
			// 遍历传递过来的list，将其转化成相应的格式
			for (int i = 0; i < listSize; i++) {
				Map<String, Object> quesMap = new HashMap<String, Object>();
				mapOfList = list.get(i);
				listOfMap = (List<Map<String, Object>>) mapOfList.get("arr");
				for (int j = 0; j < listOfMap.size(); j++) {
					Map<String, Object> map1 = listOfMap.get(j);
					String str1 = ConvertUtil.getString(map1.get("key"));
					String str2 = ConvertUtil.getString(map1.get("value"));
					quesMap.put(str1, str2);
				}
				quesMap.put("paperId", paperId);
				quesMap.put("createdBy", map.get("createdBy"));
				quesMap.put("createPGM", map.get("createPGM"));
				quesMap.put("updatedBy", map.get("updatedBy"));
				quesMap.put("updatePGM", map.get("updatePGM"));
				quesMap.put("startTime", map.get("startTime"));
				quesMap.put("endTime", map.get("endTime"));
				quesList.add(quesMap);
			}
			// 将问题插入问卷明细表中
			binOLMOCIO03_Service.saveQuestion(quesList);

		} catch (Exception ex) {
			throw new CherryException("EMO00029", ex);
		}
	}

	/**
	 * 判断系统中是否已经存在相同名称的问卷
	 * 
	 * 
	 * */
	@Override
	public boolean isExsitSameNamePaper(Map<String, Object> map) {
		
		List<Map<String,Object>> papers = binOLMOCIO03_Service.isExsitSameNamePaper(map);
		
		if(papers.isEmpty()){
			return false;
		}else{
			return true;
		}
		
	}
	
	//设定时间
	private Map<String,Object> setDateTime(Map<String,Object> map){
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");
		
		if("".equals(map.get("startHour"))){
			startTime = startTime + " 00";
		}else{
			startTime = startTime + " " + map.get("startHour");
		}
		
		if("".equals(map.get("startMinute"))){
			startTime = startTime + ":00";
		}else{
			startTime = startTime + ":" + map.get("startMinute");
		}
		
		if("".equals(map.get("startSecond"))){
			startTime = startTime + ":00";
		}else{
			startTime = startTime + ":" + map.get("startSecond");
		}
		
		if("".equals(map.get("endHour"))){
			endTime = endTime + " 23";
		}else{
			endTime = endTime + " " + map.get("endHour");
		}
		
		if("".equals(map.get("endMinute"))){
			endTime = endTime + ":59";
		}else{
			endTime = endTime + ":" + map.get("endMinute");
		}
		
		if("".equals(map.get("endSecond"))){
			endTime = endTime + ":59.997";
		}else{
			endTime = endTime + ":" + map.get("endSecond") +".997";
		}
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		return map;
	}

}
