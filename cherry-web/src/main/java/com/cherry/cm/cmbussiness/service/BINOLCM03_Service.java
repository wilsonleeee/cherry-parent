/*	
 * @(#)BINOLCM03_Service.java     1.0 2010/11/08		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 取得单据号
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM03_Service extends BaseService{
	
	public int startTransaction (){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.BEGINTRANSACATION");
		return baseServiceImpl.update(map);
	}
	public int commitTransaction (){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.COMMIT");
		return baseServiceImpl.update(map);
	}
	public int rollBack (){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.ROLLBACK");
		return baseServiceImpl.update(map);
	}
	public List getTicketNumber(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM03.getTicketNumber");
		return baseServiceImpl.getList(map);
	}
	
	public Integer getTicketNum(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM03.getTicketNumbers");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得业务日期
	 * @param map
	 * @return
	 */
	public List getBussinessDateNum(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM03.getBussinessDate");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 插入库存操作流水表，返回一个流水ID
	 * @param map
	 * @return
	 */
	public int insertPromotionInventoryLog(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM03.insertPromotionInventoryLog");
		return baseServiceImpl.saveBackId(map);
	}
	public List getSequenceCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM03.getSequenceCode");
		return baseServiceImpl.getList(map);
	}
}
