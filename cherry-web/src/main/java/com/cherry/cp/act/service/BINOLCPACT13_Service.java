/*	
 * @(#)BINOLCPACT13_Service.java     1.0 @2014-12-16	
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
package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 活动产品库存导入Service
 * 
 * @author menghao
 * 
 */
public class BINOLCPACT13_Service extends BaseService {
	
	/**
	 * 根据子活动号取得活动相关信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSubCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT13.getSubCampaignInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT13.getCounterInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 通过产品厂商编码取得产品信息或者促销品信息
	 * @param UnitCode 厂商编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT13.getPrtInfoByUnitCode");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新或新增活动产品库存
	 * @param map
	 * @return
	 */
	public int mergeCampaignStock(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPACT13.mergeCampaignStock");
		return ConvertUtil.getInt(baseServiceImpl.get(paramMap));
	}
}
