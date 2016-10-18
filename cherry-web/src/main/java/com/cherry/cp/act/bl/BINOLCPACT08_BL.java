/*	
 * @(#)BINOLCPACT08_BL.java     1.0 @2013-07-16		
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
import com.cherry.cp.act.interfaces.BINOLCPACT08_IF;
import com.cherry.cp.act.service.BINOLCPACT08_Service;

/**
 * 礼品领用详细一览BL
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT08_BL implements BINOLCPACT08_IF {

	@Resource
	private BINOLCPACT08_Service binOLCPACT08_Service;

	@Override
	public Map<String, Object> getGiftDrawDetail(Map<String, Object> map) {
		Map<String, Object> giftDrawDetail = binOLCPACT08_Service
				.getGiftDrawDetail(map);
		if (!CherryChecker.isNullOrEmpty(giftDrawDetail)) {
			// 会员生日
			String birthYear = (String) giftDrawDetail.get("birthYear");
			String birthDay = (String) giftDrawDetail.get("birthDay");
			if (!CherryChecker.isNullOrEmpty(birthYear)
					&& !CherryChecker.isNullOrEmpty(birthDay)) {
				giftDrawDetail.put("memberBirth",
						birthYear + "-" + birthDay.substring(0, 2) + "-"
								+ birthDay.substring(2, 4));
			}
		}
		return giftDrawDetail;
	}

	@Override
	public List<Map<String, Object>> getGiftDrawPrtDetail(
			Map<String, Object> map) {
		return binOLCPACT08_Service.getGiftDrawPrtDetail(map);
	}

}
