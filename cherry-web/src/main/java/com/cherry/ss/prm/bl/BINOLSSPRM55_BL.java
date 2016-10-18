/*	
 * @(#)BINOLSSPRM55_BL.java     1.0 2010/11/25		
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
package com.cherry.ss.prm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.ss.prm.service.BINOLSSPRM55_Service;

/**
 * 调拨记录查询BL
 * 
 * @author dingyc
 * @version 1.0 2010.11.25
 * 
 */
public class BINOLSSPRM55_BL {

	@Resource
	private BINOLSSPRM55_Service binolssprm55Service;

	/**
	 * 取得调拨记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getAllocationCount(Map<String, Object> map) {
		return binolssprm55Service.getAllocationCount(map);
	}

	/**
	 * 取得调拨记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map) {
		return binolssprm55Service.getAllocationList(map);
	}
	   
	/**
     * 取得产品总数量和总金额
     */
    public Map getSumInfo(Map<String,Object> map){
        return binolssprm55Service.getSumInfo(map);
    }
}
