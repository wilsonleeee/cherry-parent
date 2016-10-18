/*
 * @(#)BINOLSSPRM44_BL.java     1.0 2012/04/06
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

import com.cherry.ss.prm.service.BINOLSSPRM44_Service;

/**
 * 
 * 收货单明细BL
 * 
 * 
 * 
 * @author LuoHong
 * @version 1.0 2012.04.06
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM44_BL {

	@Resource
	private BINOLSSPRM44_Service binolssprm44_Service;

	/**
	 * 取得收货单信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchDeliverInfo(Map<String, Object> map) {
		// 取得收货单信息
		return binolssprm44_Service.getDeliverInfo(map);
	}

	/**
	 * 取得收货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverDetailList(Map<String, Object> map) {
		// 取得收货单明细List
		return binolssprm44_Service.getDeliverDetailList(map);
	}
}
