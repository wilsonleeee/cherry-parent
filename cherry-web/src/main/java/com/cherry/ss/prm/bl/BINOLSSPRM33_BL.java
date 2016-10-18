/*
 * @(#)BINOLSSPRM33_BL.java     1.0 2010/11/11
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

import com.cherry.ss.prm.service.BINOLSSPRM33_Service;

/**
 * 
 * 发货单查询BL
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.11
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM33_BL {
	
	@Resource
	private BINOLSSPRM33_Service binolssprm33_Service;
	
	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	public int searchDeliverCount(Map<String, Object> map) {
		// 取得发货单总数
		return binolssprm33_Service.getDeliverReceCount(map);
	}

	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List searchDeliverList(Map<String, Object> map) {
		// 取得发货单List
		return binolssprm33_Service.getDeliverReceList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolssprm33_Service.getSumInfo(map);
	}
}
