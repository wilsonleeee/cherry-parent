/*
 * @(#)BINOLSSPRM28_BL.java     1.0 2010/11/09
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

import com.cherry.ss.prm.service.BINOLSSPRM28_Service;

/**
 * 
 * 发货单明细BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.09
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM28_BL {

	@Resource
	private BINOLSSPRM28_Service binolssprm28_Service;

	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map searchDeliverInfo(Map<String, Object> map) {
		// 取得发货单信息
		return binolssprm28_Service.getDeliverInfo(map);
	}

	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverDetailList(Map<String, Object> map) {
		// 取得发货单明细List
		return binolssprm28_Service.getDeliverDetailList(map);
	}
}
