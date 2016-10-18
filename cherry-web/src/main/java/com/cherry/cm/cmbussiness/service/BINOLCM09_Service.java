/*  
 * @(#)BINOLCM09_Service.java     1.0 2011/05/31      
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

@SuppressWarnings("unchecked")
public class BINOLCM09_Service extends BaseService{
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public HashMap getBrandCode (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getBrandCode");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销活动表
	 * @param map
	 * @return
	 */
	public List getActiveInfoList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getActiveInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销产品信息
	 * @param map
	 * @return
	 */
	public HashMap getPromotionPrtInfo (Map<String, Object> map){
		HashMap parameterMap = new HashMap();
		// 促销产品unitCode
		parameterMap.put("promotionPrtUnitcode", map.get("promotionPrtUnitcode"));
		parameterMap.put("promotionPrtCode", map.get("promotionPrtCode"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getPromotionPrtInfo");
		return (HashMap)baseServiceImpl.get(parameterMap);
		
	}
	
	/**
	 * 根据区域市查询柜台
	 * @param map
	 * @return
	 */
	public List getCounterByIdCity (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdCity");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据渠道查询柜台
	 * @param map
	 * @return
	 */
	public List getCounterByIdChannel (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdChannel");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得活动条件结果
	 * @param map
	 * @return
	 */
	public List getActConResultList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getActConResultList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得活动结果
	 * @param map
	 * @return
	 */
	public List getActiveResultList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getActiveResultList");
		return baseServiceImpl.getList(map);
	}
	
//	/**
//	 * 删除brand数据库活动表相关数据
//	 * @param map
//	 */
//	public void delActivityTable_CHY (Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.delActivityTable_CHY");
//		baseWitServiceImpl.update(map);
//	}
//	
//	/**
//	 * 删除brand数据库活动关联表相关 
//	 * @param map
//	 */
//	public void delActivityAssociateTable_CHY (Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.delActivityAssociateTable_CHY");
//		baseWitServiceImpl.update(map);
//	}
//	
//	
//	/**
//	 * 插入brand数据库活动表
//	 * @param map
//	 */
//	public void addActivityTable_CHY (Map<String, Object> map){
//		// 批量插入
//		baseWitServiceImpl.saveAll((List) map.get("activityTableCHYList"), "BINOLCM09.addActivityTable_CHY");
//	}
//	
//	/**
//	 * 插入brand数据库活动关联表 
//	 * @param map
//	 */
//	public void addActivityAssociateTable_CHY (Map<String, Object> map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.addActivityAssociateTable_CHY");
//		baseWitServiceImpl.save(map);
//	}
	
	/**
	 * 取得基础规则属性信息
	 * @param map
	 * @return
	 */
	public List getPrmBasePropInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM09.getPrmBasePropInfoList");
		return baseServiceImpl.getList(map);
	}
}
