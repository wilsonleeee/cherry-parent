/*	
 * @(#)BINBAT148_Service.java     1.0 @2016-06-27		
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
package com.cherry.middledbout.stand.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryBatchUtil;

/**
 * 
 * 柜台特价产品导入(标准接口)(IF_ProductByCounter)FN
 * @author jijw
 *  * @version 1.0 2016.06.27
 *
 */
@SuppressWarnings("unchecked")
public class BINBAT148_Service extends BaseService {
	
	/**
	 * 获取标准IF柜台特价产品中柜台号数据
	 * 
	 * @param map
	 * @return
	 */
	public List getCntByIFOffers(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.getCntByIFOffers");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 查询新后台的柜台信息
	 * @param counterMap
	 * @return
	 */
	public List getCounterByCherryBrand(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT148.getCounterByCherryBrand");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新特价产品方案主表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductSpecialOfferSolu(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.mergeProductSpecialOfferSolu");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新特价产品方案与部门关联表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergePrtSpecialOfferSoluDepartRelation(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.mergePrtSpecialOfferSoluDepartRelation");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得柜台特价产品标准接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getStandardProductByOffersList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.getStandardProductByOffersList");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT148.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新特价产品方案明细表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductSpecialOfferSoluDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.mergeProductSpecialOfferSoluDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	
	
	/**
	 * 备注标准产品接口数据到履历表
	 * @param map
	 */
	public void backupItems(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBAT148.backupItems");
	}
	
	/**
	 * 更新柜台特价产品的同步状态（SynchFlag 1：处理中）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFOffersBySync1(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.updIFOffersBySync1");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 更新柜台特价产品的同步状态（SynchFlag 2：完成）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFOffersBySync2(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.updIFOffersBySync2");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 更新柜台特价产品的同步状态（SynchFlag 3：异常）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFOffersBySync3(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT148.updIFOffersBySync3");
		return tpifServiceImpl.update(map);
	}
}
