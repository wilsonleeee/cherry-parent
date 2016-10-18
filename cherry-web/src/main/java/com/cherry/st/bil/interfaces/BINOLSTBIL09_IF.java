/*
 * @(#)BINOLPTRPS01_IF.java     1.0 2011/3/11
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
package com.cherry.st.bil.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 盘点查询Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
public interface BINOLSTBIL09_IF extends ICherryInterface{
	
	public int searchTakingCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchTakingList(Map<String, Object> map);
	
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
    
    /**
	 * 获取导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取要导出的明细数据总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map);
}
