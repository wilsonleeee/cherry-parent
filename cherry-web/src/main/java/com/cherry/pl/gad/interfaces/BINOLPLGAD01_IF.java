/*	
 * @(#)BINOLPLGAD01_IF.java     1.0 @2012-12-5		
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
package com.cherry.pl.gad.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.ICherryInterface;

/**
 *
 * 小工具管理IF
 *
 * @author jijw
 *
 * @version  2012-12-5
 */
public interface BINOLPLGAD01_IF extends ICherryInterface {
	
	/**
	 * 查询小工具信息总数
	 * @param map
	 * @return
	 */
	public int getGadgetInfoCount(Map<String,Object> map);
	
	/**
	 * 查询小工具信息List
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getGadgetInfoList(Map<String,Object> map);
	
	/**
	 * 根据ID取得小工具信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getGadgetInfoById(Map<String,Object> map);
	
	/**
	 * 更新小工具信息
	 * @param map
	 * @return
	 * 
	 * */
	public int updGadgetInfo(Map<String,Object> map);
}
