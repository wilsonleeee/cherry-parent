/*  
 * @(#)BINOLBSPAT01_service.java     1.0 2011/10/19     
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
package com.cherry.bs.pat.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 *往来单位一览
 * 
 * @author LuoHong
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLBSPAT01_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得单位总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPartnerCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT01.getPartnerCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得单位List
	 * 
	 * @param map
	 * @return
	 */
	public List getPartnerList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT01.getPartnerList");
		return baseServiceImpl.getList(map);
	}
	
    /**
     * 单位停用
     * 
     * @param map
     * @return
     */
    public int disablePartner(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT01.disablePartner");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 单位启用
     * 
     * @param map
     * @return
     */
    public int enablePartner(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT01.enablePartner");
        return baseServiceImpl.update(map);
    }
}