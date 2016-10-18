/*	
 * @(#)BINOLCPACT07_IF.java     1.0 @2013-07-15		
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
 * 礼品领用报表interface
 * 
 * @author menghao
 * 
 * @version 1.0 2013/07/15
 */
public interface BINOLCPACT07_IF extends ICherryInterface {
	/**
	 * 取得礼品领用单据数量
	 * 
	 * @param map
	 * @return
	 */
	public int getGiftDrawCount(Map<String, Object> map);

	/**
	 * 取得礼品领用单据List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getGiftDrawList(Map<String, Object> map) throws Exception;

	/**
	 * 取得活动信息
	 * 
	 * @param map
	 * @return
	 */
	public String getActivity(Map<String, Object> map);
	
	/**
	 * 取得礼品领用一览结果导出的字节流
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
