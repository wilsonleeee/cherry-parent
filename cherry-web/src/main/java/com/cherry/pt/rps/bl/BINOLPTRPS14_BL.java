/*	
 * @(#)BINOLPTRPS14_BL.java     1.0 2011/03/16		
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

import com.cherry.pt.rps.interfaces.BINOLPTRPS14_IF;
import com.cherry.pt.rps.service.BINOLPTRPS14_Service;

/**
 * 销售记录详细查询BINOLPTRPS14_BL
 * @author zgl
 * @version 1.0 2011/03/18
 * 
 * */

public class BINOLPTRPS14_BL implements BINOLPTRPS14_IF {

	//BINOLPTRPS14_Service注入
	@Resource
	private BINOLPTRPS14_Service binolptrps14Service;
	
	/**
	 * 获取支付方式详细信息
	 * @param map
	 * @return list
	 */
	@Override
	public List<Map<String,Object>> getPayTypeDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolptrps14Service.getPayTypeDetail(map);
	}
	
	/**
	 * 获取操作员姓名
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public Map<String, Object> getEmployeeName(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolptrps14Service.getEmployeeName(map);
	}

	/**
	 * 获取产品销售记录单据详细
	 * @param map
	 * @return map
	 * 
	 * */
	@Override
	public Map<String, Object> getSaleRecordDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolptrps14Service.getSaleRecordDetail(map);
	}

	/**
	 * 获取产品销售记录单据中的产品详细
	 * @param map
	 * @return list
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getSaleRecordProductDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binolptrps14Service.getSaleRecordProductDetail(map);
	}

}
