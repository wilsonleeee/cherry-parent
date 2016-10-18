/*	
 * @(#)BINOLCPCOM05_IF.java     1.0 2013/4/18		
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
package com.cherry.cp.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;

/**
 * 活动预约表状态操作 IF
 * 
 * @author lipc
 * @version 1.0 2013.4.18
 */
public interface BINOLCPCOM05_IF {
	
	public Map<String,Object> getCampInfo(String subCampCode);

	/**
	 * 新后台批量操作
	 * 
	 * @param dto
	 * @throws CherryException 
	 */
	public int tran_campOrderBAT(Map<String,Object> comMap, Map<String,Object> campInfo,String billState,List<Map<String,Object>> orderList) throws CherryException;
	
	
	/**
	 * MQ操作
	 * 
	 * @param dto
	 * @throws CherryException 
	 */
	public int tran_campOrderMQ(Map<String,Object> map,Map<String,Object> order) throws CherryException;
	

	public int makeCampOrder(Map<String, Object> comMap,Map<String, Object> campInfo,
			Map<String, Object> order) throws CherryException;

	/**
	 * 发送积分MQ
	 * @param orderList
	 * @param comMap
	 */
	public int sendPointMQ(Map<String, Object> comMap,Map<String, Object> campInfo) throws Exception;
	
	/**
	 * 发送沟通MQ
	 * @param map
	 * @param batchNo
	 * @param eventType
	 * @return
	 * @throws Exception
	 */
	public int sendGTMQ(Map<String,Object> comMap, String batchNo, String billState);
	
	
	/**
	 * 更新活动单据
	 * 
	 * @param comMap
	 * @param order
	 */
	public void updCampOrder(Map<String,Object> comMap,Map<String,Object> order);
	
	/**
	 * 更新活动发MQ
	 * 
	 * @param comMap
	 * @param order
	 */
	public int sendPOSMQ(Map<String,Object> comMap,String order);
	
	/**
	 * 实时生成活动单据
	 * @param orgId
	 * @param brandId
	 * @param orgCode
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 * @return
	 */
	public int makeOrderMQ(int orgId,int brandId,String orgCode,String brandCode,String orderCntCode, int memId,String configType);
	
	/**
	 * 实时生成活动单据
	 * @param orgId
	 * @param brandId
	 * @param orgCode
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 * @return
	 */
	public int makeOrderMQ(int orgId,int brandId,String orgCode,String brandCode, int memId,
			String subCampType);
	
	/**
	 * 实时生成活动单据
	 * @param orgId
	 * @param brandId
	 * @param orgCode
	 * @param brandCode
	 * @param memId
	 * @param subCampType
	 * @return
	 */
	public int makeOrderWS(String orgId,String brandId,String orgCode,String brandCode, int memId,
			String subCampType);
	
	public String getErrMsg(String errCode);
	
	public Map<String,Object> tran_applyCoupon(int orgId, int brandId, String orgCode,
			String brandCode, int memId);
	
	public Map<String, Object> searchMemInfo(Map<String, Object> map);

}
