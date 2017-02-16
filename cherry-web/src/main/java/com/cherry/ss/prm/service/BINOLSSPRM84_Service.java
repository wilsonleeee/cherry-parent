/*		
 * @(#)BINOLSSPRM84_Service.java     1.0 2010/11/10		
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
package com.cherry.ss.prm.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BINOLSSPRM84_Service extends BaseService{
	
	//@Resource
	//private BaseWitServiceImpl baseWitServiceImpl;
	
	/**
	 * 取得活动总数
	 * @param map
	 * @return
	 */
	public int getActiveCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getActiveCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得活动信息(一览用)
	 * @param map
	 * @return
	 */
	public List getActiveList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getActiveList");
		return baseServiceImpl.getList(map);
	}
	
	public Map<String, Object> getCounterInfo (int id){
		return (Map<String, Object>)baseServiceImpl.get(id,"BINOLSSPRM84.getCounterInfo");
	}
	
	/**
	 * 促销品信息模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmPrt (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmPrtInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 促销柜台模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmCounter (Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmCounterInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 区域市模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmCity(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmCityInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 渠道模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmChannel(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmChannelInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 促销活动名模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmNameInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 促销主活动名模糊查询(输入框AJAX)
	 * @param map
	 * @return
	 */
	public List indSearchPrmGrpName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPrmGrpNameInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得活动条件结果
	 * @param map
	 * @return
	 */
	public List getActConResultList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getActConResultList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得活动结果
	 * @param map
	 * @return
	 */
	public List getActiveResultList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getActiveResultList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public HashMap getBrandCode (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getBrandCode");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销活动表
	 * @param map
	 * @return
	 */
	public List getActiveInfoList (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getActiveInfoList");
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
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getPromotionPrtInfo");
		return (HashMap)baseServiceImpl.get(parameterMap);
		
	}
	
	/**
	 * 根据区域市查询柜台
	 * @param map
	 * @return
	 */
	public List getCounterByIdCity (Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getCounterByIdCity");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据渠道查询柜台
	 * @param map
	 * @return
	 */
	public List getCounterByIdChannel (Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.getCounterByIdChannel");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据活动ID取得活动信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActivityInfo (String activeID){
		return (Map<String, Object>)baseServiceImpl.get(activeID,"BINOLSSPRM84.getActivityInfo");
	}

	public void checkActive1(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.checkActive1");
		baseServiceImpl.update(map);
	}
	public void checkActive2(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM84.checkActive2");
		baseServiceImpl.update(map);
	}
	
//	/**
//	 * 插入brand数据库活动表
//	 * @param map
//	 */
//	public void addActivityTable_CHY (Map<String, Object> map){
//		// 批量插入
//		baseWitServiceImpl.saveAll((List) map.get("activityTableCHYList"), "BINOLSSPRM84.addActivityTable_CHY");
//	}
//	
//	/**
//	 * 插入brand数据库促销表 
//	 * @param map
//	 */
//	public void addPromotionTable_CHY (Map<String, Object> map){
//		// 批量插入
//		baseWitServiceImpl.saveAll((List) map.get("promotionTableCHYList"), "BINOLSSPRM84.addPromotionTable_CHY");
//	}
//	
//	/**
//	 * 插入brand数据库活动关联表 
//	 * @param map
//	 */
//	public void addActivityAssociateTable_CHY (Map<String, Object> map){
//		// 批量插入
//		baseWitServiceImpl.saveAll((List) map.get("activityAssociateTableCHYList"), "BINOLSSPRM84.addActivityAssociateTable_CHY");
//	}
}
