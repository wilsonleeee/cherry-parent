/*	
 * @(#)BINOLPLGAD01_BL.java     1.0 @2012-12-5		
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.gad.interfaces.BINOLPLGAD01_IF;
import com.cherry.pl.gad.service.BINOLPLGAD01_Service;

/**
 *
 * 小工具管理BL
 *
 * @author jijw
 *
 * @version  2012-12-5
 */
public class BINOLPLGAD01_BL implements BINOLPLGAD01_IF {

	@Resource
	private BINOLPLGAD01_Service binOLPLGAD01_Service;
	
	/**
	 * 查询小工具信息总数
	 * @param map
	 * @return
	 */
	public int getGadgetInfoCount(Map<String,Object> map){
		return binOLPLGAD01_Service.getGadgetInfoCount(map);
	}
	
	/**
	 * 查询小工具信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGadgetInfoList(Map<String, Object> map) {
		return binOLPLGAD01_Service.getGadgetInfoList(map);
	}
	
	/**
	 * 根据ID取得小工具信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getGadgetInfoById(Map<String,Object> map) {
		return binOLPLGAD01_Service.getGadgetInfoById(map);
	}

	/**
	 * 更新小工具信息
	 * @param map
	 * @return
	 */
	public int updGadgetInfo(Map<String, Object> map) {
		return binOLPLGAD01_Service.updGadgetInfo(map);
	}

}
