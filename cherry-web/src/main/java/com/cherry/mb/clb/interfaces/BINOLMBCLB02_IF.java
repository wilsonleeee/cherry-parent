/*	
 * @(#)BINOLMBCLB02_IF.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.interfaces;

import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;

/**
 * 会员俱乐部添加IF
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public interface BINOLMBCLB02_IF {
	
	/**
	 * 保存俱乐部
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveClub(Map<String, Object> map) throws Exception;
	
	/**
     * 取得会员俱乐部信息
     * 
     * @param map
     * @return
     * 		会员俱乐部信息
     */
    public Map<String, Object> getClubInfo(Map<String, Object> map);
    
    /**
     * 取得会员俱乐部对应关系列表
     * 
     * @param map
     * @return
     * 		会员俱乐部对应关系列表
     */
    public List<Map<String, Object>> getClubBrandList(Map<String, Object> map);
    
    /**
	 * 取得活动地点JSON
	 * 
	 * @param locationType
	 * @param palceJson
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	public String getPlaceJson(Map<String, Object> palceMap,
			Map<String, Object> map) throws JSONException;
	
	/**
	 * 取得会员俱乐部ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map<String, Object> getClubId(Map<String, Object> map);
}
