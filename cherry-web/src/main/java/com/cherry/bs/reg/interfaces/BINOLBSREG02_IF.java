/*
 * @(#)BINOLBSREG02_IF.java     1.0 2011/11/23
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
package com.cherry.bs.reg.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 区域详细画面IF
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public interface BINOLBSREG02_IF extends ICherryInterface {
	
	/**
     * 取得区域详细信息
     * 
     * @param map 查询条件
     * @return 区域详细信息
     */
    public Map<String, Object> getRegionInfo(Map<String, Object> map);
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map);
}
