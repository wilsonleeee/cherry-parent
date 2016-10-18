/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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
package com.cherry.ct.tpl.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.tpl.service.BINOLCTTPL01_Service;
import com.cherry.ct.tpl.interfaces.BINOLCTTPL01_IF;

/**
 * 沟通模板一览BL
 * 
 * @author ZhangGS
 * @version 1.0 2012.11.06
 */
public class BINOLCTTPL01_BL implements BINOLCTTPL01_IF{
	@Resource
	private BINOLCTTPL01_Service binolcttpl01_Service;
	/**
	 * 获取沟通模板List
	 * 
	 * @param map
	 * @return 沟通模板List
	 */
	@Override
	public List<Map<String, Object>> getTemplateList(Map<String, Object> map) throws Exception{
		// 获取沟通模板List
		List<Map<String, Object>> templateList = binolcttpl01_Service.getCommTemplateList(map);
		List<Map<String, Object>> newTemplateList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> templateMap : templateList){
			Map<String,Object> newTemplateMap = new HashMap<String, Object>();
			String contents = ConvertUtil.getString(templateMap.get("contents"));
			if(contents.length() > 30){
				newTemplateMap.putAll(templateMap);
				newTemplateMap.put("contentsCut", contents.substring(0, 30)+" ...");
			}else{
				newTemplateMap.putAll(templateMap);
				newTemplateMap.put("contentsCut", contents);
			}
			newTemplateList.add(newTemplateMap);
		}
		return newTemplateList;
	}
	
	@Override
	public int getTemplateCount(Map<String, Object> map){
		// 获取沟通模板数量
		return binolcttpl01_Service.getTemplateCount(map);
	}
	
	public void disableTemplate(Map<String, Object> map){
		// 停用沟通模板
		binolcttpl01_Service.disableTemplate(map);
	}
}
