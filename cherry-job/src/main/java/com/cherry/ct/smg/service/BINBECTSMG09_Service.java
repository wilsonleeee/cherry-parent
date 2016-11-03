/*	
 * @(#)BINBECTSMG09_Service.java     1.0 2016/05/02		
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
package com.cherry.ct.smg.service;

import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 短信签名管理Service
 * 
 * @author hub
 * @version 1.0 2016/05/02
 */
public class BINBECTSMG09_Service extends BaseService{
	
	/**
     * 取得品牌的短信签名
     * 
     * @param map
     * @return String
     * 		短信签名
     */
    public String getBrandSignName(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG09.getBrandSignName");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
	 * 
	 * 更新短信签名
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSignNameInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG09.updateSignNameInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 新增短信签名
	 * 
	 * @param map 新增条件
	 * @return 
	 */
	public void addSignNameInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG09.addSignNameInfo");
		baseServiceImpl.save(map);
	}

}
