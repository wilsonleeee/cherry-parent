/*
 * @(#)BINOLSTJCS01_Service.java     1.0 2011/04/11
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
package com.cherry.st.jcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTJCS01_Service extends BaseService{
	/**
	 * 取得仓库List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.getDepotInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得实体仓库总数
	 * 
	 * 
	 * */
	public int getDepotInfoCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.getDepotInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
     * 仓库启用
     * 
     * @param map
     * @return
     */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
    public int enable(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.enableDepot");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 仓库停用
     * 
     * @param map
     * @return
     */
    @CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
    public int disable(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.disableDepot");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 是否存在仓库
     * 
     * @param map
     * @return
     */
    public String getCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.getCount");
        return (String)baseServiceImpl.get(map);
    }
    
    /**
     * 是否是测试柜台
     * 
     * @param map
     * @return
     */
    public String testType(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.testType");
        return (String)baseServiceImpl.get(map);
    }
    
    /**
     * 编辑保存
     * 
     * @param map
     * @return
     */
    @CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
    public int editInventoryInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.editInventoryInfo");
        return baseServiceImpl.update(map);
    }
    @CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
    public int editDepot(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.editDepot");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 编辑保存
     * 
     * @param map
     * @return
     */
    @CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
    public int add(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.add");
        return baseServiceImpl.update(map);
    }
    
    /**
	 * 取得仓库List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	
	public List<Map<String, Object>> getInventoryInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.getInventoryInfoList");
		return baseServiceImpl.getList(map);
	}
	
	 /**
	 * 取得仓库详细
	 * 
	 * @param map
	 * 
	 * @return
	 */
	
	public List<Map<String, Object>> inventoryInfoDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTJCS01.inventoryInfoDetail");
		return baseServiceImpl.getList(map);
	}
}
