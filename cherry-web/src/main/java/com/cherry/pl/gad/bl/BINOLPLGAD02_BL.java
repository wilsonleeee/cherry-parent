/*	
 * @(#)BINOLPLGAD02_BL.java     1.0 2013.8.29
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
package com.cherry.pl.gad.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.gad.interfaces.BINOLPLGAD02_IF;
import com.cherry.pl.gad.service.BINOLPLGAD02_Service;

/**
 * 小工具布局配置BL
 *
 * @author WangCT
 * @version 1.0 2013.8.29
 */
public class BINOLPLGAD02_BL implements BINOLPLGAD02_IF {
	
	/** 小工具布局配置Service **/
	@Resource
	private BINOLPLGAD02_Service binOLPLGAD02_Service;
	
	/**
	 * 查询某个画面的所有小工具List
	 * 
	 * @param map 查询条件
	 * @return 小工具List
	 */
	@Override
	public List<Map<String,Object>> getGadgetInfoList(Map<String,Object> map){
		
		// 查询某个画面的所有小工具List
		return binOLPLGAD02_Service.getGadgetInfoList(map);
	}

	/**
	 * 保存小工具布局配置
	 * 
	 * @param map 布局配置信息
	 */
	@Override
	public void saveGadgetInfo(Map<String, Object> map) {
		
		List<Map<String, Object>> gadgetInfoList = new ArrayList<Map<String,Object>>();
		// 小工具ID
		List<String> gadgetInfoIds = (List)map.get("gadgetInfoIds");
		// 行
		List<String> rowNumbers = (List)map.get("rowNumbers");
		// 列
		List<String> columnNumbers = (List)map.get("columnNumbers");
		if(gadgetInfoIds != null && !gadgetInfoIds.isEmpty()) {
			for(int i = 0; i < gadgetInfoIds.size(); i++) {
				Map<String, Object> gadgetInfoMap = new HashMap<String, Object>();
				gadgetInfoMap.put("gadgetInfoId", gadgetInfoIds.get(i));
				gadgetInfoMap.put("rowNumber", Integer.parseInt(rowNumbers.get(i))-1);
				gadgetInfoMap.put("columnNumber", Integer.parseInt(columnNumbers.get(i))-1);
				gadgetInfoList.add(gadgetInfoMap);
			}
			// 更新小工具布局配置
			binOLPLGAD02_Service.updGadgetInfo(gadgetInfoList);
		}
	}

}
