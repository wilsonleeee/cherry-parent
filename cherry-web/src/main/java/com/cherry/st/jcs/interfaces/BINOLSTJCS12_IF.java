/*
 * @(#)BINOLSTJCS12_IF.java     1.0 2015/12/18
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

package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 电商产品对应关系一览IF
 * @author lzs
 * @version 1.0 2015.12.18
 */
public interface BINOLSTJCS12_IF extends ICherryInterface {
	
	/**
	 * 获取电商产品对应关系一览信息count
	 * @param map
	 * @return
	 */
	public int getProductRelationCount(Map<String, Object> map);
	
	/**
	 * 获取电商产品对应关系一览信息list
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductRelationList(Map<String, Object> map);
	
}
