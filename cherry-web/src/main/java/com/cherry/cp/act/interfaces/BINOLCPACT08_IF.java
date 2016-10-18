/*	
 * @(#)BINOLCPACT08_IF.java     1.0 @2013-07-16		
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
package com.cherry.cp.act.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 礼品领用详细一览Interface
 * 
 * @author menghao
 * 
 */
public interface BINOLCPACT08_IF extends ICherryInterface {

	/**
	 * 取得礼品领用详细基本信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getGiftDrawDetail(Map<String, Object> map);

	/**
	 * 取得礼品领用的礼品详细List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGiftDrawPrtDetail(
			Map<String, Object> map);
}
