/*	
 * @(#)BINOTYIN01_Service.java     1.0 @2013-3-18		
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
package com.cherry.ot.yin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 *
 * 颖通接口：发货单退库单导入Service
 *
 * @author jijw
 *
 * @version  2013-3-18
 */
@SuppressWarnings("unchecked")
public class BINOTYIN08_Service extends BaseService {
	
	/**
	 * 更新颖通货单接口表的TrxStatus字段[TrxStatus=null]变更为[TrxStatus=1]
	 * @param map
	 * @return
	 */
	public int updTrxStatusNullTo1ForOT(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN08.updTrxStatusNullTo1ForOT");
		return tpifServiceImpl.update(map);
	}
	
	/**
	 * 取得颖通发货单接口表的单据号
	 * @param map
	 * @return
	 */
	public List<String> getDocEntryListForOT(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN08.getDocEntryListForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新颖通货单接口表的TrxStatus字段
	 * @param map
	 * @return
	 */
	public int updTrxStatusForOT(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN08.updTrxStatusForOT");
		return tpifServiceImpl.update(map);
	}
	
	/**
	 * 取得颖通发货单接口表数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExportTransListForOT(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN08.getExportTransListForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 根据itemCode取得新后台price(产品为产品价格表的"销售价格"、促销品为"促销品的标准成本")
	 * @param map
	 * @return
	 */
	public Map<String,Object> getProPrmPriceByUB(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN08.getProPrmPriceByUB");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINOTYIN08.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDateMap");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}

}
