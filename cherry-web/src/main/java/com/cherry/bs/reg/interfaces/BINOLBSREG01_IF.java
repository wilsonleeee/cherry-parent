/*
 * @(#)BINOLBSREG01_IF.java     1.0 2011/11/23
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
 * 区域一览画面IF
 * 
 * @author WangCT
 * @version 1.0 2011/11/23
 */
public interface BINOLBSREG01_IF extends ICherryInterface {
	
	/**
     * 取得区域树结构信息
     * 
     * @param map 查询条件
     * @return 区域树结构信息
     */
    public String getRegionTree(Map<String, Object> map) throws Exception;
    
    /**
     * 取得定位到的区域的所有上级区域位置
     * 
     * @param map 查询条件
     * @return 上级区域位置
     */
    public List<String> getLocationHigher(Map<String, Object> map);
    
    /**
     * 取得定位到的区域ID
     * 
     * @param map 查询条件
     * @return 定位到的区域ID
     */
    public String getLocationRegionId(Map<String, Object> map);

}
