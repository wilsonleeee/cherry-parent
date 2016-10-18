/*
 * @(#)BINOLPTRPS02_BL.java     1.0 2010/11/03
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
package com.cherry.pt.rps.bl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.pt.rps.interfaces.BINOLPTRPS02_IF;
import com.cherry.pt.rps.service.BINOLPTRPS02_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * 我的发货单
 * @author weisc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS02_BL extends SsBaseBussinessLogic implements BINOLPTRPS02_IF{
	@Resource
	private BINOLPTRPS02_Service binolptrps02_Service;

	/**
	 * 取得发货单总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int searchDeliverCount(Map<String, Object> map) {
		// 取得发货单总数
		return binolptrps02_Service.getDeliverCount(map);
	}
	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List searchDeliverList(Map<String, Object> map) {
		// 取得发货单List
		return binolptrps02_Service.getDeliverList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binolptrps02_Service.getSumInfo(map);
	}
}
