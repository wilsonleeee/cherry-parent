/*
 * @(#)BINOLMOMAN02_Service.java     1.0 2011/4/2
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
package com.cherry.mo.man.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 添加机器Service
 * 
 * @author niushunjie
 * @version 1.0 2011.4.2
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN02_Service extends BaseService{
    /**
     * 添加机器
     * 
     * @param map
     * @return 
     */
	@CacheEvict(value="CherryMachineCache",allEntries=true,beforeInvocation=false)
    public void tran_addMachineInfo(List<Map<String, Object>> list) {
        baseServiceImpl.saveAll(list, "BINOLMOMAN02.addMachineInfo");
    }

    /**
     * 判断机器编号是否已经存在
     * 
     * @param map 新后台的机器CODE
     * @return 旧机器编号
     */
    public List getMachineInfoId(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN02.getMachineInfoId");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 添加新旧机器号
     * 
     * @param map
     * @return 
     */
    public void tran_addMachineCodeCollate(List<Map<String, Object>> list) {
        baseServiceImpl.saveAll(list, "BINOLMOMAN02.addMachineCodeCollate");
    }
    
    /**
     * 取得品牌末位码
     * 
     * @param map
     * @return 品牌末位码
     */
    public String getBrandLastCode(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN02.getBrandLastCode");
        return (String)baseServiceImpl.get(map);
    }
    
    /**
     * 取得品牌简称
     * 
     * @param map
     * @return 品牌简称
     */
    public String getBrandNameShort(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN02.getBrandNameShort");
        return (String)baseServiceImpl.get(map);
    }
    
    /**
     * 取得品牌编号
     * 
     * @param map
     * @return 品牌编号
     */
    public String getBrandCode(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN02.getBrandCode");
        return (String)baseServiceImpl.get(map);
    }
}
