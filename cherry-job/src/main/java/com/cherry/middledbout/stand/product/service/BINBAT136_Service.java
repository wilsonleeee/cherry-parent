/*	
 * @(#)BINBAT136_Service.java     1.0 @2016-02-14		
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
 * 柜台产品导入(标准接口)(IF_ProductByCounter)FN
 * @author jijw
 *  * @version 1.0 2016.02.14
 *
 */
@SuppressWarnings("unchecked")
public class BINBAT136_Service extends BaseService {
	
	/**
	 * 获取标准IF柜台产品中柜台号数据
	 * 
	 * @param map
	 * @return
	 */
	public List getCntByIFProductByCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.getCntByIFProductByCounter");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT136.getCounterByCherryBrand");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新产品方案主表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductPriceSolution(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.mergeProductPriceSolution");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新产品方案与部门关联表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergePrtSoluDepartRelation(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.mergePrtSoluDepartRelation");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新柜台产品的同步状态（SynchFlag 1：处理中）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFProductByCounterBySync1(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.updIFProductByCounterBySync1");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 更新柜台产品的同步状态（SynchFlag 2：完成）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFProductByCounterBySync2(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.updIFProductByCounterBySync2");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 更新柜台产品的同步状态（SynchFlag 3：异常）
	 * @param map
	 * @return
	 * 
	 * */
	public int updIFProductByCounterBySync3(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.updIFProductByCounterBySync3");
		return tpifServiceImpl.update(map);
	}
	
	/**
	 * 取得柜台产品标准接口表数据
	 * 
	 * @param map
	 * @return
	 */
	public List getStandardProductByCounterList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.getStandardProductByCounterList");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品ID
	 * @param map
	 * @return
	 */
	public int searchProductId(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT136.searchProductId");
		return CherryBatchUtil.Object2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新产品方案明细表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductPriceSolutionDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT136.mergeProductPriceSolutionDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	
	
	/**
	 * 备注标准产品接口数据到履历表
	 * @param map
	 */
	public void backupItems(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBAT136.backupItems");
	}

	/**
	 * 更新柜台产品的同步状态
	 * @param map
	 * @return
	 * 
	 * */
	public void updIFProductByCounterBySync(List<Map<String,Object>> map){
		tpifServiceImpl.updateAll(map, "BINBAT136.updIFProductByCounterBySync");
	}
	
}
