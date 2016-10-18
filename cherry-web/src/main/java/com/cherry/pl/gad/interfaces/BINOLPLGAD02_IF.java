/*	
 * @(#)BINOLPLGAD02_IF.java     1.0 2013.8.29
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

import com.cherry.cm.core.ICherryInterface;

/**
 * 小工具布局配置IF
 *
 * @author WangCT
 * @version 1.0 2013.8.29	
 */
public interface BINOLPLGAD02_IF extends ICherryInterface {
	
	/**
	 * 查询某个画面的所有小工具List
	 * 
	 * @param map 查询条件
	 * @return 小工具List
	 */
	public List<Map<String,Object>> getGadgetInfoList(Map<String,Object> map);
	
	/**
	 * 保存小工具布局配置
	 * 
	 * @param map 布局配置信息
	 */
	public void saveGadgetInfo(Map<String,Object> map);

}
