/*  
 * @(#)MqPP_Service.java     1.0 2016-7-26    
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
package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: MqSA_Service 
 * @Description: TODO(退货申请单Service) 
 * @author nanjunbo
 * @version v1.0.0 2016-08-23 
 *
 */
public class MqSA_Service extends BaseService{
	
	/**
	 * 根据单号取得销售单信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSaleRecordInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqSA.getSaleRecordInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 插入客户退货单主表信息
	 * @param map
	 * @return
	 */
	public int insertSaleRetrunReqMain(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqSA.insertSaleRetrunReqMain");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入提货明细表数据
	 * @param list
	 */
	public void insertSaleRetrunReqDetail(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "MqSA.insertSaleRetrunReqDetail");
	}
	
	/**
	 * 插入提货支付明细
	 * @param list
	 */
	public void addSaleReturnPayList(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "MqSA.addSaleReturnPayList");
	}

}
