/*	
 * @(#)BINOLCPACT11_BL.java     1.0 @2014-01-13		
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
package com.cherry.cp.act.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.interfaces.BINOLCPACT11_IF;
import com.cherry.cp.act.service.BINOLCPACT11_Service;

/**
 * 活动履历一览BL
 * 
 * @author LUOHONG
 * 
 */
public class BINOLCPACT11_BL implements BINOLCPACT11_IF {

	@Resource
	private BINOLCPACT11_Service binOLCPACT11_Service;
	/**
	 * 活动履历Count
	 */
	@Override
	public int getCampHistoryCount(Map<String, Object> map) {
		return binOLCPACT11_Service.getCampHistoryCount(map);
	}
	/**
	 * 活动履历List
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getCampHistoryList(Map<String, Object> map) throws Exception {
		//活动履历List
		List<Map<String, Object>>  memHistoryList = binOLCPACT11_Service.getCampHistoryList(map);
		if (memHistoryList != null && !memHistoryList.isEmpty()) {
			String brandCode = ConvertUtil.getString(map.get("brandCode"));
			for (Map<String, Object> memHistoryMap : memHistoryList) {
				// 会员【手机号】字段解密
				if (!CherryChecker.isNullOrEmpty(memHistoryMap.get("mobilePhone"), true)) {
					String mobilePhone = ConvertUtil.getString(memHistoryMap.get("mobilePhone"));
					memHistoryMap.put("mobilePhone", CherrySecret.decryptData(brandCode,mobilePhone));
				}
			}
		}
		return memHistoryList;
	}

}
