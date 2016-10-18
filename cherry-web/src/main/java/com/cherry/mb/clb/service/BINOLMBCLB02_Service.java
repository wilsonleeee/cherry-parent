/*	
 * @(#)BINOLMBCLB02_Service.java     1.0 2014/04/29	
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 会员俱乐部添加Service
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB02_Service extends BaseService{
	
	/**
	 * 插入会员俱乐部表并返回俱乐部ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertMemberClub(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBCLB02.insertMemberClub");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入会员俱乐部与子品牌对应关系表
	 * @param list
	 */
	public void addMemClubBrandList (List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMBCLB02.insertMemClubBrand");
	}
	
	/**
	 * 
	 * 更新会员俱乐部表
	 * 
	 * @param map
	 * 
	 */
	public int updateMemberClub(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.updateMemberClub");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 删除会员俱乐部与子品牌对应关系
	 * 
	 * @param map
	 * 
	 */
	public int delClubBrand(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.updateDelClubBrand");
		return baseServiceImpl.update(map);
	}
	
	/**
     * 取得会员俱乐部信息
     * 
     * @param map
     * @return
     * 		会员俱乐部信息
     */
    public Map<String, Object> getClubInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.getClubInfo");
        return (Map<String, Object>) baseServiceImpl.get(map);
    }
    
    /**
     * 取得会员俱乐部对应关系列表
     * 
     * @param map
     * @return
     * 		会员俱乐部对应关系列表
     */
    public List<Map<String, Object>> getClubBrandList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.getClubBrandList");
        return baseServiceImpl.getList(map);
    }
    
    /**
	 * 取得会员俱乐部ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map<String, Object> getClubId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.getClubId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员俱乐部条件表
	 * @param list
	 */
	public void addMemClubCondition (List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLMBCLB02.insertMemClubCondition");
	}
	
	/**
	 * 
	 * 删除会员俱乐部条件表
	 * 
	 * @param map
	 * 			参数集合
	 * 
	 */
	public int delMemClubCondition(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCLB02.delMemClubCondition");
		return baseServiceImpl.remove(map);
	}
}
