/*	
 * @(#)BINOLSSPRM41_BL.java     1.0 2010/10/29		
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

import com.cherry.ss.prm.service.BINOLSSPRM41_Service;

/**
 * 办事处）柜台退库记录查询BL
 * 
 * @author lipc
 * @version 1.0 2010.10.29
 * 
 */
public class BINOLSSPRM41_BL {

	@Resource
	private BINOLSSPRM41_Service binolssprm41Service;

	/**
	 * 取得退库记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProCount(Map<String, Object> map) {
		return binolssprm41Service.getProCount(map);
	}

	/**
	 * 取得退库记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProReturnList(Map<String, Object> map) {
		return binolssprm41Service.getProReturnList(map);
	}
	
	/**
	 * 退库记录汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolssprm41Service.getSumInfo(map);
	}
}
