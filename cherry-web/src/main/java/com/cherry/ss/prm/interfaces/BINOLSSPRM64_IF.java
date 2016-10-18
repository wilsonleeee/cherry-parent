/*
 * @(#)BINOLSSPRM64_IF.java     1.0 2013/01/25
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 入库一览Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.01.25
 */
public interface BINOLSSPRM64_IF {

    /**
     * 取得入库单总数
     * 
     * @param map
     * @return 
     */
	public int getPrmInDepotCount(Map<String,Object> map);
	
	/**
	 * 取得入库单list
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrmInDepotList(Map<String,Object> map);
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
}