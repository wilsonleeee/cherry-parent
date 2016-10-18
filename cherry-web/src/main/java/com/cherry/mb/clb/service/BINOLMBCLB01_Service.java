/*	
 * @(#)BINOLMBCLB01_Service.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员俱乐部一览Service
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB01_Service extends BaseService{
	
	/**
     * 取得俱乐部List
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
    public List<Map<String, Object>> getClubList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getClubList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得俱乐部件数
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
    public int getClubCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getClubCount");
        return baseServiceImpl.getSum(paramMap);
	}
    
    /**
     * 取得俱乐部List(带权限)
     * 
     * @param map
     * @return
     * 		俱乐部List
     */
    public List<Map<String, Object>> getClubWithPrivilList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getClubWithPrivilList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 取得俱乐部件数(带权限)
     * 
     * @param map
     * @return
     * 		俱乐部件数
     */
    public int getClubWithPrivilCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getClubWithPrivilCount");
        return baseServiceImpl.getSum(paramMap);
	}
    
    /**
     * 取得需要下发的俱乐部件数
     * 
     * @param map
     * @return
     * 		需要下发的俱乐部件数
     */
    public int getNeedSendClubCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getNeedSendClubCount");
        return baseServiceImpl.getSum(map);
	}
    
    /**
     * 取得正在下发的俱乐部件数 
     * 
     * @param map
     * @return
     * 		正在下发的俱乐部件数
     */
    public int getExecClubCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.getExecClubCount");
        return baseServiceImpl.getSum(map);
	}
    
    /**
	 * 停用俱乐部
	 * 
	 * @param map
	 * @return int
	 */
	public int updateValidClub(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB01.updateValidClub");
		return baseServiceImpl.update(map);
	}
}
